package sel.mobile.application.distance.zenk.seleksi;

/**
 * Created by ZenK on 3/26/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import entitas.list;


public class ListRecord extends BaseAdapter {
    private static ArrayList<list> record;

    private LayoutInflater mInflater;

    public ListRecord(Context context, ArrayList<list> results) {
        record = results;
        mInflater = LayoutInflater.from(context);

    }

    public int getCount() {
        return record.size();
    }

    public Object getItem(int position) {
        return record.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list, null);
            holder = new ViewHolder();
            holder.txtDatetime = (TextView) convertView.findViewById(R.id.datetime);
            holder.txtlokasi = (TextView) convertView.findViewById(R.id.lokasi);
            holder.txtlokasitujuan = (TextView) convertView.findViewById(R.id.lokasi_tujuan);
            holder.txtdistance = (TextView) convertView.findViewById(R.id.distance);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtDatetime.setText(record.get(position).getDatetime());
        holder.txtlokasi.setText(record.get(position).getLokasi());
        holder.txtlokasitujuan.setText(record.get(position).getLokasi_tujuan());
        holder.txtdistance.setText(""+record.get(position).getDistance()+" km");


        return convertView;
    }

    static class ViewHolder {
        TextView txtDatetime;
        TextView txtlokasi;
        TextView txtlokasitujuan;
        TextView txtdistance;

    }
}
