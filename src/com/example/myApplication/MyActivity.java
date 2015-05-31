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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

            batteryInfo.setText(
                            "Health: "+health+"\n"+
                            "Level: "+level+"\n"+
                            "Plugged: "+plugged+"\n"+
                            "Present: "+present+"\n"+
                            "Voltage: "+voltage+"\n"+
                            "Scale: "+scale+"\n"+
                            "Status: "+status+"\n"+
                            "Technology: "+technology+"\n"+
                            "Temperature: "+temperature+"\n"

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
