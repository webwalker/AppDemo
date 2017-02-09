package com.webwalker.framework.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is used as a Json utility.
 * The base functionality comes from the Gson
 */
public final class JsonUtil {
    /**
     * Null serialize is used because else Gson will ignore all null fields.
     */
    public static Gson gson;

    static {
        gson = new GsonBuilder().serializeNulls().setDateFormat(DateUtil.ISO_DATETIME_FORMAT_SORT).create();
    }

    private JsonUtil() {
    }

    /**
     * To Json Converter using Goolge's Gson Package
     * <p/>
     * this method converts a simple object to a json string
     *
     * @param obj
     * @return a json string
     */

    /**
     * Converts a map of objects using Google's Gson Package
     *
     * @param map
     * @return a json string
     */
    public static String toJson(final Map<String, ?> map) {
        return gson.toJson(map);
    }

    /**
     * Converts a collection of objects using Google's Gson Package
     *
     * @param list
     * @return a json string array
     */
    public static <T> String toJson(final List<T> list) {
        return gson.toJson(list);
    }

    /**
     * Returns the specific object given the Json String
     *
     * @param <T>
     * @param jsonString
     * @param obj
     * @return a specific object as defined by the user calling the method
     */
    public static <T> T fromJson(final String jsonString, final Class<T> classOfT) {
        try {
            return gson.fromJson(jsonString, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJson(final String jsonString, Type type) {
        try {
            return gson.fromJson(jsonString, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> fromJsonToList(final JsonArray jsonArray, final Class<T> classOfT) {
        List<T> list = new ArrayList<>();

        try {
            for (int i = 0, size = jsonArray.size(); i < size; i++) {
                //Logger.error(jsonArray.get(i).toString());
                list.add(gson.fromJson(jsonArray.get(i), classOfT));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }

    public static <T> T fromJson(final JsonObject jsonObject, final Class<T> classOfT) {
        try {
            return gson.fromJson(jsonObject, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a list of specified object from the given json array
     *
     * @param <T>
     * @param jsonString
     * @param type       the type defined by the user
     * @return a list of specified objects as given in the json array
     */
    public static <T> List<T> fromJsonToList(final String jsonString, final Type type) {
        try {
            return gson.fromJson(jsonString, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T, V> Map<T, V> fromJsonToMap(final String jsonString, final Type type) {
        try {
            return gson.fromJson(jsonString, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a list of specified object from the given json reader
     *
     * @param <T>
     * @param json
     * @param type the type defined by the user
     * @return a list of specified objects as given in the json array
     */
    public static <T> T fromJson(final JsonReader json, final Type type) {
        try {
            return gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * To Json Converter using Goolge's Gson Package
     * <p>
     * this method converts a simple object to a json string
     *
     * @param obj
     * @return a json string
     */
    public static <T> String toJson(final T obj) {
        try {
            return JsonUtil.gson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}


