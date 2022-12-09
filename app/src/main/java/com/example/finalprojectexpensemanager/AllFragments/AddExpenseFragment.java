package com.example.finalprojectexpensemanager.AllFragments;

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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.example.finalprojectexpensemanager.MSPV3;
import com.example.finalprojectexpensemanager.R;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class AddExpenseFragment extends Fragment {
    private TextInputEditText name;
    private TextInputEditText amount;
    private TextInputLayout amount_input_text;
    private MaterialTextView date;
    private TextInputEditText desc;
    private MaterialTextView selectdate;
    private androidx.constraintlayout.widget.ConstraintLayout AddExpAds;
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
    private final Calendar c = Calendar.getInstance();
    public static String edit;
    private int id;
    private int eAmount = 0;
    private boolean isPay = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        initView();
        setStartDate();
        if(isPay){
            AddExpAds.setVisibility(View.GONE);
        }
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
        edit = MSPV3.getMe().getString("editExp", "");
        if (edit.equals("true")) {
            EditExpense();
        }
        return view;
    }

    private void setStartDate() {
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String Sday, Smonth;
        if (day < 10) {
            Sday = "0" + day;
        } else
            Sday = "" + (day);
        if (month < 10) {
            Smonth = "0" + (month + 1);
        } else
            Smonth = "" + (month + 1);
        date.setText(year + "-" + Smonth + "-" + Sday);
    }

    private void EditExpense() {
        back_add.setVisibility(View.GONE);
        ExpenseTable e = MSPV3.getMe().getObject("expense");
        title.setText("Edit Expense");
        activity.setTitle("Edit Expense");
        name.setText(e.getExpenseName());
        amount.setText(e.getAmount());
        eAmount = Integer.parseInt(e.getAmount());
        desc.setText(e.getDescription());
        date.setText(e.getDate());
        id = e.getId();
        category = e.getCategory();
        selectCate(category);
        MSPV3.getMe().putString("editExp", "false");
    }

    private void selectCate(String category) {
        switch (category) {
            case "Food":
                chipGroup.check(R.id.Food);
                break;
            case "Shopping":
                chipGroup.check(R.id.Shopping);
                break;
            case "Utilities":
                chipGroup.check(R.id.Utilities);
                break;
            case "Travel":
                chipGroup.check(R.id.Travel);
                break;
            case "Health":
                chipGroup.check(R.id.Health);
                break;
            case "Others":
                chipGroup.check(R.id.Others);
                break;
        }
    }

    private void setDate() {
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
        activity = getActivity();
        name = view.findViewById(R.id.name_input);
        amount = view.findViewById(R.id.amount_input);
        date = view.findViewById(R.id.date_input);
        desc = view.findViewById(R.id.description_input);
        selectdate = view.findViewById(R.id.selectDate);
        save = view.findViewById(R.id.addButton);
        title = view.findViewById(R.id.titleID);
        back_add = view.findViewById(R.id.back_add);
        amount_input_text = view.findViewById(R.id.amount_input_text);
        activity.setTitle("Add Expense");
        title.setText("Add Expense");
        chipGroup = view.findViewById(R.id.categoryChipGroup);
        AddExpAds =  view.findViewById(R.id.AddExpAds);
    }

    private void saveNote() {
        String nameText = name.getText().toString();
        String descText = desc.getText().toString();
        String amountText = amount.getText().toString();
        String dateText = date.getText().toString();
        String categoryData = category;
        if (nameText.trim().isEmpty() || amountText.trim().isEmpty() || dateText.trim().isEmpty() || !isSelected) {
            Toast.makeText(getContext(), "Please Fill All The Fields", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (Integer.parseInt(amountText) < 0) {
                amount_input_text.setError("Amount Can't Be Negative");
                return;
            }
        } catch (NumberFormatException e) {
            amount_input_text.setError("Amount Can't Be Negative");
            return;
        }
        ExpenseTable expenseTable = new ExpenseTable(nameText, amountText, dateText, descText, categoryData, ExpenseRepository.counter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(getString(R.string.EXPENSE_TABLE_APP));
        tryToAdd(expenseTable, myRef, amountText);
        Navigation.findNavController(view).navigate(R.id.action_fragment_add_expense_to_fragment_main);

    }

    private void tryToAdd(ExpenseTable expenseTable, DatabaseReference myRef, String amountText) {
        MSPV3.getMe().putString("newAmount", amountText);
        int myBudget = ExpenseRepository.amount;
        if ((myBudget - Integer.parseInt(expenseTable.getAmount())) < 0) {
            Toast.makeText(activity, "Not Enough Money", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (edit.equals("false")) {
                ExpenseRepository.counter++;
                id = ExpenseRepository.counter;
                myRef.child(ExpenseRepository.userName).child(getString(R.string.EXPENSE_TABLE)).child(getString(R.string.EXPENSES_COUNTER)).setValue("" + ExpenseRepository.counter);
            } else if (edit.equals("true")) {
                editExpensesAmount(amountText, myBudget, myRef);
            }
            expenseTable.setId(id);
            Toast.makeText(activity, "Added Successfully", Toast.LENGTH_SHORT).show();
            MainFragment.expenseViewModel.insert(expenseTable, myRef);
        }
    }

    private void editExpensesAmount(String amountText, int myBudget, DatabaseReference myRef) {
        int AmountToSend = Integer.parseInt(amountText) - eAmount;// what enter now - what was
        if (AmountToSend < 0) {
            AmountToSend = AmountToSend * (-1);
            if ((myBudget + AmountToSend) >= ExpenseRepository.reset) {
                myRef.child(ExpenseRepository.userName).child(getString(R.string.BUDGETDB)).setValue("" + ExpenseRepository.reset);
                MSPV3.getMe().putString("newAmount", "0");
            } else {
                MSPV3.getMe().putString("newAmount", "-" + AmountToSend);
            }
        } else {
            MSPV3.getMe().putString("newAmount", "" + AmountToSend);
        }
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
