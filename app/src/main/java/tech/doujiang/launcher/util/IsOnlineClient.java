package tech.doujiang.launcher.util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grinch on 16/8/8.
 */
public class IsonlineClient {

    public static String connectionurl = "http://169.229.85.113:8080/YunMobileSafe/Isonline";

     public IsonlineClient() {
    }

    public void onlineconnect(String username, boolean online, boolean infoerase, boolean islost, double longitude, double latitude) {
        try {
            HttpClient client = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(connectionurl);
            List<NameValuePair> params = new ArrayList<NameValuePair>(4);
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("isonline", String.valueOf(online)));
            params.add(new BasicNameValuePair("infoerase", String.valueOf(infoerase)));
            params.add(new BasicNameValuePair("islost", String.valueOf(islost)));
            params.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
            params.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);

            HttpResponse response = client.execute(httppost);

            if (response.getStatusLine().getStatusCode() == 200) {
                String outcome = getInfo(response);
                Log.v("Response", outcome.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update() {

    }

    private static String getInfo(HttpResponse response) throws Exception {

        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        byte[] data = read(is);
        return new String(data, "UTF-8");
    }

    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }

}
