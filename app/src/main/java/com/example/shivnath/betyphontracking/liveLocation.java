package com.example.shivnath.betyphontracking;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class liveLocation extends Service {
    private Timer timer = new Timer();


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Testing"+"Working");
            }
        }, 0, 60*1000);//1 Minutes
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

}
