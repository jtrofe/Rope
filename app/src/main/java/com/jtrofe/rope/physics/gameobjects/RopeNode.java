package com.jtrofe.rope.physics.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jtrofe.rope.physics.Vec;

/**
 * Created by jtrofe200 on 2/3/2016
 */
public class RopeNode extends GameObject{

    public RopeNode Parent;
    public Paint paint;

    public void SetMass(float m){
        mMass = m;
    }

    public RopeNode(Vec position){
        super(position, null, 5);

        this.mFriction = 0.9f;

        this.mType = GameObject.TYPE_ROPE_NODE;

        this.paint = new Paint();
        paint.setColor(Color.RED);
    }


    @Override
    public void Draw(Canvas canvas){
        if(Parent != null){
            canvas.drawLine(mPosition.x, mPosition.y, Parent.GetPosition().x, Parent.GetPosition().y, paint);
        }

        int r = 0;
        if(mMass > 5) r = 30;
        canvas.drawCircle(mPosition.x, mPosition.y, r, paint);
    }
}
