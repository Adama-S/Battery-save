package com.example.myApplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.*;

public class MyActivity extends Activity {
    private TextView batteryInfo;
    private ImageView imageBatteryState;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //button saving energy
        Button bSaving = (Button)findViewById(R.id.button);
        bSaving.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, Choice.class);
                startActivity(intent);
            }
        });


        //information of battery
        batteryInfo=(TextView)findViewById(R.id.textViewBatteryInfo);
        imageBatteryState=(ImageView)findViewById(R.id.imageViewBatteryState);


        //list of applications
        loadApps();

        this.registerReceiver(this.pluggedReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private void loadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> mApps = getPackageManager().queryIntentActivities(mainIntent, 0);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new AppsAdapter(this,mApps));
    }

    public BroadcastReceiver pluggedReceiver = new BroadcastReceiver() {
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

            String sPlugged = "";
            if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
                sPlugged = "In load (AC)";
            } else if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {
                sPlugged = "In load (USB)";
            } else if (plugged == 0) {
                sPlugged = "Battery mode";
            } else {
                sPlugged = "Unknown";
            }


            //Santé de la batterie
            String sHealth = "";
            switch (health) {
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    sHealth = "Dead";
                    break;
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    sHealth = "Good";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    sHealth = "In overheating";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    sHealth = "Overvoltage";
                    break;
                case BatteryManager.BATTERY_HEALTH_COLD:
                    sHealth = "Cold";
                    break;
                default:
                    sHealth = "Non spécifié";
                    break;
            }



            batteryInfo.setText(
                            "Health: "+sHealth+"\n"+
                            "Level: "+level+"\n"+
                            "Plugged: "+sPlugged+"\n"+
                            "Voltage: "+String.valueOf((float) voltage / 1000) + " V"+"\n"+
                            "Technology: "+technology+"\n"+
                            "Temperature: "+String.valueOf((float) temperature / 10) + " C"+"\n"

            );

            imageBatteryState.setImageResource(icon_small);
        }
    };

    public class AppsAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<ResolveInfo> mApps;

        public AppsAdapter(Context context, List<ResolveInfo> mApps) {
            this.inflater = LayoutInflater.from(context);
            this.mApps = mApps;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHendler hendler;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_application, null);
                hendler = new ViewHendler();
                hendler.textLable = (TextView)convertView.findViewById(R.id.label);
                hendler.iconImage = (ImageView)convertView.findViewById(R.id.imageView);
                convertView.setTag(hendler);
            } else {
                hendler = (ViewHendler)convertView.getTag();
            }
            ResolveInfo info = this.mApps.get(position);
            hendler.iconImage.setImageDrawable(info.loadIcon(getPackageManager()));
            hendler.textLable.setText(info.loadLabel(getPackageManager()));

            return convertView;

        }
        class ViewHendler{
            TextView textLable;
            ImageView iconImage;
        }


        public final int getCount() {
            return mApps.size();
        }

        public final Object getItem(int position) {
            return mApps.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }



}
