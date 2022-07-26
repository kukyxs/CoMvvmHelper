extra.apply {
    // google maven repository https://maven.google.com/web/index.html

    val lifecycleVersion = "2.5.0"
    val koinVersion = "3.2.0"
    val appcompatVersion = "1.4.2"

    set(
        "imps", mapOf(
            "kotlin" to "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.0"
        )
    )

    set(
        "kapts", mapOf(
            "lifecyclerCompiler" to "androidx.lifecycle:lifecycle-compiler:$lifecycleVersion"
        )
    )

    set(
        "apis", mapOf(
            // google maven repository
            "androidxAppcompact" to "androidx.appcompat:appcompat:$appcompatVersion",
            "androidxAppconpactResource" to "androidx.appcompat:appcompat-resources:$appcompatVersion",
            "androidKtx" to "androidx.core:core-ktx:1.7.0",
            "fragment_ktx" to "androidx.fragment:fragment-ktx:1.5.0",
            "constraintLayout" to "androidx.constraintlayout:constraintlayout:2.1.3",
            "lifecycleViewModel" to "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion",
            "lifecycleLiveData" to "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion",
            "lifecycleRuntime" to "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion",
            "lifecycleSaveState" to "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion",
            "lifecycleReactiveStream" to "androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleVersion",
            "lifecyclerProcess" to "androidx.lifecycle:lifecycle-process:$lifecycleVersion",
            "lifecyclerService" to "androidx.lifecycle:lifecycle-service:$lifecycleVersion",
            "googleMaterial" to "com.google.android.material:material:1.6.1",
            "androidxRecyclerView" to "androidx.recyclerview:recyclerview:1.2.1",
            "androidxViewPager2" to "androidx.viewpager:viewpager:1.0.0",

            // github repository
            "kotlinCoroutines" to "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0",
            "koinAndroid" to "io.insert-koin:koin-android:$koinVersion",
            "koinCompat" to "io.insert-koin:koin-android-compat:$koinVersion",
            "retrofit" to "com.squareup.retrofit2:retrofit:2.9.0",
            "gsonConverter" to "com.squareup.retrofit2:converter-gson:2.9.0",
            "okLogginInterceptor" to "com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.4",
            "mmkv" to "com.tencent:mmkv-static:1.2.13"

        )
    )
}