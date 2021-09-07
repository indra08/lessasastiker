-keepattributes Signature

-keepattributes *Annotation*

#Sangat Penting Untuk Keamanan Kode biar ga gampang di baca
#Start Ganti Package Dibawah ini
-keepclassmembers class com.testing.testung.model.** {
*;
}
-keep class com.testing.testung.provider.StickerContentProvider{
*;
}
-keep class com.testing.testung.MainActivity{*;}
#Sampe Sini


#Billing
-keep class com.android.vending.**{
*;
}

# For Google Play Services
-keep class com.google.** {*;}


# For mediation
-keepattributes *Annotation*

# Other required classes for Google Play Services
# Read more at http://developer.android.com/google/play-services/setup.html
-keep class * extends java.util.ListResourceBundle {
   protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
   public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
   @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
   public static final ** CREATOR;
}

-keep class persistence.** {
  *;
}


-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

-keep class com.google.firebase.quickstart.database.java.viewholder.** {
    *;
}

-keepclassmembers class com.google.firebase.quickstart.database.java.models.** {
    *;
}

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }