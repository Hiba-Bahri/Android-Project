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
import com.tekup.androidproject.databinding.ActivityMainBinding;
import com.tekup.androidproject.entities.Advert;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ListAdapter listAdapter;
    ArrayList<Advert> dataArrayList = new ArrayList<>();


    private String selectedFilter = "all";
    private String currentSearchText = "";
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("adverts");

        initSearchWidgets();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Advert advert = snapshot.getValue(Advert.class);
                    dataArrayList.add(advert);
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        SendInfos(dataArrayList);

    }

    private void SendInfos(ArrayList<Advert> data) {
        listAdapter = new ListAdapter(MainActivity.this, data);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Advert clickedAd = data.get(i);
                Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
                intent.putExtra("description", clickedAd.getDescription());
                intent.putExtra("estateType", clickedAd.getEstateType());
                intent.putExtra("adType", clickedAd.getAdType());
                intent.putExtra("location", clickedAd.getLocation());
                intent.putExtra("image", clickedAd.getImageURL());
                intent.putExtra("nbRooms", clickedAd.getNbRooms());
                intent.putExtra("price", clickedAd.getPrice());
                intent.putExtra("surfaceArea", clickedAd.getSurfaceArea());
                startActivity(intent);
            }
        });
    }


    private void initSearchWidgets() {
        searchView = (SearchView) findViewById(R.id.shapeListSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                currentSearchText = s;
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
                SendInfos(filteredAds);
                return false;
            }
        });
    }

    public void onIconButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }


}

