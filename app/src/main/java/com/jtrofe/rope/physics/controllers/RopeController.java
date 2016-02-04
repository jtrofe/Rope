package com.jtrofe.rope.physics.controllers;

import com.jtrofe.rope.physics.Engine;
import com.jtrofe.rope.physics.Vec;
import com.jtrofe.rope.physics.gameobjects.GameObject;
import com.jtrofe.rope.physics.gameobjects.RopeNode;

import java.util.List;

/**
 * Created by jtrofe200 on 2/3/2016
 */
public class RopeController extends Controller {

    public RopeController(Engine engine){
        super(engine);
    }

    @Override
    public void Update(float timeStep){
        List<GameObject> nodeList = mEngine.GetType(GameObject.TYPE_ROPE_NODE);

        int cnt = 0;
        for(GameObject nodeObject:nodeList){
            RopeNode r = (RopeNode) nodeObject;


            if(r.Parent == null && mEngine.TouchPoint != null){
                Vec n = mEngine.TouchPoint.Subtract(r.GetPosition());

                float d = n.Length();

                r.ApplyForce(n.ScalarMultiply(0.8f), r.GetPosition());

            }
        }
    }
}
