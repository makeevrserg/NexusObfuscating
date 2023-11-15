# proguard-rules.pro

-dontwarn kotlinx.**

-ignorewarnings
-keep public class com.makeevrserg.sample.SampleApi { *; }

-keep class com.makeevrserg.sample.SampleApiKt { *; }

-keepkotlinmetadata
-keep,allowobfuscation class com.makeevrserg.sample.** { *; }
