package tech.doujiang.launcher.util;

import android.content.Context;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by doujiang_zheng on 16/8/8.
 */
public class HttpThread extends Thread{

    TelephonyManager tm=null;
    GsmCellLocation gcl=null;
    int cid=0;
    int lac=0;
    int mcc = 0;
    int mnc =0;
    StringBuffer sb=null;

    public HttpThread(Context context){
        tm=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        gcl=(GsmCellLocation) tm.getCellLocation();
        cid=gcl.getCid();
        lac=gcl.getLac();
        mcc = Integer.valueOf(tm.getNetworkOperator().substring(0, 3));
        mnc = Integer.valueOf(tm.getNetworkOperator().substring(3,5));

        sb=new StringBuffer();
        sb.append("cid:"+cid + "\n");
        sb.append("lac:"+lac + "\n");
        sb.append("mcc:"+mcc + "\n");
        sb.append("mnc:"+mnc + "\n");
    }

    public void run(){
        try {
//            Log.e("HttpThread: ", "start");
            JSONObject jObject = new JSONObject();
            jObject.put("version", "1.1.0");
            jObject.put("host", "maps.google.com");
            jObject.put("request_address", true);
            if (mcc == 460) {
                jObject.put("address_language", "zh_CN");
            } else {
                jObject.put("address_language", "en_US");
            }
            JSONArray jArray = new JSONArray();
            JSONObject jData = new JSONObject();
            jData.put("cell_id", cid);
            jData.put("location_area_code", lac);
            jData.put("mobile_country_code", mcc);
            jData.put("mobile_network_code", mnc);
            jArray.put(jData);
            jObject.put("cell_towers", jArray);

            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://www.minigps.net/map.html");
            StringEntity se = new StringEntity(jObject.toString());
            post.setEntity(se);
            HttpResponse resp = client.execute(post);
            BufferedReader br = null;
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                sb.append("联网成功\n");
                br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            }else{
                sb.append("联网获取数据失败!\n");
            }

            String result = br.readLine();
            while (result != null) {
                sb.append(result);
                result = br.readLine();
            }
//            Log.e("HttpThread: ", sb.toString());
        }catch(Exception ex){
            sb.append(ex.getMessage());
        }
//        Message msg=new Message();
//        msg.what=1;
    }
}