package com.jtrofe.rope.physics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jtrofe.rope.physics.constraints.Constraint;
import com.jtrofe.rope.physics.controllers.FlailController;
import com.jtrofe.rope.physics.controllers.MouseController;
import com.jtrofe.rope.physics.controllers.RopeController;
import com.jtrofe.rope.physics.gameobjects.GameObject;
import com.jtrofe.rope.physics.controllers.Controller;
import com.jtrofe.rope.physics.controllers.ParticleController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by MAIN on 1/20/16
 */
public class Engine{

    public boolean Initialized = false;

    private List<GameObject> mBodies;
    private List<GameObject> mBodiesToAdd;
    private List<GameObject> mBodiesToRemove;

    private List<Constraint> mConstraints;
    private List<Constraint> mConstraintsToAdd;
    private List<Constraint> mConstraintsToRemove;


    private GameSurfaceView mGameSurfaceView;



    public void SetSurfaceView(GameSurfaceView gameSurfaceView){
        this.mGameSurfaceView = gameSurfaceView;
    }


    private int mWorldWidth;
    private int mWorldHeight;

    public int GetWorldWidth(){
        return mWorldWidth;
    }

    public int GetWorldHeight(){
        return mWorldHeight;
    }

    private List<Controller> mControllers;


    public Vec TouchPoint = null;

    /**
     * Create a basic world
     * @param worldWidth Screen width
     * @param worldHeight Screen height
     */
    public Engine(int worldWidth, int worldHeight, GameSurfaceView gameSurfaceView){
        this.mWorldWidth = worldWidth;
        this.mWorldHeight = worldHeight;
        this.mGameSurfaceView = gameSurfaceView;

        mBodies = new ArrayList<>();
        mBodiesToAdd = new ArrayList<>();
        mBodiesToRemove = new ArrayList<>();
        mControllers = new ArrayList<>();

        mConstraints = new ArrayList<>();
        mConstraintsToAdd = new ArrayList<>();
        mConstraintsToRemove = new ArrayList<>();

        // Add controllers
        mControllers.add(new ParticleController(this));
        mControllers.add(new RopeController(this));
        mControllers.add(new FlailController(this));
        mControllers.add(new MouseController(this));


        debugBitmap = Bitmap.createBitmap(worldWidth, worldHeight,
                Bitmap.Config.ARGB_8888);
        debugCanvas = new Canvas(debugBitmap);
    }

    public void AddConstraint(Constraint c){
        mConstraintsToAdd.add(c);
    }

    public void RemoveConstraint(Constraint c){
        mConstraintsToRemove.add(c);
    }

    private void addWaitingConstraints(){
        mConstraints.addAll(mConstraintsToAdd);

        mConstraintsToAdd = new ArrayList<>();
    }

    private void removeWaitingConstraints(){
        mConstraints.removeAll(mConstraintsToRemove);

        mConstraintsToRemove = new ArrayList<>();
    }

    public void AddBody(GameObject b){
        mBodiesToAdd.add(b);
    }

    public void RemoveBody(GameObject b){
        mBodiesToRemove.add(b);
    }

    private void addWaitingBodies(){
        mBodies.addAll(mBodiesToAdd);

        mBodiesToAdd = new ArrayList<>();
    }

    private void removeWaitingBodies(){
        mBodies.removeAll(mBodiesToRemove);

        mBodiesToRemove = new ArrayList<>();
    }

    public void SetWorldBounds(int worldWidth, int worldHeight){
        mWorldWidth = worldWidth;
        mWorldHeight = worldHeight;

        debugBitmap = Bitmap.createBitmap(worldWidth, worldHeight,
                Bitmap.Config.ARGB_8888);
        debugCanvas = new Canvas(debugBitmap);
    }

    private void resetForces(){
        for(GameObject b:mBodies){
            b.ClearForce();
        }
    }

    private void computeForces(float timeStep){
        for (Controller c:mControllers){
            c.Update(timeStep);
        }
    }

    private void updatePositions(float timeStep){
        for(GameObject b:mBodies){
            b.UpdateForceAndTorque(timeStep);

            Vec l = b.GetLinearVelocity();
            if(b.GetPosition().y > mWorldHeight){
                b.SetPosition(new Vec(b.GetPosition().x, mWorldHeight));
                b.SetLinearVelocity(new Vec(-l.x, -l.y));
            }

            if(b.GetPosition().x > mWorldWidth){
                b.SetPosition(new Vec(mWorldWidth, b.GetPosition().y));
                b.SetLinearVelocity(b.GetLinearVelocity().Negate());
            }else if(b.GetPosition().x < 0){
                b.SetPosition(new Vec(0, b.GetPosition().y));
                b.SetLinearVelocity(b.GetLinearVelocity().Negate());
            }
        }
    }

    public List<GameObject> GetType(int type){
        List<GameObject> objects = new ArrayList<>();

        for(GameObject o:mBodies){
            if(o.GetType() == type){
                objects.add(o);
            }
        }

        return objects;
    }

    private void solveConstraints(float timeStep){
        for(Constraint c:mConstraints){
            c.Solve(timeStep);
        }
    }

    /**
     * Run one iteration of the simulation
     * @param timeStep How many seconds to advance the simulation
     */
    public void Step(float timeStep){

        if(!Initialized) return;

        resetForces();

        computeForces(timeStep);

        solveConstraints(timeStep);

        updatePositions(timeStep);

        // Clean up by adding bodies waiting to be
        //    added and removing ones waiting to be
        //    removed
        addWaitingBodies();
        removeWaitingBodies();

        addWaitingConstraints();
        removeWaitingConstraints();
    }

    private Bitmap debugBitmap;
    public Canvas debugCanvas;

    public void Draw(Canvas canvas){

        if(mBodies.size() == 0) return;

        int canvasSave = canvas.save();

        // TODO come up with a better background
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.WHITE);
        canvas.drawRect(50, 50, mWorldWidth - 50, mWorldHeight - 50, p);


        for(GameObject b:mBodies){
            b.Draw(canvas);
        }

        // TODO remove debug stuff before release
        /*if(debugBitmap != null)
            canvas.drawBitmap(debugBitmap, 0, 0, null);
        int w = Math.max(canvas.getWidth(), canvas.getHeight());
        int h = Math.min(canvas.getWidth(), canvas.getHeight());
        debugBitmap = Bitmap.createBitmap(w, h,
                Bitmap.Config.ARGB_8888);
        debugCanvas = new Canvas(debugBitmap);*/

        // Restore the orientation of the canvas
        canvas.restoreToCount(canvasSave);
    }
}