package com.example.finalprojectexpensemanager.LoginAndSaved;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentKt;

import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.example.finalprojectexpensemanager.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SavedProfileFragment extends Fragment {

        private SharedPreferences sharedPreferences;
        private TextInputLayout name_saved;
        private TextInputLayout budget_saved;
        private TextInputLayout income_saved;
        private MaterialToolbar addAppBar2;
        private Button saveButton;
        private Activity activity;


    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_firebase_login, container, false);
            activity = this.getActivity();
            name_saved = view.findViewById(R.id.name_saved);
            budget_saved = view.findViewById(R.id.budget_saved);
            income_saved =view.findViewById(R.id.income_saved);
            addAppBar2 = view.findViewById(R.id.addAppBar2);
            saveButton = view.findViewById(R.id.saveButton);
            return view;
        }
    public void setActivity(AppCompatActivity mainActivity) {
        this.activity = mainActivity;
    }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            sharedPreferences = activity.getSharedPreferences("login", 0);
            String tempName = sharedPreferences.getString("Name", "Shivam");
            String tempBudget = sharedPreferences.getString("Budget", "1000");
            String tempIncome = sharedPreferences.getString("Income", "1000");

            name_saved.getEditText().setText(tempName);
            budget_saved.getEditText().setText(tempBudget);
            income_saved.getEditText().setText(tempIncome);
            //        disabling the edit text till the user clicks the pencil
            name_saved.setEnabled(false);
            budget_saved.setEnabled(false);
            income_saved.setEnabled(false);

            addAppBar2.setOnMenuItemClickListener((Toolbar.OnMenuItemClickListener)(new Toolbar.OnMenuItemClickListener() {
                public final boolean onMenuItemClick(MenuItem menuItem) {
                    switch(menuItem.getItemId()) {
                        case R.id.edit_pencil:
                            name_saved.setEnabled(true);
                            budget_saved.setEnabled(true);
                            income_saved.setEnabled(true);
                            saveButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
                                public final void onClick(View it) {


                                    sharedPreferences.edit().putString("Name",name_saved.getEditText().getText().toString()).apply();
                                    sharedPreferences.edit().putString("Budget",budget_saved.getEditText().getText().toString()).apply();
                                    sharedPreferences.edit().putString("Income",income_saved.getEditText().getText().toString()).apply();
                                    Navigation.findNavController(activity,R.id.action_savedProfileFragment_to_fragment_main);
                                   // FragmentKt.findNavController(SavedProfileFragment.this).navigate(SavedProfileFragmentDirections.actionSavedProfileFragmentToFragmentMain());
                                }
                            }));
                            return true;
                        default:
                            return false;
                    }
                    
                    
                }
            }));
        }

    }

