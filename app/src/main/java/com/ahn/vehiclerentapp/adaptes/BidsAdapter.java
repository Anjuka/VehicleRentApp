package com.ahn.vehiclerentapp.adaptes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.models.posts.DriverData;
import com.ahn.vehiclerentapp.models.posts.PostsDataList;

import java.util.ArrayList;

public class BidsAdapter extends BaseAdapter {

    Context context;
    ArrayList<DriverData> bidsList;
    ItemClickListener itemClickListener;
    ItemClickListenerButtons itemClickListenerButtons;
    DriveInfoClickListener driveInfoClickListener;
    String post_position_id;

    public BidsAdapter(Context context, ArrayList<DriverData> bidsList,
                       ItemClickListener itemClickListener,
                       ItemClickListenerButtons itemClickListenerButtons, String post_position_id, DriveInfoClickListener driveInfoClickListener) {
        this.context = context;
        this.bidsList = bidsList;
        this.itemClickListener = itemClickListener;
        this.itemClickListenerButtons = itemClickListenerButtons;
        this.post_position_id = post_position_id;
        this.driveInfoClickListener = driveInfoClickListener;
    }

    @Override
    public int getCount() {
        return bidsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            contentView = inflater.inflate(R.layout.item_bids, parent, false);
        }

        TextView tv_driver_name = contentView.findViewById(R.id.tv_driver_name);
        TextView tv_bit = contentView.findViewById(R.id.tv_bit);
        TextView tv_accept = contentView.findViewById(R.id.tv_accept);
        TextView tv_cancel = contentView.findViewById(R.id.tv_cancel);
        ImageView iv_drive_info = contentView.findViewById(R.id.iv_drive_info);

        tv_driver_name.setText(bidsList.get(position).getName());
       // tv_vehicle_type.setText(context.getResources().getText(R.string.vehicle_type_post) + " " + bidsList.get(position).getPhone_number());
        tv_bit.setText(context.getResources().getText(R.string.rupee) + bidsList.get(position).getBit());

        tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListenerButtons.onItemClickButtons(position, bidsList.get(position), 1,
                        post_position_id, bidsList.get(position).getBit());
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListenerButtons.onItemClickButtons(position, bidsList.get(position),
                        0, post_position_id, bidsList.get(position).getBit());
            }
        });

        iv_drive_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driveInfoClickListener.onItemClickInfo(position, bidsList.get(position).getDriver_id());
            }
        });

        return contentView;
    }

    public interface ItemClickListener{
        void onItemClick(int position, PostsDataList postsDataLists);
    }

    public interface ItemClickListenerButtons{
        void onItemClickButtons(int position, DriverData driverData, int type, String post_position_id, String selected_bid);
    }

    public interface DriveInfoClickListener{
        void onItemClickInfo(int position, String driver_id);
    }
}
