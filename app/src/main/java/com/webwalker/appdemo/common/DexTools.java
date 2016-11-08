package com.webwalker.appdemo.common;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class DexTools {
    private static HashSet<File> loadDex = new HashSet<>();

    static {
        loadDex.clear();
    }

    public static void loadFixedDex(Context context) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        if (context == null) return;
        File fileDir = context.getDir(Consts.DEX_DIR, Context.MODE_PRIVATE);
        File[] listFile = fileDir.listFiles();
        for (File file : listFile) {
            if (file.getName().startsWith("classes") || file.getName().endsWith(".dex")) {
                loadDex.add(file);
            }
        }
        doDexInject(context, fileDir, loadDex);
    }

    public static void doDexInject(Context app, File filesDir, HashSet<File> loadDex) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        String optimizeDir = filesDir.getAbsolutePath() + File.separator + "opt_dex";
        File fopt = new File(optimizeDir);
        if (!fopt.exists()) {
            fopt.mkdirs();
        }
        for (File dex : loadDex) {
            DexClassLoader classLoader = new DexClassLoader(dex.getAbsolutePath(), fopt.getAbsolutePath(), null, app.getClassLoader());
            inject(classLoader, app);
        }
    }

    public static Object geField(Object obj, Class<?> clzz, String field) throws NoSuchFieldException,
            IllegalAccessException {
        Field localField = clzz.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    public static Object getPathList(Object baseDexLoader) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Object object = geField(baseDexLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
        return object;
    }

    public static void setField(Object obj, Class<?> clzz, String field, Object values) throws
            NoSuchFieldException, IllegalAccessException {
        Field localField = clzz.getDeclaredField(field);
        localField.setAccessible(true);
        localField.set(obj, values);
    }

    public static void inject(DexClassLoader loder, Context context) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        Object dexElements = combineArray(getDexElements(getPathList(loder)), getDexElements(getPathList(pathClassLoader)));
        Object pathList = getPathList(pathClassLoader);
        setField(pathList, pathList.getClass(), "dexElements", dexElements);
    }

    public static Object getDexElements(Object paramsObject) throws NoSuchFieldException, IllegalAccessException {
        Object filed = geField(paramsObject, paramsObject.getClass(), "dexElements");
        return filed;
    }

    public static Object combineArray(Object firstArr, Object secondArr) {
        Class<?> componentType = firstArr.getClass().getComponentType();
        int i = Array.getLength(firstArr);
        int j = i + Array.getLength(secondArr);
        Object result = Array.newInstance(componentType, j);
        for (int k = 0; k < j; k++) {
            if (k < i) {
                Array.set(result, k, Array.get(firstArr, k));
            } else {
                Array.set(result, k, Array.get(secondArr, k - i));
            }
        }
        return result;
    }
}
