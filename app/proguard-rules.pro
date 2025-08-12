# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }

# Coil uses OkHttp/Coroutines - keep metadata
-keep class kotlinx.** { *; }
-keep class okhttp3.** { *; }