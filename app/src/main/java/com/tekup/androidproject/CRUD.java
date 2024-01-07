package com.tekup.androidproject;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tekup.androidproject.databinding.ActivityCrudBinding;
import com.tekup.androidproject.entities.Advert;

import java.util.ArrayList;

public class CRUD extends AppCompatActivity {

    ActivityCrudBinding binding;
    ListAdapter listAdapter;
    ArrayList<Advert> dataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrudBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Firebase setup
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("adverts");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing dataArrayList before adding new data
                dataArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Advert advert = snapshot.getValue(Advert.class);
                    advert.tempKey = snapshot.getKey();
                    dataArrayList.add(advert);
                }

                listAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors if any
            }
        });

        // Initialize ListView and Adapter
        listAdapter = new ListAdapter(CRUD.this, dataArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CRUD.this, DetailedActivity.class);
                intent.putExtra("description", dataArrayList.get(i).getDescription());
                intent.putExtra("estateType", dataArrayList.get(i).getEstateType());
                intent.putExtra("adType", dataArrayList.get(i).getAdType());
                intent.putExtra("location", dataArrayList.get(i).getLocation());
                intent.putExtra("image", dataArrayList.get(i).getImageURL());
                intent.putExtra("nbRooms", dataArrayList.get(i).getNbRooms());
                intent.putExtra("price", dataArrayList.get(i).getPrice());
                intent.putExtra("surfaceArea", dataArrayList.get(i).getSurfaceArea());
                startActivity(intent);
            }
        });


        Button createButton = findViewById(R.id.addBtn);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the second activity
                Intent intent = new Intent(CRUD.this, createAdvActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        binding.listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showOptionsDialog(position);
                return true;
            }
        });
    }

    private void showOptionsDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options")
                .setItems(new CharSequence[]{"Delete", "Update"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Advert selectedAdvert = dataArrayList.get(position);
                        switch (which) {
                            case 0:
                                // Delete
                                deleteAdvert(selectedAdvert.getTempKey(), position);
                                break;
                            case 1:
                                // Update
                                updateAdvert(selectedAdvert.getTempKey(), position);
                                break;
                        }
                    }
                });

        builder.create().show();
    }
    private void deleteAdvert(String tempKey, int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("adverts");
        databaseReference.child(tempKey).removeValue();
        // Refresh the data after deletion
        listAdapter.notifyDataSetChanged();
    }

    private void updateAdvert(String tempKey, int position) {
        // Redirect to an activity to edit the fields
        Intent intent = new Intent(CRUD.this, UpdateAdvertActivity.class);
        // Pass necessary data to the UpdateAdvertActivity, e.g., advert key or details
        intent.putExtra("advertKey", tempKey);
        intent.putExtra("adType", dataArrayList.get(position).getAdType());
        intent.putExtra("estateType", dataArrayList.get(position).getEstateType());
        intent.putExtra("location", dataArrayList.get(position).getLocation());
        intent.putExtra("nbRooms", dataArrayList.get(position).getNbRooms());
        intent.putExtra("price", dataArrayList.get(position).getPrice());
        intent.putExtra("surfaceArea", dataArrayList.get(position).getSurfaceArea());
        intent.putExtra("description", dataArrayList.get(position).getDescription());
        intent.putExtra("imageURL", dataArrayList.get(position).getImageURL());
        // Start the activity for result if needed
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Handle the result from the second activity
            // For example, you can update UI or perform some action
        }
    }
}


/*
package com.tekup.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.tekup.androidproject.databinding.ActivityCrudBinding;

import java.util.ArrayList;

public class CRUD extends AppCompatActivity {

    ActivityCrudBinding binding;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrudBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] imageList = {R.drawable.pasta, R.drawable.maggi, R.drawable.cake, R.drawable.pancake, R.drawable.pizza, R.drawable.burger, R.drawable.fries};
        int[] ingredientList = {R.string.pastaIngredients, R.string.maggiIngredients,R.string.cakeIngredients,R.string.pancakeIngredients,R.string.pizzaIngredients, R.string.burgerIngredients, R.string.friesIngredients};
        int[] descList = {R.string.pastaDesc, R.string.maggieDesc, R.string.cakeDesc,R.string.pancakeDesc,R.string.pizzaDesc, R.string.burgerDesc, R.string.friesDesc};
        String[] nameList = {"Pasta", "Maggi", "Cake", "Pancake", "Pizza","Burgers", "Fries"};
        String[] timeList = {"30 mins", "2 mins", "45 mins","10 mins", "60 mins", "45 mins", "30 mins"};

        for (int i = 0; i < imageList.length; i++){
            listData = new ListData(nameList[i], timeList[i], ingredientList[i], descList[i], imageList[i]);
            dataArrayList.add(listData);
        }
        listAdapter = new ListAdapter(CRUD.this, dataArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CRUD.this, DetailedActivity.class);
                intent.putExtra("name", nameList[i]);
                intent.putExtra("time", timeList[i]);
                intent.putExtra("ingredients", ingredientList[i]);
                intent.putExtra("desc", descList[i]);
                intent.putExtra("image", imageList[i]);
                startActivity(intent);
            }
        });
    }
}*/
