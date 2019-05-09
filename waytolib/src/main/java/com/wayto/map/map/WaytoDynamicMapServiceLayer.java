package com.wayto.map.map;

import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.DrawingInfo;

import java.util.Map;

/**
 * @Description:
 * @Author: shujie <1583534549@qq.com>
 * @CreateDate: 2019/5/8 16:38
 * @UpdateUser:
 * @UpdateDate: 2019/5/8 16:38
 * @UpdateRemark:
 * @Version: 1.0
 * Copyright (c) 2019 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
 */
public class WaytoDynamicMapServiceLayer extends ArcGISDynamicMapServiceLayer {
    public WaytoDynamicMapServiceLayer(String url, int[] visiblelayers) {
        super(url, visiblelayers);
    }

    public WaytoDynamicMapServiceLayer(String url, int[] visiblelayers, UserCredentials credentials, boolean initLayer) {
        super(url, visiblelayers, credentials, initLayer);
    }

    public WaytoDynamicMapServiceLayer(String url, int[] visiblelayers, int[] invisibleLegendLayers, UserCredentials credentials, boolean initLayer) {
        super(url, visiblelayers, invisibleLegendLayers, credentials, initLayer);
    }

    public WaytoDynamicMapServiceLayer(String url) {
        super(url);
    }

    public WaytoDynamicMapServiceLayer(String url, int[] visiblelayers, UserCredentials credentials) {
        super(url, visiblelayers, credentials);
    }

    public WaytoDynamicMapServiceLayer(String url, int[] visiblelayers, Map<Integer, DrawingInfo> drawingOptions, UserCredentials credentials) {
        super(url, visiblelayers, drawingOptions, credentials);
    }
}
