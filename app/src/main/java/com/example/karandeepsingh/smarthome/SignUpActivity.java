package com.example.karandeepsingh.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword,inputFullName,inputSecurityKey;
    private Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private CircleImageView imageViewUser;
    private final int PICK_IMAGE_REQUEST = 77;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    String imageUri,name,email;
    boolean complete=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        auth = FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("Users");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignUp = findViewById(R.id.sign_up_button);
        inputEmail = findViewById(R.id.editTextEmail);
        inputPassword = findViewById(R.id.editTextPassword);
        inputFullName=findViewById(R.id.editTextName);
        progressBar =  findViewById(R.id.progressBar);
        imageViewUser=findViewById(R.id.imageViewUser);
        inputSecurityKey=findViewById(R.id.editTextSecurityKey);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = inputFullName.getText().toString().trim();
                email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    inputFullName.setError("Enter Full Name!");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Enter Email Address!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Enter Password!");
                    return;
                }

                if (password.length() < 6) {
                    inputPassword.setError("Password too short, enter minimum 6 characters!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    uploadImage();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageViewUser.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUri= String.valueOf(uri);
                                    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    UserPC user=new UserPC();
                                    user.setName(name);
                                    user.setImageUrl(imageUri);
                                    reference.child(uid).setValue(user);

                                    int securityKey= 111111;
                                    if(complete && (Integer.parseInt(inputSecurityKey.getText().toString())==securityKey)) {
                                        Toast.makeText(SignUpActivity.this, "SignedUp", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }
                            });
                            progressDialog.dismiss();
                            complete=true;
                            Toast.makeText(SignUpActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}
