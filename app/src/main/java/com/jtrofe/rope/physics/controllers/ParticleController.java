package com.jtrofe.rope.physics.controllers;

import com.jtrofe.rope.physics.Engine;
import com.jtrofe.rope.physics.gameobjects.GameObject;
import com.jtrofe.rope.physics.gameobjects.Particle;

import java.util.List;

public class ParticleController extends Controller {

    public ParticleController(Engine engine){
        super(engine);
    }

    @Override
    public void Update(float timeStep){
        List<GameObject> particleList = mEngine.GetType(GameObject.TYPE_PARTICLE);

        for(GameObject particleObject:particleList){
            Particle particle = (Particle) particleObject;

            particle.LifeSpan --;

            if(particle.LifeSpan == 0){
                mEngine.RemoveBody(particle);
            }
        }
    }
}