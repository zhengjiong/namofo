# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zhengjiong/android/sdk_5.1.1/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-allowaccessmodification
-repackageclasses
-dontusemixedcaseclassnames
-dontoptimize
-dontshrink
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-ignorewarnings

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.v13.**

#annotation
-dontwarn android.support.annotation.**
-keep class android.support.annotation.**{*;}
-keep public class * extends android.support.annotation.**

#multidex
-dontwarn android.support.multidex.**
-keep class android.support.multidex.**{*;}
-keep public class * extends android.support.multidex.**

#drawable
-dontwarn android.support.graphics.drawable.**
-keep class android.support.graphics.drawable.**{*;}
-keep public class * extends android.support.graphics.drawable.**

#eventBus
-keep class org.greenrobot.** {*;}
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keepclassmembers class ** {
    public void onEvent*(**);
}
-keepclassmembers class ** {
    public void onEventMainThread(**);
}

#--------------------------------------Retrofit  start-------------------------------
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
#--------------------------------------Retrofit  end--------------------------------------


#--------------------------------------Glide  start-------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#--------------------------------------Glide  end--------------------------------------



#--------------------------------------greendao  start-------------------------------
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use Rx:
-dontwarn rx.**
#--------------------------------------greendao  end-------------------------------


#--------------------------------------七牛云直播 start------------------------------
-keep class com.pili.pldroid.player.**
-keep class tv.danmaku.ijk.media.player.** {*;}
#--------------------------------------七牛云直播 end------------------------------

#--------------------------------------okhttp3 start------------------------------
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.**{*;}
-keep public class * extends com.squareup.okhttp.**
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-keep public class * extends okhttp3.**
#--------------------------------------okhttp3 end------------------------------


-dontwarn com.namofo.radio.entity.**
-keep class com.namofo.radio.entity.**{*;}
-keep public class * extends com.namofo.radio.entity.**


#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}