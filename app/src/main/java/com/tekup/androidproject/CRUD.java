package com.tekup.androidproject;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tekup.androidproject.databinding.ActivityCrudBinding;
import com.tekup.androidproject.entities.Advert;

import java.util.ArrayList;

public class CRUD extends AppCompatActivity {

    //this is responsible for associating data with the visual elements of the list
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


        //Listener on the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing dataArrayList before adding new data
                dataArrayList.clear();

                //Iterate over the snapshot, A snapshot is an instance of the database at the time you read the database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Advert advert = snapshot.getValue(Advert.class);
                    advert.tempKey = snapshot.getKey();
                    dataArrayList.add(advert);
                }
                // Notify the adapter that the data has changed
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(getApplicationContext(),String.valueOf(databaseError),Toast.LENGTH_SHORT).show();
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
        //Create Button handler
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the second activity
                Intent intent = new Intent(CRUD.this, createAdvActivity.class);
                //bidirectional intent
                startActivityForResult(intent, 1);
            }
        });

        //When I long click on any item of the list view, an options Dialog shows up.
        binding.listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showOptionsDialog(position);
                return true;
            }
        });
    }

    //The Options Dialog handler
    private void showOptionsDialog(int position) {
        //We build an alert using the context
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options")
                //add 2 items in the dialog, one to delete the advert and the other to delete
                .setItems(new CharSequence[]{"Delete", "Update"}, new DialogInterface.OnClickListener() {
                    @Override
                    //Options Click Handler
                    public void onClick(DialogInterface dialog, int which) {
                        //Retrieve the position of the long clicked advert
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

    //Deletion Handler
    private void deleteAdvert(String tempKey, int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("adverts");
        //Remove the advert using the tempKey which is a unique value for each advert
        databaseReference.child(tempKey).removeValue();
        // Refresh the data after deletion
        listAdapter.notifyDataSetChanged();
    }

    //Update Handler
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
        // Start the activity for result - Bidirectional intent, because we're waiting for the updated advert
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //No need to type anything in here because the listView updates automatically.
        }
    }
}