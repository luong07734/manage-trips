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
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmanagement.R;
import com.example.tripmanagement.model.Trip;

import java.util.ArrayList;

public class TripListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
        private RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            imgIcon = itemView.findViewById(R.id.icon_iv);
            txtTripName = itemView.findViewById(R.id.trip_name_tv);
            txtDestination = itemView.findViewById(R.id.destination_tv);
            txtDate = itemView.findViewById(R.id.date_tv);
            relativeLayout = itemView.findViewById(R.id.rowFG);
        }
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder{

        private ImageView imgIcon;
        private TextView txtTripName;
        private TextView txtDestination;
        private TextView txtDate;
        private RelativeLayout relativeLayout;
        public ViewHolder1(@NonNull View itemView){
            super(itemView);
            imgIcon = itemView.findViewById(R.id.icon_iv);
            txtTripName = itemView.findViewById(R.id.trip_name_tv);
            txtDestination = itemView.findViewById(R.id.destination_tv);
            txtDate = itemView.findViewById(R.id.date_tv);
            relativeLayout = itemView.findViewById(R.id.rowFG);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{

        private ImageView imgIcon;
        private TextView txtTripName;
        private TextView txtDestination;
        private TextView txtDate;
        private RelativeLayout relativeLayout;
        public ViewHolder2(@NonNull View itemView){
            super(itemView);
            imgIcon = itemView.findViewById(R.id.icon_iv);
            txtTripName = itemView.findViewById(R.id.trip_name_tv);
            txtDestination = itemView.findViewById(R.id.destination_tv);
            txtDate = itemView.findViewById(R.id.date_tv);
            relativeLayout = itemView.findViewById(R.id.rowFG);
        }
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder{

        private ImageView imgIcon;
        private TextView txtTripName;
        private TextView txtDestination;
        private TextView txtDate;
        private RelativeLayout relativeLayout;
        public ViewHolder3(@NonNull View itemView){
            super(itemView);
            imgIcon = itemView.findViewById(R.id.icon_iv);
            txtTripName = itemView.findViewById(R.id.trip_name_tv);
            txtDestination = itemView.findViewById(R.id.destination_tv);
            txtDate = itemView.findViewById(R.id.date_tv);
            relativeLayout = itemView.findViewById(R.id.rowFG);
        }
    }

    public class ViewHolder4 extends RecyclerView.ViewHolder{

        private ImageView imgIcon;
        private TextView txtTripName;
        private TextView txtDestination;
        private TextView txtDate;
        private RelativeLayout relativeLayout;
        public ViewHolder4(@NonNull View itemView){
            super(itemView);
            imgIcon = itemView.findViewById(R.id.icon_iv);
            txtTripName = itemView.findViewById(R.id.trip_name_tv);
            txtDestination = itemView.findViewById(R.id.destination_tv);
            txtDate = itemView.findViewById(R.id.date_tv);
            relativeLayout = itemView.findViewById(R.id.rowFG);
        }
    }

    public class ViewHolder5 extends RecyclerView.ViewHolder{

        private ImageView imgIcon;
        private TextView txtTripName;
        private TextView txtDestination;
        private TextView txtDate;
        private RelativeLayout relativeLayout;
        public ViewHolder5(@NonNull View itemView){
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

        switch (viewType) {
            case 0: return new ViewHolder(tripView);
            case 1: return new ViewHolder1(tripView);
            case 2: return new ViewHolder2(tripView);
            case 3: return new ViewHolder3(tripView);
            case 4: return new ViewHolder4(tripView);

            default:
                return new ViewHolder5(tripView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolder viewHolder = (ViewHolder)holder;

                viewHolder.txtTripName.setText(trip.getTripName());
                viewHolder.txtDestination.setText(trip.getDestination());
                viewHolder.txtDate.setText(trip.getDate());
                int color = ContextCompat.getColor(context, R.color.recycler_0);
                viewHolder.relativeLayout.setBackgroundColor(color);
                viewHolder.imgIcon.setImageResource(R.drawable.rv_icon_0);
                break;

            case 1:
                ViewHolder1 viewHolder1 = (ViewHolder1)holder;
                viewHolder1.txtTripName.setText(trip.getTripName());
                viewHolder1.txtDestination.setText(trip.getDestination());
                viewHolder1.txtDate.setText(trip.getDate());
                int color1 = ContextCompat.getColor(context, R.color.recycler_1);
                viewHolder1.relativeLayout.setBackgroundColor(color1);
                viewHolder1.imgIcon.setImageResource(R.drawable.rv_icon_1);
                break;

            case 2:
                ViewHolder2 viewHolder2 = (ViewHolder2)holder;
                viewHolder2.txtTripName.setText(trip.getTripName());
                viewHolder2.txtDestination.setText(trip.getDestination());
                viewHolder2.txtDate.setText(trip.getDate());
                int color2 = ContextCompat.getColor(context, R.color.recycler_2);
                viewHolder2.relativeLayout.setBackgroundColor(color2);
                viewHolder2.imgIcon.setImageResource(R.drawable.rv_icon_2);
                break;

            case 3:
                ViewHolder3 viewHolder3 = (ViewHolder3)holder;
                viewHolder3.txtTripName.setText(trip.getTripName());
                viewHolder3.txtDestination.setText(trip.getDestination());
                viewHolder3.txtDate.setText(trip.getDate());
                int color3 = ContextCompat.getColor(context, R.color.recycler_3);
                viewHolder3.relativeLayout.setBackgroundColor(color3);
                viewHolder3.imgIcon.setImageResource(R.drawable.rv_icon_3);
                break;

            case 4:
                ViewHolder4 viewHolder4 = (ViewHolder4)holder;
                viewHolder4.txtTripName.setText(trip.getTripName());
                viewHolder4.txtDestination.setText(trip.getDestination());
                viewHolder4.txtDate.setText(trip.getDate());
                int color4 = ContextCompat.getColor(context, R.color.recycler_4);
                viewHolder4.relativeLayout.setBackgroundColor(color4);
                viewHolder4.imgIcon.setImageResource(R.drawable.rv_icon_4);
                break;

            case 5:
                ViewHolder5 viewHolder5 = (ViewHolder5)holder;
                viewHolder5.txtTripName.setText(trip.getTripName());
                viewHolder5.txtDestination.setText(trip.getDestination());
                viewHolder5.txtDate.setText(trip.getDate());
                int color5 = ContextCompat.getColor(context, R.color.recycler_5);
                viewHolder5.relativeLayout.setBackgroundColor(color5);
                viewHolder5.imgIcon.setImageResource(R.drawable.rv_icon_5);
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
