package com.example.finalprojectexpensemanager.LoginAndSaved;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.android.material.textfield.TextInputLayout;
import com.example.finalprojectexpensemanager.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginFragment extends Fragment implements AdapterView.OnItemSelectedListener {
        private TextInputLayout PersonName;
        private TextInputLayout PersonBudget;
        private TextInputLayout PersonIncome;
        private Button continueButton;
        private Activity activity;
        private View view;
        private Spinner spinner;
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
                    PersonName.setError("This Field Cannot Be Empty");
                }

                if (personBudget.isEmpty()) {
                    continueButton.setEnabled(false);
                    PersonBudget.setError("This Field Cannot Be Empty");

                }
                if(!personBudget.isEmpty()) {
                    try {
                        if (Integer.parseInt(personBudget) < 0) {
                            continueButton.setEnabled(false);
                            PersonBudget.setError("Budget Can't Be Negative");
                        }
                        else {
                            continueButton.setEnabled(true);
                            PersonBudget.setError("");
                            PersonName.setError("");
                        }
                    }
                    catch (NumberFormatException e){
                        continueButton.setEnabled(false);
                        PersonBudget.setError("Budget Need To Be A Integer");
                    }
                }

            }
        };

    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
            activity = this.getActivity();
            activity.setTitle("Personal Details");
            view = inflater.inflate(R.layout.fragment_login, container, false);
            initView(view);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(container.getContext(),
                    R.array.coins_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
            return view;
        }

    private void initView(View view) {
        PersonName = view.findViewById(R.id.PersonName);
        PersonBudget = view.findViewById(R.id.PersonBudget);
        PersonIncome= view.findViewById(R.id.PersonIncome);
        continueButton = view.findViewById(R.id.continueButton);
        spinner =  (Spinner) view.findViewById(R.id.spinner);

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
            DatabaseReference myRef = database.getReference(getString(R.string.EXPENSE_TABLE_APP));
            myRef.child( ExpenseRepository.userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.NAMEDB)).setValue(name);
            myRef.child( ExpenseRepository.userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.BUDGETDB)).setValue(""+budget);
            myRef.child( ExpenseRepository.userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.RESET)).setValue(""+budget);
            myRef.child( ExpenseRepository.userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.INCOMEDB)).setValue(""+income);
            ExpenseRepository.reset = Integer.parseInt(budget);
        }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String coin = parent.getItemAtPosition(position).toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(getString(R.string.EXPENSE_TABLE_APP));
        switch (coin){
            case "NIS":
                myRef.child( ExpenseRepository.userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.COIN)).setValue("₪");
                ExpenseRepository.coin = "₪";
                break;
            case "Dollar":
                myRef.child( ExpenseRepository.userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.COIN)).setValue("$");
                ExpenseRepository.coin = "$";
                break;
            case "Euro":
                myRef.child( ExpenseRepository.userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.COIN)).setValue("€");
                ExpenseRepository.coin = "€";
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
