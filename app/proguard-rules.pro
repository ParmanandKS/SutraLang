# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-dontusemixedcaseclassnames
-verbose

# Preserve line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep all public and protected classes and members
-keep public class * {
    public <init>();
    public <methods>;
    protected <methods>;
}

# Keep Google Play Services
-keep class com.google.android.gms.** { *; }
-keep class com.google.android.gms.internal.** { *; }

# Keep all Activities and Services
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Keep all model classes
-keep class com.sutralang.ide.models.** { *; }

# Keep all adapters
-keep class com.sutralang.ide.adapters.** { *; }

# Keep engine
-keep class com.sutralang.ide.engine.** { *; }

# Remove logging
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}
