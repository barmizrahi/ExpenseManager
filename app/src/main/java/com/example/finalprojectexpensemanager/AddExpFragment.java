package com.example.finalprojectexpensemanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;

public class AddExpFragment extends Fragment {
    private TextInputEditText name;
    private TextInputEditText amount;
    private MaterialTextView date;
    private TextInputEditText desc;
    private MaterialTextView selectdate;
    private MaterialTextView title;
    private MaterialButton save;
    private int mYear, mMonth, mDay;
    private ChipGroup chipGroup;
    private String category;
    private ImageButton back_add;
    private Boolean isSelected = false;
    private Activity activity;
    private View view;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        initView();
        activity.setTitle("Add Expense");
        context = container.getContext();
        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
                // Get Current Date
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        //ChipGroup
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = chipGroup.findViewById(checkedId);
                if (chip != null) {
                    category = chip.getText().toString();
                    isSelected = true;
                }
            }
        });
        back_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_add_expense_to_fragment_main);
            }
        });
        return view;
    }

    private void setDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String day, month;
                        if (dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                        } else
                            day = "" + (dayOfMonth);
                        if (monthOfYear < 10) {
                            month = "0" + (monthOfYear + 1);
                        } else
                            month = "" + (monthOfYear + 1);
                        date.setText(year + "-" + month + "-" + day);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void initView() {
        this.activity = getActivity();
        name = view.findViewById(R.id.name_input);
        amount = view.findViewById(R.id.amount_input);
        date = view.findViewById(R.id.date_input);
        desc = view.findViewById(R.id.description_input);
        selectdate = view.findViewById(R.id.selectDate);
        save = view.findViewById(R.id.addButton);
        title = view.findViewById(R.id.titleID);
        back_add = view.findViewById(R.id.back_add);
        activity.setTitle("Add Expense");
        title.setText("Add Expense");
        chipGroup = view.findViewById(R.id.categoryChipGroup);
    }

    private void saveNote() {
        String nameText = name.getText().toString();
        String descText = desc.getText().toString();
        String amountText = amount.getText().toString();
        String dateText = date.getText().toString();
        String categoryData = category;
        if (nameText.trim().isEmpty() || descText.trim().isEmpty() || amountText.trim().isEmpty() || dateText.trim().isEmpty() || !isSelected) {
            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
    try {
        if (Integer.parseInt(amountText) < 0) {
            amount.setError("Amount Can't Be Negative");
            return;
        }
    }
    catch (NumberFormatException e){
        amount.setError("Amount Need To Be A Integer");
        return;
    }
        ExpenseTable expenseTable = new ExpenseTable(nameText, amountText, dateText, descText, categoryData, ExpenseRepository.counter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(getString(R.string.EXPENSE_TABLE_APP));
        tryToAdd(expenseTable,myRef,amountText);
        Navigation.findNavController(view).navigate(R.id.action_fragment_add_expense_to_fragment_main);

    }

    private void tryToAdd(ExpenseTable expenseTable, DatabaseReference myRef, String amountText) {
        myRef.child(ExpenseRepository.userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.BUDGETDB)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((Integer.parseInt(snapshot.getValue(String.class)) - Integer.parseInt(expenseTable.getAmount())) < 0) {
                    Toast.makeText(activity, "Not Enough Money", Toast.LENGTH_SHORT).show();
                } else {
                    ExpenseRepository.counter++;
                    expenseTable.setId(ExpenseRepository.counter);
                    Toast.makeText(activity, "Added Successfully", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("Amount", amountText);
                    getParentFragmentManager().setFragmentResult("dataFromAddExp", bundle);
                    MainFragment.expenseViewModel.insert(expenseTable,myRef);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = activity.getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_expense:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
