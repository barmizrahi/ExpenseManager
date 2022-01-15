package com.example.finalprojectexpensemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectexpensemanager.LoginAndSaved.LoginFragment;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import com.example.finalprojectexpensemanager.Adapters.ExpenseAdapter;
import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.example.finalprojectexpensemanager.ViewModel.ExpenseViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class MainFragment extends Fragment {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    public static final int RESULT_OK = -1;
    private Button category_button;
    public static ExpenseViewModel expenseViewModel;
    private MaterialDialog mDialog;
    private TextView deleteAll;
    private TextView no_expense;
    private TextView transactionText;
    private TextView viewAllTransaction;
    private LinearLayoutManager HorizontalLayout;
    private Activity activity;
    private TextView amount_remaining2;
    private TextView name_text;
    private View view;
    final String[] getkey = {"0"};
    public static List<ExpenseTable> finalE;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = container.getContext();
        view = inflater.inflate(R.layout.fragment_main, container, false);
        activity = this.getActivity();
        activity.setTitle("Expense Manager");
        FloatingActionButton buttonAddNote = view.findViewById(R.id.button_add_note);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("login", Context.MODE_PRIVATE);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        deleteAll = view.findViewById(R.id.deleteAllExpense);
        no_expense = view.findViewById(R.id.no_expense);
        transactionText = view.findViewById(R.id.transaction);
        viewAllTransaction = view.findViewById(R.id.viewAllTransaction);
        final ExpenseAdapter adapter = new ExpenseAdapter();
        category_button = view.findViewById(R.id.category_button);
        amount_remaining2 = view.findViewById(R.id.amount_remaining2);
        name_text = view.findViewById(R.id.name_text);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ExpenseRepository.EXPENSE_TABLE_APP);
        myRef.child(ExpenseRepository.userName).child(ExpenseRepository.EXPENSE_TABLE).child(LoginFragment.NAMEDB).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name_text.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getParentFragmentManager().setFragmentResultListener("dataFromAddExp", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                getkey[0] = result.getString("Amount");

            }
        });
        final int[] cur = new int[1];
        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        myRef.child(ExpenseRepository.userName).child(ExpenseRepository.EXPENSE_TABLE).child(LoginFragment.BUDGETDB).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String helper = snapshot.getValue(String.class);
                cur[0] = Integer.parseInt(helper) -  Integer.parseInt(getkey[0]);
                myRef.child(ExpenseRepository.userName).child(ExpenseRepository.EXPENSE_TABLE).child(LoginFragment.BUDGETDB).setValue(""+cur[0]);
                amount_remaining2.setText(""+cur[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //final boolean[] bol = {false};
        List<ExpenseTable> e = new ArrayList<>();
        finalE = e;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (child.getKey().equals(ExpenseRepository.userName)) {//then enter to the user now
                                for (DataSnapshot child1 : child.getChildren()) {//expneses
                                    for (DataSnapshot child2 : child1.getChildren()) {
                                        for (DataSnapshot child3 : child2.getChildren()) {//items of type
                                            try {
                                                ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                                                finalE.add(expenseTable);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (finalE.isEmpty()) {
                            no_expense.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            deleteAll.setVisibility(View.INVISIBLE);
                            transactionText.setVisibility(View.INVISIBLE);
                            viewAllTransaction.setVisibility(View.INVISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            deleteAll.setVisibility(View.VISIBLE);
                            transactionText.setVisibility(View.VISIBLE);
                            no_expense.setVisibility(View.INVISIBLE);
                            viewAllTransaction.setVisibility(View.VISIBLE);
                        }
                        expenseViewModel.setAllExpenses(finalE);
                        adapter.setNotes(expenseViewModel.getAllExpense());
                        recyclerView.setAdapter(adapter);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



        recyclerView.setHasFixedSize(true);

        HorizontalLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_main_to_fragment_add_expense);

            }
        });

        viewAllTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_main_to_fragmentViewAllExpense);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.UP) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }).attachToRecyclerView(recyclerView);


        //To delete all Expenses
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();

            }
        });

        //Go to category page
        category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to frangent category page
                Navigation.findNavController(view).navigate(R.id.action_fragment_main_to_fragment_category_page);

            }
        });

        //Material Dialog
        mDialog = new MaterialDialog.Builder(activity)
                .setTitle("Delete?")
                .setMessage("Are you sure want to delete all the expenses?")
                .setCancelable(false)
                .setPositiveButton("Delete", R.drawable.ic_delete, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        expenseViewModel.deleteAllExpense();
                        adapter.setNotes(expenseViewModel.getAllExpense());
                        recyclerView.setAdapter(adapter);
                        dialogInterface.dismiss();
                        no_expense.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        deleteAll.setVisibility(View.INVISIBLE);
                        transactionText.setVisibility(View.INVISIBLE);
                        viewAllTransaction.setVisibility(View.INVISIBLE);
                    }
                })
                .setNegativeButton("Cancel", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        return view;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = activity.getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                expenseViewModel.deleteAllExpense();
                Toast.makeText(activity, "All expenses deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
