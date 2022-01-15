package com.example.finalprojectexpensemanager.LoginAndSaved;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.android.material.textfield.TextInputLayout;
import com.example.finalprojectexpensemanager.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginFragment extends Fragment{
    public static final String NAMEDB = "Name";
    public static final String BUDGETDB = "Budget";
    public static final String INCOMEDB = "Income";
        private SharedPreferences sharedPreferences;
        private TextInputLayout PersonName;
        private TextInputLayout PersonBudget;
        private TextInputLayout PersonIncome;
        private Button continueButton;
        Activity activity;
        private View view;
        private TextWatcher boardingTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String personName = PersonName.getEditText().getText().toString();
                String personBudget = PersonBudget.getEditText().getText().toString();

                if (personName.isEmpty()) {
                    continueButton.setEnabled(false);
                    PersonName.setError("This field cannot be empty");
                }

                if (personBudget.isEmpty()) {
                    continueButton.setEnabled(false);
                    PersonBudget.setError("This field cannot be empty");
                } else {
                    continueButton.setEnabled(true);
                    PersonBudget.setError("");
                    PersonName.setError("");
                }
            }
        };

    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
            Log.i("pttt","here");
            activity = this.getActivity();
            view = inflater.inflate(R.layout.fragment_login, container, false);
            this.sharedPreferences = activity.getSharedPreferences("login", Context.MODE_PRIVATE);
            PersonName = view.findViewById(R.id.PersonName);
            PersonBudget = view.findViewById(R.id.PersonBudget);
            PersonIncome= view.findViewById(R.id.PersonIncome);
            continueButton = view.findViewById(R.id.continueButton);
            boolean boarded = sharedPreferences.getBoolean("isLogin", false);

            return view;
        }

        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            PersonName.getEditText().addTextChangedListener(boardingTextWatcher);
            PersonBudget.getEditText().addTextChangedListener(boardingTextWatcher);
            PersonIncome.getEditText().addTextChangedListener(boardingTextWatcher);

            continueButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
                public final void onClick(View it) {
                    String name = PersonName.getEditText().getText().toString();
                    String budget = PersonBudget.getEditText().getText().toString();
                    String income = PersonIncome.getEditText().getText().toString();
                    saveCredentials(name,budget,income);
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_fragment_main);
                }
            }));
        }

        private void saveCredentials(String name, String budget, String income) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(ExpenseRepository.EXPENSE_TABLE_APP);
            myRef.child( ExpenseRepository.userName).child(ExpenseRepository.EXPENSE_TABLE).child(NAMEDB).setValue(name);
            myRef.child( ExpenseRepository.userName).child(ExpenseRepository.EXPENSE_TABLE).child(BUDGETDB).setValue(""+budget);
            myRef.child( ExpenseRepository.userName).child(ExpenseRepository.EXPENSE_TABLE).child(INCOMEDB).setValue(""+income);
            sharedPreferences.edit().putBoolean("isLogin", true).apply();
            sharedPreferences.edit().putString("Name", name).apply();
            sharedPreferences.edit().putString("Budget", budget).apply();
            sharedPreferences.edit().putString("Income", income).apply();
        }
}
