package com.example.finalprojectexpensemanager.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.finalprojectexpensemanager.MSPV3;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.example.finalprojectexpensemanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FirebaseGoogleLoginFragmentJava extends Fragment {
    private View view;
    private static FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private final String TAG = "GoogleActivity";
    private final int RC_SIGN_IN = 9001;
    private Button buttonxyz;
    private SignInButton sign_in_button;
    private Activity activity;
    private boolean isCounterChange;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        //UpdateUI(currentUser);
    }

    private final void UpdateUI(FirebaseUser currentUser) {

        if (currentUser != null) {
            Navigation.findNavController(this.view).navigate(R.id.action_firebaseGoogleLoginJavaFragment_to_fragmentMain);
        }

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GoogleSignInOptions gso = (new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)).requestIdToken("268956387720-ubd2soa3eeje942mtm16f2to2vgb0tac.apps.googleusercontent.com").requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient((requireActivity()), gso);
        Context context = container.getContext();

        view = inflater.inflate(R.layout.fragment_firebase_google_login, container, false);
        activity = this.getActivity();
        buttonxyz = view.findViewById(R.id.buttonxyz);
        sign_in_button = view.findViewById(R.id.sign_in_button);
        return view;
    }

    private final void signIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        super.startActivityForResult(intent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(getString(R.string.TAG), "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }


    private final void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, (String) null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), (OnCompleteListener) (new OnCompleteListener() {
                    public final void onComplete(Task task) {
                        if (task.isSuccessful()) {
                            //FragmentKt.findNavController(FirebaseGoogleLoginFragmentJava.this).navigate(FirebaseGoogleLoginFragmentJavaDirections.actionFirebaseGoogleLoginJavaFragmentToLoginFragment2());
                            Log.d(getString(R.string.TAG), "signInWithCredential:success");
                            FirebaseUser var2 = getAuth().getCurrentUser();
                            Navigation.findNavController(view).navigate(R.id.action_firebaseGoogleLoginJavaFragment_to_loginFragment);
                        } else {
                            Toast.makeText(FirebaseGoogleLoginFragmentJava.this.requireContext(), (CharSequence) "error", Toast.LENGTH_SHORT).show();
                            Log.w(getString(R.string.TAG), "signInWithCredential:failure", (Throwable) task.getException());
                        }

                    }
                }));
    }

    private static FirebaseAuth getAuth() {
        return auth;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String logIn = MSPV3.getMe().getString(getString(R.string.LogInBolean), "");
        if(logIn.equals("true")){
            ExpenseRepository.userName = MSPV3.getMe().getString(getString(R.string.UserName), "");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(getString(R.string.EXPENSE_TABLE_APP));
            editCounterAndCoin(myRef,ExpenseRepository.userName);
            Navigation.findNavController(view).navigate(R.id.action_firebaseGoogleLoginJavaFragment_to_fragmentMain);
        }

        (buttonxyz).setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                Navigation.findNavController(view).navigate(R.id.action_firebaseGoogleLoginJavaFragment_to_firebaseLoginFragment);
            }
        }));
        (sign_in_button).setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                signIn();
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
                                for (DataSnapshot child2 : child1.getChildren()) {//all keys until we arrive to counter
                                    if (child2.getKey().equals(getString(R.string.EXPENSES_COUNTER))) {
                                        ExpenseRepository.counter = Integer.parseInt(child2.getValue(String.class));
                                        isCounterChange = true;
                                    }
                                    if(child2.getKey().equals(getString(R.string.COIN))){
                                        ExpenseRepository.coin = child2.getValue(String.class);
                                    }
                                }
                            }
                            break;
                        }

                    }

                    if (isCounterChange == false) {
                        myRef.child(mailToDataBase).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.EXPENSES_COUNTER)).setValue("" + 0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });

        } catch (Exception e) {
            myRef.child(mailToDataBase).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.EXPENSES_COUNTER)).setValue("" + 0);
        }
    }

}
