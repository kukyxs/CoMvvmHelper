extra.apply {
    val lifecycleVersion = "2.5.1"
    val appcompatVersion = "1.6.0"

    set(
        "imps", mapOf("kotlin" to "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0")
    )

    set(
        "kapts", mapOf("lifecycleCompiler" to "androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")
    )

    set(
        "apis", mapOf(
            "androidxAppcompat" to "androidx.appcompat:appcompat:$appcompatVersion",
            "androidxAppconpatResource" to "androidx.appcompat:appcompat-resources:$appcompatVersion",
            "androidKtx" to "androidx.core:core-ktx:1.9.0",
            "fragmentKtx" to "androidx.fragment:fragment-ktx:1.5.5",
            "constraintLayout" to "androidx.constraintlayout:constraintlayout:2.1.4",
            "lifecycleViewModel" to "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion",
            "lifecycleLiveData" to "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion",
            "lifecycleRuntime" to "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion",
            "lifecycleSaveState" to "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion",
            "lifecycleReactiveStream" to "androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleVersion",
            "lifecycleProcess" to "androidx.lifecycle:lifecycle-process:$lifecycleVersion",
            "lifecycleService" to "androidx.lifecycle:lifecycle-service:$lifecycleVersion",
            "googleMaterial" to "com.google.android.material:material:1.8.0",
            "androidxRecyclerView" to "androidx.recyclerview:recyclerview:1.2.1",
            "androidxViewPager" to "androidx.viewpager:viewpager:1.0.0",
            "androidxViewPager2" to "androidx.viewpager2:viewpager2:1.0.0",
            "kotlinCoroutines" to "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4",
            "retrofit" to "com.squareup.retrofit2:retrofit:2.9.0",
            "gsonConverter" to "com.squareup.retrofit2:converter-gson:2.9.0",
            "okLoggingInterceptor" to "com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.4",
            "mmkv" to "com.tencent:mmkv-static:1.2.15"
        )
    )
}