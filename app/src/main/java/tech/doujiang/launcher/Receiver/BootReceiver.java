package tech.doujiang.launcher.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import tech.doujiang.launcher.service.CallSmsFirewallService;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        *   开机启动防火墙服务
        * */
        Intent service = new Intent(context, CallSmsFirewallService.class);
        context.startService(service);
        Log.e("Receiver", "StartCallSmsFirewallService");

    }
}
