package com.ahn.vehiclerentapp.adaptes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.models.posts.DriverData;
import com.ahn.vehiclerentapp.models.posts.PostsDataList;

import java.util.ArrayList;

public class AcceptedDriverPostAdapter extends BaseAdapter {

    Context context;
    ArrayList<PostsDataList> postsDataLists;
    ItemClickListenerAccList itemClickListener;
    ItemClickListenerCompleting itemClickListenerCompl;
    String phone ="";

    public AcceptedDriverPostAdapter(Context context, ArrayList<PostsDataList> postsDataLists,
                                     ItemClickListenerAccList itemClickListener, ItemClickListenerCompleting itemClickListenerCompl) {
        this.context = context;
        this.postsDataLists = postsDataLists;
        this.itemClickListener = itemClickListener;
        this.itemClickListenerCompl = itemClickListenerCompl;
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
        return 0;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            contentView = inflater.inflate(R.layout.item_post_accept_driver, parent, false);
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
        View v_status_line = contentView.findViewById(R.id.v_status_line);
        TextView tv_vehi_type = contentView.findViewById(R.id.tv_vehi_type);
        TextView tv_driver = contentView.findViewById(R.id.tv_driver);
        TextView tv_bid = contentView.findViewById(R.id.tv_bid);
        TextView tv_call = contentView.findViewById(R.id.tv_call);
        TextView tv_start = contentView.findViewById(R.id.tv_start);
        TextView tv_end = contentView.findViewById(R.id.tv_end);
        TextView tv_complete = contentView.findViewById(R.id.tv_complete);

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
        tv_start.setText(context.getResources().getText(R.string.start_location) + " " + String.valueOf(postsDataLists.get(position).getStart_location()));
        tv_end.setText(context.getResources().getText(R.string.end_location) + " " + String.valueOf(postsDataLists.get(position).getEnd_location()));

       /* for (DriverData driverData: postsDataLists.get(position).getDriverData()){
            if (driverData.getDriver_id().equals(postsDataLists.get(position).getApproved_driver())){

                tv_driver.setText(context.getResources().getText(R.string.driver_name) + driverData.getName());
                phone = driverData.getPhone_number();
            }
        }*/

        tv_driver.setText(context.getResources().getText(R.string.client_name) + postsDataLists.get(position).getCreated_user_name());
        phone = postsDataLists.get(position).getCreated_user_phone();

        tv_bid.setText(context.getResources().getText(R.string.bid) + " " + postsDataLists.get(position).getApproved_bid());


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


        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClickAcceptDriver(position, postsDataLists.get(position), phone);
            }
        });

        tv_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListenerCompl.onItemClickComplete(position, postsDataLists.get(position), postsDataLists.get(position).getEnd_timestamp());
            }
        });

        return contentView;
    }

    public interface ItemClickListenerAccList{
        void onItemClickAcceptDriver(int position, PostsDataList postsDataLists, String phone);
    }

    public interface ItemClickListenerCompleting{
        void onItemClickComplete(int position, PostsDataList postsDataLists, String end_time);
    }
}
