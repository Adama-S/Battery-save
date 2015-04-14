package com.example.myApplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.*;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.*;

public class MyActivity extends Activity {
    private TextView batteryInfo;
    private ImageView imageBatteryState;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        batteryInfo=(TextView)findViewById(R.id.textViewBatteryInfo);
        imageBatteryState=(ImageView)findViewById(R.id.imageViewBatteryState);

        this.registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int  health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            int  icon_small= intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0);
            int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            int  plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
            boolean  present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
            int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            int  status= intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            String  technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            int  temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            int  voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            PackageManager appInfo = getPackageManager();
            List<ApplicationInfo> list = appInfo.getInstalledApplications(0);

            Drawable icon ;
            Drawable logo ;
            CharSequence label = "" ;
            ArrayList<CharSequence> lstChrS = new ArrayList<CharSequence>();
            ArrayList<Drawable> lstDraw = new ArrayList<Drawable>();
            for(ApplicationInfo applicationInfo : list){
                icon = getPackageManager().getApplicationIcon(applicationInfo);
                label = getPackageManager().getApplicationLabel(applicationInfo);
                logo = getPackageManager().getApplicationLogo(applicationInfo);
                lstDraw.add(icon);
                lstDraw.add(logo);
                lstChrS.add(label);
            }


            batteryInfo.setText(
//                    "Health: "+health+"\n"+
                            "Level: "+level+"\n"+
                            "Plugged: "+plugged+"\n"+
                            "Present: "+present+"\n"+
                            "Voltage: "+voltage+"\n"+
//                            "Scale: "+scale+"\n"+
//                            "Status: "+status+"\n"+
                            "Technology: "+technology+"\n"+
                            "Temperature: "+temperature+"\n"+
                            "Label: "+lstChrS+"\n"+
                            "Drawlable: "+lstDraw+"\n");


            imageBatteryState.setImageResource(icon_small);
        }
    };

}
