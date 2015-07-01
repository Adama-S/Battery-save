package com.example.myApplication;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings.System;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;


public class Choice extends Activity {
    //the content resolver used as a handle to the system's settings
    private ContentResolver cResolver;
    //a variable to store the system brightness
    private int brightness;

    WindowManager.LayoutParams layoutParams;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_saving);


        //button submit
        Button bSubmit = (Button) findViewById(R.id.bSubmit);


        bSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // brightness is checked
                boolean isBrightnessChecked = ((Switch) findViewById(R.id.switchBright)).isChecked();
                // wifi is checked
                boolean isWifiChecked = ((Switch) findViewById(R.id.switchWifi)).isChecked();

                if (isBrightnessChecked) {
                    Toast.makeText(Choice.this, "You choose brightness!", Toast.LENGTH_LONG).show();

                    //get the content resolver
                    cResolver = getContentResolver();
                    //set system brightness at minimum
                    brightness = 20;
                    //set the system brightness using the brightness variable value
                    // todo check if auto-brightness is active
                    android.provider.Settings.System.putInt(getContentResolver(), System.SCREEN_BRIGHTNESS_MODE,
                            System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    android.provider.Settings.System.putInt(getContentResolver(),
                            android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
                }

                if (isWifiChecked) {
                    Toast.makeText(Choice.this, "You choose wifi!", Toast.LENGTH_LONG).show();
                    WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(false); // true or false to activate/deactivate wifi

                }

                if(isWifiChecked && isBrightnessChecked){
                    createNotification("Battery Save","Your phone is in power save mode");
                }

            }
        });


    }


    public void createNotification(String title, String description) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, Choice.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Build notification
        Notification noti = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(description).setSmallIcon(R.mipmap.ic_batterysave)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        noti.defaults |= Notification.DEFAULT_SOUND;
        noti.defaults |= Notification.DEFAULT_VIBRATE;
        noti.defaults |= Notification.DEFAULT_LIGHTS;




        notificationManager.notify(0, noti);

    }

}

