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

    }

    public void onStart() {
        super.onStart();

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_firebase_login, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = this.getActivity();
        activity.setTitle("Login");
        notRegistered = view.findViewById(R.id.notRegistered);
        login = view.findViewById(R.id.login);
        firebaseEmailLogin = view.findViewById(R.id.firebaseEmailLogin);
        firebasePasswordLogin = view.findViewById(R.id.firebasePasswordLogin);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private final void UpdateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Navigation.findNavController(view).navigate(R.id.action_firebaseLoginJavaFragment_to_loginJavaFragment);
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
                String e1 = email.replaceAll("\\."," ");
                String pass = firebasePasswordLogin.getEditText().getText().toString();
                if (validateInput(e1, pass)) {
                    progressBar.setVisibility(View.VISIBLE);
                    //if(paid)
                     //ExpenseRepository.PaidForExport = true;
                    ExpenseRepository.userName = e1;
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(getString(R.string.EXPENSE_TABLE_APP));
                    editCounterAndCoin(myRef, e1);
                    getAuth().signInWithEmailAndPassword(email, pass).addOnCompleteListener((OnCompleteListener) (new OnCompleteListener() {
                        public final void onComplete(Task task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                MSPV3.getMe().putString("Start", "true");
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

    private void editCounterAndCoin(DatabaseReference myRef, String mailToDataBase) {
        try {
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {//users
                        if (child.getKey().equals(mailToDataBase)) {//if enter then in the user
                            for (DataSnapshot child1 : child.getChildren()) {//expneses
                                handleDataFromDataBase(child1);
                            }
                            break;
                        }

                    }

                    if (isCounterChange == false) {
                        myRef.child(mailToDataBase).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.EXPENSES_COUNTER)).setValue("" + 0);
                    }
                    //Navigation.findNavController(view).navigate(R.id.action_firebaseLoginJavaFragment_to_fragment_main);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            myRef.child(mailToDataBase).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.EXPENSES_COUNTER)).setValue("" + 0);
        }
    }

    private void handleDataFromDataBase(DataSnapshot child1) {
        if (child1.getKey().equals(getString(R.string.EXPENSE_TABLE))) {
            for (DataSnapshot child2 : child1.getChildren()) {//all keys until we arrive to counter
                if (child2.getKey().equals(getString(R.string.EXPENSES_COUNTER))) {
                    ExpenseRepository.counter = Integer.parseInt(child2.getValue(String.class));
                    MSPV3.getMe().putString("MPCounter", child2.getValue(String.class));
                    // also insert into MPV3
                    isCounterChange = true;
                }

            }
        } else if (child1.getKey().equals(getString(R.string.COIN))) {
            ExpenseRepository.coin = child1.getValue(String.class);
            MSPV3.getMe().putString("MPCoin", child1.getValue(String.class));
            // also insert into MPV3
        } else if (child1.getKey().equals(getString(R.string.BUDGETDB))) {
            //pull from MPV3
            ExpenseRepository.amount = Integer.parseInt(child1.getValue(String.class));
            MSPV3.getMe().putString("MPAmount", "" + ExpenseRepository.amount);
        }
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
