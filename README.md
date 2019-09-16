# Kotlin Android `hexutils` Library


The `hexutils` library provides functions to convert between printable hex characters and bytes


## What's included
- Android library
- Jvm library
- Dokka documentation


## Getting started

### Create a Dependency to the library

- The library is available from Github and Artifactory.
- First setup the repository (local or Artifactory) - see below
- Import by adding the following dependencies

```
dependencies {
    // Use for JVM
    // implementation 'com.ingonoka:hexutilslib-jvm:v1.0_RC1'
    // implementation 'com.ingonoka:hexutilslib-jvm:v1.0_RC1:javadoc'
    // Use for Android (no debug info)
    // implementation 'com.ingonoka:hexutilslib-android:v1.0_RC1'
    // implementation 'com.ingonoka:hexutilslib-android:v1.0_RC1:javadoc'
    // Use for Android (with debug info)
    // implementation 'com.ingonoka:hexutilslib-android-debug:v1.0_RC1'
    // implementation 'com.ingonoka:hexutilslib-android-debug:v1.0_RC1:javadoc'

}
```

### Import from local Maven repository
. Clone the Github repository
. Compile and publish it in your local Maven repository (`~/.m2/repository`) by executing the `publishToMavenLocal` Gradle tasks

```
./gradlew clean cleanBuildCache publishToMavenLocal
```
. Add  `mavenLocal` to your repository in `build.gradle`

```groovy
repositories {
    mavenLocal()  
}
```


### Import from Artifactory repository
Artifactory is a repository server software that can be setup locally or on your own cloud server
instance.

- Update file `gradle.build` in module directory

```
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.dsl.ResolverConfig

plugins {
    id("com.jfrog.artifactory") version "4.9.1"
}

artifactory {
    setContextUrl("http://209.97.175.64:8081/artifactory")
    val repoKey = BuildConfig.artifactoryRepoKey

    resolve(delegateClosureOf<ResolverConfig> {
        setProperty("repoKey", repoKey)
        setProperty("username", <USER NAME>)
        setProperty("password", <PASSWORD>)
        setProperty("maven", true)
    })
}
```

## Usage

The library functions are provided as extension functions to `ByteArray`, `Byte`
and `String`.

### Converting a `ByteArray` to a printable hex string

- Convert [ByteArray] into string of hex characters.  Each Byte is separated by ", " and prefixed with "0x"


```
byteArrayOf(1,2).toHex(brackets = true) // [ 0x01, 0x02 ]
byteArrayOf(1,2).toHex(brackets = false) // 0x01 0x02
```

- Convert [ByteArray] into string of hex characters.  Each Byte is separated by " "

```
byteArrayOf(1,2).toHexShort() // 01 02
```

- Convert [ByteArray] into string of hex characters.  Bytes are not separated

```
byteArrayOf(1,2).toHexShortShort() // 0102
```

- Convert a ByteArray into lines of [columns] bytes


```
val ba = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 0x31 , 0x32, 0x33)

ba.toHexChunked(printable = false)
// 00   01 02 03 04 05 06 07 08 09 00 01 02 03 04 05 06
// 01   01 02 03

ba.toHexChunked(printable = true)
// 00   01 02 03 04 05 06 07 08 09 00 01 02 03 04 05 06    ................
// 01   31 32 33                                           123

ba.toHexChunked(printable = true, lineNums = false)
// 01 02 03 04 05 06 07 08 09 00 01 02 03 04 05 06    ................
// 31 32 33                                           123

ba.toHexChunked(printable = true, lineNums = true, columns = 10)
// 01 02 03 04 05 06 07 08 09 00    ..........
// 02 02 03 04 05 06 31 32 33       .....123
```

### Convert a `Byte` into a printable hex String

```
(0x10).toHex() // 10
(0x10).toHex("0x") // 0x10
```

### Convert a string of hex characters to a `ByteArray`
If `remove` is set to `true` then the following characters and character sequences will be removed before the string is converted: space,comma, square brackets and "0x"

```
"01 02 03 04".hexToBytes(removeSpace = true) // byteArrayOf(1, 2, 3 ,4)

"010203".hexToBytes() // byteArrayOf(1,2,3)
```

*Removing spaces is done with regular expressions and is almost 30% slower and should be avoided*



## Stability

This software is considered **Beta**.


## Roadmap



## Contact

This project is maintained by link:https://github.com/ingonoka[Ingo Noka]


## License

[link=http://creativecommons.org/licenses/by-nc-nd/4.0/]
image::https://i.creativecommons.org/l/by-nc-nd/4.0/88x31.png[]

This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
