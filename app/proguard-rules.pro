-keep class com.fibbot.** { *; }
-keep class com.fibbot.models.** { *; }
-keep class com.fibbot.api.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.<Method> <methods>;
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Gson
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Room
-keep class androidx.room.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase { *; }

# Coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }

# Serialization
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}