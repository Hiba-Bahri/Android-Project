package com.tekup.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.tekup.androidproject.databinding.ActivityDetailedBinding;
public class DetailedActivity extends AppCompatActivity {
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
            int image = intent.getIntExtra("image", R.drawable.maggi);
            binding.detailAdType.setText(adType);
            binding.detailDesc.setText(desc);
            binding.detailEstateType.setText(estateType);
            binding.detailLocation.setText(location);
            binding.detailPrice.setText(String.valueOf((int) price));
            binding.detailSurfaceArea.setText(String.valueOf((int) surfaceArea));
            binding.detailImage.setImageResource(image);
            binding.detailNbRooms.setText(String.valueOf(nbRooms));
        }
    }
}