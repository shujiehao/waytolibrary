package com.wayto.map.google;

/**
 * 地图图层类型
 * <p>
 * author: hezhiWu <wuhezhi007@gmail.com>
 * version: V1.0.0
 * created at 2017/3/14 10:48
 * <p>
 * Copyright (c) 2017 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
 */
public interface WaytoMapLayerTypes {
    /**
     * 矢量地图服务 ======市政图
     */
    static final int VECTOR_WAYTO_MAP = 1;
    /**
     * 影像地图服务 ====== 卫星图
     */
    static final int IMAGE_WAYTO_MAP = 2;
    /**
     * 地形地图服务=======地形图
     */
    static final int TERRAIN_WAYTO_MAP = 3;
    /**
     * 道路等POI地图服务 ====纯道路图
     */
    static final int ANNOTATION_WAYTO_MAP = 4;
}
