Change Log
==========

Version 1.5.0 *(2024-02-10)*
----------------------------

### **Importance notice**
The module ID of the Footprint library for java will be changed from `footprint` to `footprint-java`.
All methods will be deprecated in footprint:1.5.0.

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
debugImplementation 'com.morayl:footprint:1.5.0'
// After(Latest version recommended)
debugImplementation 'io.github.morayl:footprint-java:1.5.0'
```

4. Fix imports.
```java
// Before
import com.morayl.footprint.Footprint;
// After
import com.morayl.footprint.java.Footprint;
```

Other updates
 * Update compile sdk version to 28 -> 33.
 * Update gson 2.8.1 -> 2.10.1.

Version 1.4.0 *(2019-01-20)*
----------------------------

 * NEW: Update sdk version to 28.


Version 1.3.0 *(2017-09-10)*
----------------------------

 * FIX: `dialog` method can't show String or primitive value.
 * NEW: `notify` method can show notification with time, line and messages. This method available Android OS 16 or later.


Version 1.2.1 *(2016-10-25)*
----------------------------

 * modify `AndroidManifest.xml`. Delete <application> definition.


Version 1.2.0 *(2016-10-23)*
----------------------------

 * NEW: `setEnable` method can change all public log method enable.


Version 1.1.0 *(2016-10-22)*
----------------------------

 * `changeLogTag` becomes `setLogTag`.
 * modify Javadoc.
 * NEW: `dialog` method can show dialog and message. It's useful when you leave long-long message which is too long to display using logcat.


Version 1.0.1 *(2016-10-22)*
----------------------------

 * NEW: Javadoc.


Version 1.0.0 *(2016-07-03)*
----------------------------

Initial release.
