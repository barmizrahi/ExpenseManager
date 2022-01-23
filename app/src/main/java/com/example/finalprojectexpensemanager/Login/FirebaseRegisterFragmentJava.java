package com.example.finalprojectexpensemanager.Login;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.finalprojectexpensemanager.MSPV3;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.finalprojectexpensemanager.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseRegisterFragmentJava extends Fragment {
    private static FirebaseAuth auth;
    private Button AlreadyRegistered;
    private Button register;
    private TextInputLayout firebaseEmailRegister;
    private TextInputLayout firebasePasswordRegister;
    private ProgressBar progressBar2;
    private View view;
    private Activity activity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.auth = FirebaseAuth.getInstance();
    }

    public void onStart() {
        super.onStart();

    }

    private final void UpdateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Navigation.findNavController(view).navigate(R.id.action_firebaseRegisterFragment_to_loginFragment);
        }

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_firebase_register, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        AlreadyRegistered = view.findViewById(R.id.AlreadyRegistered);
        register = view.findViewById(R.id.register);
        activity = getActivity();
        activity.setTitle("Register");
        firebaseEmailRegister = view.findViewById(R.id.firebaseEmailRegister);
        firebasePasswordRegister = view.findViewById(R.id.firebasePasswordRegister);
        progressBar2 = view.findViewById(R.id.progressBar2);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AlreadyRegistered.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                Navigation.findNavController(view).navigate(R.id.action_firebaseRegisterFragment_to_firebaseLoginJavaFragment);
            }
        }));
        register.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                firebaseEmailRegister.setError((CharSequence) null);
                firebasePasswordRegister.setError((CharSequence) null);

                String email = firebaseEmailRegister.getEditText().getText().toString();
                String pass = firebasePasswordRegister.getEditText().getText().toString();
                if (validateInput(email, pass)) {
                    progressBar2.setVisibility(View.VISIBLE);
                    FirebaseRegisterFragmentJava.getAuth().createUserWithEmailAndPassword(email, pass).addOnCompleteListener((OnCompleteListener) (new OnCompleteListener() {
                        public final void onComplete(Task task) {
                            progressBar2.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                String[] mailToDataBase = email.split("@");
                                ExpenseRepository.userName = mailToDataBase[0];
                                MSPV3.getMe().putString(getString(R.string.UserName), ExpenseRepository.userName);
                                MSPV3.getMe().putString(getString(R.string.LogInBolean), "true");
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference(getString(R.string.EXPENSE_TABLE_APP));
                                myRef.child(mailToDataBase[0]).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.EXPENSES_COUNTER)).setValue("" + 0);

                                Navigation.findNavController(view).navigate(R.id.action_firebaseRegisterFragment_to_loginFragment);
                            } else {
                                Context var3 = (Context) FirebaseRegisterFragmentJava.this.requireActivity();
                                StringBuilder var10001 = (new StringBuilder()).append("Authentication failed: ");
                                Exception var10002 = task.getException();
                                Toast toast = Toast.makeText(var3, (CharSequence) var10001.append(var10002 != null ? var10002.getMessage() : null).toString(), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toast.show();
                            }

                        }
                    }));
                }
            }
        }));
    }


    private final boolean validateInput(String email, String pass) {
        boolean valid = true;
        if (email.isEmpty()) {
            firebaseEmailRegister.setError("Please enter an email address");
            valid = false;
        } else if (pass.length() < 8) {
            firebasePasswordRegister.setError("Password show 8 character or more");
            valid = false;
        }

        return valid;
    }

    private static FirebaseAuth getAuth() {
        return auth;
    }

}
