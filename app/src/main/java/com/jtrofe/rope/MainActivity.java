package com.jtrofe.rope;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jtrofe.rope.physics.GameSurfaceView;

public class MainActivity extends Activity {

    GameSurfaceView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameSurfaceView(this);
        setContentView(gameView);

    }

    @Override
    protected void onResume(){
        super.onResume();

        gameView.Resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        gameView.Pause();
    }

}
