package com.example.finalprojectexpensemanager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
    private Button category_button;
    public static ExpenseViewModel expenseViewModel;
    private MaterialDialog mDialog;
    private TextView deleteAll;
    private TextView no_expense;
    private TextView transactionText;
    private TextView viewAllTransaction;
    private ImageView img_refresh_balance;
    private LinearLayoutManager HorizontalLayout;
    private Activity activity;
    private TextView amount_remaining2;
    private TextView name_text;
    private FloatingActionButton buttonAddNote;
    private View view;
    final String[] getkey = {"0"};
    public static List<ExpenseTable> finalE;
    private String userName;
    private RecyclerView recyclerView;
    private ImageView Iv_exit;
    private ImageView info;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MSPV3.getMe().putString(getString(R.string.UserName), ExpenseRepository.userName);
        MSPV3.getMe().putString(getString(R.string.LogInBolean), "true");
        Context context = container.getContext();
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        final ExpenseAdapter adapter = new ExpenseAdapter();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(getString(R.string.EXPENSE_TABLE_APP));
        //insert into name_text the name of user
        editName(myRef);
        //insert into amount_remaining2 a value
        editBudget(myRef);
        //insert into recyclerView some expneses
        editLastItems(myRef,recyclerView,adapter);
        recyclerView.setHasFixedSize(true);
        HorizontalLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        //add an expenses
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_main_to_fragment_add_expense);
            }
        });
        //to show all expneses
        viewAllTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_main_to_fragmentViewAllExpense);
            }
        });

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
                Navigation.findNavController(view).navigate(R.id.action_fragment_main_to_fragment_category_page);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_main_to_fragmentInfoMain);
            }
        });

        initMDialog(recyclerView,adapter);
        //to set the balance to default
        img_refresh_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.RESET)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String helper = snapshot.getValue(String.class);
                        myRef.child(userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.BUDGETDB)).setValue(""+helper);
                        if(ExpenseRepository.coin.equals("null")){
                            amount_remaining2.setText(""+helper);
                        }
                        else{
                            amount_remaining2.setText(""+helper+ExpenseRepository.coin);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        //exit from the account
        Iv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MSPV3.getMe().putString(getString(R.string.UserName), "");
                MSPV3.getMe().putString(getString(R.string.LogInBolean), "false");
                Navigation.findNavController(view).navigate(R.id.action_fragment_main_to_firebaseGoogleLoginJavaFragment);
            }
        });
        myRef.child(userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.RESET)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ExpenseRepository.reset = Integer.parseInt(snapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void initMDialog(RecyclerView recyclerView, ExpenseAdapter adapter) {
        mDialog = new MaterialDialog.Builder(activity)
                .setTitle("Delete?")
                .setMessage("Are You Sure Want To Delete All The Expenses?")
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
    }

    private void editLastItems(DatabaseReference myRef, RecyclerView recyclerView, ExpenseAdapter adapter) {
        finalE = new ArrayList<>();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (child.getKey().equals(userName)) {//then enter to the user now
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
    }

    private void editBudget(DatabaseReference myRef) {
        final int[] cur = new int[1];
        myRef.child(userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.BUDGETDB)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String helper = snapshot.getValue(String.class);
                cur[0] = Integer.parseInt(helper) -  Integer.parseInt(getkey[0]);
                myRef.child(userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.BUDGETDB)).setValue(""+cur[0]);
                if(ExpenseRepository.coin == null){
                    amount_remaining2.setText(""+cur[0]);
                }
                else{
                    amount_remaining2.setText(""+cur[0]+ExpenseRepository.coin);
                }
                //amount_remaining2.setText(""+cur[0]+ExpenseRepository.coin);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void editName(DatabaseReference myRef) {
        myRef.child(userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.NAMEDB)).addListenerForSingleValueEvent(new ValueEventListener() {
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
    }

    private void initView(View view) {
        activity = this.getActivity();
        activity.setTitle("Expense Manager");
        img_refresh_balance = view.findViewById(R.id.img_refresh_balance);
        buttonAddNote = view.findViewById(R.id.button_add_note);
        deleteAll = view.findViewById(R.id.deleteAllExpense);
        Iv_exit = view.findViewById(R.id.Iv_exit);
        no_expense = view.findViewById(R.id.no_expense);
        info = view.findViewById(R.id.info);
        transactionText = view.findViewById(R.id.transaction);
        viewAllTransaction = view.findViewById(R.id.viewAllTransaction);
        category_button = view.findViewById(R.id.category_button);
        amount_remaining2 = view.findViewById(R.id.amount_remaining2);
        name_text = view.findViewById(R.id.name_text);
        userName = ExpenseRepository.userName;
        recyclerView = view.findViewById(R.id.recycler_view);
        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
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
                Toast.makeText(activity, "All Expenses Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void callParentMethod(){
        activity.onBackPressed();
    }

}
