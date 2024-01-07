package com.tekup.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tekup.androidproject.databinding.ActivityDetailedBinding;
import com.squareup.picasso.Picasso;
public class DetailedActivity extends AppCompatActivity {
    EditText numberofroomstext;
    ActivityDetailedBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = this.getIntent();
        if (intent != null){
            String desc = intent.getStringExtra("description");
            String estateType = intent.getStringExtra("estateType");
            String adType = intent.getStringExtra("adType");
            String location = intent.getStringExtra("location");
            int nbRooms = intent.getIntExtra("nbRooms",0);
            float price = intent.getFloatExtra("price", 0);
            float surfaceArea = intent.getFloatExtra("surfaceArea", 0);
            String imageURL = intent.getStringExtra("image");
            binding.detailAdType.setText(adType);
            binding.detailDesc.setText(desc);
            binding.detailEstateType.setText(estateType);
            binding.detailLocation.setText(location);
            binding.detailPrice.setText(String.valueOf((int) price));
            binding.detailSurfaceArea.setText(String.valueOf((int) surfaceArea));
            Picasso.get().load(imageURL).into(binding.detailImage);
            if ("field".equalsIgnoreCase(estateType)) {
                binding.nbroomstext.setVisibility(View.GONE);  // Hide the TextView
                binding.detailNbRooms.setVisibility(View.GONE);  // Hide the TextView
                binding.detailNbRooms.setText("");
                binding.nbroomstext.setText("");// Clear the value
            } else {
                binding.nbroomstext.setVisibility(View.VISIBLE);  // Hide the TextView
                // Set the value and show the TextView for other estate types
                binding.detailNbRooms.setText(String.valueOf(nbRooms));
                binding.detailNbRooms.setVisibility(View.VISIBLE);  // Show the TextView
            }
/*
            binding.detailNbRooms.setText(String.valueOf(nbRooms));
*/
        }
    }
}