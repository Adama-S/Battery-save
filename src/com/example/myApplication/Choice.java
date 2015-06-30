package com.example.myApplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
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
        Button bSubmit = (Button)findViewById(R.id.bSubmit);


        bSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // brightness is checked
                boolean isBrightnessChecked = ((Switch) findViewById(R.id.switchBright)).isChecked();
                // wifi is checked
                boolean isWifiChecked = ((Switch) findViewById(R.id.switchWifi)).isChecked();

                if(isBrightnessChecked) {
                    Toast.makeText(Choice.this, "You choose brightness!", Toast.LENGTH_LONG).show();

                    //get the content resolver
                    cResolver = getContentResolver();
                    //set system brightness at minimum
                    brightness = 20;
                    //set the system brightness using the brightness variable value
                    // todo check if auto-brightness is active
                    android.provider.Settings.System.putInt(getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
                }

                if(isWifiChecked){
                    Toast.makeText(Choice.this, "You choose wifi!", Toast.LENGTH_LONG).show();
                    WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(false); // true or false to activate/deactivate wifi
                }

            }
            });




    }


}

