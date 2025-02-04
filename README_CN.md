# Longan

[English](https://github.com/DylanCaiCoding/Longan) | 中文

[![](https://www.jitpack.io/v/DylanCaiCoding/Longan.svg)](https://www.jitpack.io/#DylanCaiCoding/Longan) [![](https://img.shields.io/badge/License-Apache--2.0-blue.svg)](https://github.com/DylanCaiCoding/Longan/blob/master/LICENSE)

Longan 是一个简化 Android 开发的 Kotlin 工具类集合，可以使代码更加简洁易读。**目前有超过 500 个常用方法或属性，能有效提高开发效率**。每个用法都会思考很多，并且参考官方 KTX 库的命名规则和用法，用起来会更加的舒服。具体的实现代码也会优化几版，用尽可能简洁轻量的代码实现功能，有兴趣的可以读下源码。

本库会长期维护，有任何使用上的问题或者想要的功能都可以加我微信 `DylanCaiCoding` 反馈，个人会及时处理。

## Gradle

在根目录的 build.gradle 添加：

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://www.jitpack.io' }
    }
}
```

添加依赖：

```groovy
dependencies {
    implementation 'com.github.DylanCaiCoding.Longan:longan:1.0.5'
    // Optional
    implementation 'com.github.DylanCaiCoding.Longan:longan-design:1.0.5'
}
```

## 用法

:pencil: **[使用文档](https://dylancaicoding.github.io/Longan)**

## 示例

下面介绍部分常用的功能：

在需要 Context 或 Activity 的时候，可直接获取 `application` 或 `topActivity` 属性。

跳转 Activity 并传参：

```kotlin
startActivity<SomeOtherActivity>("id" to 5)
```

在 Activity 内使用属性委托获取参数：

```kotlin
class SomeActivity : AppCompatActivity() {
  private val name: String? by intentExtras("name")          // 通过 Intent 获取可空的参数
  private val position: Int by intentExtras("position", 0)   // 通过 Intent 获取含默认值的非空参数
  private val id: String by safeIntentExtras("id")           // 通过 Intent 获取人为保证非空的参数
}
```

创建带参数的 Fragment：

```kotlin
val fragment = SomeFragment().withArguments("id" to 5)
```

在 Fragment 内使用属性委托获取参数：

```kotlin
class SomeFragment : Fragment() {
  private val name: String? by arguments("name")          // 通过 arguments 获取可空的参数
  private val position: Int by arguments("position", 0)   // 通过 arguments 获取含默认值的非空参数
  private val id: String by safeArguments("id")           // 通过 arguments 获取人为保证非空的参数
}
```

显示 Toast 或 Snackbar 消息：

```kotlin
toast("Hi there!")
snackbar("Action, reaction", "Click me!") { doStuff() }
```

显示隐藏键盘：

```kotlin
editText.showKeyboard()
editText.hideKeyboard()
```

快速实现 TabLayout + ViewPager2 的自定义样式的底部导航栏：

```kotlin
viewPager2.adapter = FragmentStateAdapter(HomeFragment(), ShopFragment(), MineFragment())
tabLayout.setupWithViewPager2(viewPager2, enableScroll = false) { tab, position ->
  tab.setCustomView(R.layout.layout_bottom_tab) {
    findViewById<TextView>(R.id.tv_title).setText(titleList[position])
    findViewById<ImageView>(R.id.iv_icon).apply {
      setImageResource(iconList[position])
      contentDescription = getString(titleList[position])
    }
  }
}
```

一行代码实现双击返回键退出 App 或者点击返回键不退出 App 回到桌面：

```kotlin
pressBackTwiceToExitApp("再次点击退出应用")
// pressBackToNotExitApp()
```

实现沉浸式状态栏，并且给标题栏的顶边距增加状态栏高度，可以适配刘海水滴屏：

```kotlin
immerseStatusBar()
toolbar.addStatusBarHeightToMarginTop()
// toolbar.addStatusBarHeightToPaddingTop()
```

快速实现获取验证码的倒计时（默认 60 秒）：

```kotlin
btnSendCode.startCountDown(this,
  onTick = {
    text = "${it}秒"
  },
  onFinish = {
    text = "获取验证码"
  })
```

设置按钮在输入框有内容时才能点击：

```kotlin
btnLogin.enableWhenOtherTextNotEmpty(edtAccount, edtPwd)
```

点击事件可以设置的点击间隔，防止一段时间内重复点击：

```kotlin
btnLogin.doOnClick(clickIntervals = 500) { 
  // ...
}
```

在 RecyclerView 数据为空的时候自动显示一个空布局：

```kotlin
recyclerView.setEmptyView(this, emptyView)
```

RecyclerView 的 `smoothScrollToPosition()` 方法是滑动到 item 可见，如果从上往下滑会停在底部，一般不符合需求。所以增加了个始终滑动到顶部位置的扩展方法。

```kotlin
recyclerView.smoothScrollToStartPosition(position)
```

简化自定义控件获取自定义属性：

```kotlin
withStyledAttributes(attrs, R.styleable.CustomView) {
  textSize = getDimension(R.styleable.CustomView_textSize, 12.sp)
  textColor = getColor(R.styleable.CustomView_textColor, getCompatColor(R.color.text_normal))
  icon = getDrawable(R.styleable.CustomView_icon) ?: getCompatDrawable(R.drawable.default_icon)
  iconSize = getDimension(R.styleable.CustomView_iconSize, 30.dp)
}
```

自定义控件绘制居中或者垂直居中的文字：

```kotlin
canvas.drawCenterText(text, centerX, centerY, paint)
canvas.drawCenterVerticalText(text, centerX, centerY, paint)
```

切换到主线程，用法与 `thread {...}` 保持了统一：

```kotlin
mainThread { 
  // ...
}
```

更多的用法请查看[使用文档](https://dylancaicoding.github.io/Longan)。

## 更新日志

[Releases](https://github.com/DylanCaiCoding/Longan/releases)

## 相关文章

- [《如何更好地使用 Kotlin 语法糖封装工具类》](https://juejin.cn/post/7020988520474869791)

## 反馈

有任何使用上的问题或者想要的功能都可以提 [issues](https://github.com/DylanCaiCoding/Longan/issues/new) 或者加我微信直接反馈，微信号 `DylanCaiCoding` ，我会拉个群。加好友时备注个 GitHub，个人不乱加陌生人。

## 作者其它的库

| 库                                                           | 简介                                           |
| ------------------------------------------------------------ | ---------------------------------------------- |
| [LoadingStateView](https://github.com/DylanCaiCoding/LoadingStateView) | 深度解耦标题栏或加载中、加载失败、无数据等视图 |
| [ViewBindingKTX](https://github.com/DylanCaiCoding/ViewBindingKTX) | 最全面的 ViewBinding 工具                    |
| [MMKV-KTX](https://github.com/DylanCaiCoding/MMKV-KTX)       | 用属性委托的方式使用 MMKV                            |

## License

```
Copyright (C) 2021. Dylan Cai

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
