package com.wayto.map.utils;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.identify.IdentifyParameters;

import java.util.Map;

/**
 * 地图查询工具类
 * <p>
 * author: Yang Du <youngdu29@gmail.com>
 * version: V1.0.0
 * created at 22/09/2017 10:29 PM
 * </p>
 * Copyright (c) 2017 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
*/
public class MapSearchTaskUtil {

    private MapSearchTaskUtil() {

    }

    /**
     * 创建地图属性查询QueryTask参数
     * @param whereClause 查询条件 eg: "field1=100 and field2='test' or field3 like '%test%'"
     * @param outFields 返回的字段 eg: new String[]{"field1","field2"}
     * @param spatialReference 地图空间索引 eg: new SpatialReference("2383")
     * @return
     */
    public Query getQueryParams(String whereClause,String [] outFields, SpatialReference spatialReference){
        Query query = new Query();
//		SpatialReference sr = new SpatialReference("2383");
        query.setOutSpatialReference(spatialReference);// 设置查询输出的坐标系
        query.setReturnGeometry(true);// 是否返回空间信息
        query.setWhere(whereClause);// where条件
        query.setOutFields(outFields);
        return query;
    }

    /**
     * 创建地图空间查询Identify参数
     * @param tolerance 空间查询半径范围 像素
     * @param identifyPoint 空间查询中心点
     * @param spatialReference 当前地图空间索引 通常由MapView获取
     * @param mapHeight 当前地图屏幕高度 通常由MapView获取
     * @param mapWidth 当前地图屏幕宽度 通常由MapView获取
     * @param env 查询范围 通常由MapView获取
     * @param layers 图层索引
     * @param map 图层查询限制条件 key图层索引、value图层条件
     * @return
     */
    public IdentifyParameters getIdentifyParametersV2(int tolerance, Point identifyPoint, SpatialReference spatialReference, int mapHeight, int mapWidth, Envelope env, int[] layers, Map<String, String> map) {
        IdentifyParameters params = new IdentifyParameters();
        params.setTolerance(tolerance);// 像素值容差
        params.setDPI(98);
        params.setLayers(layers);// 图层ID
        params.setLayerMode(IdentifyParameters.ALL_LAYERS);

        params.setLayerDefs(map);

        params.setGeometry(identifyPoint);
        params.setSpatialReference(spatialReference);
        params.setMapHeight(mapHeight);
        params.setMapWidth(mapWidth);
        params.setReturnGeometry(true);
        // add the area of extent to identify parameters
        params.setMapExtent(env);
        return params;
    }

}
