package com.example.finalprojectexpensemanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        return;
    }

    @Override
    public void finish() {
        super.finish();
        MSPV3.getMe().putString("MPCounter", ""+ ExpenseRepository.counter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MSPV3.getMe().putString("MPCounter", ""+ ExpenseRepository.counter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MSPV3.getMe().putString("MPCounter", ""+ ExpenseRepository.counter);
    }
}