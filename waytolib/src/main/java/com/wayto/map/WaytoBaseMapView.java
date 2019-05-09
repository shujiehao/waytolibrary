package com.wayto.map;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnPanListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Point;
import com.wayto.android.map.greedao.DaoMaster;
import com.wayto.android.map.greedao.DaoSession;
import com.wayto.map.common.Constant;
import com.wayto.map.google.WaytoMapLayer;
import com.wayto.map.google.WaytoMapLayerTypes;
import com.wayto.map.view.CompassView;

import java.lang.ref.SoftReference;

/**
 * ArcGis 基础图层
 * <p>
 * author: hezhiWu <wuhezhi007@gmail.com>
 * version: V1.0.0
 * created at 2017/3/14 10:45
 * <p>
 * Copyright (c) 2017 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
 */
public abstract class WaytoBaseMapView extends MapView implements OnStatusChangedListener, OnPanListener, OnZoomListener {
    private final String TAG = getClass().getSimpleName();

    private final String CLIENT_ID = "1eFHW78avlnRUPHm";

    protected ImageView zoomoutIv, zoominIv, locationIv;
    protected CompassView locArrowView;
    protected TextView switchLayerBtn;
    private LinearLayout zoomoutLayout, zoominLayout;
    protected LinearLayout btmLayout;
    protected ImageView layerIV;
    protected RelativeLayout proLayout;
    protected RelativeLayout locationLayout;
    protected LinearLayout rightTopZoominZoomioutLayout, rightBottomZoominZoomioutLayout, leftBottomZoominZoomioutLayout;
    protected FloatingActionButton mLocationFlowActionButton;

    protected Point point;
    protected WaytoMapLayer terrainLayer;
    protected WaytoMapLayer imageLayer;
    protected WaytoMapLayer annotationLayer;
    //    protected GaodeMapLayer roadLayer;
//    protected GaodeMapLayer imageLayer;
    private boolean isImageLayer = true;//默认显示影像图

    protected boolean isFlow = false;//是否跟随
    protected boolean mShowArrow = true;//默认显示定位箭头
    protected boolean mShowCompass = true;//默认显示指南针

    // 指南针
    protected CompassView mPointerSmall;// 指南针view
    private SensorManager mSensorManager;// 传感器管理对象
    private SensorEventListener mOrientationSensorEventListener;//传感器监听
    private Sensor mAccelerometer;
    private Sensor mMagneticmeter;
    private Handler mSensorHandler; //传感器Handler
    private Runnable mCompassViewUpdater;//处理指南针刷新
    protected float azimut;//设备与地磁北方向之间的方位角
    private float[] mGravity;
    private float[] mGeomagnetic;
    private float mDirection;// 当前浮点方向
    private float mTargetDirection;// 目标浮点方向
    private float mArrowDirection;//当前定位箭头方向
    private float mTargetArrowDirection;//目标定位箭头方向
    private AccelerateInterpolator mInterpolator;// 动画从开始到结束，变化率是一个加速的过程,就是一个动画速率
    private boolean mStopDrawing;// 是否停止指南针旋转的标志位
    protected boolean mIsMapMoving;//是否在移动地图

    private final float MAX_ROATE_DEGREE = 1.0f;// 最多旋转一周，即360°
    private static final int FROM_RADS_TO_DEGS = -57;//360／(2*3.141592653)
    public static final int SENSOR_REFRESH_INTERVAL = 200;//指南针刷新频率ms

    protected double x = 1.2697781415967792E7;//默认定位x坐标
    protected double y = 2579888.187931014;//默认定位y坐标

    private static DaoSession daoSession;

    protected MapViewClickListener listener;

    public WaytoBaseMapView(Context context) {
        super(context);
        initDaoSession(context);
        initView();
        initDefaultConfig();
    }

    public WaytoBaseMapView(Context context, AttributeSet attri) {
        super(context, attri);
        initDaoSession(context);
        initView();
        initDefaultConfig();
    }

    /**
     * 初始化地图的默认配制
     */
    private void initDefaultConfig() {
        setMapBackground(0xf5f5f5f5, Color.TRANSPARENT, 0, 0);
        ArcGISRuntime.setClientId(Constant.ARCGIS_KEY);
        terrainLayer = new WaytoMapLayer(WaytoMapLayerTypes.VECTOR_WAYTO_MAP, getContext());
        imageLayer = new WaytoMapLayer(WaytoMapLayerTypes.IMAGE_WAYTO_MAP, getContext());
        annotationLayer = new WaytoMapLayer(WaytoMapLayerTypes.ANNOTATION_WAYTO_MAP, getContext());

        addLayer(imageLayer);
        addLayer(annotationLayer);
        addLayer(terrainLayer);

        //设置图层显示
        terrainLayer.setVisible(true);
        imageLayer.setVisible(false);
        annotationLayer.setVisible(false);

//        roadLayer = new GaodeMapLayer(GaoDeMapLayerTypes.ROAD_GAODE_MAP, getContext());
//        imageLayer = new GaodeMapLayer(GaoDeMapLayerTypes.IMAGE_GAODE_MAP, getContext());

//        addLayer(roadLayer);
//        addLayer(imageLayer);
//
//        roadLayer.setVisible(true);
//        imageLayer.setVisible(false);

        //设置手势旋转地图
//        setAllowRotationByPinch(true);

        setOnStatusChangedListener(this);
        setOnPanListener(this);// 地图平移监听
        setOnZoomListener(this);// 地图定位误差范围圈
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.map_base_layout, null);
        locArrowView = view.findViewById(R.id.loc_rotation);
        zoominLayout = view.findViewById(R.id.map_zoomin_layout);
        zoomoutLayout = view.findViewById(R.id.map_zoomout_layout);
        btmLayout = view.findViewById(R.id.map_btm_layout);
        locationIv = view.findViewById(R.id.map_loc_iv);
        proLayout = view.findViewById(R.id.map_cop_layout);
        switchLayerBtn = view.findViewById(R.id.map_outside_switch_iv);
        rightTopZoominZoomioutLayout = view.findViewById(R.id.Map_ZoominZoomout_RightTop_Layout);
        rightBottomZoominZoomioutLayout = view.findViewById(R.id.Map_ZoominZoomout_RightBottom_layout);
        leftBottomZoominZoomioutLayout = view.findViewById(R.id.Map_ZoominZoomout_LeftBottom_layout);
        mLocationFlowActionButton = view.findViewById(R.id.MapView_Location_Flow);

        zoominIv = view.findViewById(R.id.map_zoomin_iv);
        zoomoutIv = view.findViewById(R.id.map_zoomout_iv);

        layerIV = view.findViewById(R.id.map_layer_iv);
        mPointerSmall = view.findViewById(R.id.map_compass_iv);
        locationLayout = view.findViewById(R.id.map_loc_layout);
        //图层切换
        layerIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageLayer) {
                    layerIV.setImageResource(R.mipmap.homeicon_image_toggle0);
                    terrainLayer.setVisible(false);
                    imageLayer.setVisible(true);
                    annotationLayer.setVisible(true);
//                    roadLayer.setVisible(false);
//                    imageLayer.setVisible(true);

                    isImageLayer = false;
                } else {
                    layerIV.setImageResource(R.mipmap.homeicon_image_toggle1);
                    terrainLayer.setVisible(true);
                    imageLayer.setVisible(false);
                    annotationLayer.setVisible(false);
//                    roadLayer.setVisible(true);
//                    imageLayer.setVisible(false);

                    isImageLayer = true;
                }
            }
        });
        view.findViewById(R.id.map_zoominLeft_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomin();
                zoominLayout.setClickable(true);
                zoomoutLayout.setClickable(true);
            }
        });
        view.findViewById(R.id.map_zoomoutLeft_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomout();
                zoominLayout.setClickable(true);
                zoomoutLayout.setClickable(true);
            }
        });
        view.findViewById(R.id.map_zoominRightTop_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomin();
                zoominLayout.setClickable(true);
                zoomoutLayout.setClickable(true);
            }
        });
        view.findViewById(R.id.map_zoomoutRightTop_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomout();
                zoominLayout.setClickable(true);
                zoomoutLayout.setClickable(true);
            }
        });
        //放大
        zoominLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomin();
                zoominLayout.setClickable(true);
                zoomoutLayout.setClickable(true);

            }
        });
        //缩小
        zoomoutLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomout();
                zoominLayout.setClickable(true);
                zoomoutLayout.setClickable(true);
            }
        });

        locationIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFlow) {
                    setExtent(point);
                    translateArrowToLocation();//定位时更新定位箭头位置
                    setScale(WaytoMapLayer.scales[16]);
                    isFlow = true;
                    locationIv.setImageResource(R.mipmap.custom_follow);
                    mIsMapMoving = true;
                    locToCenter(point);
                    locationIv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIsMapMoving = false;//延时200ms
                        }
                    }, 200);
                } else {
                    isFlow = false;
                    locationIv.setImageResource(R.mipmap.custom_loc);
                }
                if (listener != null) {
                    listener.onClickFlow();
                }
            }
        });

        mLocationFlowActionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFlow) {
                    setExtent(point);
                    translateArrowToLocation();//定位时更新定位箭头位置
                    setScale(WaytoMapLayer.scales[16]);
                    isFlow = true;
//                    locationIv.setImageResource(R.mipmap.custom_follow);
                    mLocationFlowActionButton.setImageResource(R.drawable.ic_loc_flow);
                    mIsMapMoving = true;
                    locToCenter(point);
                    locationIv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIsMapMoving = false;//延时200ms
                        }
                    }, 200);
                } else {
                    isFlow = false;
//                    locationIv.setImageResource(R.mipmap.custom_loc);
                    mLocationFlowActionButton.setImageResource(R.drawable.ic_loc_nor);
                }
                if (listener != null) {
                    listener.onClickFlow();
                }
            }
        });
        mShowArrow = locArrowView.getVisibility() == View.VISIBLE;
        mShowCompass = mPointerSmall.getVisibility() == View.VISIBLE;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        addView(view);
        //去掉水印
        ArcGISRuntime.setClientId(CLIENT_ID);
        //初始化传感器
        initSensor();
    }

    /**
     * 定位到中心点
     * author: Yang Du <youngdu29@gmail.com>
     * created at 27/04/2017 5:04 PM
     */
    protected void locToCenter(Point point) {

    }

    @Override
    public void unpause() {
        super.unpause();
        startSensor();//启动传感器
    }

    @Override
    public void pause() {
        super.pause();
        stopSensor();//停止传感器监听
    }

    // 初始化方向传感器
    private void initSensor() {
        if (mShowArrow || mShowCompass) {

            // sensor manager
            mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

            if (mOrientationSensorEventListener == null) {
                mOrientationSensorEventListener = new OrientationSensorEventListener(WaytoBaseMapView.this);
            }

            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mMagneticmeter = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mSensorManager.registerListener(mOrientationSensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
            mSensorManager.registerListener(mOrientationSensorEventListener, mMagneticmeter, SensorManager.SENSOR_DELAY_UI);

            mDirection = 0.0f;// 初始化起始方向
            mTargetDirection = 0.0f;// 初始化目标方向
            mArrowDirection = 0.0f;
            mInterpolator = new AccelerateInterpolator();// 实例化加速动画对象
            mStopDrawing = false;
            mIsMapMoving = false;

            if (mSensorHandler == null) {
                mSensorHandler = new Handler();
            }
            if (mCompassViewUpdater == null) {
                mCompassViewUpdater = new CompassRefreshRunnable(WaytoBaseMapView.this);
            }
            mSensorHandler.postDelayed(mCompassViewUpdater, SENSOR_REFRESH_INTERVAL);// 执行一次更新指南针图片旋转
        }
    }

    /**
     * 开启传感器
     * <p>
     * author: Yang Du <youngdu29@gmail.com>
     * version: V1.0.0
     * created at 26/04/2017 4:27 PM
     * </p>
     * Copyright (c) 2017 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
     */
    public void startSensor() {
        if (mSensorManager == null) {
            initSensor();
        } else {
            mSensorManager.registerListener(mOrientationSensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
            mSensorManager.registerListener(mOrientationSensorEventListener, mMagneticmeter, SensorManager.SENSOR_DELAY_UI);
            mStopDrawing = false;
            mIsMapMoving = false;
            if (mSensorHandler == null) {
                mSensorHandler = new Handler();
            }
            mSensorHandler.postDelayed(mCompassViewUpdater, SENSOR_REFRESH_INTERVAL);// 20毫米后重新执行自己，比定时器好
        }
    }

    /**
     * 停止传感器调用
     * <p>
     * author: Yang Du <youngdu29@gmail.com>
     * version: V1.0.0
     * created at 26/04/2017 4:18 PM
     * </p>
     * Copyright (c) 2017 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
     */
    public void stopSensor() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(mOrientationSensorEventListener);
        }
        mStopDrawing = true;
    }

    /**
     * 方向传感器事件监听
     */
    private static final class OrientationSensorEventListener implements SensorEventListener {

        private SoftReference<WaytoBaseMapView> mReference;

        public OrientationSensorEventListener(WaytoBaseMapView mapView) {
            this.mReference = new SoftReference<>(mapView);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (mReference == null || mReference.get() == null) {
                return;
            }
            WaytoBaseMapView mapView = mReference.get();
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mapView.mGravity = event.values;
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mapView.mGeomagnetic = event.values;
            if (mapView.mGravity != null && mapView.mGeomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mapView.mGravity, mapView.mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];// orientation contains: azimut, pitch and roll
                    SensorManager.getOrientation(R, orientation);
                    mapView.azimut = orientation[0] * FROM_RADS_TO_DEGS; //degrees of rotation about the -z axis
                }
            }
            mapView.mTargetDirection = mapView.normalizeDegree(mapView.azimut);// 赋值给全局变量，让指南针旋转
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    /**
     * 指南针刷新Runnable
     * 每20毫秒检测方向变化值，对应更新指南针旋转
     */
    private static final class CompassRefreshRunnable implements Runnable {

        private SoftReference<WaytoBaseMapView> mReference;

        public CompassRefreshRunnable(WaytoBaseMapView mapView) {
            this.mReference = new SoftReference<>(mapView);
        }

        @Override
        public void run() {
            if (mReference == null || mReference.get() == null) {
                return;
            }
            WaytoBaseMapView mapView = mReference.get();
            if (!mapView.mStopDrawing) {
                if (mapView.mDirection != mapView.mTargetDirection) {
                    mapView.mDirection = mapView.fixRotation(mapView.mDirection, mapView.mTargetDirection, true);
                    if (mapView.mDirection > 0) {
                        if (mapView.mPointerSmall != null && mapView.mPointerSmall.getVisibility() == View.VISIBLE) {
                            mapView.mPointerSmall.updateDirection(mapView.mDirection);//更新指南针旋转
                        }
                        if (mapView.mShowArrow && !mapView.mIsMapMoving) {
                            mapView.translateArrowToLocation();//定时刷新地图定位图标方向
                        }
                    }
                }
                if (mapView.mSensorHandler == null) {
                    mapView.mSensorHandler = new Handler();
                }
                mapView.mSensorHandler.postDelayed(this, SENSOR_REFRESH_INTERVAL);// 重新执行自己，比定时器好
            }
        }
    }

    /**
     * 修正方向
     *
     * @param mDirection
     * @param mTargetDirection
     * @param calculateShortRoutine
     * @return
     */
    private float fixRotation(float mDirection, float mTargetDirection, boolean calculateShortRoutine) {
        // calculate the short routine
        float to = mTargetDirection;
        if (calculateShortRoutine) {
            if (to - mDirection > 180) {
                to -= 360;
            } else if (to - mDirection < -180) {
                to += 360;
            }
        }
        // limit the max speed to MAX_ROTATE_DEGREE
        float distance = to - mDirection;
        if (Math.abs(distance) > MAX_ROATE_DEGREE) {
            distance = distance > 0 ? MAX_ROATE_DEGREE
                    : (-1.0f * MAX_ROATE_DEGREE);
        }
        // need to slow down if the distance is short
        if (calculateShortRoutine && mInterpolator != null) {
            mDirection = normalizeDegree(mDirection
                    + ((to - mDirection) * mInterpolator
                    .getInterpolation(Math.abs(distance) > MAX_ROATE_DEGREE ? 0.4f
                            : 0.3f)));// 用了一个加速动画去旋转图片，很细致
        }
        return mDirection;
    }

    // 调整方向传感器获取的值
    private float normalizeDegree(float degree) {
        return (degree + 720) % 360;
    }

    /**
     * 平移动方向箭头到当前定位位置
     * author: Yang Du <youngdu29@gmail.com>
     * created at 26/04/2017 10:04 PM
     */
    public void translateArrowToLocation() {
        if (!mShowArrow) {
            return;
        }
        mArrowDirection = fixRotation(mArrowDirection, azimut * -1.0f, true);
        locArrowView.setVisibility(View.GONE);
        if (this.point != null) {
            final Point screenPoint = toScreenPoint(point);
            if (locArrowView != null && screenPoint != null) {
                locArrowView.post(new Runnable() {
                    @Override
                    public void run() {
                        locArrowView.updateDirectionAndPosition(mArrowDirection, (float) screenPoint.getX(), (float) screenPoint.getY());//TODO scrollto 方法不起作用 待研究
                    }
                });
            }
        } else {
            locArrowView.updateDirection(mArrowDirection);
        }
    }

    @Override
    public void onStatusChanged(Object o, STATUS status) {
        if (status == STATUS.LAYER_LOADED) {
            setExtent(new Point(x, y));
            setScale(WaytoMapLayer.scales[16]);
        }
    }

    @Override
    public void prePointerMove(float v, float v1, float v2, float v3) {
        mIsMapMoving = true;
    }

    @Override
    public void postPointerMove(float v, float v1, float v2, float v3) {
        translateArrowToLocation();//移动地图时刷新定位图标
    }

    @Override
    public void prePointerUp(float v, float v1, float v2, float v3) {

    }

    @Override
    public void postPointerUp(float v, float v1, float v2, float v3) {
        mIsMapMoving = false;
    }

    /**
     * 是否停止指南针刷新
     *
     * @return
     */
    public boolean isStopDrawing() {
        return mStopDrawing;
    }


    /**
     * DAO对象
     *
     * @return
     */
    private static DaoSession initDaoSession(Context context) {
        if (daoSession == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), "MapLayer_db", null);
            DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public interface MapViewClickListener {
        void onClickFlow();
    }
}
