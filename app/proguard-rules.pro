-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep public class * extends androidx.compose.**

# Retrofit
-keepclasseswithmembernames class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# GSON
-keepclassmembers class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Timber
-dontwarn org.jetbrains.annotations.**