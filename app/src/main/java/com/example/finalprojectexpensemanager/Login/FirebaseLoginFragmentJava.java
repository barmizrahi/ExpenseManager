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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.finalprojectexpensemanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FirebaseLoginFragmentJava extends Fragment {
    private FirebaseAuth auth;
    private Button notRegistered;
    private Button login;
    private TextInputLayout firebaseEmailLogin;
    private TextInputLayout firebasePasswordLogin;
    private ProgressBar progressBar;
    private Activity activity;
    private View view;
    private boolean isCounterChange = false;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.auth = FirebaseAuth.getInstance();
/*
        if (auth.getCurrentUser() != null) {

            FragmentKt.findNavController(this).navigate(FirebaseLoginFragmentJavaDirections.actionFirebaseLoginJavaFragmentToLoginJavaFragment());
        }

 */


    }

    public void onStart() {
        super.onStart();


        //  FirebaseUser currentUser = auth.getCurrentUser();
        // UpdateUI(currentUser);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_firebase_login, container, false);
        activity = this.getActivity();

        notRegistered = view.findViewById(R.id.notRegistered);
        login = view.findViewById(R.id.login);
        firebaseEmailLogin = view.findViewById(R.id.firebaseEmailLogin);
        firebasePasswordLogin = view.findViewById(R.id.firebasePasswordLogin);
        progressBar = view.findViewById(R.id.progressBar);
        return view;
    }

    private final void UpdateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Navigation.findNavController(view).navigate(R.id.action_firebaseLoginJavaFragment_to_loginJavaFragment);
            // FragmentKt.findNavController(this).navigate(FirebaseLoginFragmentJavaDirections.actionFirebaseLoginJavaFragmentToLoginJavaFragment());
        }

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notRegistered.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                Navigation.findNavController(view).navigate(R.id.action_firebaseLoginJavaFragment_to_firebaseRegisterJavaFragment);
            }
        }));
        login.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                firebaseEmailLogin.setError((CharSequence) null);
                firebasePasswordLogin.setError((CharSequence) null);

                String email = firebaseEmailLogin.getEditText().getText().toString();
                String pass = firebasePasswordLogin.getEditText().getText().toString();
                if (validateInput(email, pass)) {
                    progressBar.setVisibility(View.VISIBLE);
                    String[] mailToDataBase = email.split("@");
                    ExpenseRepository.userName = mailToDataBase[0];
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(ExpenseRepository.EXPENSE_TABLE_APP);
                    try {
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot child : snapshot.getChildren()) {//users
                                    if (child.getKey().equals(mailToDataBase[0])) {//if enter then in the user
                                        for (DataSnapshot child1 : child.getChildren()) {//expneses
                                            for (DataSnapshot child2 : child1.getChildren()) {//all catagoty ulnil we ariive to counter
                                                if (child2.getKey().equals(ExpenseRepository.EXPENSES_COUNTER)) {
                                                    ExpenseRepository.counter = Integer.parseInt(child2.getValue(String.class));
                                                    isCounterChange = true;
                                                }

                                            }
                                        }
                                        break;
                                    }

                                }

                                if (isCounterChange == false) {
                                    myRef.child(mailToDataBase[0]).child(ExpenseRepository.EXPENSE_TABLE).child(ExpenseRepository.EXPENSES_COUNTER).setValue("" + 0);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {


                            }
                        });

                    } catch (Exception e) {
                        myRef.child(mailToDataBase[0]).child(ExpenseRepository.EXPENSE_TABLE).child(ExpenseRepository.EXPENSES_COUNTER).setValue("" + 0);
                    }

                    getAuth().signInWithEmailAndPassword(email, pass).addOnCompleteListener((OnCompleteListener) (new OnCompleteListener() {
                        public final void onComplete(Task task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                Navigation.findNavController(view).navigate(R.id.action_firebaseLoginJavaFragment_to_fragment_main);
                            } else {
                                Context var3 = (Context) FirebaseLoginFragmentJava.this.requireActivity();
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

    private FirebaseAuth getAuth() {
        return auth;
    }

    private final boolean validateInput(String email, String pass) {
        boolean valid = true;
        if (email.isEmpty()) {
            firebaseEmailLogin.setError("Please enter an email address");
            valid = false;
        } else if (pass.length() < 8) {
            firebasePasswordLogin.setError("Password show 8 character or more");
            valid = false;
        }
        return valid;
    }
}
