package com.example.finalprojectexpensemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.ArrayList;
import java.util.List;

import com.example.finalprojectexpensemanager.Adapters.AllTransactionAdapter;
import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.example.finalprojectexpensemanager.ViewModel.CategoryViewModel;

public class MyCategoryDetalis extends Fragment {
    private RecyclerView category_view;
    private CategoryViewModel categoryViewModel;
    private float totalSpending=0;
    private TickerView totalSpendingtext;
    private  TextView categoryTitle;
    private ImageButton backbtn;
    private  Activity activity;
    final String[] getkey = {""};
    final AllTransactionAdapter adapter = new AllTransactionAdapter();
    //public void setActivity(AppCompatActivity activity) {
       // this.activity = activity;
    //}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_details, container, false);
        //activity.getSupportActionBar().hide();
        this.activity = getActivity();
        Context context = container.getContext();
        backbtn = view.findViewById(R.id.back);
        category_view = view.findViewById(R.id.recycler_view3);
        totalSpendingtext = view.findViewById(R.id.totalspending);
        categoryTitle = view.findViewById(R.id.category_title);
        totalSpendingtext.setCharacterLists(TickerUtils.provideNumberList());
        category_view.setLayoutManager(new LinearLayoutManager(context));
        getParentFragmentManager().setFragmentResultListener("dataFromCatagory", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                getkey[0] = result.getString(CategoryPage.EXTRA_CATEGORY);
                int x = 0;
                Toast.makeText(context, "selected key" + getkey[0], Toast.LENGTH_SHORT).show();
                //final AllTransactionAdapter adapter = new AllTransactionAdapter();
                category_view.setAdapter(adapter);
                categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
                //assert getkey != null;
                if (getkey[0] == null) {
                    throw new AssertionError("Object cannot be null");
                }
                else
                    mySwichCase(getkey[0]);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentMyCategotyDetalis_to_fragment_main);
            }
        });

        return view;
    }

    private void mySwichCase(String key) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ExpenseRepository.EXPENSE_TABLE_APP);
        List<ExpenseTable> foodExp = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //LiveData<List<ExpenseTable>> ExpenseTable = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {//users
                    if (child.getKey().equals(ExpenseRepository.userName)) {//if enter then in the user
                        for (DataSnapshot child1 : child.getChildren()) {//expneses
                            for (DataSnapshot child2 : child1.getChildren()) {
                                switch (key) {
                                    case "Food":
                                        categoryTitle.setText("Food Expenses");
                                        if (child2.getKey().equals("Food")) {
                                            for (DataSnapshot child3 : child2.getChildren()) {//items of type
                                                try {
                                                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                                                    foodExp.add(expenseTable);
                                                } catch (Exception ex) {
                                                }
                                            }
                                            adapter.setNotes(foodExp);
                                            for (int i = 0; i < foodExp.size(); i++) {
                                                totalSpending = totalSpending + Float.parseFloat(foodExp.get(i).getAmount());
                                            }
                                            totalSpendingtext.setText("Rs " + totalSpending);
                                            return;
                                        }
                                        break;
                                    case "Travel":
                                        categoryTitle.setText("Travel Expenses");
                                        if (child2.getKey().equals("Travel")) {
                                            for (DataSnapshot child3 : child2.getChildren()) {//items of type
                                                try {
                                                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                                                    foodExp.add(expenseTable);
                                                } catch (Exception ex) {
                                                }
                                            }
                                            adapter.setNotes(foodExp);
                                            for (int i = 0; i < foodExp.size(); i++) {
                                                totalSpending = totalSpending + Float.parseFloat(foodExp.get(i).getAmount());
                                            }
                                            totalSpendingtext.setText("Rs " + totalSpending);
                                            return;
                                        }
                                    /*
                                    categoryViewModel.getTravelCategory().observe(getViewLifecycleOwner(), new Observer<List<ExpenseTable>>() {
                                        @Override
                                        public void onChanged(List<ExpenseTable> expenseTables) {
                                            adapter.setNotes(expenseTables);
                                            for (int i = 0; i < expenseTables.size(); i++) {
                                                totalSpending = totalSpending + Float.parseFloat(expenseTables.get(i).getAmount());
                                            }
                                            totalSpendingtext.setText("Rs " + totalSpending);
                                        }
                                    });

                                     */
                                        break;
                                    case "Utilities":
                                        categoryTitle.setText("Utilities Expenses");
                                        if (child2.getKey().equals("Utilities")) {
                                            for (DataSnapshot child3 : child2.getChildren()) {//items of type
                                                try {
                                                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                                                    foodExp.add(expenseTable);
                                                } catch (Exception ex) {
                                                }
                                            }
                                            adapter.setNotes(foodExp);
                                            for (int i = 0; i < foodExp.size(); i++) {
                                                totalSpending = totalSpending + Float.parseFloat(foodExp.get(i).getAmount());
                                            }
                                            totalSpendingtext.setText("Rs " + totalSpending);
                                            return;
                                        }
                                    /*
                                    categoryViewModel.getUtilityCategory().observe(getViewLifecycleOwner(), new Observer<List<ExpenseTable>>() {
                                        @Override
                                        public void onChanged(List<ExpenseTable> expenseTables) {
                                            adapter.setNotes(expenseTables);
                                            for (int i = 0; i < expenseTables.size(); i++) {
                                                totalSpending = totalSpending + Float.parseFloat(expenseTables.get(i).getAmount());
                                            }
                                            totalSpendingtext.setText("Rs " + totalSpending);
                                        }
                                    });

                                     */
                                        break;
                                    case "Health":
                                        categoryTitle.setText("Health Expenses");
                                        if (child2.getKey().equals("Health")) {
                                            for (DataSnapshot child3 : child2.getChildren()) {//items of type
                                                try {
                                                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                                                    foodExp.add(expenseTable);
                                                } catch (Exception ex) {
                                                }
                                            }
                                            adapter.setNotes(foodExp);
                                            for (int i = 0; i < foodExp.size(); i++) {
                                                totalSpending = totalSpending + Float.parseFloat(foodExp.get(i).getAmount());
                                            }
                                            totalSpendingtext.setText("Rs " + totalSpending);
                                            return;
                                        }
                                    /*
                                    categoryViewModel.getHealthCategory().observe(getViewLifecycleOwner(), new Observer<List<ExpenseTable>>() {
                                        @Override
                                        public void onChanged(List<ExpenseTable> expenseTables) {
                                            adapter.setNotes(expenseTables);
                                            for (int i = 0; i < expenseTables.size(); i++) {
                                                totalSpending = totalSpending + Float.parseFloat(expenseTables.get(i).getAmount());
                                            }
                                            totalSpendingtext.setText("Rs " + totalSpending);
                                        }
                                    });

                                     */
                                        break;
                                    case "Shopping":
                                        categoryTitle.setText("Shopping Expenses");
                                        if (child2.getKey().equals("Shopping")) {
                                            for (DataSnapshot child3 : child2.getChildren()) {//items of type
                                                try {
                                                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                                                    foodExp.add(expenseTable);
                                                } catch (Exception ex) {
                                                }
                                            }
                                            adapter.setNotes(foodExp);
                                            for (int i = 0; i < foodExp.size(); i++) {
                                                totalSpending = totalSpending + Float.parseFloat(foodExp.get(i).getAmount());
                                            }
                                            totalSpendingtext.setText("Rs " + totalSpending);
                                            return;
                                        }
                                    /*
                                    categoryViewModel.getShoppingCategory().observe(getViewLifecycleOwner(), new Observer<List<ExpenseTable>>() {
                                        @Override
                                        public void onChanged(List<ExpenseTable> expenseTables) {
                                            adapter.setNotes(expenseTables);
                                            for (int i = 0; i < expenseTables.size(); i++) {
                                                totalSpending = totalSpending + Float.parseFloat(expenseTables.get(i).getAmount());
                                            }
                                            totalSpendingtext.setText("Rs " + totalSpending);
                                        }
                                    });

                                     */
                                        break;
                                    case "Others":
                                        categoryTitle.setText("Others Expenses");
                                        if (child2.getKey().equals("Others")) {
                                            for (DataSnapshot child3 : child2.getChildren()) {//items of type
                                                try {
                                                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                                                    foodExp.add(expenseTable);
                                                } catch (Exception ex) {
                                                }
                                            }
                                            adapter.setNotes(foodExp);
                                            for (int i = 0; i < foodExp.size(); i++) {
                                                totalSpending = totalSpending + Float.parseFloat(foodExp.get(i).getAmount());
                                            }
                                            totalSpendingtext.setText("Rs " + totalSpending);
                                            return;
                                        }
                                    /*
                                    categoryViewModel.getOthersCategory().observe(getViewLifecycleOwner(), new Observer<List<ExpenseTable>>() {
                                        @Override
                                        public void onChanged(List<ExpenseTable> expenseTables) {
                                            adapter.setNotes(expenseTables);
                                            for (int i = 0; i < expenseTables.size(); i++) {
                                                totalSpending = totalSpending + Float.parseFloat(expenseTables.get(i).getAmount());
                                            }
                                            totalSpendingtext.setText("Rs " + totalSpending);
                                        }
                                    });

                                     */
                                        break;
                                }
                            }
                        }
                    }
                }
            }
                                /*
                                if(child2.getKey().equals("Food")) {
                                    for (DataSnapshot child3 : child2.getChildren()) {//items of type
                                        try {
                                            ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                                            foodExp.add(expenseTable);
                                        } catch (Exception ex) {
                                        }
                                    }
                                    adapter.setNotes(foodExp);
                                    for (int i = 0; i < foodExp.size(); i++) {
                                        totalSpending = totalSpending + Float.parseFloat(foodExp.get(i).getAmount());
                                    }
                                    totalSpendingtext.setText("Rs " + totalSpending);
                                    return;
                                }
                            }
                        }

                    }
                }
                // if (callBack_cars != null) {
                //   callBack_cars.dataReady(cars);
                //  }


                                 */


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
/*
        switch (key) {
            case "Food":
                categoryTitle.setText("Food Expenses");


                //List<ExpenseTable> foodExpenses = new ArrayList<>();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //LiveData<List<ExpenseTable>> ExpenseTable = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {//users
                            if (child.getKey().equals(ExpenseRepository.userName)) {//if enter then in the user
                                for (DataSnapshot child1 : child.getChildren()) {//expneses
                                    for (DataSnapshot child2 : child1.getChildren()) {
                                        if(child2.getKey().equals("Food")) {
                                            for (DataSnapshot child3 : child2.getChildren()) {//items of type
                                                try {
                                                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                                                    foodExp.add(expenseTable);
                                                } catch (Exception ex) {
                                                }
                                            }
                                            adapter.setNotes(foodExp);
                                            for (int i = 0; i < foodExp.size(); i++) {
                                                totalSpending = totalSpending + Float.parseFloat(foodExp.get(i).getAmount());
                                            }
                                            totalSpendingtext.setText("Rs " + totalSpending);
                                            return;
                                        }
                                    }
                                }

                            }
                        }
                        // if (callBack_cars != null) {
                        //   callBack_cars.dataReady(cars);
                        //  }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


                categoryViewModel.getFoodCategory().observe(getViewLifecycleOwner(), new Observer<List<ExpenseTable>>() {
                    @Override
                    public void onChanged(List<ExpenseTable> expenseTables) {
                        adapter.setNotes(expenseTables);
                        for (int i = 0; i < expenseTables.size(); i++) {
                            totalSpending = totalSpending + Float.parseFloat(expenseTables.get(i).getAmount());
                        }
                        totalSpendingtext.setText("Rs " + totalSpending);
                    }
                });


                break;
            case "Travel":
                categoryTitle.setText("Travel Expenses");
                categoryViewModel.getTravelCategory().observe(getViewLifecycleOwner(), new Observer<List<ExpenseTable>>() {
                    @Override
                    public void onChanged(List<ExpenseTable> expenseTables) {
                        adapter.setNotes(expenseTables);
                        for (int i = 0; i < expenseTables.size(); i++) {
                            totalSpending = totalSpending + Float.parseFloat(expenseTables.get(i).getAmount());
                        }
                        totalSpendingtext.setText("Rs " + totalSpending);
                    }
                });
                break;
            case "Utilities":
                categoryTitle.setText("Utilities Expenses");
                categoryViewModel.getUtilityCategory().observe(getViewLifecycleOwner(), new Observer<List<ExpenseTable>>() {
                    @Override
                    public void onChanged(List<ExpenseTable> expenseTables) {
                        adapter.setNotes(expenseTables);
                        for (int i = 0; i < expenseTables.size(); i++) {
                            totalSpending = totalSpending + Float.parseFloat(expenseTables.get(i).getAmount());
                        }
                        totalSpendingtext.setText("Rs " + totalSpending);
                    }
                });
                break;
            case "Health":
                categoryTitle.setText("Health Expenses");
                categoryViewModel.getHealthCategory().observe(getViewLifecycleOwner(), new Observer<List<ExpenseTable>>() {
                    @Override
                    public void onChanged(List<ExpenseTable> expenseTables) {
                        adapter.setNotes(expenseTables);
                        for (int i = 0; i < expenseTables.size(); i++) {
                            totalSpending = totalSpending + Float.parseFloat(expenseTables.get(i).getAmount());
                        }
                        totalSpendingtext.setText("Rs " + totalSpending);
                    }
                });
                break;
            case "Shopping":
                categoryTitle.setText("Shopping Expenses");
                categoryViewModel.getShoppingCategory().observe(getViewLifecycleOwner(), new Observer<List<ExpenseTable>>() {
                    @Override
                    public void onChanged(List<ExpenseTable> expenseTables) {
                        adapter.setNotes(expenseTables);
                        for (int i = 0; i < expenseTables.size(); i++) {
                            totalSpending = totalSpending + Float.parseFloat(expenseTables.get(i).getAmount());
                        }
                        totalSpendingtext.setText("Rs " + totalSpending);
                    }
                });
                break;
            case "Others":
                categoryTitle.setText("Others Expenses");
                categoryViewModel.getOthersCategory().observe(getViewLifecycleOwner(), new Observer<List<ExpenseTable>>() {
                    @Override
                    public void onChanged(List<ExpenseTable> expenseTables) {
                        adapter.setNotes(expenseTables);
                        for (int i = 0; i < expenseTables.size(); i++) {
                            totalSpending = totalSpending + Float.parseFloat(expenseTables.get(i).getAmount());
                        }
                        totalSpendingtext.setText("Rs " + totalSpending);
                    }
                });
                break;


        }
    }

 */
    }
}
