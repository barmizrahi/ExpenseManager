package com.example.finalprojectexpensemanager;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalprojectexpensemanager.ViewModel.CategoryViewModel;

public class GetTotalAmount extends AppCompatActivity {
    CategoryViewModel categoryViewModel;
    float foodTotal=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new ViewModelProvider(GetTotalAmount.this).get(CategoryViewModel.class);

    }




}
