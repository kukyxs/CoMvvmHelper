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
	implementation 'com.github.kukyxs:CoMvvmHelper:release-version'
}
```

## How to use
then at your application, add
```kotlin
   startCov { // register your koin and retrofit params (if use both koin and retrofit at your project)
        koinModules = mutableListOf(viewModelModule) // your koin modules
        baseUrl = "https://www.google.com" // your retrofit base url if use
//      koinPropertiesFile = "" // koin properties file
//      client = OkHttpClient.Builder().build() // OkHttp or Retrofit client
//      customRetrofitCallAdapterArray = mutableListOf() // your retrofit call adapters if use
//      customRetrofitConverterFactoryArray = mutableListOf() // your retrofit converter factories if use
   }
```
or 
```kotlin
   koinInit { // register your koin params (if use koin at your project)
       koinPropertiesFile = ""
       koinModules = mutableListOf(viewModelModule)
   }

   globalHttpClient { // register your retrofit params (if use retrofit at your project)
       baseUrl = "https://www.google.com"
       client = OkHttpClient.Builder().build()
       customCallAdapter = mutableListOf()
       customConvertAdapter = mutableListOf()
   }
```

# More detail usage is coming...