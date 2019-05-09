package com.wayto.map.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * author: hezhiWu <wuhezhi007@gmail.com>
 * version: V1.0.0
 * created at 2017/3/14 10:46
 * <p>
 * Copyright (c) 2017 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
 */
public class CircleView extends View {

    private float x = 10, y = 10;

    public CircleView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawCircle(120, 20, 20, paint);
    }

    public void setCircleX(float x) {
        this.x = x;
    }

    public void setCircleY(float y) {
        this.y = y;
    }
}
