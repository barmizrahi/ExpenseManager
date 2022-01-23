package com.example.finalprojectexpensemanager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
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

public class MyCategoryDetalis extends Fragment  {
    private RecyclerView category_view;
    private CategoryViewModel categoryViewModel;
    private float totalSpending = 0;
    private TickerView totalSpendingtext;
    private TextView categoryTitle;
    private ImageButton backbtn;
    private Activity activity;
    final String[] getkey = {""};
    final AllTransactionAdapter adapter = new AllTransactionAdapter();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_details, container, false);
        initView(view);
        Context context = container.getContext();
        totalSpendingtext.setCharacterLists(TickerUtils.provideNumberList());
        category_view.setLayoutManager(new LinearLayoutManager(context));
        getParentFragmentManager().setFragmentResultListener("dataFromCatagory", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                getkey[0] = result.getString(getString(R.string.EXTRA_CATEGORY));
                Toast.makeText(context, "Selected Key" + getkey[0], Toast.LENGTH_SHORT).show();
                category_view.setAdapter(adapter);
                categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
                if (getkey[0] == null) {
                    throw new AssertionError("Object cannot be null");
                } else
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

    private void initView(View view) {
        this.activity = getActivity();
        backbtn = view.findViewById(R.id.back);
        category_view = view.findViewById(R.id.recycler_view3);
        totalSpendingtext = view.findViewById(R.id.totalspending);
        categoryTitle = view.findViewById(R.id.category_title);
    }

    private void mySwichCase(String key) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(getString(R.string.EXPENSE_TABLE_APP));
        List<ExpenseTable> expnsesToShowByCategory = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {//users
                    if (child.getKey().equals(ExpenseRepository.userName)) {//if enter then in the user
                        for (DataSnapshot child1 : child.getChildren()) {//expneses
                            for (DataSnapshot child2 : child1.getChildren()) {
                                mySwichCase1(child2, key, expnsesToShowByCategory);
                            }
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void mySwichCase1(DataSnapshot child2, String key, List<ExpenseTable> expnsesToShowByCategory) {
        switch (key) {
            case "Food":
                categoryTitle.setText("Food Expenses");
                if (child2.getKey().equals("Food")) {
                    insertIntoExpnsesToShowByCategory(child2, expnsesToShowByCategory);
                }
                break;
            case "Travel":
                categoryTitle.setText("Travel Expenses");
                if (child2.getKey().equals("Travel")) {
                    insertIntoExpnsesToShowByCategory(child2, expnsesToShowByCategory);
                }
                break;
            case "Utilities":
                categoryTitle.setText("Utilities Expenses");
                if (child2.getKey().equals("Utilities")) {
                    insertIntoExpnsesToShowByCategory(child2, expnsesToShowByCategory);
                }
                break;
            case "Health":
                categoryTitle.setText("Health Expenses");
                if (child2.getKey().equals("Health")) {
                    insertIntoExpnsesToShowByCategory(child2, expnsesToShowByCategory);
                }


                break;
            case "Shopping":
                categoryTitle.setText("Shopping Expenses");
                if (child2.getKey().equals("Shopping")) {
                    insertIntoExpnsesToShowByCategory(child2, expnsesToShowByCategory);
                }
                break;
            case "Others":
                categoryTitle.setText("Others Expenses");
                if (child2.getKey().equals("Others")) {
                    insertIntoExpnsesToShowByCategory(child2, expnsesToShowByCategory);
                }
                break;
        }
    }

    private void insertIntoExpnsesToShowByCategory(DataSnapshot child2, List<ExpenseTable> expnsesToShowByCategory) {
        for (DataSnapshot child3 : child2.getChildren()) {//items of type
            try {
                ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                expnsesToShowByCategory.add(expenseTable);
            } catch (Exception ex) {
            }
        }
        adapter.setNotes(expnsesToShowByCategory);
        for (int i = 0; i < expnsesToShowByCategory.size(); i++) {
            totalSpending = totalSpending + Float.parseFloat(expnsesToShowByCategory.get(i).getAmount());
        }
        totalSpendingtext.setText( ""+ totalSpending+ExpenseRepository.coin);
    }
}
