package com.ahn.vehiclerentapp.adaptes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.models.posts.DriverData;
import com.ahn.vehiclerentapp.models.posts.PostsDataList;

import java.util.ArrayList;

public class PostDriverViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<PostsDataList> postsDataLists;
    itemClickListner itemClickListner;
    String user_id;

    public PostDriverViewAdapter(Context context, ArrayList<PostsDataList> postsDataLists, itemClickListner itemClickListner, String user_id) {
        this.context = context;
        this.postsDataLists = postsDataLists;
        this.itemClickListner = itemClickListner;
        this.user_id = user_id;
    }

    @Override
    public int getCount() {
        return postsDataLists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            contentView = inflater.inflate(R.layout.item_post_new_driver, parent, false);
        }

        TextView tv_trip_type = contentView.findViewById(R.id.tv_trip_type);
        TextView tv_no_pass = contentView.findViewById(R.id.tv_no_pass);
        TextView tv_no_nights = contentView.findViewById(R.id.tv_no_nights);
        TextView tv_appl_bids = contentView.findViewById(R.id.tv_appl_bids);
        TextView tv_status = contentView.findViewById(R.id.tv_status);
        TextView tv_date = contentView.findViewById(R.id.tv_date);
        TextView tv_time = contentView.findViewById(R.id.tv_time);
        TextView night_list = contentView.findViewById(R.id.night_list);
        TextView tv_more_bids = contentView.findViewById(R.id.tv_more_bids);
        TextView tv_bid = contentView.findViewById(R.id.tv_bid);
        View v_status_line = contentView.findViewById(R.id.v_status_line);
        TextView tv_vehi_type = contentView.findViewById(R.id.tv_vehi_type);
        Button btn_send_bid = contentView.findViewById(R.id.btn_send_bid);
        EditText et_bid = contentView.findViewById(R.id.et_bid);
        LinearLayout ll_bid = contentView.findViewById(R.id.ll_bid);
        LinearLayout ll_bid_send = contentView.findViewById(R.id.ll_bid_send);

        if (postsDataLists.get(position).getNo_nights().isEmpty()){
            tv_no_nights.setText(context.getResources().getText(R.string.nights) + " 0" );
        }
        else {
            tv_no_nights.setText(context.getResources().getText(R.string.nights) + " " + postsDataLists.get(position).getNo_nights());
        }

        tv_trip_type.setText(postsDataLists.get(position).getTour_type());
        tv_no_pass.setText(context.getResources().getText(R.string.passengers) + " " + postsDataLists.get(position).getNo_passengers());
        tv_date.setText(context.getResources().getText(R.string.date_post) + " " + postsDataLists.get(position).getStart_date());
        tv_time.setText(context.getResources().getText(R.string.time_post) + " " + postsDataLists.get(position).getStart_time());
        tv_appl_bids.setText(context.getResources().getText(R.string.applied_bids) + " " + String.valueOf(postsDataLists.get(position).getDriverData().size()));
        tv_vehi_type.setText(context.getResources().getText(R.string.vehicle_type_post) + " " + String.valueOf(postsDataLists.get(position).getVehicle_type()));

        tv_status.setText(postsDataLists.get(position).isStatus());
        switch (postsDataLists.get(position).isStatus()){
            case "new":
                v_status_line.setBackgroundColor(context.getResources().getColor(R.color.status_new));
                tv_status.setBackground(context.getResources().getDrawable(R.drawable.status_new_back));
                break;
            case "accepted":
                v_status_line.setBackgroundColor(context.getResources().getColor(R.color.status_accept));
                tv_status.setBackground(context.getResources().getDrawable(R.drawable.status_accepted_back));
                break;
            case "completed":
                v_status_line.setBackgroundColor(context.getResources().getColor(R.color.status_complete));
                tv_status.setBackground(context.getResources().getDrawable(R.drawable.status_completed_back));
                break;
        }

        for (DriverData driverData : postsDataLists.get(position).getDriverData()){
            if (driverData.getDriver_id().equals(user_id)){
                ll_bid.setVisibility(View.VISIBLE);
                ll_bid_send.setVisibility(View.GONE);
                tv_bid.setText(context.getResources().getText(R.string.bid) + " " +
                        context.getResources().getText(R.string.rupee) + String.valueOf(driverData.getBit()));
                break;
            }
        }


        btn_send_bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!et_bid.getText().toString().isEmpty()) {
                    itemClickListner.onItemClick(position,
                            postsDataLists.get(position).getPosition(),
                            postsDataLists.get(position).getDriverData(),
                            et_bid.getText().toString().trim(),
                            postsDataLists.get(position).getStart_timestamp(),
                            postsDataLists.get(position).getCreated_user_id(),
                            postsDataLists.get(position).getCreated_user_fcm_token());
                }
            }
        });

        return contentView;
    }

    public interface itemClickListner{
        void onItemClick(int position, String item_position, ArrayList<DriverData> driverData, String bid, String start_timestamp, String created_user_id, String created_user_fcm_token); }
}
