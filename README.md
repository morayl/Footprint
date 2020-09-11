# Footprint
Debug logger for Android-Kotlin

Usage
-----

 You just write `footprint()`, logcat show `[ClassName#MethodName:LineNumber]`.  
 Footprint has more useful functions, log multiple params, json, stacktrace, pair values, etc.  
 Please see examples [KtxExampleActivity.kt](/app/src/main/java/com/morayl/footprintexample/KtxExampleActivity.kt).  
 You can see library methods [Footprint.kt](/footprint-ktx/src/main/java/com/morayl/footprintktx/Footprint.kt).

Download
--------

```groovy
repositories {
    maven { url 'http://raw.github.com/morayl/Footprint/master/repository' }
}
dependencies {
    debugImplementation 'com.morayl:footprint-ktx:1.0.0'
}
```
Major functions are a little bit slow, because using `stack trace`.  
Recommend using only `debugImplementation` or switch enable using `configureFootprint()`.  
(Or please wait releasing `NoOp`.)

ChangeLog
--------

 See [ChangeLog](./CHANGELOG.md)
 
### Footprint.java
Footprint.java has stopped development.  
But you can use. ([Readme](./README_for_java.md) / [ChangeLog](./CHANGELOG_for_java.md)).  
In Java, you "can" use Footprint-ktx to write FootprintKt.~ but it's not useful.

License
--------

    Copyright 2020 morayl

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
