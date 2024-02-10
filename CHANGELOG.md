Change Log
==========

Version 2.0.0 *(2024-02-10)*
----------------------------

### **Importance notice**
The module ID of the Footprint library will be changed from `footprint-ktx` to `footprint`.
All methods will be deprecated in footprint-ktx:2.0.0.

#### **Migration guide**
Change dependencies and imports.
1. Delete the repository maven definition for Footprint below.
```groovy
repositories {
    maven { url 'http://raw.github.com/morayl/Footprint/master/repository' }
}
```

2. (If there is no definition) Add mavenCentral definition for repository.
```groovy
repositories {
    mavenCentral()
}
```

3. Change library name.
```groovy
// Before
debugImplementation 'com.morayl:footprint-ktx:2.0.0'
releaseImplementation 'com.morayl:footprint-ktx-noop:2.0.0'
// After(Latest version recommended)
debugImplementation 'io.github.morayl:footprint:2.0.0'
releaseImplementation 'io.github.morayl:footprint-noop:2.0.0'
```

4. Fix imports.
```kotlin
// Before
import com.morayl.footprintktx.*
// After
import com.morayl.footprint.*
```

Other updates
 * Update compile sdk version to 28 -> 33.
 * Update gson 2.9.0 -> 2.10.1.

Version 1.2.0 *(2022-11-25)*
----------------------------

 * New: Added links to the line of code. You can jump to the line of code when you press it.

Version 1.1.0 *(2021-02-27)*
----------------------------

 * New: NoOp Library `footprint-ktx-noop` released. It is useful in releaseImplementation.
 * New: `withJsonFootprint` and `withFootprint` are able to receive "block (T) -> Any?".
 * New: `withSimpleFootprint`.

Version 1.0.1 *(2021-02-24)*
----------------------------

 * Fix: Changed to consider nullable of StackTraceElement#getFileName.

Version 1.0.0 *(2020-09-11)*
----------------------------

 * Stable release with examples and library comment.

Version 0.4.0 *(2020-09-07)*
----------------------------

 * Initial release.
 * Function development for 1.0.0 has almost completed.
 * Library and example comment preparing has not completed.
