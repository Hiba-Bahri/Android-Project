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

        // Firebase setup
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("adverts");

        initSearchWidgets();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing dataArrayList before adding new data
                dataArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Advert advert = snapshot.getValue(Advert.class);
                    dataArrayList.add(advert);
                }

                listAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors if any
            }
        });



        listAdapter = new ListAdapter(MainActivity.this, dataArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
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

                        // Check the selected filter
                        if (selectedFilter.equals("all") ||
                                ad.getLocation().toLowerCase().contains(selectedFilter) ||
                                ad.getEstateType().toLowerCase().contains(selectedFilter) ||
                                ad.getAdType().toLowerCase().contains(selectedFilter)) {
                            filteredAds.add(ad);
                        }
                    }
                }

                ListAdapter adapter = new ListAdapter(getApplicationContext(), filteredAds);
                binding.listview.setAdapter(adapter);
                binding.listview.setClickable(true);

                return false;
            }
        });
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
