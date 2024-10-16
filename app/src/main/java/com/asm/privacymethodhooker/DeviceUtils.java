package com.asm.privacymethodhooker;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 设备相关工具类
 *
 * @author Guiwen.Chen
 */
public class DeviceUtils {


    /**
     * 密度转换像素
     *
     * @param pDipValue dp值
     * @return 像素
     */
    public static float dip2fpx(int pDipValue) {
        return dip2fpx(App.appContext, pDipValue);
    }

    /**
     * 密度转换像素
     *
     * @param pDipValue dp值
     * @return 像素
     */
    public static float dip2fpx(Context context, float pDipValue) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return pDipValue * dm.density;
    }

}
