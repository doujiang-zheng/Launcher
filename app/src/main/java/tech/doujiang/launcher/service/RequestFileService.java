package tech.doujiang.launcher.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class RequestFileService extends Service {
    String username = null;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        username = intent.getStringExtra("username");
        String connectionurl = "http://23.83.251.48:8080/DocIssue";
        try {

            HttpClient client = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(connectionurl);
            // Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<NameValuePair>(1);
            params.add(new BasicNameValuePair("username", username));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            Log.v("RequestFileService", "PostEntity has already been filled!");
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
            HttpResponse response = client.execute(httppost);

            if (response.getStatusLine().getStatusCode() == 200) {
                getInfo(response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return START_STICKY;
    }
    private static void getInfo(HttpResponse response) throws Exception {

        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        int len = is.available();
        byte[] data = new byte[len];
        is.read(data);
        is.close();
        String content = new String(data, "UTF-8" );
        int pathend = content.indexOf(".txt")+4;
        int pathstart = 12;
        String rela_path = content.substring(pathstart, pathend);
        String exter_path = Environment.getExternalStorageDirectory().getPath();
        String filepath = exter_path + rela_path;
        try{
            File file = new File(filepath);
            if( !file.exists() ){
                if( !file.getParentFile().exists() )
                {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            FileOutputStream os = new FileOutputStream(file);
            os.write(data);
            os.close();
        }catch( Exception ex){
            ex.toString();
        }
    }
}

