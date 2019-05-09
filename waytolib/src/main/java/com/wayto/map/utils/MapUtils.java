package com.wayto.map.utils;

import android.location.Location;

/**
 * 地图上常用工具
 * <p>
 * author: hezhiWu <wuhezhi007@gmail.com>
 * version: V1.0.0
 * created at 2017/3/14 10:47
 * <p>
 * Copyright (c) 2017 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
 */
public class MapUtils {
    private static final  double EARTH_RADIUS_M = 6378137;//赤道半径(单位m)
    /**
     * 计算两点间距离(单位M)
     * 精度不够，结果不准确，请使用{@link #LantitudeLongitudeDist}
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    @Deprecated
    public static double distanceBetween(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }

    /**
     * 基于余弦定理求两经纬度距离 (精度180m)
     * @param lon1 第一点的经度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的经度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位km
     * */
    public static double LantitudeLongitudeDist(double lon1, double lat1,double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);

        double radLon1 = rad(lon1);
        double radLon2 = rad(lon2);

        if (radLat1 < 0)
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south
        if (radLat1 > 0)
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north
        if (radLon1 < 0)
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west
        if (radLat2 < 0)
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south
        if (radLat2 > 0)
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north
        if (radLon2 < 0)
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west
        double x1 = EARTH_RADIUS_M * Math.cos(radLon1) * Math.sin(radLat1);
        double y1 = EARTH_RADIUS_M * Math.sin(radLon1) * Math.sin(radLat1);
        double z1 = EARTH_RADIUS_M * Math.cos(radLat1);

        double x2 = EARTH_RADIUS_M * Math.cos(radLon2) * Math.sin(radLat2);
        double y2 = EARTH_RADIUS_M * Math.sin(radLon2) * Math.sin(radLat2);
        double z2 = EARTH_RADIUS_M * Math.cos(radLat2);

        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)+ (z1 - z2) * (z1 - z2));
        //余弦定理求夹角
        double theta = Math.acos((EARTH_RADIUS_M * EARTH_RADIUS_M + EARTH_RADIUS_M * EARTH_RADIUS_M - d * d) / (2 * EARTH_RADIUS_M * EARTH_RADIUS_M));
        double dist = theta * EARTH_RADIUS_M;
        return dist;
    }

    /**
     * 转化为弧度(rad)
     * */
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }


    /**
     * 两点的速度
     * @param distance 距离(单位M)
     * @param time 时间（ms）
     * @return
     */
    public static float speedBetween(double distance,long time){
        double dis=distance*1000;
        long t=1000*60*60;
        float speed=(float) dis/t;
        return speed;
    }
}
