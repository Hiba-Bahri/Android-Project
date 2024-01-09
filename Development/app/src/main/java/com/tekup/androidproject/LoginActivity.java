package com.tekup.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtPassword;

    //Firebase Auth is a firebase Service
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get an Instance of the firebase auth service
        mAuth = FirebaseAuth.getInstance();

        this.txtEmail = (EditText) findViewById(R.id.loginInput);
        this.txtPassword = (EditText) findViewById(R.id.passwordInput);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We retrieve the email and password of the input
                String email=txtEmail.getText().toString();
                String pwd=txtPassword.getText().toString();
                System.out.println("--------------------------------------------");
                System.out.println(email);
                System.out.println(pwd);
                signIn(email,pwd);

            }
        });
    }
        private void signIn(String email, String pwd) {


            // We're using the Email and password provider that Firebase Authentication Service provides
            mAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Sign in success,
                            if (task.isSuccessful()) {
                                //Update UI or navigate to the next activity
                                Intent intent = new Intent(LoginActivity.this, CRUD.class);
                                startActivity(intent);
                                finish();
                            } //If sign in fails
                            else {
                                //Display a message to the user.
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }