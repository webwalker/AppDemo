# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/sky/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

#----------------------------基础配置--------------------------------
#-printmapping proguardMapping.txt #日志
-optimizationpasses 5 #指定代码的压缩级别
#-ignorewarnings #忽略所有警告
#-dontobfuscate
-dontusemixedcaseclassnames #不混淆出形形色色的类名
#-dontskipnonpubliclibraryclasses #不去忽略非公共的库类
#-dontskipnonpubliclibraryclassmembers #指定不去忽略包可见的库类的成员
#-dontshrink #不压缩输入的类文件
#-dontoptimize #不优化输入的类文件
-dontpreverify #预校验
-verbose #混淆时是否记录日志
#-printseeds seeds.txt #列出类和成员
#-printmapping mapping.txt #映射文件
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/* #混淆时所采用的算法
#----------------------------系统类--------------------------------
#-allowaccessmodification
#--------保护注解--------
-keepattributes *Annotation*,InnerClasses,Exceptions,Deprecated
-keepattributes Signature,EnclosingMethod #避免混淆泛型 如果混淆报错建议关掉
-keepattributes SourceFile,LineNumberTable
#--------Java--------
-dontwarn javax.inject.**
-keep class javax.** { *; }
#-keep class * extends java.lang.annotation.Annotation {*;}
#--------自定义控件--------
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#--------基础组件--------
-keep public class * extends android.app.Fragment
-keep public class * extends com.ymatou.shop.reconstract.base.BaseFragment
-keep public class * extends com.ymatou.shop.reconstract.base.BaseBridgeFragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
#--------support--------
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep class com.android.test.runner.* { *; }
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }
-dontwarn android.support.**    # 忽略警告
#--------Parcelable--------
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
#--------Serializable--------
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable { #Serializable&enum
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}
#--------R--------
-keepclassmembers class **.R$* {
    public static <fields>;
}
#--------Native--------
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
}
-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}
#----------------------------第三方-Jars文件--------------------------------
#--------alipaysdk--------
-dontwarn com.talkingdata.sdk.**
-dontwarn com.tendcloud.appcpa.**
-keep class com.talkingdata.sdk.** { *; }
-keep class com.tendcloud.appcpa.** { *; }
#--------alipaysdk--------
-dontwarn com.alipay.**
-dontwarn com.ta.utdid2.**
-dontwarn com.ut.device.**
-dontwarn org.json.alipay.**
-keep class com.alipay.** { *; }
-keep class com.ta.utdid2.** { *; }
-keep class com.ut.device.** { *; }
-keep class org.json.alipay.** { *; }
#--------AndroidSwipeLayout--------
-dontwarn com.daimajia.swipe.**
-keep class com.daimajia.swipe.** { *; }
#--------BaiduLBS--------
-dontwarn com.baidu.**
-keep class com.baidu.** { *; }
#--------butterknife--------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keep class **$$ViewInjector { *; }
-keepclasseswithmembernames class * { @butterknife.Bind *;}
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#--------eventbus--------
-dontwarn de.greenrobot.event.**
-keep class de.greenrobot.event.** { *; }
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}
-keepclassmembers class * {
    void *(**On*Event);
}
-keepclassmembers class ** {
    public void onEventMainThread(**);
}
#--------Getui--------
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}
#--------glide--------
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.**{*;}
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#    **[] $VALUES;
#    public *;
#}
#--------libammsdk--------
-dontwarn com.tencent.**
-keep class com.tencent.** { *; }
#--------mframework--------
-dontwarn m.framework.**
-keep class m.framework.** { *; }
#--------ormlite--------
-dontwarn com.j256.ormlite.**
-keep class com.j256.ormlite.** { *; }
#--------umeng--------
-dontwarn com.umeng.**
-keep class com.umeng.** { *; }
-keep class com.umeng.analytics.** { *; }
-keep class com.umeng.fb.** { *; }
-keep class com.umeng.update.** { *; }
#--------image-loader--------
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }
#--------image-loader--------
-dontwarn com.ymt.tracker.**
-keep class com.ymt.tracker.** { *; }
#--------ymt-download--------
-dontwarn com.liulishuo.filedownloader.**
-keep class com.liulishuo.filedownloader.** { *; }
#--------cmbkeyboard--------
-dontwarn cmb.pb.**
-keep class cmb.pb.** { *; }
#--------nineoldandroids--------
-dontwarn com.nineoldandroids.**
-keep class com.nineoldandroids.** { *; }
#--------友盟一键分享--------
-dontwarn com.mob.commons.**
-keep class com.mob.commons.** { *; }
-dontwarn com.mob.tools.**
-keep class com.mob.tools.** { *; }
-dontwarn cn.sharesdk.**
-keep class cn.sharesdk.** { *; }
-dontwarn com.sina.**
-keep class com.sina.** { *; }
#--------趣拍--------
-dontwarn com.duanqu.qupai.**
-keep class com.duanqu.qupai.** { *; }
#----------------------------第三方-Module---------------------------------
-keep class com.google.android.cameraview.** { *; }
-keep class com.rockerhieu.** { *; }
-keep class com.viewpagerindicator.** { *; }
-keep class jp.co.cyberagent.android.gpuimage.** { *; }
-keep class it.sephiroth.android.library.** { *; }
-keep class com.imagezoom.** { *; }
-dontwarn com.netease.**
-keep class com.netease.** { *; }
-keep class cn.sharesdk.onekeyshare.** { *; }
-keep class com.handmark.pulltorefresh.library.** { *; }
#----------------------------第三方-Gradle依赖----------------------------
#--------Gson--------
-keep class com.google.** {*;}
#-keep class com.google.gson.stream.** {*;}
#--------OkHttp3--------
-dontwarn okhttp3.**
-keep class okhttp3.** {*;}
-dontwarn okio.**
-keep class okio.**{*;}
#--------retrofit--------
-dontwarn retrofit2.**
#-dontnote retrofit2.Platform
#-dontnote retrofit2.Platform$IOS$MainThreadExecutor
#-dontwarn retrofit2.Platform$Java8
-keep class retrofit2.** { *; }
#--------RxJava RxAndroid--------
-dontwarn sun.misc.**
-dontwarn rx.**
-keep class rx.**{*;}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#--------upyun--------
-dontwarn com.upyun.library.**
-keep class com.upyun.library.** { *; }
#--------jcvideoplayer--------
-dontwarn fm.jiecao.jcvideoplayer_lib.**
-keep class fm.jiecao.jcvideoplayer_lib.** { *; }
#--------jcvideoplayer--------
-dontwarn com.fasterxml.jackson.**
-keep class com.fasterxml.jackson.** { *; }
#--------dagger--------
-dontwarn dagger.**
-keep class dagger.** { *; }
#----------------------------实体类--------------------------------
-keep class com.ymt.framework.http.model.BaseResult { *; }
-keep class * extends com.ymt.framework.http.model.BaseResult { *; }
-keep class * extends com.ymt.framework.http.model.NewBaseResult { *; }
-keep class com.ymatou.shop.**$* {
    *;
}
-keep class com.ymatou.shop.**$*$* {
    *;
}
-keep class com.ymatou.shop.**$*$*$* {
    *;
}
#-keep class com.ymatou.shop.reconstract.mine.attention.model.**$* {
#    *;
#}
-keep class com.ymt.framework.web.bridge.model.**{*;}
-keep class com.ymt.framework.web.bridge.params.**{*;}
-keep class com.ymt.framework.web.model.**{*;}
-keep class com.ymt.framework.web.cache.model.**{*;}
#----------------------------Web--------------------------------
-keepattributes *JavascriptInterface*
-keep class com.ymt.framework.web.YmtSdk { *; }
-dontwarn android.webkit.WebView
-keep class android.webkit.** { *; }
-keepclassmembers class com.ymt.framework.web.BaseWebView {
   public *;
}
#----------------------------其他保护--------------------------------
-keep class * extends com.antfortune.freeline.**{*;}
-keep class com.ymatou.shop.reconstract.base.utils.YMTGlideModule

-dontwarn demo.**
-keep class demo.** { *; }
-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforge.pinyin4j.** { *; }
-dontwarn com.hp.hpl.sparta.**
-keep class com.hp.hpl.sparta.** { *; }