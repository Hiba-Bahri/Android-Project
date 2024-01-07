package com.tekup.androidproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.tekup.androidproject.entities.Advert;

public class UpdateAdvertActivity extends AppCompatActivity {

    private Spinner updateAdType, updateEstateType, updateLocation;
    private EditText updateNbRoomsInput, updatePriceInput, updateSurfaceAreaInput, updateDescInput;
    private ImageView updateImageView;
    private Button uploadImageButton, updateBtn;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    private String advertKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_advert);

        updateAdType = findViewById(R.id.updateAdType);
        updateEstateType = findViewById(R.id.updateEstateType);
        updateLocation = findViewById(R.id.updateLocation);
        updateNbRoomsInput = findViewById(R.id.updateNbRoomsInput);
        updatePriceInput = findViewById(R.id.updatePriceInput);
        updateSurfaceAreaInput = findViewById(R.id.updateSurfaceAreaInput);
        updateDescInput = findViewById(R.id.updateDescInput);
        updateImageView = findViewById(R.id.updateImageView);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        updateBtn = findViewById(R.id.updateBtn);

        Intent intent = getIntent();
        advertKey = intent.getStringExtra("advertKey");
        String adType = intent.getStringExtra("adType");
        String estateType = intent.getStringExtra("estateType");
        String location = intent.getStringExtra("location");
        int nbRooms = intent.getIntExtra("nbRooms", 0);
        float price = intent.getFloatExtra("price", 0.0f);
        float surfaceArea = intent.getFloatExtra("surfaceArea", 0.0f);
        String description = intent.getStringExtra("description");
        String imageURL = intent.getStringExtra("imageURL");

        updateAdType.setSelection(getIndex(updateAdType, adType));
        updateEstateType.setSelection(getIndex(updateEstateType, estateType));
        updateLocation.setSelection(getIndex(updateLocation, location));
        updateNbRoomsInput.setText(String.valueOf(nbRooms));
        updatePriceInput.setText(String.valueOf(price));
        updateSurfaceAreaInput.setText(String.valueOf(surfaceArea));
        updateDescInput.setText(description);

        // Load and display the image using Picasso or another image-loading library
        Picasso.get().load(imageURL).into(updateImageView);

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAdvert();
            }
        });

    }
    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0; // Default to the first item if not found
    }
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            updateImageView.setImageURI(imageUri);
        }
    }
    private void updateAdvert() {
        String adType = updateAdType.getSelectedItem().toString();
        String estateType = updateEstateType.getSelectedItem().toString();
        String location = updateLocation.getSelectedItem().toString();
        String nbRooms = updateNbRoomsInput.getText().toString();
        String price = updatePriceInput.getText().toString();
        String surfaceArea = updateSurfaceAreaInput.getText().toString();
        String description = updateDescInput.getText().toString();

        // Perform validations on the input data if needed

        float surfaceAreaValue = Float.parseFloat(surfaceArea);
        float priceValue = Float.parseFloat(price);
        int nbRoomsValue = Integer.parseInt(nbRooms);

        if (imageUri != null) {
            uploadImage(advertKey, imageUri);
        }
        // Create an updated Advert object
        Advert updatedAdvert = new Advert(null, description, adType, estateType, surfaceAreaValue, nbRoomsValue, location, priceValue, null,advertKey);

        // Update the advert in the Firebase Realtime Database using the existing key
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("adverts");
        databaseReference.child(advertKey).setValue(updatedAdvert);

        // Optionally, you can finish the activity or perform any other actions
        // For example, you might want to go back to the previous activity
        finish();
    }

    private void uploadImage(String advertKey, Uri imageUri) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("ad_images").child(advertKey);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Get the download URL of the uploaded image
                        // You can save this URL in the Realtime Database along with other advert details
                        String imageUrl = uri.toString();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("adverts");
                        databaseReference.child(advertKey).child("imageURL").setValue(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful image upload
                });
    }
}