package com.jtrofe.rope.physics.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jtrofe.rope.physics.Vec;

/**
 * Created by jtrofe200 on 2/4/2016
 */
public class MouseObject extends GameObject{

    public MouseObject(){
        super(new Vec(), null, Float.MAX_VALUE);

        this.mType = GameObject.TYPE_MOUSE;
    }


    @Override
    public void Draw(Canvas canvas){
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.YELLOW);

        canvas.drawCircle(mPosition.x, mPosition.y, 40, p);
    }
}
