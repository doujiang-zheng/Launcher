package tech.doujiang.launcher.adapter;

import java.util.*;

//import org.apache.commons.beanutils.Converter;

import android.R.integer;
import android.content.*;
import android.net.Uri;
//import android.support.annotation.CallSuper;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import tech.doujiang.launcher.R;
import tech.doujiang.launcher.model.CallLogBean;

/**
 * Created by 豆浆 on 2016-07-12.
 */
public class DialAdapter extends BaseAdapter{
    private Context ctx;
    private List<CallLogBean> callLogs;
    private LayoutInflater inflater;

    public DialAdapter(Context context, List<CallLogBean> callLogs) {
        this.ctx = context;
        this.callLogs = callLogs;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return callLogs.size();
    }

    @Override
    public Object getItem(int position) {
        return callLogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_record_list_item
                    , null);
            holder = new ViewHolder();
            holder.call_type = (ImageView) convertView.findViewById(R.id.call_type);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.call_btn = (TextView) convertView.findViewById(R.id.call_btn);
            convertView.setTag(holder); // »º´æ
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CallLogBean callLog = callLogs.get(position);
        switch (callLog.getType()) {
            case 1:
                holder.call_type
                        .setBackgroundResource(R.drawable.touxiang);
                break;
            case 2:
                //Log.e("e", "HOLDER HERE!");
                holder.call_type
                        .setBackgroundResource(R.drawable.touxiang);
                break;
            case 3:
                holder.call_type
                        .setBackgroundResource(R.drawable.touxiang);
                break;
            default:
                break;
        }
        holder.name.setText(callLog.getName());
        holder.number.setText(callLog.getNumber());
        holder.time.setText(callLog.getDate());

        addViewListener(holder.call_btn, callLog, position);
        return convertView;
    }

    private static class ViewHolder {
        ImageView call_type;
        TextView name;
        TextView number;
        TextView time;
        TextView call_btn;
    }

    private void addViewListener(View view, final CallLogBean callLog
            , final int position) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + callLog.getNumber());
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                ctx.startActivity(intent);
            }
        });
    }
}
