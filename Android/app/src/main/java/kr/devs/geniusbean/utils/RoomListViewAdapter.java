package kr.devs.geniusbean.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import kr.devs.geniusbean.R;

public class RoomListViewAdapter extends BaseAdapter {

    private class ViewHolder {
        public TextView room_size;
        public TextView room_name;
        public ImageView room_locker;
        public TextView room_bat;
    }

    private Context mContext = null;
    public ArrayList<RoomListData> mListData = new ArrayList<RoomListData>();

    public RoomListViewAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clearData() {
        mListData.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_room, null);

            holder.room_size = (TextView) convertView.findViewById(R.id.list_room_size);
            holder.room_name = (TextView) convertView.findViewById(R.id.list_room_name);
            holder.room_locker = (ImageView) convertView.findViewById(R.id.list_room_locker);
            holder.room_bat = (TextView) convertView.findViewById(R.id.list_room_bat);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        RoomListData mData = mListData.get(position);

        if (mData.room_locked) {
            holder.room_locker.setVisibility(View.VISIBLE);
            holder.room_locker.setImageDrawable(mData.room_locker);
        }else{
            holder.room_locker.setVisibility(View.INVISIBLE);
            holder.room_locker.setImageDrawable(mData.room_locker);
        }

        holder.room_size.setText(mData.room_size);
        holder.room_name.setText(mData.room_name);
        holder.room_bat.setText(mData.room_bat);

        return convertView;

    }
}
