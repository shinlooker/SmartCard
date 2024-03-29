package com.shinlooker.demo;

import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OSUtils {
    private static final String ROM_MIUI = "MIUI";
    private static final String ROM_EMUI = "EMUI";
    private static final String ROM_FLYME = "FLYME";
    private static final String ROM_OPPO = "OPPO";
    private static final String ROM_SMARTISAN = "SMARTISAN";
    private static final String ROM_VIVO = "VIVO";
    private static final String ROM_QIKU = "QIKU";
    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";
    private static String sName;
    private static String sVersion;

    public OSUtils() {
    }

    public static boolean isEmui() {
        return check("EMUI");
    }

    public static boolean isMiui() {
        return check("MIUI");
    }

    public static boolean isVivo() {
        return check("VIVO");
    }

    public static boolean isOppo() {
        return check("OPPO");
    }

    public static boolean isFlyme() {
        return check("FLYME");
    }

    public static boolean is360() {
        return check("QIKU") || check("360");
    }

    public static boolean isSmartisan() {
        return check("SMARTISAN");
    }

    public static String getName() {
        if (sName == null) {
            check("");
        }

        return sName;
    }

    public static String getVersion() {
        if (sVersion == null) {
            check("");
        }

        return sVersion;
    }

    private static boolean check(String rom) {
        if (sName != null) {
            return sName.equals(rom);
        } else {
            if (!TextUtils.isEmpty(sVersion = getProp("ro.miui.ui.version.name"))) {
                sName = "MIUI";
            } else if (!TextUtils.isEmpty(sVersion = getProp("ro.build.version.emui"))) {
                sName = "EMUI";
            } else if (!TextUtils.isEmpty(sVersion = getProp("ro.build.version.opporom"))) {
                sName = "OPPO";
            } else if (!TextUtils.isEmpty(sVersion = getProp("ro.vivo.os.version"))) {
                sName = "VIVO";
            } else if (!TextUtils.isEmpty(sVersion = getProp("ro.smartisan.version"))) {
                sName = "SMARTISAN";
            } else {
                sVersion = Build.DISPLAY;
                if (sVersion.toUpperCase().contains("FLYME")) {
                    sName = "FLYME";
                } else {
                    sVersion = "unknown";
                    sName = Build.MANUFACTURER.toUpperCase();
                }
            }

            return sName.equals(rom);
        }
    }

    private static String getProp(String name) {
        BufferedReader input = null;

        Object var4;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            String line = input.readLine();
            input.close();
            return line;
        } catch (IOException var14) {
            var4 = null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException var13) {
                    var13.printStackTrace();
                }
            }

        }

        return (String) var4;
    }
}