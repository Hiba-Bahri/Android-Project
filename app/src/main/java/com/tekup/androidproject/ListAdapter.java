package com.tekup.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.tekup.androidproject.entities.Advert;

public class ListAdapter extends ArrayAdapter<Advert>{
    public ListAdapter(@NonNull Context context, ArrayList<Advert> dataArrayList){
        super(context, R.layout.list_item,dataArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent){
        Advert advert = getItem(position);
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listLocation = view.findViewById(R.id.listLocation);
        TextView listPrice = view.findViewById(R.id.listPrice);
        if(advert.getImageURL()!=null) {
            Picasso.get().load(advert.getImageURL()).into(listImage);
        }
        listLocation.setText(advert.getLocation());
        listPrice.setText(String.valueOf(advert.getPrice()));


        return view;
    }
}

/*public class ListAdapter extends ArrayAdapter<ListData> {
    public ListAdapter(@NonNull Context context, ArrayList<ListData> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ListData listData = getItem(position);
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listName = view.findViewById(R.id.listName);
        TextView listTime = view.findViewById(R.id.listTime);
        listImage.setImageResource(listData.image);
        listName.setText(listData.name);
        listTime.setText(listData.time);
        return view;
    }
}*/
