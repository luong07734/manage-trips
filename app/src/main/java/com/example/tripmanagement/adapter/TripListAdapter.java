package com.example.tripmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmanagement.R;
import com.example.tripmanagement.model.Trip;

import java.util.ArrayList;

public class TripListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private ArrayList<Trip> tripList;

    public TripListAdapter(Context _context, ArrayList<Trip> _tripList){
        this.tripList = _tripList;
        this.context = _context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imgIcon;
        private final TextView txtTripName;
        private final TextView txtDestination;
        private final TextView txtDate;
        private final RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            imgIcon = itemView.findViewById(R.id.icon_iv);
            txtTripName = itemView.findViewById(R.id.trip_name_tv);
            txtDestination = itemView.findViewById(R.id.destination_tv);
            txtDate = itemView.findViewById(R.id.date_tv);
            relativeLayout = itemView.findViewById(R.id.rowFG);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tripView = inflater.inflate(R.layout.row_trip, parent, false);

        return new ViewHolder(tripView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.txtTripName.setText(trip.getTripName());
        viewHolder.txtDestination.setText(trip.getDestination());
        viewHolder.txtDate.setText(trip.getDate());
        switch (holder.getItemViewType()) {
            case 0:
                int color = ContextCompat.getColor(context, R.color.recycler_0);
                viewHolder.relativeLayout.setBackgroundColor(color);
                viewHolder.imgIcon.setImageResource(R.drawable.rv_icon_0);
                break;

            case 1:
                int color1 = ContextCompat.getColor(context, R.color.recycler_1);
                viewHolder.relativeLayout.setBackgroundColor(color1);
                viewHolder.imgIcon.setImageResource(R.drawable.rv_icon_1);
                break;

            case 2:
                int color2 = ContextCompat.getColor(context, R.color.recycler_2);
                viewHolder.relativeLayout.setBackgroundColor(color2);
                viewHolder.imgIcon.setImageResource(R.drawable.rv_icon_2);
                break;

            case 3:
                int color3 = ContextCompat.getColor(context, R.color.recycler_3);
                viewHolder.relativeLayout.setBackgroundColor(color3);
                viewHolder.imgIcon.setImageResource(R.drawable.rv_icon_3);
                break;

            case 4:
                int color4 = ContextCompat.getColor(context, R.color.recycler_4);
                viewHolder.relativeLayout.setBackgroundColor(color4);
                viewHolder.imgIcon.setImageResource(R.drawable.rv_icon_4);
                break;

            case 5:
                int color5 = ContextCompat.getColor(context, R.color.recycler_5);
                viewHolder.relativeLayout.setBackgroundColor(color5);
                viewHolder.imgIcon.setImageResource(R.drawable.rv_icon_5);
                break;
        }


    }

    @Override
    public int getItemViewType(int position){
        return tripList.get(position).getTripId() % 6;
    }


    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public void setFilteredList(ArrayList<Trip> filteredList){
        this.tripList = filteredList;
        notifyDataSetChanged();
    }
}
