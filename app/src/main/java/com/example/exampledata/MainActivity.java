package com.example.exampledata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText dateformat;
    int year;
    int month;
    int day;

    String[] item = {"Male", "Female"};

    String[] item2 = {"User", "Worker"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItem;

    //This part is for firestore database storage.
    EditText userName, userEmail, dateofBirth, userGender,userPhone, userType;
    MaterialButton RegisterButton;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //For Date of Birth
        dateformat = findViewById(R.id.eDateofBirth);
        Calendar calendar = Calendar.getInstance();
        dateformat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateformat.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                    }
                }, year,month,day);
                datePickerDialog.show();
            }
        });

        //For AutoComplreteTextView
        autoCompleteTextView = findViewById(R.id.selectGender);
        adapterItem = new ArrayAdapter<String>(this, R.layout.list_item, item);

        autoCompleteTextView.setAdapter(adapterItem);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        //For AutiCompleteText for user type
        autoCompleteTextView = findViewById(R.id.selectUser);
        adapterItem = new ArrayAdapter<String>(this, R.layout.list_users, item2);

        autoCompleteTextView.setAdapter(adapterItem);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Item:"+ item, Toast.LENGTH_LONG).show();
            }
        });

        //Firebase work starts from here.

        db = FirebaseFirestore.getInstance();
        userName = findViewById(R.id.eTextName);
        userEmail = findViewById(R.id.eTextEmail);
        dateofBirth = findViewById(R.id.eDateofBirth);
        userGender = findViewById(R.id.selectGender);
        userPhone = findViewById(R.id.ePhone);
        userType = findViewById(R.id.selectUser);
        RegisterButton = findViewById(R.id.submit_Button);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = userName.getText().toString();
                String Email = userEmail.getText().toString();
                String DateofBirth = dateofBirth.getText().toString();
                String Gender = userGender.getText().toString();
                String Mobile = userPhone.getText().toString();
                String UserType = userType.getText().toString();
                Map<String, Object> user_detail = new HashMap<>();
                user_detail.put("User_Name", Name);
                user_detail.put("User_Email", Email);
                user_detail.put("User_DateofBirth", DateofBirth);
                user_detail.put("User_Gender", Gender);
                user_detail.put("User_Mobile", Mobile);
                user_detail.put("User_Type", UserType);

                db.collection("user_detail")
                        .add(user_detail)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(MainActivity.this, "Sucessful",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


    }
}