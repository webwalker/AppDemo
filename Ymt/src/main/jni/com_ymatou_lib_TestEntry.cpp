#include <com_ymatou_lib_TestEntry.h>

JNIEXPORT jstring JNICALL Java_com_ymatou_lib_TestEntry_getStrFromC
  (JNIEnv* env, jclass cls)
{
    return env->NewStringUTF( "Hello from JNI !  Compiled with ABI .");
}