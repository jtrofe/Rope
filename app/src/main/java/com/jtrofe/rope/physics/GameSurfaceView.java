package com.jtrofe.rope.physics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jtrofe.rope.physics.constraints.Constraint;
import com.jtrofe.rope.physics.constraints.DistanceConstraint;
import com.jtrofe.rope.physics.gameobjects.Flail;
import com.jtrofe.rope.physics.gameobjects.MouseObject;
import com.jtrofe.rope.physics.gameobjects.Particle;
import com.jtrofe.rope.physics.gameobjects.RopeNode;


/**
 *
 */
public class GameSurfaceView extends SurfaceView implements Runnable{

    private final static int MAX_FPS = 40; //desired fps
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;

    private SurfaceHolder holder;
    private boolean isRunning = false;
    private Thread gameThread;





    private int screenWidth;
    private int screenHeight;

    private Engine mEngine;


    public GameSurfaceView(Context context){
        super(context);

        holder = getHolder();
        holder.addCallback(surfaceCallback);


        mEngine = new Engine(1000, 1500, this);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mEngine.TouchPoint = new Vec(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                mEngine.TouchPoint = new Vec(event.getX(), event.getY());
                return true;
        }

        return super.onTouchEvent(event);
    }

    /**
     * On lifecycle resume
     */
    public void Resume(){
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * On lifecycle pause
     */
    public void Pause(){
        isRunning = false;
        boolean retry = true;
        while(retry){
            try{
                gameThread.join();
                retry = false;
            }catch (InterruptedException e){
                retry = true;
            }
        }
    }

    protected void render(Canvas canvas){
        canvas.drawColor(Color.BLACK);

        mEngine.Draw(canvas);
    }

    protected void step(){
        float TIME_STEP = 0.8f;

        mEngine.Step(TIME_STEP);
    }

    @Override
    public void run(){
        while(isRunning){
            if(!holder.getSurface().isValid()) continue;

            long started = System.currentTimeMillis();

            // Update
            step();

            // Draw
            Canvas canvas = holder.lockCanvas();
            if(canvas != null){
                render(canvas);
                holder.unlockCanvasAndPost(canvas);
            }


            float deltaTime = (System.currentTimeMillis() - started);
            int sleepTime = (int) (FRAME_PERIOD - deltaTime);
            if (sleepTime > 0) {
                try {
                    //gameThread.sleep(sleepTime);
                    Thread.sleep(sleepTime);
                }catch (InterruptedException e) {
                    // ok
                }
            }
            while (sleepTime < 0) {
                step();
                sleepTime += FRAME_PERIOD;
            }
        }
    }

    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {}

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            screenWidth = Math.max(width, height);
            screenHeight = Math.min(width, height);


            mEngine.SetWorldBounds(screenWidth, screenHeight);



            if(!mEngine.Initialized){
                InitializeLevel();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {}
    };

    private void InitializeLevel(){

        Flail flail = new Flail(new Vec(100, 100), null, 20, 0.6f, 20);

        MouseObject m = new MouseObject();
        mEngine.AddBody(m);

        //mEngine.AddBody(flail);

        /*RopeNode p = new RopeNode(new Vec(300, 400));
        RopeNode p2 = new RopeNode(new Vec(340, 400));

        DistanceConstraint d = new DistanceConstraint(p, p2, 30);

        mEngine.AddBody(p);
        mEngine.AddBody(p2);

        mEngine.AddConstraint(d);*/

        RopeNode lastNode = null;
        RopeNode firstNode = null;
        int NODE_COUNT = 20;
        for(int i=0;i<NODE_COUNT;i++){
            RopeNode p = new RopeNode(new Vec(300 + 10 * i, 400));

            if(firstNode == null){
                firstNode = p;
            }
            if(i == 0){
                p.paint.setColor(Color.RED);
            }

            mEngine.AddBody(p);
            p.Parent = lastNode;

            if(lastNode != null) {
                DistanceConstraint d = new DistanceConstraint(p, p.Parent, 10);

                mEngine.AddConstraint(d);
            }
            lastNode = p;

            if(i == NODE_COUNT - 1){
                p.SetMass(30);
            }
        }

        DistanceConstraint d = new DistanceConstraint(m, firstNode, 5);
        mEngine.AddConstraint(d);

        mEngine.Initialized = true;
    }
}