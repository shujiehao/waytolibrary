package com.wayto.map.map;

import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.FeatureSet;


/**
 * @Description:
 * @Author: shujie <1583534549@qq.com>
 * @CreateDate: 2019/5/8 16:40
 * @UpdateUser:
 * @UpdateDate: 2019/5/8 16:40
 * @UpdateRemark:
 * @Version: 1.0
 * Copyright (c) 2019 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
 */
public class WaytoFeatureLayer extends ArcGISFeatureLayer {
    public WaytoFeatureLayer(String url, Options layerOption) {
        super(url, layerOption);
    }

    public WaytoFeatureLayer(String url, Options layerOption, UserCredentials credentials) {
        super(url, layerOption, credentials);
    }

    public WaytoFeatureLayer(String url, Options layerOption, UserCredentials credentials, boolean useAdvancedSymbols) {
        super(url, layerOption, credentials, useAdvancedSymbols);
    }

    public WaytoFeatureLayer(String url, Options layerOption, UserCredentials credentials, boolean useAdvancedSymbols, RenderingMode rmode) {
        super(url, layerOption, credentials, useAdvancedSymbols, rmode);
    }

    public WaytoFeatureLayer(String url, MODE mode) {
        super(url, mode);
    }

    public WaytoFeatureLayer(String url, String layerDefinition, MODE mode) {
        super(url, layerDefinition, mode);
    }

    public WaytoFeatureLayer(String url, String layerDefinition, MODE mode, boolean initLayer) {
        super(url, layerDefinition, mode, initLayer);
    }

    public WaytoFeatureLayer(String url, String layerDefinition, MODE mode, boolean initLayer, UserCredentials credentials) {
        super(url, layerDefinition, mode, initLayer, credentials);
    }

    public WaytoFeatureLayer(String url, String layerDefinition, MODE mode, boolean initLayer, UserCredentials credentials, boolean useAdvancedSymbols) {
        super(url, layerDefinition, mode, initLayer, credentials, useAdvancedSymbols);
    }

    public WaytoFeatureLayer(String url, String layerDefinition, MODE mode, boolean initLayer, UserCredentials credentials, boolean useAdvancedSymbols, RenderingMode rmode) {
        super(url, layerDefinition, mode, initLayer, credentials, useAdvancedSymbols, rmode);
    }

    public WaytoFeatureLayer(String url, MODE mode, UserCredentials credentials) {
        super(url, mode, credentials);
    }

    public WaytoFeatureLayer(String layerDefinition, FeatureSet featureCollection, Options layerOption) {
        super(layerDefinition, featureCollection, layerOption);
    }

    public WaytoFeatureLayer(String layerDef, String layerDefinitionOverride, FeatureSet featureCollection, Options layerOption, boolean initLayer) {
        super(layerDef, layerDefinitionOverride, featureCollection, layerOption, initLayer);
    }
}
