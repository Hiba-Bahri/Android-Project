package com.tekup.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import com.tekup.androidproject.databinding.ActivityDetailedBinding;

public class DetailedActivity extends AppCompatActivity {
    EditText numberofroomstext;
    //The binding associates the data to the visual Elements
    ActivityDetailedBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Retrieving the intent
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
            //We're filling the elements of the page with the values sent from the CRUD
            binding.detailAdType.setText(adType);
            binding.detailDesc.setText(desc);
            binding.detailEstateType.setText(estateType);
            binding.detailLocation.setText(location);
            binding.detailPrice.setText(String.valueOf((int) price));
            binding.detailSurfaceArea.setText(String.valueOf((int) surfaceArea));
            //Picasso is a library that converts the imageUrl into an image.
            //Here it's loading the image url into the binding. so that the image shows.
            Picasso.get().load(imageURL).into(binding.detailImage);

            //Field Handling
            //if the estate type is field, We hide the number of rooms
            if ("field".equalsIgnoreCase(estateType)) {
                binding.nbroomstext.setVisibility(View.GONE);  // Hide the TextView
                binding.detailNbRooms.setVisibility(View.GONE);  // Hide the TextView
                binding.detailNbRooms.setText("");
                binding.nbroomstext.setText("");// Clear the value
            } else {
                //else, We show the number of rooms
                binding.nbroomstext.setVisibility(View.VISIBLE);  // Hide the TextView
                // Set the value and show the TextView for other estate types
                binding.detailNbRooms.setText(String.valueOf(nbRooms));
                binding.detailNbRooms.setVisibility(View.VISIBLE);  // Show the TextView
            }
        }
    }
}