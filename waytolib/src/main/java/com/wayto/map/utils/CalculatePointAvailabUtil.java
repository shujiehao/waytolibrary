package com.wayto.map.utils;

import com.wayto.map.entity.PointEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 计算定位点是否有效
 * <p>
 * author: hezhiWu <wuhezhi007@gmail.com>
 * version: V1.0.0
 * created at 2017/3/14 10:46
 * <p>
 * Copyright (c) 2017 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
 */
public class CalculatePointAvailabUtil {
    private static final String TAG = "CalculatePointAvailabUtil";
    //参样点[10个点]
    private static List<PointEntity> referPoints = new ArrayList<>();

    /**
     * @param lng
     * @param lat
     * @return
     */
    public static boolean isAvailabPoint(double lng, double lat, long time) {
        if (referPoints.size() < 10) {
            //记录10有位置变化的点
            if (referPoints.size() == 0) {
                referPoints.add(new PointEntity(lat, lng, time));
            } else {
                double d = MapUtils.distanceBetween(lat, lng, referPoints.get(referPoints.size() - 1).getLat(), referPoints.get(referPoints.size() - 1).getLng());
                if (d > 0) {
                    referPoints.add(new PointEntity(lat, lng, time));
                }
            }
            return true;
        } else {
            return availabPoint(lng, lat, time);
        }
    }

    /**
     * 计算点有的效性，如果有效放入参样集合中
     * TODO 以步行为例【1m/s】
     *
     * @param lng
     * @param lat
     * @param time
     * @return
     */
    private static boolean availabPoint(double lng, double lat, long time) {
        double distance = MapUtils.distanceBetween(lng, lat, referPoints.get(referPoints.size() - 1).getLng(), referPoints.get(referPoints.size() - 1).getLat());
        MLog.d(TAG, "两点的距离===" + distance);
        /**
         * 计算该点的有效性
         * 小于平均距离的一半或者大于平均距离的2位 视该点无效
         */
        if (distance < calculateDistance(referPoints) / 2 || distance > calculateDistance(referPoints) * 2) {
            return false;
        } else {
            referPoints.remove(0);
            referPoints.add(new PointEntity(lat, lng, time));
            return true;
        }
    }

//    /**
//     * 计算参样点的平均速度
//     *
//     * @param lists
//     * @return
//     */
//    private static float calculateSpeed(List<PointEntity> lists) {
//        float speed = 0;
//        for (int i = 0; i < lists.size(); i++) {
//            if (i >= lists.size() - 2) {
//                break;
//            }
//            speed += MapUtils.speedBetween(lists.get(i).getLng(), lists.get(i).getLat(), lists.get(i + 1).getLat(), lists.get(i + 1).getLng());
//        }
//        return 0.0f;
//    }

    /**
     * 计算参样点的平均距离
     *
     * @param lists
     * @return
     */
    private static double calculateDistance(List<PointEntity> lists) {
        double distance = 0;
        for (int i = 0; i < lists.size(); i++) {
            if (i >= lists.size() - 2) {
                break;
            }
            distance += MapUtils.distanceBetween(lists.get(i).getLng(), lists.get(i).getLat(), lists.get(i + 1).getLng(), lists.get(i + 1).getLat());
        }
        MLog.d(TAG, "平均距离==" + distance / lists.size());
        return distance / lists.size();
    }
}
