/*
 * Copyright (c) 2021. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package com.dylanc.longan

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField.DAY_OF_MONTH
import java.time.temporal.ChronoField.DAY_OF_YEAR
import java.time.temporal.ChronoUnit.MONTHS
import java.time.temporal.ChronoUnit.YEARS
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val systemTimeZone: TimeZone by object : ReadOnlyProperty<Any?, TimeZone> {
  private lateinit var timeZone: TimeZone

  override fun getValue(thisRef: Any?, property: KProperty<*>): TimeZone {
    if (!::timeZone.isInitialized) {
      timeZone = TimeZone.currentSystemDefault()
      application.registerReceiver(object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
          if (intent?.action == Intent.ACTION_TIMEZONE_CHANGED) {
            timeZone = TimeZone.currentSystemDefault()
          }
        }
      }, IntentFilter(Intent.ACTION_TIMEZONE_CHANGED))
    }
    return timeZone
  }
}

fun Instant.Companion.parse(text: String, pattern: String, timeZone: TimeZone = systemTimeZone): Instant =
  java.time.ZonedDateTime.parse(text, DateTimeFormatter.ofPattern(pattern).withZone(timeZone.toJavaZoneId())).toInstant().toKotlinInstant()

fun LocalDateTime.Companion.parse(text: String, pattern: String): LocalDateTime =
  java.time.LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern)).toKotlinLocalDateTime()

fun LocalDate.Companion.parse(text: String, pattern: String): LocalDate =
  java.time.LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern)).toKotlinLocalDate()

fun String.toInstant(pattern: String, timeZone: TimeZone = systemTimeZone): Instant = Instant.parse(this, pattern, timeZone)

fun String.toLocalDateTime(pattern: String): LocalDateTime = LocalDateTime.parse(this, pattern)

fun String.toLocalDate(pattern: String): LocalDate = LocalDate.parse(this, pattern)

fun String.toEpochMilliseconds(pattern: String, timeZone: TimeZone = systemTimeZone): Long = toInstant(pattern, timeZone).toEpochMilliseconds()

fun String.toEpochSeconds(pattern: String, timeZone: TimeZone = systemTimeZone): Long = toInstant(pattern, timeZone).epochSeconds

fun LocalDateTime.toInstant(): Instant = toInstant(systemTimeZone)

fun Instant.toLocalDateTime(): LocalDateTime = toLocalDateTime(systemTimeZone)

fun Instant.format(pattern: String, timeZone: TimeZone = systemTimeZone, locale: Locale? = null): String =
  DateTimeFormatter(pattern, locale).withZone(timeZone.toJavaZoneId()).format(toJavaInstant())

fun LocalDateTime.format(pattern: String, locale: Locale? = null): String =
  DateTimeFormatter(pattern, locale).format(toJavaLocalDateTime())

fun LocalDate.format(pattern: String, locale: Locale? = null): String =
  DateTimeFormatter(pattern, locale).format(toJavaLocalDate())

val Clock.System.today: LocalDate get() = Clock.System.todayAt(systemTimeZone)

fun LocalDateTime.isToday(timeZone: TimeZone = systemTimeZone): Boolean = date.isToday(timeZone)

fun LocalDate.isToday(timeZone: TimeZone = systemTimeZone): Boolean = this == Clock.System.todayAt(timeZone)

fun LocalDateTime.isYesterday(timeZone: TimeZone = systemTimeZone): Boolean = date.isYesterday(timeZone)

fun LocalDate.isYesterday(timeZone: TimeZone = systemTimeZone): Boolean =
  this == Clock.System.todayAt(timeZone).minus(1, DateTimeUnit.DAY)

fun LocalDateTime.withYear(year: Int): LocalDateTime = toJavaLocalDateTime().withYear(year).toKotlinLocalDateTime()

fun LocalDateTime.withMonth(month: Int): LocalDateTime = toJavaLocalDateTime().withMonth(month).toKotlinLocalDateTime()

fun LocalDateTime.withDayOfMonth(dayOfMonth: Int): LocalDateTime = toJavaLocalDateTime().withDayOfMonth(dayOfMonth).toKotlinLocalDateTime()

fun LocalDateTime.withDayOfYear(dayOfYear: Int): LocalDateTime = toJavaLocalDateTime().withDayOfYear(dayOfYear).toKotlinLocalDateTime()

fun LocalDateTime.withHour(hour: Int): LocalDateTime = toJavaLocalDateTime().withHour(hour).toKotlinLocalDateTime()

fun LocalDateTime.withMinute(minute: Int): LocalDateTime = toJavaLocalDateTime().withMinute(minute).toKotlinLocalDateTime()

fun LocalDateTime.withSecond(second: Int): LocalDateTime = toJavaLocalDateTime().withSecond(second).toKotlinLocalDateTime()

fun LocalDateTime.withNano(nano: Int): LocalDateTime = toJavaLocalDateTime().withNano(nano).toKotlinLocalDateTime()

fun LocalDateTime.with(adjuster: TemporalAdjuster): LocalDateTime = toJavaLocalDateTime().with(adjuster).toKotlinLocalDateTime()

fun LocalDateTime.firstDayOfYear(): LocalDateTime = with(TemporalAdjusters.firstDayOfYear())

fun LocalDateTime.lastDayOfYear(): LocalDateTime = with(TemporalAdjusters.lastDayOfYear())

fun LocalDateTime.firstDayOfNextYear(): LocalDateTime = with(TemporalAdjusters.firstDayOfNextYear())

fun LocalDateTime.firstDayOfLastYear(): LocalDateTime = with { it.with(DAY_OF_YEAR, 1).minus(1, YEARS) }

fun LocalDateTime.firstDayOfMonth(): LocalDateTime = with(TemporalAdjusters.firstDayOfMonth())

fun LocalDateTime.lastDayOfMonth(): LocalDateTime = with(TemporalAdjusters.lastDayOfMonth())

fun LocalDateTime.firstDayOfNextMonth(): LocalDateTime = with(TemporalAdjusters.firstDayOfNextMonth())

fun LocalDateTime.firstDayOfLastMonth(): LocalDateTime = with { it.with(DAY_OF_MONTH, 1).minus(1, MONTHS) }

fun LocalDateTime.firstInMonth(dayOfWeek: DayOfWeek): LocalDateTime = with(TemporalAdjusters.firstInMonth(dayOfWeek))

fun LocalDateTime.lastInMonth(dayOfWeek: DayOfWeek): LocalDateTime = with(TemporalAdjusters.lastInMonth(dayOfWeek))

fun LocalDateTime.dayOfWeekInMonth(ordinal: Int, dayOfWeek: DayOfWeek): LocalDateTime = with(TemporalAdjusters.dayOfWeekInMonth(ordinal, dayOfWeek))

fun LocalDateTime.next(dayOfWeek: DayOfWeek): LocalDateTime = with(TemporalAdjusters.next(dayOfWeek))

fun LocalDateTime.nextOrSame(dayOfWeek: DayOfWeek): LocalDateTime = with(TemporalAdjusters.nextOrSame(dayOfWeek))

fun LocalDateTime.previous(dayOfWeek: DayOfWeek): LocalDateTime = with(TemporalAdjusters.previous(dayOfWeek))

fun LocalDateTime.previousOrSame(dayOfWeek: DayOfWeek): LocalDateTime = with(TemporalAdjusters.previousOrSame(dayOfWeek))

fun LocalDate.withYear(year: Int): LocalDate = toJavaLocalDate().withYear(year).toKotlinLocalDate()

fun LocalDate.withMonth(month: Int): LocalDate = toJavaLocalDate().withMonth(month).toKotlinLocalDate()

fun LocalDate.withDayOfMonth(dayOfMonth: Int): LocalDate = toJavaLocalDate().withDayOfMonth(dayOfMonth).toKotlinLocalDate()

fun LocalDate.withDayOfYear(dayOfYear: Int): LocalDate = toJavaLocalDate().withDayOfYear(dayOfYear).toKotlinLocalDate()

fun LocalDate.with(adjuster: TemporalAdjuster): LocalDate = toJavaLocalDate().with(adjuster).toKotlinLocalDate()

fun LocalDate.firstDayOfYear(): LocalDate = with(TemporalAdjusters.firstDayOfYear())

fun LocalDate.lastDayOfYear(): LocalDate = with(TemporalAdjusters.lastDayOfYear())

fun LocalDate.firstDayOfNextYear(): LocalDate = with(TemporalAdjusters.firstDayOfNextYear())

fun LocalDate.firstDayOfLastYear(): LocalDate = with { it.with(DAY_OF_YEAR, 1).minus(1, YEARS) }

fun LocalDate.firstDayOfMonth(): LocalDate = with(TemporalAdjusters.firstDayOfMonth())

fun LocalDate.lastDayOfMonth(): LocalDate = with(TemporalAdjusters.lastDayOfMonth())

fun LocalDate.firstDayOfNextMonth(): LocalDate = with(TemporalAdjusters.firstDayOfNextMonth())

fun LocalDate.firstDayOfLastMonth(): LocalDate = with { it.with(DAY_OF_MONTH, 1).minus(1, MONTHS) }

fun LocalDate.firstInMonth(dayOfWeek: DayOfWeek): LocalDate = with(TemporalAdjusters.firstInMonth(dayOfWeek))

fun LocalDate.lastInMonth(dayOfWeek: DayOfWeek): LocalDate = with(TemporalAdjusters.lastInMonth(dayOfWeek))

fun LocalDate.dayOfWeekInMonth(ordinal: Int, dayOfWeek: DayOfWeek): LocalDate = with(TemporalAdjusters.dayOfWeekInMonth(ordinal, dayOfWeek))

fun LocalDate.next(dayOfWeek: DayOfWeek): LocalDate = with(TemporalAdjusters.next(dayOfWeek))

fun LocalDate.nextOrSame(dayOfWeek: DayOfWeek): LocalDate = with(TemporalAdjusters.nextOrSame(dayOfWeek))

fun LocalDate.previous(dayOfWeek: DayOfWeek): LocalDate = with(TemporalAdjusters.previous(dayOfWeek))

fun LocalDate.previousOrSame(dayOfWeek: DayOfWeek): LocalDate = with(TemporalAdjusters.previousOrSame(dayOfWeek))

fun Instant.plus(period: DateTimePeriod): Instant = plus(period, systemTimeZone)

fun Instant.plus(value: Int, unit: DateTimeUnit): Instant = plus(value, unit, systemTimeZone)

fun Instant.plus(value: Long, unit: DateTimeUnit): Instant = plus(value, unit, systemTimeZone)

fun Instant.plus(unit: DateTimeUnit): Instant = plus(unit, systemTimeZone)

fun Instant.minus(period: DateTimePeriod): Instant = minus(period, systemTimeZone)

fun Instant.minus(value: Int, unit: DateTimeUnit): Instant = minus(value, unit, systemTimeZone)

fun Instant.minus(value: Long, unit: DateTimeUnit): Instant = minus(value, unit, systemTimeZone)

fun Instant.minus(unit: DateTimeUnit): Instant = minus(unit, systemTimeZone)

fun Instant.minus(other: Instant, unit: DateTimeUnit): Long = minus(other, unit, systemTimeZone)

fun Instant.until(other: Instant, unit: DateTimeUnit): Long = until(other, unit, systemTimeZone)

fun Instant.daysUntil(other: Instant): Int = daysUntil(other, systemTimeZone)

fun Instant.monthsUntil(other: Instant): Int = monthsUntil(other, systemTimeZone)

fun Instant.yearsUntil(other: Instant): Int = yearsUntil(other, systemTimeZone)

fun Instant.periodUntil(other: Instant): DateTimePeriod = periodUntil(other, systemTimeZone)

private fun DateTimeFormatter(pattern: String, locale: Locale?): DateTimeFormatter =
  if (locale != null) DateTimeFormatter.ofPattern(pattern, locale) else DateTimeFormatter.ofPattern(pattern)
