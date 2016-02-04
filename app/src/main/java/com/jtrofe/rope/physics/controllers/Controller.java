package com.jtrofe.rope.physics.controllers;

import com.jtrofe.rope.physics.Engine;

public abstract class Controller{

    protected Engine mEngine;

    public Controller(Engine engine){
        this.mEngine = engine;
    }

    public abstract void Update(float timeStep);
}