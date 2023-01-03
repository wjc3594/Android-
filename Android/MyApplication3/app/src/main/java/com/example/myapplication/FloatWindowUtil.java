package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by wjc on 2021/3/17.
 */
public class FloatWindowUtil {
    public static boolean disableShowPermissionPopup;//是否展示悬浮窗权限开启弹窗
    private static WindowManager windowManager;
    private static View mView;

    public static void showFloatingWindow(Context context, View view, WindowManager.LayoutParams params) {
        mView = view;
        show(context, view, params);
    }

    public static boolean checkOverlayPermission(final Context context) {
        /**
         * 检测是否开启了悬浮窗权限
         */
        if (FloatWindowUtil.canDrawOverlays(context)) {
            return true;
        }
        return false;
    }

    public static void remove(Context context, View view) {
        mView = null;
        getWindowManager(context).removeView(view);
    }

    private static void show(Context context, View view, WindowManager.LayoutParams params) {
        getWindowManager(context).addView(view, params);
    }

    /**
     * 获取windowManager
     *
     * @param context
     * @return
     */
    public static WindowManager getWindowManager(Context context) {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }

    /**
     * 获取浮窗view
     *
     * @return
     */
    public static View getView(Context context) {
        return mView;
    }

    private static boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        else if (Settings.canDrawOverlays(context)) return true;
        else return false;
    }
}
