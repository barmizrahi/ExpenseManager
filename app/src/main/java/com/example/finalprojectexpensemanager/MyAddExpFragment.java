package com.example.finalprojectexpensemanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.example.finalprojectexpensemanager.LoginAndSaved.LoginFragment;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MyAddExpFragment extends Fragment {
    public static final String EXTRA_ID = "id_input";
    public static final String EXTRA_TITLE = "name_input";
    public static final String EXTRA_AMOUNT = "amount_input";
    public static final String EXTRA_DATE = "date_input";
    public static final String EXTRA_DESCRIPTION = "desc_input";
    public static final String EXTRA_CATEGORY = "category_input";
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
    // private static FirebaseAuth auth;
    private Boolean isSelected = false;
    private Activity activity;
    private View view;

    //public void setActivity(AppCompatActivity activity) {
    // this.activity = activity;
    // }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        //  auth = FirebaseAuth.getInstance();
        Context context = container.getContext();
        this.activity = getActivity();
        //activity.getSupportActionBar().hide();
        name = view.findViewById(R.id.name_input);
        amount = view.findViewById(R.id.amount_input);
        date = view.findViewById(R.id.date_input);
        desc = view.findViewById(R.id.description_input);
        selectdate = view.findViewById(R.id.selectDate);
        save = view.findViewById(R.id.addButton);
        title = view.findViewById(R.id.titleID);
        chipGroup = view.findViewById(R.id.categoryChipGroup);
        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String day , month;
                                if(dayOfMonth<10){
                                    day = "0"+dayOfMonth;
                                }
                                else
                                    day = ""+(dayOfMonth);
                                if(monthOfYear<10){
                                    month = "0"+(monthOfYear+1);
                                }
                                else
                                    month = ""+(monthOfYear+1);
                                //add 0 to less then 10
                                date.setText(year+"-"+month+"-"+day);

                                //date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        //activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent data = activity.getIntent();
        if (data.hasExtra(EXTRA_ID)) {
            activity.setTitle("Edit Expense");
            title.setText("Update Expense");
            name.setText(data.getStringExtra(EXTRA_TITLE));
            amount.setText(data.getStringExtra(EXTRA_AMOUNT));
            date.setText(data.getStringExtra(EXTRA_DATE));
            desc.setText(data.getStringExtra(EXTRA_DESCRIPTION));
        } else {
            activity.setTitle("Add Expense");
            title.setText("Add Expense");
        }

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
                    //Toast.makeText(context, "" + chip.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void saveNote() {
        ExpenseRepository.counter++;
        String nameText = name.getText().toString();
        String descText = desc.getText().toString();
        String amountText = amount.getText().toString();
        String dateText = date.getText().toString();
        String categoryData = category;


        if (nameText.trim().isEmpty() || descText.trim().isEmpty() || amountText.trim().isEmpty() || dateText.trim().isEmpty() || !isSelected) {
            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        ExpenseTable expenseTable = new ExpenseTable(nameText, amountText, dateText, descText, categoryData,ExpenseRepository.counter);
        MainFragment.expenseViewModel.insert(expenseTable);
        Toast.makeText(activity, "Added Successfully", Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putString("Amount",amountText);
        getParentFragmentManager().setFragmentResult("dataFromAddExp",bundle);
        Navigation.findNavController(view).navigate(R.id.action_fragment_add_expense_to_fragment_main);

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
