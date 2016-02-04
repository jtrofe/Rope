package com.jtrofe.rope.physics.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.jtrofe.rope.physics.Vec;


/**
 * Basic flail
 */
public class Flail extends GameObject{

    /**
     * Physics properties
     */
    protected float mRadius;

    public Vec TouchPoint;
    public float RopeLength = 400;

    public float GetRadius(){
        return mRadius;
    }

    /**
     * Physics variables
     */
    protected float mK;
    public boolean PlowThrough = false; // If true, flail will not bounce off robots

    public float GetK(){
        return mK;
    }

    /**
     * Control variables
     */
    public int TouchPointId = -1;
    public Vec HandlePoint;

    /**
     * Drawing variables
     */
    protected Paint mChainPaint;

    public Flail(Vec position, Bitmap image, float mass, float k, float radius){
        super(position, image, mass);

        this.mType = GameObject.TYPE_FLAIL;

        this.mMass = mass;
        this.mK = k;

        this.mFriction = 0.92f;

        this.mRadius = radius;


        // Set up paint to draw the chain
        this.mChainPaint = new Paint();
        this.mChainPaint.setStyle(Paint.Style.STROKE);
        this.mChainPaint.setStrokeWidth(2);
        this.mChainPaint.setColor(Color.WHITE);
    }

    private Rect getFlail(){
        return new Rect(0, 0, 100, 100);
    }

    /**
     * If the user is dragging the flail, draw a chain
     * connecting the head and handle
     * @param canvas Drawing canvas
     */
    @Override
    public void Draw(Canvas canvas){

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);

        if(HandlePoint != null){
            Vec d = HandlePoint.Subtract(mPosition);
            Vec n = d.Normalize().Right();

            float off = Math.max(RopeLength - d.Length(), 0);

            int NUM_BENDS = 5;

            float startPercent = 0.0f;
            float endPercent = 1.0f;
            float percentInc = (endPercent - startPercent) / (NUM_BENDS * 2);
            float per = 0;

            Path rope = new Path();
            rope.moveTo(mPosition.x, mPosition.y);
//            rope.lineTo(mPosition.x + (d.x * startPercent), mPosition.y + (d.y * startPercent));

            int dir = 1;
            for(int i=0;i<NUM_BENDS;i++){
                float amt = ((float) i / (float) NUM_BENDS) + percentInc;
                float s = (float) Math.sin(amt * Math.PI);

                float stretch = off * s * 0.5f;

                Vec v = d.ScalarMultiply(per + percentInc);

                Vec control = mPosition.Add(v).Add(n.ScalarMultiply(stretch * dir));

                Vec nextPoint = mPosition.Add(d.ScalarMultiply(per + (percentInc * 2)));

                rope.quadTo(control.x, control.y, nextPoint.x, nextPoint.y);


                per += percentInc * 2;
                dir *= -1;
            }

            rope.lineTo(HandlePoint.x, HandlePoint.y);

            /*n = n.ScalarMultiply(off);

            d = d.ScalarMultiply(0.5f);


            rope.quadTo(mPosition.x + d.x + n.x, mPosition.y + d.y + n.y, HandlePoint.x, HandlePoint.y);*/

            canvas.drawPath(rope, p);
            //canvas.drawLine(mPosition.x, mPosition.y, HandlePoint.x, HandlePoint.y, p);
        }

        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mPosition.x, mPosition.y, 20, p);


    }
}