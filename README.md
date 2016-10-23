# Footprint
Debug logger for Android

Usage
-----

 You just write, `Footprint.leave();`

 Please see examples
 [ExampleActivity](/app/src/main/java/com/morayl/footprintexample/ExampleActivity.java)

Download
--------

```groovy
repositories {
    maven { url 'http://raw.github.com/morayl/Footprint/master/repository' }
}
dependencies {
    debugCompile 'com.morayl:footprint:1.2.0'
}
```
This function is a little bit slow, because using `stack trace`.
So I recommend you to use only `debugCompile`

License
-------

    Copyright 2016 morayl

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
