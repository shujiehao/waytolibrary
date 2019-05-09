package com.wayto.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.wayto.map.common.Constant;
import com.wayto.map.google.WaytoMapLayer;
import com.wayto.map.utils.MSpfUtil;
import com.wayto.map.utils.MapSymbolDrawTool;

import java.util.Map;

/**
 * 定位图层、设置点线图层
 * <p>
 * author: hezhiWu <wuhezhi007@gmail.com>
 * version: V1.0.0
 * created at 2017/3/14 10:45
 * <p>
 * Copyright (c) 2017 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
 */
public class WaytoLocationLayer extends WaytoBaseMapView {

    private final String TAG = getClass().getSimpleName();
    /*定位图块*/
    private GraphicsLayer mGraphicsLayerLoc = null;
    /*定位点缓冲图层*/
    private GraphicsLayer mGraphicsLayerRange = null;
    /*足迹采集图层*/
    private GraphicsLayer mGraphicsLayerTrack = null;
    /*点图层*/
    private GraphicsLayer mGraphicsLayerPoint = null;

    private int graphicId = -1;
    private int mRangeGraphicId = -1;
    private static final int rangeRadius = 50;
    private static final int DRAW_RANGE_SCALE = 9030;
    //记录是否开启足迹
    public boolean isTrackRecording = false;

    private boolean isAddLocIcon = false;

    private boolean isFirst = true;

    public WaytoLocationLayer(Context context) {
        super(context, null);
    }

    public WaytoLocationLayer(Context context, AttributeSet attr) {
        super(context, attr);
        initLocationLayer();
    }

    /**
     * 初始化图层
     */
    private void initLocationLayer() {
        mGraphicsLayerLoc = new GraphicsLayer();
        mGraphicsLayerTrack = new GraphicsLayer();
        mGraphicsLayerPoint = new GraphicsLayer();

        addLayer(mGraphicsLayerTrack);
        addLayer(mGraphicsLayerPoint);
    }

    @Override
    public void preAction(float v, float v1, double v2) {

    }

    @Override
    public void postAction(float v, float v1, double v2) {
        drawLocRange();//地图缩放时刷新
    }

    @Override
    protected void locToCenter(Point point) {
        super.locToCenter(point);
        drawLocRange();//手动定位时刷新
    }

    @Override
    public void postPointerMove(float v, float v1, float v2, float v3) {
        super.postPointerMove(v, v1, v2, v3);
        drawLocRange();//移动地图时刷新
    }

    /**
     * 更新当前位置
     *
     * @param point
     */
    public GraphicsLayer updateCurrentLocation(Point point) {
        this.point = point;
        if (!isAddLocIcon) {
            addLayer(mGraphicsLayerLoc);
            isAddLocIcon = true;
        }
        if (isFlow || isTrackRecording || isFirst) {
            isFirst = false;
            x=point.getX();
            y=point.getY();
            setExtent(point);
            setScale(getScale());
            Log.i("DUY", "updateCurrentLocation: " + getScale());
            MSpfUtil.setValue(getContext(), Constant.LNG_FLAG, point.getX());
            MSpfUtil.setValue(getContext(), Constant.LAT_FLAG, point.getY());
        }
        if (!mIsMapMoving) {
            drawLocRange();//定位回调时刷新
        }
        mShowArrow=true;
        // TODO: 26/04/2017 暂时注释 采用view层绘制
        addLocation(point, mGraphicsLayerLoc, R.mipmap.main_icon_follow);// ic_navigation_black_24dp

        return mGraphicsLayerLoc;
    }

    /**
     * 绘制定位范围圈
     * author: Yang Du <youngdu29@gmail.com>
     * created at 27/04/2017 4:59 PM
     */
    public void drawLocRange() {
        if (getScale() <= DRAW_RANGE_SCALE && this.point != null&&mShowArrow) {
            setLocRangeLayerVisibility(true);
            openLocRangeLayer(this.point);
        } else {
            setLocRangeLayerVisibility(false);
        }
    }

    /**
     * 更新当前位置
     *
     * @param point
     * @param resId 定位图标
     */
    public GraphicsLayer updateCurrentLocation(Point point, int resId) {
        this.point = point;
        if (!isAddLocIcon) {
            addLayer(mGraphicsLayerLoc);
            isAddLocIcon = true;
        }
        if (isFlow || isTrackRecording || isFirst) {
            isFirst = false;
            x=point.getX();
            y=point.getY();
            setScale(getScale());
            MSpfUtil.setValue(getContext(), Constant.LNG_FLAG, point.getX());
            MSpfUtil.setValue(getContext(), Constant.LAT_FLAG, point.getY());
        }
        addLocation(point, mGraphicsLayerLoc, resId);

        return mGraphicsLayerLoc;
    }

    /**
     * 足迹采集定位点
     *
     * @param point
     */
    public GraphicsLayer updateTrackRecordLocation(Point point) {
        setExtent(point);
        setScale(WaytoMapLayer.scales[16]);
        addLocation(point, mGraphicsLayerTrack, R.mipmap.main_icon_follow);

        return mGraphicsLayerTrack;
    }

    public void addLocation(Point point, GraphicsLayer graphicsLayer, int resId) {
        Bitmap bmpStart = BitmapFactory.decodeResource(getResources(), resId);
        Drawable drawable = new BitmapDrawable(getResources(), bmpStart);
        addPictureMarkerSimple(point, graphicsLayer, drawable, null);
    }

    /**
     * @param mPoint
     * @param graphicsLayer
     * @param drawable
     * @param attributes    附带属性
     * @return
     * @author duyang
     * @version V1.1
     */
    public Graphic addPictureMarkerSimple(Point mPoint, GraphicsLayer graphicsLayer, Drawable drawable, Map<String, Object> attributes) {
        PictureMarkerSymbol picSys = new PictureMarkerSymbol(drawable);
        Graphic graphic = null;
        if (graphicsLayer != null && mPoint != null) {
            graphic = new Graphic(mPoint, picSys, attributes, 0);
            if (graphicId != -1) {
                mGraphicsLayerLoc.updateGraphic(graphicId, graphic);
            } else {
                graphicId = graphicsLayer.addGraphic(graphic);
                setScale(WaytoMapLayer.scales[16]);
            }
        }
        return graphic;
    }

    /**
     * 是否足迹跟随
     *
     * @param TrackRecording
     */
    public void setTrackRecording(boolean TrackRecording) {
        this.isFlow = TrackRecording;
    }


    /**
     * 打开定位点误差图层
     *
     * @param point
     */
    public void openLocRangeLayer(Point point) {
        /**添加范围圈*/
        //TODO 更新太频繁 到时候根据地图比例尺进行控制
        if (mGraphicsLayerRange == null) {
            mGraphicsLayerRange = new GraphicsLayer();
            addLayer(mGraphicsLayerRange);
        }
        if (mGraphicsLayerRange.isVisible()) {
            Graphic rangeGraphic = MapSymbolDrawTool.createCircleSymbol(point, rangeRadius, Color.BLUE, Color.BLUE, 0.5f, 10);
            if (mRangeGraphicId != -1) {
                mGraphicsLayerRange.updateGraphic(mRangeGraphicId, rangeGraphic);
            } else {
                mRangeGraphicId = mGraphicsLayerRange.addGraphic(rangeGraphic);
            }
            //mGraphicsLayerRange=drawCircleSymbol(point, rangeRadius, Color.BLUE, Color.BLUE, 2f, 10);
        }
    }

    /**
     * 设置定位点误差图层可见性
     */
    public void setLocRangeLayerVisibility(boolean isVisible) {
        if (mGraphicsLayerRange != null) {
            mGraphicsLayerRange.setVisible(isVisible);
        }
    }
}
