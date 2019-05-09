package com.wayto.map.map;

import com.esri.android.map.ags.ArcGISLocalTiledLayer;

/**
 * @Description:
 * @Author: shujie <1583534549@qq.com>
 * @CreateDate: 2019/5/8 16:42
 * @UpdateUser:
 * @UpdateDate: 2019/5/8 16:42
 * @UpdateRemark:
 * @Version: 1.0
 * Copyright (c) 2019 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
 */
public class WaytoLocalTiledLayer extends ArcGISLocalTiledLayer {
    public WaytoLocalTiledLayer(String path) {
        super(path);
    }

    public WaytoLocalTiledLayer(String path, boolean initlayer) {
        super(path, initlayer);
    }
}
