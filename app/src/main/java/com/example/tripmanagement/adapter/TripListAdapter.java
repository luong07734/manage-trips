package com.example.tripmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmanagement.R;
import com.example.tripmanagement.model.Trip;

import java.util.ArrayList;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Trip> tripList;


    public TripListAdapter(Context _context, ArrayList<Trip> _tripList){
        this.tripList = _tripList;
        this.context = _context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgIcon;
        private TextView txtTripName;
        private TextView txtDestination;
        private TextView txtDate;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            imgIcon = itemView.findViewById(R.id.icon_iv);
            txtTripName = itemView.findViewById(R.id.trip_name_tv);
            txtDestination = itemView.findViewById(R.id.destination_tv);
            txtDate = itemView.findViewById(R.id.date_tv);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tripView = inflater.inflate(R.layout.row_trip, parent, false);
        return new ViewHolder(tripView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = tripList.get(position);

        holder.txtTripName.setText(trip.getTripName());
        holder.txtDestination.setText(trip.getDestination());
        holder.txtDate.setText(trip.getDate());
    }



    @Override
    public int getItemCount() {
        return tripList.size();
    }
}
