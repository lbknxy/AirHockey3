package com.arcsoft.firstopenglproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AirHockeyActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    private boolean rendererSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        setContentView(glSurfaceView);



        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >=0x20000;


        if(supportsEs2){
            glSurfaceView.setEGLContextClientVersion(2);

            glSurfaceView.setRenderer(new AirHockeyRenderer(this));

            rendererSet = true;
        }else{
            Toast.makeText(this,"this device dose not support opengl es 2.0",Toast.LENGTH_SHORT).show();
            return ;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(rendererSet){
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        glSurfaceView.onResume();
    }
}
