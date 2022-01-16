package com.example.finalprojectexpensemanager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.airbnb.lottie.L;
import com.example.finalprojectexpensemanager.Adapters.AllTransactionAdapter;
import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.example.finalprojectexpensemanager.LoginAndSaved.LoginFragment;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.example.finalprojectexpensemanager.ViewModel.AllExpenseViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class ViewAllExpenseFragment extends Fragment {
    private RecyclerView expenseView;
    private AllExpenseViewModel expenseViewModel;
    private ImageButton back;
    private Activity activity;
    private List<ExpenseTable> expenseTables = new ArrayList<>();
    private MaterialDialog mDialog;
    private int choose;
    RecyclerView.ViewHolder viewHolder1;


    class SortByDate implements Comparator<ExpenseTable> {
        @Override
        public int compare(ExpenseTable a, ExpenseTable b) {
            return b.getDate().compareTo(a.getDate());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_expense, container, false);
        Context context = container.getContext();
        activity = getActivity();
        expenseView = view.findViewById(R.id.recycler_view2);
        back = view.findViewById(R.id.back_all);
        expenseView.setLayoutManager(new LinearLayoutManager(context));
        final AllTransactionAdapter adapter = new AllTransactionAdapter();
        expenseView.setAdapter(adapter);
        expenseViewModel = new ViewModelProvider(this).get(AllExpenseViewModel.class);
        expenseTables = expenseViewModel.getAllExpense();
        Collections.sort(expenseTables, new SortByDate());
        adapter.setNotes(expenseTables);
        mDialog = new MaterialDialog.Builder(activity)
                .setTitle("Delete?")
                .setMessage("Do you want a refund?")
                .setCancelable(false)
                .setPositiveButton("Refund", R.drawable.ic_delete, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        ExpenseTable e = expenseViewModel.myDelete(viewHolder1.getAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder1.getAdapterPosition());
                        adapter.notifyItemRangeChanged(viewHolder1.getAdapterPosition(), ExpenseRepository.allExpenses.size());
                        viewHolder1.itemView.setVisibility(View.GONE);
                        dialogInterface.dismiss();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(ExpenseRepository.EXPENSE_TABLE_APP);
                        myRef.child(ExpenseRepository.userName).child(ExpenseRepository.EXPENSE_TABLE).child(LoginFragment.BUDGETDB).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String helper = snapshot.getValue(String.class).toString();
                                int newAmount = Integer.parseInt(helper) + Integer.parseInt(e.getAmount());
                                myRef.child(ExpenseRepository.userName).child(ExpenseRepository.EXPENSE_TABLE).child(LoginFragment.BUDGETDB).setValue("" + newAmount);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                })
                .setNegativeButton("No Refund", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        expenseViewModel.myDelete(viewHolder1.getAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder1.getAdapterPosition());
                        adapter.notifyItemRangeChanged(viewHolder1.getAdapterPosition(), ExpenseRepository.allExpenses.size());
                        viewHolder1.itemView.setVisibility(View.GONE);
                        dialogInterface.dismiss();
                    }
                })
                .build();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                viewHolder1 = viewHolder;
                mDialog.show();

            }


        }).attachToRecyclerView(expenseView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentViewAllExpense_to_fragment_main);
            }
        });
        return view;
    }
}


