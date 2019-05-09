package com.wayto.map.utils;

import android.content.Context;
import android.location.LocationManager;

/**
 * author: hezhiWu <wuhezhi007@gmail.com>
 * version: V1.0.0
 * created at 2017/3/14 10:46
 * <p>
 * Copyright (c) 2017 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
 */
public class GPSUtils {
    /**
     * 判断GPS打开状态
     *
     * @param context
     * @return
     */
    public static boolean isOpenGps(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean on = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return on;
    }
}
