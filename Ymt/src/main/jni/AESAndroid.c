#include <string.h>
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include "memory.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_ymatou_lib_AESUtil
 * Method:    Encrypt
 */
/*********************加密*********************/
JNIEXPORT jint JNICALL Java_com_ymatou_lib_AESUtil_Encrypt(JNIEnv *env,
                                                                   jclass thiz, jbyteArray msg, jbyteArray key, jbyteArray cipher,
                                                                   jint length) {
    jbyte * pMsg = (jbyte*) (*env)->GetByteArrayElements(env, msg, 0);
    jbyte * pKey = (jbyte*) (*env)->GetByteArrayElements(env, key, 0);
    jbyte *pCipher = (jbyte*) (*env)->GetByteArrayElements(env, cipher, 0);

    if (!pMsg || !pKey || !pCipher) {
        return -1;
    }
    int flag = Encrypt(pMsg, pKey, pCipher, length); //加密函数
    (*env)->ReleaseByteArrayElements(env, msg, pMsg, 0);
    (*env)->ReleaseByteArrayElements(env, key, pKey, 0);
    (*env)->ReleaseByteArrayElements(env, cipher, pCipher, 0);
    return flag;
}

/*
 * Class:     com_ymatou_lib_AESUtil
 * Method:    Decrypt
 */
/*********************解密*********************/
JNIEXPORT jint JNICALL Java_com_ymatou_lib_AESUtil_Decrypt(JNIEnv *env,
                                                                   jclass thiz, jbyteArray cipher, jbyteArray key, jbyteArray result,
                                                                   jint length) {
    jbyte * pCipher = (jbyte*) (*env)->GetByteArrayElements(env, cipher, 0);
    jbyte * pKey = (jbyte*) (*env)->GetByteArrayElements(env, key, 0);
    jbyte *pResult = (jbyte*) (*env)->GetByteArrayElements(env, result, 0);

    if (!pResult || !pKey || !pCipher) {
        return -1;
    }
    int flag = Decrypt(pCipher, pKey, pResult, length); //解密函数
    (*env)->ReleaseByteArrayElements(env, result, pResult, 0);
    (*env)->ReleaseByteArrayElements(env, key, pKey, 0);
    (*env)->ReleaseByteArrayElements(env, cipher, pCipher, 0);
    return flag;
}

#ifdef __cplusplus
}
#endif

