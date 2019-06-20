package com.wayto.map.exchange;

import android.content.Context;
import android.content.Intent;

import com.amap.api.location.AMapLocationClient;

/**
 * @Description:
 * @Author: shujie <1583534549@qq.com>
 * @CreateDate: 2019/6/20 18:23
 * @UpdateUser:
 * @UpdateDate: 2019/6/20 18:23
 * @UpdateRemark:
 * @Version: 1.0
 * Copyright (c) 2019 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
 */
public class WaytoLocationClient extends AMapLocationClient {
    public WaytoLocationClient(Context context) {
        super(context);
    }

    public WaytoLocationClient(Context context, Intent intent) {
        super(context, intent);
    }
}
