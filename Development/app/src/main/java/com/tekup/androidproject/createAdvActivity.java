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

    private EditText nbRoomsInput, priceInput, surfaceAreaInput, descInput;
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
        surfaceAreaInput = findViewById(R.id.surfaceAreaInput);
        descInput = findViewById(R.id.descInput);

        adTypeSpinner = findViewById(R.id.adType);
        estateTypeSpinner = findViewById(R.id.estateType);

        locationSpinner = findViewById(R.id.location);

        estateTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Check the selected estate type
                String selectedEstateType = estateTypeSpinner.getSelectedItem().toString();

                // If "Field" is selected, disable nbRoomsInput and set its value to null
                if ("Field".equals(selectedEstateType)) {
                    nbRoomsInput.setEnabled(false);
                    nbRoomsInput.setText(null);
                } else {
                    // If any other estate type is selected, enable nbRoomsInput
                    nbRoomsInput.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if nothing is selected
            }
        });

        imageView = findViewById(R.id.imageView);

        //Image Upload handling

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
                Integer nbRoomsValue = 0;
                String price = priceInput.getText().toString();
                String surfaceArea = surfaceAreaInput.getText().toString();
                String description = descInput.getText().toString();

                float surfaceAreaValue = Float.parseFloat(surfaceArea);
                float priceValue = Float.parseFloat(price);
                if ("".equals(nbRooms)) {
                    nbRoomsValue = null;
                } else {
                    nbRoomsValue = Integer.parseInt(nbRooms);
                }
                // Create an Advert object with the collected data
                Advert advert = new Advert(null, description, adType, estateType, surfaceAreaValue, nbRoomsValue, location, priceValue, null,null);
                // Save the Advert object to the Firebase Realtime Database
                saveAdvertToDatabase(advert);
            }
        });
    }

    //Intent for file choice with startActivityForResult
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when an image is selected from the fileChoser, the image will be set as a preview in the UI
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void saveAdvertToDatabase(Advert advert) {
        //We get an instance of the adverts from the database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("adverts");
        //We generate a unique key for the new advert
        String key = databaseReference.push().getKey();

        //If the admin added an image to the creation form
        if (imageUri != null) {
            //StorageReference uses the Storage service provided by Firebase, in Which we created a folder called ad_images to upload images to
            storageReference = FirebaseStorage.getInstance().getReference("ad_images").child(key + "." + getFileExtension(imageUri));

            //upload the picture to the storage
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        //if the upload is success
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //retrieve the image Url
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    //set the image Url in the advert object and save the advert object in the database
                                    advert.setImageURL(downloadUri.toString());
                                    databaseReference.child(key).setValue(advert);
                                    String imageUrl = advert.getImageURL();

                                    finish();
                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        //If the image didn't upload
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(createAdvActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {

            // Save the advert to the database using the generated key
            databaseReference.child(key).setValue(advert);

            String imageUrl= advert.getImageURL();

            finish();
        }
    }
    //This method is responsible for retrieving the file extension that we chose in the fileChoserIntent.
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}