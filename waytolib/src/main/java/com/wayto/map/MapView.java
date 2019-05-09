package com.wayto.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.wayto.map.map.WaytoFeatureLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MapView
 * <p>
 * author: hezhiWu <wuhezhi007@gmail.com>
 * version: V1.0.0
 * created at 2017/3/14 10:45
 * <p>
 * Copyright (c) 2017 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
 */
public class MapView extends WaytoLocationLayer {

    public MapView(Context context) {
        super(context);
    }

    public MapView(Context context, AttributeSet attri) {
        super(context, attri);
    }

    /**
     * 添加 Feature图层
     *
     * @param url
     */
    public WaytoFeatureLayer addFeatureLayer(String url) {
        WaytoFeatureLayer featureLayer = new WaytoFeatureLayer(url, WaytoFeatureLayer.MODE.ONDEMAND);
        addLayer(featureLayer);
        return featureLayer;
    }

    public WaytoFeatureLayer addFeatureLayer(String url, String expression) {
        WaytoFeatureLayer featureLayer = new WaytoFeatureLayer(url, WaytoFeatureLayer.MODE.ONDEMAND);
        addLayer(featureLayer);
        featureLayer.setDefinitionExpression(expression);
        return featureLayer;
    }

    /**
     * 底部添加View
     *
     * @param view
     */
    public void addBtmMapView(View view) {
        btmLayout.addView(view);
        btmLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 消除底部View
     */
    public void removeBtmMapView() {
        btmLayout.removeAllViews();
        btmLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置指南针显示状态
     * <p>
     * author: hezhiWu
     * created at 2017/9/19 13:55
     */
    public void setPointerViewVisibility(int visibility) {
        mPointerSmall.setVisibility(visibility);
    }

    /**
     * 设置图层切换显示状态
     * <p>
     * author: hezhiWu
     * created at 2017/9/19 13:56
     */
    public void setLayerImageViewVisibility(int visibility) {
        layerIV.setVisibility(visibility);
    }

    /**
     * 获取空间查询参数  查询图层上的要素
     *
     * @param tolerance
     * @param identifyPoint
     * @param spatialReference
     * @param mapHeight
     * @param mapWidth
     * @param env
     * @param layers
     * @return
     * @auth:duyang
     */
    public IdentifyParameters getIdentifyParameters(int tolerance, Point identifyPoint, SpatialReference spatialReference, int mapHeight, int mapWidth, Envelope env, int[] layers, String LayerDefs) {
        IdentifyParameters params = new IdentifyParameters();
        params.setTolerance(tolerance);// 像素值容差
        params.setDPI(98);
        params.setLayers(layers);// 图层ID
        params.setLayerMode(IdentifyParameters.ALL_LAYERS);

        Map<String, String> map = new HashMap<>();
        map.put("1", LayerDefs);
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

    /**
     * 获取空间查询参数  查询图层上的要素
     * <p>
     * author: hezhiWu
     * created at 2017/9/22 17:34
     *
     * @param map key图层索引、value图层条件
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

    public IdentifyParameters getIdentifyParameters(int tolerance, Point identifyPoint, SpatialReference spatialReference, int mapHeight, int mapWidth, Envelope env, int[] layers) {
        IdentifyParameters params = new IdentifyParameters();
        params.setTolerance(tolerance);// 像素值容差
        params.setDPI(98);
        params.setLayers(layers);// 图层ID
        params.setLayerMode(IdentifyParameters.ALL_LAYERS);

        params.setGeometry(identifyPoint);
        params.setSpatialReference(spatialReference);
        params.setMapHeight(mapHeight);
        params.setMapWidth(mapWidth);
        params.setReturnGeometry(true);
        // add the area of extent to identify parameters
        params.setMapExtent(env);
        return params;
    }

    /**
     * 生成ArcGIS Polyline
     *
     * @param points 点集
     * @return
     * @author duyang
     */
    public Polyline generatePolyline(List<Point> points) {
        Polyline polyLine;
        if (points == null || points.size() < 2) {
            polyLine = null;
        } else {
            polyLine = new Polyline();
            polyLine.startPath(points.get(0));
            for (Point point :
                    points) {
                polyLine.lineTo(point);
            }
        }
        return polyLine;
    }

    /**
     * 生成ArcGIS Polyline
     *
     * @param points 点集
     * @return
     * @author duyang
     */
    public Polyline generatePolyline(Point... points) {
        Polyline polyLine;
        if (points == null || points.length < 2) {
            polyLine = null;
        } else {
            polyLine = new Polyline();
            polyLine.startPath(points[0]);
            for (Point point :
                    points) {
                polyLine.lineTo(point);
            }
        }
        return polyLine;
    }


    /**
     * 绘制实线段
     *
     * @param pl
     * @param color
     * @param size
     * @param graphicsLayer
     * @return
     * @author duyang
     */
    public Graphic addPolylineToGraphicsLayer(Polyline pl, int color, int size, GraphicsLayer graphicsLayer) {
        Graphic g = null;
        if (pl != null && pl.getPointCount() > 1) {
            SimpleLineSymbol sls = new SimpleLineSymbol(color, size,
                    com.esri.core.symbol.SimpleLineSymbol.STYLE.SOLID);
            g = new Graphic(pl, sls);
            graphicsLayer.addGraphic(g);
        }
        return g;
    }

    /**
     * 绘制虚线
     *
     * @param pl
     * @param color
     * @param size
     * @param graphicsLayer
     * @return
     * @author duyang
     */
    public Graphic addPolylineDotToGraphicsLayer(Polyline pl, int color, int size, GraphicsLayer graphicsLayer) {
        Graphic g = null;
        if (pl != null && pl.getPointCount() > 1) {
            SimpleLineSymbol sls = new SimpleLineSymbol(color, size,
                    com.esri.core.symbol.SimpleLineSymbol.STYLE.DOT);
            g = new Graphic(pl, sls);
            graphicsLayer.addGraphic(g);
        }
        return g;
    }

    /**
     * 绘制线
     * <p>
     * author: hezhiWu
     * created at 2017/6/28 17:12
     */
    public Graphic addPolylineGraphicsLayer(Polyline polyline, int lineColor, int lineSize, GraphicsLayer graphicsLayer, SimpleLineSymbol.STYLE style) {
        Graphic g = null;
        if (polyline != null && polyline.getPointCount() > 1) {
            SimpleLineSymbol sls = new SimpleLineSymbol(lineColor, lineSize,
                    com.esri.core.symbol.SimpleLineSymbol.STYLE.DOT);
            g = new Graphic(polyline, sls);
            graphicsLayer.addGraphic(g);
        }
        return g;
    }

    /**
     * 绘制图形覆盖物
     *
     * @param mPoint
     * @param graphicsLayer
     * @param drawable
     * @param attributes    附带属性
     * @return
     * @author duyang
     */
    public Graphic addPictureMarkerSimple1(Point mPoint, GraphicsLayer graphicsLayer, Drawable drawable, Map<String, Object> attributes) {
        PictureMarkerSymbol picSys = new PictureMarkerSymbol(drawable);
        Graphic graphic = null;
        if (graphicsLayer != null && mPoint != null) {
            graphic = new Graphic(mPoint, picSys, attributes, 0);
            graphicsLayer.addGraphic(graphic);
        }
        return graphic;
    }

    /**
     * 设置底部控制View visibility
     *
     * @param visibility
     */
    public void setBtmOperaLayoutVisibility(int visibility) {
        proLayout.setVisibility(visibility);
    }

    /**
     * 设置定位Btn visibility
     *
     * @param visibility
     */
    public void setLocationBtnVisibility(int visibility) {
        locationIv.setVisibility(visibility);
    }


    /**
     * 设置定位图标显示状态
     * <p>
     * author: hezhiWu
     * created at 2017/6/28 9:46
     */
    public void setLocationIconVisibility(int visibility) {
        locArrowView.setVisibility(visibility);
    }

    /**
     * 设置定位图标
     *
     * @param resId
     */
    public void setLocationBtnBag(int resId) {
        locationIv.setImageResource(resId);
    }

    /**
     * 左边底部显示
     * <p>
     * author: hezhiWu
     * created at 2017/4/17 15:22
     */
    public void setLeftBottomZoominZoomoutVisibility() {
        leftBottomZoominZoomioutLayout.setVisibility(VISIBLE);
        rightBottomZoominZoomioutLayout.setVisibility(GONE);
        rightTopZoominZoomioutLayout.setVisibility(GONE);
    }

    /**
     * 右边顶部显示
     * <p>
     * author: hezhiWu
     * created at 2017/4/17 15:22
     */
    public void setRightTopZomminZoomoutVisibility() {
        leftBottomZoominZoomioutLayout.setVisibility(GONE);
        rightBottomZoominZoomioutLayout.setVisibility(GONE);
        rightTopZoominZoomioutLayout.setVisibility(VISIBLE);
    }

    /**
     * author: hezhiWu
     * created at 2017/7/21 21:28
     */
    public void setZomminZoomoutVisibility(int visibility) {
        rightBottomZoominZoomioutLayout.setVisibility(visibility);
    }


    /**
     * 设置地图上某个点
     *
     * @param point 点信息
     * @param res   图标资源
     */
    public void setPoint(Point point, int res) {
        locArrowView.setVisibility(GONE);
        Bitmap bmpStart = BitmapFactory.decodeResource(getResources(), res);
        Drawable drawable = new BitmapDrawable(getResources(), bmpStart);
        PictureMarkerSymbol picSys = new PictureMarkerSymbol(drawable);
        Graphic graphic = new Graphic(point, picSys, null, 0);
        GraphicsLayer graphicsLayer = new GraphicsLayer();
        graphicsLayer.addGraphic(graphic);
        addLayer(graphicsLayer);
    }

    /**
     * 设置地图上某个点(优化轨迹停留点)
     *
     * @param graphicsLayer 点图层
     * @param point         点信息
     * @param attributes    点属性
     * @param res           图标资源
     */
    public GraphicsLayer setPoint(GraphicsLayer graphicsLayer, Point point, Map<String, Object> attributes, int res) {
        locArrowView.setVisibility(GONE);
        Bitmap bmpStart = BitmapFactory.decodeResource(getResources(), res);
        Drawable drawable = new BitmapDrawable(getResources(), bmpStart);
        PictureMarkerSymbol picSys = new PictureMarkerSymbol(drawable);
        Graphic graphic = new Graphic(point, picSys, attributes, 0);
        graphicsLayer.addGraphic(graphic);
        addLayer(graphicsLayer);
        return graphicsLayer;
    }

    /**
     * 生成箭头线段
     *
     * @param arrowLineSegmentList
     * @param pointStart
     * @param pointEnd
     * @param length               箭头线的长度 一般是2
     * @param angleValue           箭头与直线之间的角度 一般是Math.PI/5
     */
    public void calculateArrowLine(List<Polyline> arrowLineSegmentList, Point pointStart, Point pointEnd, int length, double angleValue) {
        if (arrowLineSegmentList != null && pointStart != null && pointEnd != null) {
            int r = length;
            double angle = angleValue;
            double delta = 0;//斜率
            double param = 0;//临时变量
            double pointTemX, pointTemY; //临时点坐标
            double pointX, pointY, pointX1, pointY1; //箭头两个点
            if (pointEnd.getX() - pointStart.getX() == 0) { //斜率不存在是时
                pointTemX = pointEnd.getX();
                if (pointEnd.getY() > pointStart.getY()) {
                    pointTemY = pointEnd.getY() - r;
                } else {
                    pointTemY = pointEnd.getY() + r;
                }
                //已知直角三角形两个点坐标及其中一个角，求另外一个点坐标算法
                pointX = pointTemX - r * Math.tan(angle);
                pointX1 = pointTemX + r * Math.tan(angle);
                pointY = pointY1 = pointTemY;
            } else  //斜率存在时
            {
                delta = (pointEnd.getY() - pointStart.getY()) / (pointEnd.getX() - pointStart.getX());
                param = Math.sqrt(delta * delta + 1);

                if ((pointEnd.getX() - pointStart.getX()) < 0) //第二、三象限
                {
                    pointTemX = pointEnd.getX() + r / param;
                    pointTemY = pointEnd.getY() + delta * r / param;
                } else//第一、四象限
                {
                    pointTemX = pointEnd.getX() - r / param;
                    pointTemY = pointEnd.getY() - delta * r / param;
                }
                //已知直角三角形两个点坐标及其中一个角，求另外一个点坐标算法
                pointX = pointTemX + Math.tan(angle) * r * delta / param;
                pointY = pointTemY - Math.tan(angle) * r / param;

                pointX1 = pointTemX - Math.tan(angle) * r * delta / param;
                pointY1 = pointTemY + Math.tan(angle) * r / param;
            }
            List<Point> points = new ArrayList<>();
            points.add(pointEnd);
            points.add(new Point(pointX, pointY));
            //
            arrowLineSegmentList.add(generatePolyline(points));
            points.clear();
            points.add(pointEnd);
            points.add(new Point(pointX1, pointY1));
            arrowLineSegmentList.add(generatePolyline(points));
        }
    }

    public void setFlow(boolean flow) {
        this.isFlow = flow;
    }


    public void setDefaultX(double x) {
        this.x = x;
    }

    public void setDefaultY(double y) {
        this.y = y;
    }

    public void setClickListener(MapViewClickListener listener) {
        this.listener = listener;
    }

    public int getScan() {
        return (int) getScale();
    }
}
