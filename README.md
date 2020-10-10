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
    // current latest version is 0.1
	implementation 'com.github.kukyxs:CoMvvmHelper:release-version'
}
```

## How to use
then at your application, add
```kotlin
   startCov { // register your koin and retrofit params (if use both koin and retrofit at your project)
        loadEngine = GlideEngine() // your image load engine
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
   globalLoadEngine(GlideEngine()) // your image load engine

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
or nothing if not use koin or retrofit

## More detail usage is coming...

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