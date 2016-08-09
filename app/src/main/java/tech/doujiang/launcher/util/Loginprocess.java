package tech.doujiang.launcher.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.lang.Object;

public class Loginprocess {

    private static String connectionurl;
    private Loginfo myinfo;

    public Loginprocess(Loginfo loginfo) {
        this.myinfo = loginfo;
    }

    public static boolean networktest() {
        try {
            connectionurl = "http://23.83.251.48:8080/YunMobileSafe/Userconfirm";
            Log.v("Loginprocess", "networktest");
            connectionurl += "?networktest=test";
            Log.v("URL", connectionurl);
            URL url = new URL(connectionurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Connection", "Close");
            //conn.connect();

            Log.v("response", String.valueOf(conn.getResponseCode()));
            if (conn.getResponseCode() == 200) {
                return true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean confirm() {
        String username = myinfo.getUsername();
        String psw = myinfo.getPsw();
        connectionurl = "http://23.83.251.48:8080/YunMobileSafe/Userconfirm";
        try {
			/*
			 * Map<String, String> params = new HashMap<String, String>();
			 * params.put("username", username); params.put("psw", psw);
			 * List<NameValuePair> pair = new ArrayList<NameValuePair>(); if(
			 * params != null && !params.isEmpty() ){ for( Map.Entry<String,
			 * String> entry : params.entrySet() ){ pair.add(new
			 * BasicNameValuePair(entry.getKey(), entry.getValue())); } }
			 * UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pair,
			 * "UTF-8");
			 */
            HttpClient client = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(connectionurl);
            // Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("psw", psw));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            Log.v("Entity", "PostEntity has already been filled!");
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
            HttpResponse response = client.execute(httppost);

            if (response.getStatusLine().getStatusCode() == 200) {
                String outcome = getInfo(response);
                Log.v("Response", outcome.toString());
                if( outcome.equals("authorized") )
                    return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;

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
