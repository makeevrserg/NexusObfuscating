# proguard-rules.pro

-dontwarn kotlinx.**

-ignorewarnings
-keep public class com.makeevrserg.sample.SampleApi { *; }
-keep class com.makeevrserg.sample.SampleApiKt { *; }

-keep public class com.makeevrserg.sample.JvmOnlyInterface { *; }
-keep public class com.makeevrserg.sample.JvmOnlyInterfaceKt { *; }

-keepkotlinmetadata
-keep,allowobfuscation class com.makeevrserg.sample.** { *; }
