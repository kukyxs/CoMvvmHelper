# Cov(CoMvvmHelper)
A lightweight tool-collection for quickly-developing an android app use (Kotlin + Jetpack + DataBinding) 

## How to import 
1. Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

2. Add the dependency
```groovy
dependencies {
    // current latest version is 0.1.7
    // current latest alpha version is 0.1.7-alpha
	implementation 'com.github.kukyxs:CoMvvmHelper:release-version'
}
```

## How to use
Application configurations -> [App](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/App.kt)


ImageLoadEngine(if use default image DataBindingAdapter) -> [example: GlideEngine](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/helper/GlideEngine.kt)


Permission Demo -> [PermissionDemoActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/PermissionDemoActivity.kt)


Http Demo(include download, dsl http request, request by retrofit) -> [HttpDemoActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/HttpDemoActivity.kt)


LiveDataManagerPool Demo() -> [HttpDemoActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/HttpDemoActivity.kt)


RecyclerView Demo(include Update Items, add HeaderView/FooterView, MultiItemLayout) -> [RecyclerViewDemoActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/RecyclerViewDemoActivity.kt)


ImageDisplay and DataStore(alpha branch) Demo -> [ImageDisplayActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/ImageDisplayActivity.kt), [ImageDisplayAlpha](https://github.com/kukyxs/CoMvvmHelper/blob/alpha/app/src/main/java/com/kuky/comvvmhelper/ui/activity/ImageDisplayActivity.kt)


DialogFragment Demo -> [DemoDialogFragment](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/DemoDialogFragment.kt)


More Koltin Extension Functions can read source code

## License
```text
MIT License

Copyright (c) 2020 kuky_xs

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
