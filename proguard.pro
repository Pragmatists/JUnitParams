-dontoptimize
-dontobfuscate

# See https://www.guardsquare.com/en/proguard/manual/examples#library

-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,
                SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keep public class junitparams.** {
      public protected *;
}

-keepclassmembernames class junitparams.** {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

-keepclasseswithmembernames,includedescriptorclasses class junitparams.** {
    native <methods>;
}

-keepclassmembers,allowoptimization enum junitparams.** {
    public static **[] values(); public static ** valueOf(java.lang.String);
}

-keepclassmembers class junitparams.** implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# JUnitParams related ignorables (reflective access)
-dontnote junitparams.internal.parameters.ParametersFromCustomProvider$ParametersProviderFactory

# Guava related ignorables
-dontwarn com.google.j2objc.annotations.Weak
-dontwarn java.lang.ClassValue
-dontwarn java.lang.SafeVarargs
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn sun.misc.Unsafe

# Guava related ignorables (reflective access - generally pertaining to Android)
-dontnote repackaged.com.google.common.base.Throwables
-dontnote repackaged.com.google.common.base.internal.Finalizer
-dontnote repackaged.com.google.common.cache.Striped64
-dontnote repackaged.com.google.common.cache.Striped64$Cell
-dontnote repackaged.com.google.common.util.concurrent.AbstractFuture$UnsafeAtomicHelper
-dontnote repackaged.com.google.common.util.concurrent.MoreExecutors
