package com.jtrofe.rope.physics.controllers;

import android.graphics.Color;
import android.graphics.Paint;

import com.jtrofe.rope.physics.gameobjects.Flail;
import com.jtrofe.rope.physics.gameobjects.GameObject;
import com.jtrofe.rope.physics.gameobjects.Particle;
import com.jtrofe.rope.physics.Engine;
import com.jtrofe.rope.physics.Vec;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAIN on 1/23/16
 */
public class FlailController extends Controller{

    public FlailController(Engine engine){
        super(engine);
    }

    @Override
    public void Update(float timeStep){
        List<GameObject> flailList = mEngine.GetType(GameObject.TYPE_FLAIL);

        // Validate the touch points on the flails
        for(GameObject f:flailList){
            Flail flail = (Flail) f;

            flail.HandlePoint = mEngine.TouchPoint;
        }


        for(GameObject f:flailList){
            Flail flail = (Flail) f;

            if(flail.HandlePoint != null){
                Vec d = flail.HandlePoint.Subtract(flail.GetPosition());

                if(d.LengthSquared() > flail.RopeLength * flail.RopeLength){
                    float off = d.Length() - flail.RopeLength;
                    d = d.Normalize();

                    d = d.ScalarMultiply((off * flail.GetMass()) / timeStep);

                    flail.ApplyImpulse(d, flail.GetPosition());
                }

            }
            /*
            // If the user is dragging across the screen, move the
            //   flail to the touch point
            if(flail.TouchPointId != -1){
                // Hooke's law:
                //  F = kX

                float k = ((Flail) f).GetK();

                Engine.TouchPoint p = mEngine.GetTouchPointById(flail.TouchPointId);

                if(p != null) {
                    Vec T = mEngine.GetTouchPointById(flail.TouchPointId).Point;
                    Vec X = T.Subtract(f.GetPosition());

                    Vec F = X.ScalarMultiply(k);

                    F = F.Clamp(300); // Max force that will be applied

                    f.ApplyForce(F, f.GetPosition());
                    flail.HandlePoint = p.Point.Clone();
                }else{
                    flail.HandlePoint = null;
                }
            }else{
                flail.HandlePoint = null;
            }*/

        }
    }
}