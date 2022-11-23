package com.example.finalprojectexpensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;

import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class MainActivity extends AppCompatActivity {
//public static  String path;
public static File path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //path = Environment.getExternalStorageDirectory() +"/Expenses.xls";
        path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Expenses.xlsx");
       // path = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS,"Expenses.xls") ;
    /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            path = this.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/Expenses.xlsx";

        }
        else
        {
            path = Environment.getExternalStorageDirectory()
                    .toString() +"/Expenses.xls";

        }
        */

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