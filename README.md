# Footprint-ktx
Debug logger for Android-Kotlin

Usage
-----

 You just write `footprint()` logcat show `[ClassName#MethodName:LineNumber]`

 Please see examples
 [KTxExampleActivity](/app/src/main/java/com/morayl/footprintexample/KtxExampleActivity.kt)

Download
--------

```groovy
repositories {
    maven { url 'http://raw.github.com/morayl/Footprint/master/repository' }
}
dependencies {
    debugImplementation 'com.morayl:footprint-ktx:0.4.0'
}
```
This function is a little bit slow, because using `stack trace`.
So I recommend you to use only `debugImplementation` or switch enable with `configureFootprin()`.
I'll release `NoOp`.

ChangeLog
--------

 See [ChangeLog](./CHANGELOG.md)
 
### Footprint.java
Footprint.java has stopped development.
But you can use. ([Readme](./README_for_java.md) / [ChangeLog](./CHANGELOG_for_java.md)) 

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
