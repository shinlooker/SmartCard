package com.shinlooker.smartcard.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;


public final class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(@NonNull final Context context) {
        init((Application) context.getApplicationContext());
    }

    public static void init(@NonNull final Application app) {
        if (sApplication == null) {
            Utils.sApplication = app;
        }
    }

    public static Application getApp() {
        if (sApplication != null) {
            return sApplication;
        }
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object at = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(at);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            init((Application) app);
            return sApplication;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }

    public static boolean isEmpty(String... ss) {
        if (ss == null) {
            return true;
        }
        for (String s : ss) {
            if (s == null || s.length() < 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 字符串反转
     */
    public static String reverse(String str) {
        if (isEmpty(str)) {
            return null;
        }
        //String str="10203040506";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if ((i + 1) % 2 == 0) {
                //获取上一个
                char pre = str.charAt(i - 1);
                //当前的
                char current = str.charAt(i);
                sb.append(current).append(pre);
            }
        }
        //如果是奇数则再加上最后一位
        if (str.length() % 2 != 0) {
            sb.append(str.charAt(str.length() - 1));
        }
        return sb.toString();
    }
}
