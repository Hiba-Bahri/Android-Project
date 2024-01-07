package com.tekup.androidproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tekup.androidproject.entities.Advert;

public class createAdvActivity extends AppCompatActivity {

    private EditText nbRoomsInput, priceInput, editTextNumber3, descInput;
    private Spinner adTypeSpinner, estateTypeSpinner, locationSpinner;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageView;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_adv);

        nbRoomsInput = findViewById(R.id.nbRoomsInput);
        priceInput = findViewById(R.id.priceInput);
        editTextNumber3 = findViewById(R.id.editTextNumber3);
        descInput = findViewById(R.id.descInput);

        adTypeSpinner = findViewById(R.id.adType);
        estateTypeSpinner = findViewById(R.id.estateType);
        locationSpinner = findViewById(R.id.location);

        imageView = findViewById(R.id.imageView);

        Button uploadImageButton = findViewById(R.id.uploadImageButton);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        Button createBtn = findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected values from Spinners and the text from EditTexts
                String adType = adTypeSpinner.getSelectedItem().toString();
                String estateType = estateTypeSpinner.getSelectedItem().toString();
                String location = locationSpinner.getSelectedItem().toString();
                String nbRooms = nbRoomsInput.getText().toString();
                String price = priceInput.getText().toString();
                String surfaceArea = editTextNumber3.getText().toString();
                String description = descInput.getText().toString();

                float surfaceAreaValue = Float.parseFloat(surfaceArea);
                float priceValue = Float.parseFloat(price);
                int nbRoomsValue = Integer.parseInt(nbRooms);
                // Create an Advert object with the collected data
                Advert advert = new Advert(null, description, adType, estateType, surfaceAreaValue, nbRoomsValue, location, priceValue, null);
                // Save the Advert object to the Firebase Realtime Database
                saveAdvertToDatabase(advert);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void saveAdvertToDatabase(Advert advert) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("adverts");
        // Generate a unique key for the new advert
        String key = databaseReference.push().getKey();

        // Save the advert to the database using the generated key
        databaseReference.child(key).setValue(advert);

        // Optionally, you can also finish the activity or perform any other actions
        // For example, you might want to go back to the previous activity
        finish();
    }
}