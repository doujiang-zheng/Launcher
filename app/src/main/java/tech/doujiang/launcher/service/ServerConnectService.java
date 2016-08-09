package tech.doujiang.launcher.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tech.doujiang.launcher.R;
import tech.doujiang.launcher.activity.LauncherActivity;
import tech.doujiang.launcher.util.IsonlineClient;
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
            TelephonyManager tm=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            GsmCellLocation gcl=(GsmCellLocation) tm.getCellLocation();
            int cid=gcl.getCid();
            int lac=gcl.getLac();
            int mcc = Integer.valueOf(tm.getNetworkOperator().substring(0, 3));
            int mnc = Integer.valueOf(tm.getNetworkOperator().substring(3,5));

            Location myloc = getLocation();
//            Log.v("ServerConnectService","Location " + myloc.equals(null));
            if( myloc != null ){
                longitude = myloc.getLongitude();
                latitude = myloc.getLatitude();
                IsonlineClient isonlineClient = new IsonlineClient();
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