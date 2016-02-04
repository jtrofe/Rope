package com.jtrofe.rope.physics.controllers;

import com.jtrofe.rope.physics.Engine;
import com.jtrofe.rope.physics.gameobjects.GameObject;

import java.util.List;

/**
 * Created by jtrofe200 on 2/4/2016
 */
public class MouseController extends Controller{

    public MouseController(Engine engine){
        super(engine);
    }

    @Override
    public void Update(float timeStep){
        List<GameObject> mouseList = mEngine.GetType(GameObject.TYPE_MOUSE);

        for(GameObject mouseObject:mouseList){
            if(mEngine.TouchPoint != null)
                    mouseObject.SetPosition(mEngine.TouchPoint);
        }
    }
}
