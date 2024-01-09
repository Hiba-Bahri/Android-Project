package com.tekup.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tekup.androidproject.databinding.ActivityCrudBinding;
import com.tekup.androidproject.databinding.ActivityMainBinding;
import com.tekup.androidproject.entities.Advert;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //ActivityMainBinding instance
    ActivityMainBinding binding;
    //ListAdapter instance
    ListAdapter listAdapter;
    //List of Objects of type "advert"
    ArrayList<Advert> dataArrayList = new ArrayList<>();


    private String selectedFilter = "all";
    private String currentSearchText = "";
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflating the layout using ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // Setting the content view of the activity to the root view of the inflated layout
        setContentView(binding.getRoot());

        // Firebase setup
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("adverts");

        // Call of the method initSearchWidget
        initSearchWidgets();

        // Adding a ValueEventListener to the databaseReference
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing dataArrayList before adding new data
                dataArrayList.clear();
                // Retrieve Advert objects
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Advert advert = snapshot.getValue(Advert.class);
                    // Set a temporary key for the advert using the snapshot's key
                    advert.tempKey = snapshot.getKey();
                    // Add the advert to the dataArrayList
                    dataArrayList.add(advert);
                }
                // Notify the adapter that the data has changed
                listAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors if any
            }
        });

        // Send the filtered data to the ListAdapter
        SendInfos(dataArrayList);
    }

    private void SendInfos(ArrayList<Advert> data) {
        // Creating a new ListAdapter instance with the current activity context (CRUD.this) and a data ArrayList
        listAdapter = new ListAdapter(MainActivity.this, data);
        // Setting the adapter for the ListView in the layout using the created ListAdapter
        binding.listview.setAdapter(listAdapter);
        // Allowing the ListView to be clickable
        binding.listview.setClickable(true);

        // Setting an OnItemClickListener for the ListView
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Advert clickedAd = data.get(i);
                // Creating an Intent to navigate to the DetailedActivity
                Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
                intent.putExtra("description", clickedAd.getDescription());
                intent.putExtra("estateType", clickedAd.getEstateType());
                intent.putExtra("adType", clickedAd.getAdType());
                intent.putExtra("location", clickedAd.getLocation());
                intent.putExtra("image", clickedAd.getImageURL());
                intent.putExtra("nbRooms", clickedAd.getNbRooms());
                intent.putExtra("price", clickedAd.getPrice());
                intent.putExtra("surfaceArea", clickedAd.getSurfaceArea());
                // Starting the DetailedActivity with the prepared Intent
                startActivity(intent);
            }
        });
    }

    // Method to initialize search widget
    private void initSearchWidgets() {
        searchView = (SearchView) findViewById(R.id.shapeListSearchView);
        // Listener for the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // Handle text submission if needed
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // Update the current search text
                currentSearchText = s;
                // Filter the data based on the search text and selected filter
                ArrayList<Advert> filteredAds = new ArrayList<>();
                for (Advert ad : dataArrayList) {
                    if (ad.getLocation().toLowerCase().contains(s.toLowerCase()) ||
                            ad.getEstateType().toLowerCase().contains(s.toLowerCase()) ||
                            ad.getAdType().toLowerCase().contains(s.toLowerCase())) {
                        if (selectedFilter.equals("all") ||
                                ad.getLocation().toLowerCase().contains(selectedFilter) ||
                                ad.getEstateType().toLowerCase().contains(selectedFilter) ||
                                ad.getAdType().toLowerCase().contains(selectedFilter)) {
                            filteredAds.add(ad);
                        }
                    }
                }
                // Send the filtered data to the ListAdapter
                SendInfos(filteredAds);
                return false;
            }
        });
    }

    // Intent to navigate to LoginActivity when the user icon is clicked
    public void onIconButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}