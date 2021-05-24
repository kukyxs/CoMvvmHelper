英文文档|[中文文档](https://github.com/kukyxs/CoMvvmHelper/blob/master/README_CN.md)

Cov is a lightweight tool-collection for quickly-developing an android app use (Kotlin + JetPack + DataBinding)

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
    // current latest release version is 0.6.2
	implementation 'com.github.kukyxs:CoMvvmHelper:version'
}
```

## Migrate version to 0.5.x
Due to koin lifecycleScope has been Deprecated and not supported any more, you can implementation KoinScopeComponent and override scope field,

then call `scopeInject`(an extension function for KoinScopeComponent) to replace `lifecycleScope.inject`, for example

```kotlin
// register your koin scoped
val adapterModule = module {
    scope<GuideActivity> {
        scoped { (items: MutableList<GuideDisplay>) -> GuideAdapter(items) }
    }

    scope<TestNewKoinFragment> {
        scoped { EntityForKoinScopeTest() }
    }
}
```

```kotlin
// make your activity implement from KoinScopeComponent
class GuideActivity : BaseActivity<ActivityGuideBinding>(), KoinScopeComponent {
    // create koin scope
    override val scope: Scope by lazy { activityScope() }

    // your registered scope instance
    private val mGuideAdapter by scopeInject<GuideAdapter> {
        parametersOf(mGuideItems)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.close() // close tied scope
    }
}
```

```kotlin
// make your fragment implement from KoinScopeComponent
class TestNewKoinFragment : BaseFragment<FragmentTestNewKoinBinding>(), KoinScopeComponent {
    // create koin scope
    override val scope: Scope by lazy { createScopeAndLink() }

    // your registered scope instance
    private val aInstance by scopeInject<EntityForKoinScopeTest>()

    override fun onDestroy() {
        super.onDestroy()
        scope.close() // close tied scope
    }
}
```

## How to use
Application configurations -> [App](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/App.kt)


ImageLoadEngine(if use default image DataBindingAdapter) -> [example: GlideEngine](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/helper/GlideEngine.kt)


Permission Demo -> [PermissionDemoActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/PermissionDemoActivity.kt)


Http Demo(include download, dsl http request, request by retrofit<Support Dynamic Base Url>) -> [HttpDemoActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/HttpDemoActivity.kt)


LiveDataManagerPool Demo() -> [HttpDemoActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/HttpDemoActivity.kt)


RecyclerView Demo(include Update Items, add HeaderView/FooterView, MultiItemLayout) -> [RecyclerViewDemoActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/RecyclerViewDemoActivity.kt)


MultiDisplay Demo -> [MultiItemDisplayActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/MultiItemDisplayActivity.kt)


ImageDisplay and DataStore(alpha branch) Demo -> [ImageDisplayActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/ImageDisplayActivity.kt), [ImageDisplayAlpha](https://github.com/kukyxs/CoMvvmHelper/blob/alpha/app/src/main/java/com/kuky/comvvmhelper/ui/activity/ImageDisplayActivity.kt)


DialogFragment Demo -> [DemoDialogFragment](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/DemoDialogFragment.kt), [MultiItemDisplayActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/MultiItemDisplayActivity.kt)


Background By DataBinding -> [activity_shape_display](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/res/layout/activity_shape_display.xml)


Paging3 Demo -> [PagingDemoActivity](https://github.com/kukyxs/CoMvvmHelper/blob/alpha/app/src/main/java/com/kuky/comvvmhelper/ui/activity/PagingDemoActivity.kt)


More Koltin Extension Functions can read source code

## Third Libraries Implemented at Cov
see -> [LibConfigs](https://github.com/kukyxs/CoMvvmHelper/blob/master/configs.gradle)

proguard rules referer the proguard rules of third library(OkHttp and Retrofit proguard rules has been added)

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
