package com.example.navbotdialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.navbotdialog.databinding.ActivityDetailedBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class DetailedActivity extends AppCompatActivity {

    ActivityDetailedBinding binding;
    BottomNavigationView deleteEditnav;
    private MyDBHelper dbHelper;
    ImageView cancelButton;
    private String mat = "";
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new MyDBHelper(getApplicationContext());

        // Initialize data from Intent
        Intent intent = getIntent();
        if (intent != null) {
            mat = intent.getStringExtra("Matricule");
            String job = intent.getStringExtra("Job");
            String firstname = intent.getStringExtra("Firstname");
            String lastname = intent.getStringExtra("Lastname");
            String numberPhone = intent.getStringExtra("Number phone");
            String email = intent.getStringExtra("Email");
            String dateOfBirth = intent.getStringExtra("Date of birth");
            String address = intent.getStringExtra("Address");
            String image = intent.getStringExtra("Image");

            binding.detailFirstname.setText(firstname);
            binding.detailLastname.setText(lastname);
            binding.addressDetailed.setText("Address: " + address);
            binding.emailDetailed.setText("Email: " + email);
            binding.NumberPhoneDetailed.setText("Phone: " + numberPhone);
            binding.dateOfBirthDetailed.setText("Date of birth: " + dateOfBirth);
            binding.matriculeDetailed.setText("Matricule: " + mat);
            binding.jobDetailed.setText("Job: " + job);
            if (image.equals(""))
            {
                binding.detailImage.setImageResource(R.drawable.person);
            }
            else
            {
                try {
                    Uri uri = Uri.parse(image);
                    binding.detailImage.setImageURI(uri);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    // Handle the case where the URI string is null
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle other exceptions, such as if the URI string is invalid
                }
            }

            ImageView mailIcon = findViewById(R.id.mailIcon);
            mailIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMailIconClick(v, email);
                }
            });

            ImageView phoneIcon = findViewById(R.id.phoneIcon);
            phoneIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPhoneIconClick(v, numberPhone);
                }
            });

            ImageView messageIcon = findViewById(R.id.messageIcon);
            messageIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMessageIconClick(v, numberPhone);
                }
            });
        }

        // Initialize deleteEditnav by finding the view in the layout XML
        deleteEditnav = findViewById(R.id.deleteEditnav);

        int s = dbHelper.getEmployeeId(mat);

        // Check if deleteEditnav is not null before performing operations on it
        if (deleteEditnav != null) {
            deleteEditnav.setOnItemSelectedListener(item -> {
                if (item.getItemId() == R.id.delete) {
                    // Show the delete confirmation dialog
                    showDeleteConfirmationDialog();
                } else if (item.getItemId() == R.id.edit) {
                    mat = intent.getStringExtra("Matricule");
                    goToMainActivity();
                }
                return true;
            });
        }

        cancelButton = findViewById(R.id.cancelButton_Detailed);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mat = "";
                goToMainActivity();
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                cancelButton.performClick();
            }
        });
    }

    private void goToMainActivity() {
        // Back is pressed... Starting the MainActivity
        Intent intent = new Intent(DetailedActivity.this, MainActivity.class);
        intent.putExtra("mat", mat);
        startActivity(intent);
        finish();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the message and title for the dialog
        builder.setMessage("Are you sure you want to delete?")
                .setTitle("Confirmation");

        // Add "Yes" button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Yes button, perform delete operation
                if (mat != null) {
                    String message = dbHelper.deleteData(mat);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if (message.equals("Employee deleted successfully")) {
                        cancelButton.performClick();
                    }
                }
                dialog.dismiss();
            }
        });

        // Add "No" button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog, do nothing or handle accordingly
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onMailIconClick(View view, String email ) {

        try {
            Intent emailIntent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + email));
            startActivity(emailIntent);
        } catch(Exception e) {
            Toast.makeText(this, "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onPhoneIconClick(View view, String numberPhone) {

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + numberPhone));

        if (callIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(callIntent);
        } else {

            Toast.makeText(this, "Aucune application d'appel n'est disponible", Toast.LENGTH_SHORT).show();
        }
    }

    public void onMessageIconClick(View view, String phoneNumber) {
        Intent messageIntent = new Intent(Intent.ACTION_VIEW);
        messageIntent.setData(Uri.parse("sms:" + phoneNumber));

        if (messageIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(messageIntent);
        } else {
            Toast.makeText(this, "No messaging app is available", Toast.LENGTH_SHORT).show();
            // Provide alternative handling if no messaging app is available
        }
    }
}
