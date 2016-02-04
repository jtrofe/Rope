package com.jtrofe.rope.physics.constraints;

import com.jtrofe.rope.physics.gameobjects.GameObject;

/**
 * Created by jtrofe200 on 2/4/2016
 */
public abstract class Constraint{

    protected GameObject mBodyA;
    protected GameObject mBodyB;

    public Constraint(GameObject bodyA, GameObject bodyB){
        this.mBodyA = bodyA;
        this.mBodyB = bodyB;
    }


    public abstract void Solve(float timeStep);
}
