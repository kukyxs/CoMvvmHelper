一款适用于 `Kotlin + Jectpack + DataBinding` 快速构建的开发框架

支持的开发插件 [CovDevPlugin](https://github.com/kukyxs/CovPlugin)

### 引入 `cov`
1. 在根目录 `build.gradle` 文件添加

   ```groovy
   allprojects {
   	repositories {
   		maven { url 'https://jitpack.io' }
   	}
   }
   ```

2. 在 `module` 的 `build.gradle` 文件添加

   ```groovy
   dependencies {
       // 最新版本 0.9.1.6/0.9.1.6-x, 版本后面带 x 为无 koin 版本
   	implementation 'com.github.kukyxs:CoMvvmHelper:version'
   }
   ```

### 版本更新记录(from 0.6.0)

0.6.0 - 增加 `MMKV` 支持

0.6.4 - 使用系统 `MimeTypeMap` 进行方法替换

0.6.5 - 移除了 `jcenter` 全部依赖, `anko-common` 使用本地代码(dialog, intent, toast 方法可使用, 修改包名即可)

0.7.0 - 升级 `koin` 版本 3.1.2, `scope` api 有改动，具体查看 -- (迁移 0.7.x)

0.7.6/0.7.6-x - 升级 `appcompat`, `ktx` 等版本，移除 `DataBinding namespace`，具体查看(迁移 0.7.6/0.7.6-x)

0.8.0/0.8.0-x `migration gradle 7.x`

0.8.2.2/0.8.2.2-x 增加 `livedata` 粘性处理，以及协程的一些扩展方法

0.8.4/0.8.4.1-x 增加新的 Http 请求方式，具体查看[demo](https://github.com/kukyxs/CoMvvmHelper/blob/no-koin/app/src/main/java/com/kuky/comvvmhelper/ui/activity/HttpDemoActivity.kt#L67)

0.8.5 支持开发插件 [CovDevPlugin](https://github.com/kukyxs/CovPlugin)

0.9.0 修复一些 bug，重新处理构建方案

0.9.1.4/0.9.1.4-x 修改下载器

0.9.1.5/0.9.1.5-x 修改下载器

### `Downloader 使用`

```kotlin
        launch {
            Downloader.instance(context).download {
                url = "https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF" // 下载地址

                downloadToPublic = false // 是否下载到公共目录，默认下载到公共目录

                // 如果下载到私有目录该配置生效
                privateStoreFile = File(filesDir, "download/a.jpg")

                // 公共目录配置，最终文件为 [PUBLIC_DIR]/[PRIMARY_DIR]/[FILE_NAME]
                publicStoreFileName = "a.jpg" // 文件名
                publicPrimaryDir = "$packageName.download" // 次级目录名
                targetPublicDir = PublicDirectoryType.PICTURES // 公共目录
                //
                downloadIfFileExists = false // 如果当前文件存在是否替换文件

                // 下载器监听
                onDownloadProgressChange = { ePrint { "progress: $it" } }
                onDownloadCompleted = { toast("download finished:$it") }
                onDownloadFailed = { ePrint("download failed", it) }
            }
        }
```

### 迁移 0.7.6/0.7.6-x 需注意

`compileSdkVersion` 需升级到 31，`targetSdkVersion` 不影响，
`dataBinding adapter namespace` 修改，移除 `bind:` 前缀，防止 `gradle build` 过程出现 `warming`,
升级版本后可通过 `android studio` 全局替换功能，设置 `File mask` 为 `*.xml` 并将 `bind:` 替换成 `''` 即可


### 迁移 0.7.x 版本(不使用 `koin` 可忽略)

由于 `koin` 升级 3.2.3 版本，`scope` 内容发生比较大的改变，具体替换如下(主要为 `Activity/Fragment` 的改变)
```kotlin
// 原来 `Activity/Fragment` 需实现 `KoinScopeComponent`, 改为 `AndroidScopeComponent`
// 创建 `scope` 方法之前使用 `by lazy{ activityScope() } / by lazy { createScopeAndLink() }` 改为
// by activityScope()/ by fragmentScope()
// 原有的 `scopeInject` 方法过时，使用 `AndroidScopeComponent.inject` 方法即可

// activity 操作
class GuideActivity : BaseActivity<ActivityGuideBinding>(), AndroidScopeComponent {
      // 通过 `activityScope` 方法创建 scope
      override val scope: Scope by activityScope()

      // 使用 `inject` 代替原来的 `scopeInject`
      private val mGuideAdapter by inject<GuideAdapter> {
          parametersOf(mGuideItems)
      }
  }

// fragment 操作
class TestNewKoinFragment : BaseFragment<FragmentTestNewKoinBinding>(), AndroidScopeComponent {
      // 通过扩展方法 `createScopeAndLink` 创建 scope, 或者使用 `fragmentScope` 方法创建(该方法不会绑定当前的 scope 到对应的 activity)
      override val scope: Scope by fragmentScope()

      // 使用 `inject` 代替原来的 `scopeInject`
      private val aInstance by inject<EntityForKoinScopeTest>()
  }
```

### 迁移 0.5.x 版本(不使用 `Koin` 可忽略)

由于 `koin` 升级后，`lifecycleScope.inject` 方法被标记过时, 且无法再使用, 重写了扩展方法 `scopeInject` 来代替 `lifecycleScope.inject` 方法, 未使用 `scope` 的不影响

```kotlin
val adapterModule = module {
    scope<GuideActivity> { // 在 module 中注册 scope
        scoped { (items: MutableList<GuideDisplay>) -> GuideAdapter(items) }
    }

    scope<TestNewKoinFragment> {
        scoped { EntityForKoinScopeTest() }
    }
}
```

- 在 `Activity` 中使用 `Scope`

  ```kotlin
  // 实现 `KoinScopeComponent` 接口
  class GuideActivity : BaseActivity<ActivityGuideBinding>(), KoinScopeComponent {
      // 通过 `activityScope` 方法创建 scope
      override val scope: Scope by lazy { activityScope() }

      // 使用 `scopeInject` 代替原来的 `lifecycleScope.inject`
      private val mGuideAdapter by scopeInject<GuideAdapter> {
          parametersOf(mGuideItems)
      }

      override fun onDestroy() {
          super.onDestroy()
          scope.close() // destroy 的时候需要销毁 scope
      }
  }
  ```

- 在 `Fragment` 中使用 `Scope`

  ```kotlin
  // 同样需实现 `KoinScopeComponent` 接口
  class TestNewKoinFragment : BaseFragment<FragmentTestNewKoinBinding>(), KoinScopeComponent {
      // 通过扩展方法 `createScopeAndLink` 创建 scope, 或者使用 `fragmentScope` 方法创建(该方法不会绑定当前的 scope 到对应的 activity)
      override val scope: Scope by lazy { createScopeAndLink() }

      // 使用 `scopeInject` 代替原来的 `lifecycleScope.inject`
      private val aInstance by scopeInject<EntityForKoinScopeTest>()

      override fun onDestroy() {
          super.onDestroy()
          scope.close() // destroy 的时候需要销毁 scope
      }
  }
  ```

- 或者可以继承官方提供的 `ScopeActivity/ScopeFragment`  类, 该方案有侵入性, 不推荐使用


### 项目配置和使用

- `Application` 基础配置

    ```kotlin
    // 在 Application onCreate 中进行相应的配置(retrofit, koin, imageLoad)

    // 如果项目中同时使用 retrofit, koin, DataBindingImageLoad 配置如下
            startCov {
                // DataBinding 加载图片, 可灵活配置三方图片加载框架, 具体可查看 demo
                // GlideEngine: https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/helper/GlideEngine.kt
                // CoilEngine: https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/helper/CoilEngine.kt
                loadEngine = GlideEngine()

                koinModules = mutableListOf(viewModelModule, adapterModule) // koin 注入 module

                baseUrl = Constant.WAN_URL // retrofit baseUrl(RetrofitApiService 支持动态 url, 详见后续)

                koinPropertiesFile = "" // koin properties 配置文件名

                client = OkHttpClient.Builder().build() // 网络 client 配置

                customRetrofitCallAdapterArray = mutableListOf() // retrofit call adapter 配置

                customRetrofitConverterFactoryArray = mutableListOf() // retrofit converter 配置(默认已有 Gson)
            }

    // 如果只使用其中某一个可分开进行配置
            globalLoadEngine(GlideEngine()) // 配置 DataBindingImageLoad

            koinInit {            			// 配置 koin
                koinPropertiesFile = ""

                koinModules = mutableListOf(viewModelModule, adapterModule)
            }

            globalHttpClient {              // 配置 retrofit
                baseUrl = Constant.WAN_UR

                client = OkHttpClient.Builder().build()

                customCallAdapter = mutableListOf()

                customConvertAdapter = mutableListOf()
            }

            MMKV.initialize(this) // 配置 MMKV
    ```

    配置完成后即可开始快乐的使用了

- 使用 `BaseActivity/BaseFragment/BaseKeepFragment`

    ```kotlin
    // 创建完成 xml 后, 需转换成 DataBinding 格式的 xml(推荐一个插件-DataBindingConvert 可一键转换)
    // Base 需传入一个 DataBinding 类型的泛型
    // 通过注解 ActivityConfig 可以对 activity 一些信息进行配置, Fragment 目前不支持, 后期考虑
    annotation class ActivityConfig(
        val windowState: Int = WindowState.NORMAL, // 配置是否透明状态栏
        val statusBarColorString: String = "", // 状态栏颜色(颜色值需匹配正则["#([0-9A-Fa-f]{8}|[0-9A-Fa-f]{6})"])
        val statusBarTextColorMode: Int = StatusBarTextColorMode.LIGHT, // 状态栏文字
        val contentUpToStatusBarWhenTransparent: Boolean = false, // 透明状态栏时，是否延伸内容到状态栏下
        @Deprecated("Replaced by param colorString and will be removed at future version", level = DeprecationLevel.WARNING)
        @ColorInt val statusBarColor: Int = Color.TRANSPARENT // 过时属性, 勿用
    )
    ```

    `BaseActivity` 使用可参考 [`GuideActivity`](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/GuideActivity.kt)

    `BaseFragment` 使用可参考 [`TestNewKoinFragment`](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/fragment/TestNewKoinFragment.kt)

    `BaseKeepFragment` 使用类似 `BaseFragment`, 主要用于 `Navigation` 时使用，配合 `ViewModel` 进行状态保持，预防重建产生的负面效果

- 使用 `BaseDialogFragment`

    ```kotlin
    // BaseDialogFragment 可快速通过 xml 构建 dialog，可配置宽高动画等属性
    // 通过重写 dialogFragmentAnim 可修改 dialog 弹出和消失的动画
    // 通过重写 dialogFragmentDisplayConfigs 可修改 dialog 宽高，背景，位置属性
    // 推荐使用 `showAllowStateLoss` 代替 `show` 方法
    ```

    `BaseDialog` 使用可参考 [`DemoDialogFragment`](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/dialog/DemoDialogFragment.kt)

- 使用 `BaseRecyclerViewAdapter`

  `BaseRecyclerViewAdapter` 支持添加头部底部，支持 `DiffUtil`，支持 `DataBinding` 绑定到 `xml`

  多布局可使用其子类 `BaseMultiLayoutAdapter`，具体使用可参考

  [`RecyclerViewDemoActivity`](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/RecyclerViewDemoActivity.kt)

  [`StringAdapter`](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/adapter/StringAdapter.kt)

  [`MultiLayoutAdapter`](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/adapter/MultiLayoutAdapter.kt)

- 动态切换 `Retrofit BaseUrl`

    通过注解 `Headers` 进行替换

    ```kotlin
    interface ApiService {
        @Headers("${DynamicUrlInterceptor.URL_HEADER_TAG}:${Constant.WAN_URL}")
        @GET("friend/json")
        suspend fun requestRepositoryInfo(): BaseResultData<MutableList<WebsiteData>>

        @Headers("${DynamicUrlInterceptor.URL_HEADER_TAG}:${Constant.JH_URL}")
        @GET("/toutiao/index")
        suspend fun requestTop(@Query("key") key: String): Tops
    }
    ```

    使用参考 [HttpDemoActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/HttpDemoActivity.kt)

- `binding` 包使用

    `binging` 包下包含 `DataBingingAdapter` 的处理

    - `DrawableBinding` 用于快速构建 `shape/selector` 等效果，无需创建对应的 `xml` 文件，使用参考 [`shape_display`](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/res/layout/activity_shape_display.xml)
    - `ImageViewBinding` 用于快速加载图片文件，使用参考 [`image_display`](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/res/layout/activity_image_display.xml)
    - `RecyclerViewBinding` 用于快速绑定 `RecyclerView adapter` 点击事件等，使用参考 [recycler_view_demo](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/res/layout/activity_recycler_view_demo.xml)
    - `WidgetBinding` 用于一些控件属性的快速绑定，使用参考 [http_demo](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/res/layout/activity_http_demo.xml)

- `extension` 包使用

    `extension` 包下包含各类扩展函数

    - `BooleanExtension` 用于替换 `if(){} else {}` 操作

      ```kotlin
      // 以下等同于 val a = if(statement){ result1 } else { result2 }
      val a = (statement).yes{ result1 }.otherwise{ result2 }
      ```

    - `ClickExtension` 用于处理点击防抖动

      ```kotlin
      v.setOnDebounceClickListener{
          //click handle
      }
      ```

    - `CoroutineLaunchExtension` 对协程进行扩展，使用参考 [HttpDemoActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/HttpDemoActivity.kt)

    - `ResourceExtensions` 快速获取资源

      ```kotlin
      val drawable = drawableValue(R.drawable.ic_launcher)
      ```

    - `TextViewExtensions` 代码快速设置 `TextView` 的 `drawable`，可手动设置大小

      ```kotlin
      textview.drawableStart(R.drawable.ic_launcher, size = 10f.dp2px().toInt)

      textview.drawableStart("//image_path", size = 10f.dp2px().toInt)
      ```

    - 其余可自行查看源码 .......

- `helper` 包使用

    `helper` 包下包括一些常用的方法(文件拷贝，网络，权限申请，下载文件，快速构建单例等)

    - `FileCopy` 文件拷贝方法，已适配 `SAF`

      - `copyFileBelowQ` 用于 `AndroidQ` 以下文件拷贝
      - `copyFileToPublicPictureOnQ` 等用于 `AndroidQ` 以上，将文件拷贝到系统公共目录

    - `Http` 为 `OkHttp DSL` 写法，使用参考 [HttpDemoActivity](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/HttpDemoActivity.kt)

    - `Permission` 用于权限申请，使用参考 [`PermissionDemoActivity`](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/PermissionDemoActivity.kt)

    - `SimpleDownload` 用于文件下载，支持暂停等操作，使用参考  [HttpDemoActivity-download](https://github.com/kukyxs/CoMvvmHelper/blob/master/app/src/main/java/com/kuky/comvvmhelper/ui/activity/HttpDemoActivity.kt)

    - `SingletonHelper` 用于快速构建单例，目前支持构造函数为 `0-3` 个参数的类的快速构建

      ```kotlin
      class HttpSingle private constructor() {
          companion object : SingletonHelperArg0<HttpSingle>(::HttpSingle)
      }

      class SingletonTest private constructor(context: Context){
          companion object : SingletonHelperArg1<SingletonTest, Context>(::SingletonTest)
      }
      ```

    - `CenterDrawableHelper` 用于构建图标紧贴文字的 `TextView` (子类也适用)，使用参考 [CenterDrawableTextView](https://github.com/kukyxs/CoMvvmHelper/blob/master/comvvmhelper/src/main/java/com/kk/android/comvvmhelper/widget/CenterDrawableTextView.kt)

    - `Logger` 为打印工具，类继承 `KLogger` 即可快速使用打印方法 `ePrint/wPrint/iPrint/dPrint/vPrint/jsonPrint` 等，`BaseActivity` 等已默认继承

      ```kotlin
      class Test : KLogger {
          fun test(){
              ePrint{ "KLogger go->" } // logger tag is your class name
          }
      }
      ```

- `utils` 包使用

    `utils` 包下包含工具类的使用，包括 `Share`, `MMKV` 等

    - `MMKV` 使用参考 (MMKV 官方文档)[https://github.com/Tencent/MMKV/wiki/android_setup]

