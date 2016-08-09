package tech.doujiang.launcher.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tech.doujiang.launcher.R;
import tech.doujiang.launcher.activity.LauncherActivity;
import tech.doujiang.launcher.util.IsOnlineClient;
import tech.doujiang.launcher.util.HttpThread;

public class ServerConnectService extends Service {
    private final String TAG = "ServerConnectService";
    private Timer mTimer;
    private LockTask lockTask;
    String username = "Grinch";
    Boolean isonline = true;
    Boolean infoerase = false;
    Boolean islost = false;
    private LocationManager locationManager;
    private double longitude = 0.0;
    private double latitude = 0.0;
    private int mId = 1235;

//    LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//        }
//
//        @Override
//        public void onLocationChanged(Location location) {
//            longitude = location.getLongitude();
//            latitude = location.getLatitude();
//            Log.d(TAG, "Location longitude:" + longitude + " latitude: " + latitude);
//        }
//    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        // Build Foreground Service.
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.info)
                .setContentTitle("MobileSafe")
                .setContentText("Keep tracing location.");
        Intent resultIntent = new Intent(this, LauncherActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(LauncherActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, mBuilder.build());

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
//        criteria.setAltitudeRequired(false);
//        criteria.setBearingRequired(false);
//        criteria.setCostAllowed(true);
//        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
//        String provider =locationManager.getBestProvider(criteria, true);
//        locationManager.requestLocationUpdates(provider, 5 * 1000, 1, locationListener);
        super.onCreate();
        Log.v(TAG, "ServerConnectService is Created");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "ServerConnectService is Started");
        startTimer();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }

        Log.d(TAG, "Service is Destroyed");
        super.onDestroy();
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
            lockTask = new LockTask(this);
            mTimer.schedule(lockTask, 0, 5000);
        }
    }

    class LockTask extends TimerTask {

        private Context context;
        LockTask lockTask = this;

        public LockTask(Context context) {
            this.context = context;
            Log.d(TAG, "LockTask is Created");
        }

        @Override
        public void run() {

            Location myloc = getLocation();
//            Log.v("ServerConnectService","Location " + myloc.equals(null));
            if( myloc != null ){
                longitude = myloc.getLongitude();
                latitude = myloc.getLatitude();
                IsOnlineClient isonlineClient = new IsOnlineClient();
                isonlineClient.onlineconnect(username, isonline,  infoerase, islost, longitude, latitude);
                Log.e("ServerConnectService", "send information");
            }else{
//                Log.e("ServerConnectService", "location unavailable");
            }

            HttpThread httpThread = new HttpThread(context);
            httpThread.start();
        }
    }

    public Location getLocation() {
        LocationManager locManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers_list = locManger.getAllProviders();
        for (String item : providers_list) {
            Location loc = locManger.getLastKnownLocation(item);
            if (loc != null) {
                return loc;
            }
        }

        return null;
    }
}