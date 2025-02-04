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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri

fun CharSequence.copyToClipboard(label: CharSequence? = null) =
  (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .setPrimaryClip(ClipData.newPlainText(label, this))

fun Uri.copyToClipboard(label: CharSequence? = null) =
  (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .setPrimaryClip(ClipData.newUri(contentResolver, label, this))

fun Intent.copyToClipboard(label: CharSequence? = null) =
  (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .setPrimaryClip(ClipData.newIntent(label, this))

fun getTextFromClipboard(): CharSequence? =
  (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .primaryClip?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.coerceToText(application)

fun clearClipboard() =
  (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .setPrimaryClip(ClipData.newPlainText(null, ""))

fun doOnClipboardChanged(listener: ClipboardManager.OnPrimaryClipChangedListener) =
  (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .addPrimaryClipChangedListener(listener)

fun ClipboardManager.OnPrimaryClipChangedListener.cancel() =
  (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .removePrimaryClipChangedListener(this)
