package com.example.finalprojectexpensemanager.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import com.example.finalprojectexpensemanager.Dao.ExpenseDao;;
import com.example.finalprojectexpensemanager.Database.ExpenseDatabase;
import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExpenseRepository {
    public static final String EXPENSE_TABLE_APP = "myExpenseApp";
    public static final String EXPENSE_TABLE = "expenses";
    public static final String EXPENSES_COUNTER = "counter";
    public static int counter;
    public static String userName;
    private ExpenseDao expenseDao;
    private List<ExpenseTable> foodExpenses ;
    private List<ExpenseTable> travelExpenses;
    private List<ExpenseTable> utilitiesExpenses;
    private List<ExpenseTable> healthExpenses ;
    private List<ExpenseTable> shoppingExpenses ;
    private List<ExpenseTable> othersExpenses ;
    public static List<ExpenseTable> allExpenses;

public void setAllExpenses(List<ExpenseTable> allExpenses){
    this.allExpenses = allExpenses;
}
    public ExpenseRepository(Application application) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(EXPENSE_TABLE_APP);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //LiveData<List<ExpenseTable>> ExpenseTable = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getKey().equals(userName)) {//then enter to the user now
                        for (DataSnapshot child1 : child.getChildren()) {//expneses
                            for (DataSnapshot child2 : child1.getChildren()) {
                                String key = child2.getKey();
                                myswichCase(child2,key);
                                }
                            }
                        }
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        ExpenseDatabase expenseDatabase = ExpenseDatabase.getInstance(application);
        expenseDao = expenseDatabase.expenseDao();
    }

    private void myswichCase(DataSnapshot child2, String key) {
        switch (key) {
            case "Food":
                for (DataSnapshot child3 : child2.getChildren()) {//items of type
                    try {
                        ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                        foodExpenses.add(expenseTable);
                        //allExpenses.add(expenseTable);
                    } catch (Exception ex) {
                    }
                }
                break;
            case "Travel":
                for (DataSnapshot child3 : child2.getChildren()) {//items of type
                    try {
                        ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                        travelExpenses.add(expenseTable);
                        //allExpenses.add(expenseTable);
                    } catch (Exception ex) {
                    }
                }
                break;
            case "Utilities":
                for (DataSnapshot child3 : child2.getChildren()) {//items of type
                    try {
                        ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                        utilitiesExpenses.add(expenseTable);
                        // allExpenses.add(expenseTable);
                    } catch (Exception ex) {
                    }
                }
                break;
            case "Health":
                for (DataSnapshot child3 : child2.getChildren()) {//items of type
                    try {
                        ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                        healthExpenses.add(expenseTable);
                        //allExpenses.add(expenseTable);
                    } catch (Exception ex) {
                    }
                }
                break;
            case "Shopping":
                for (DataSnapshot child3 : child2.getChildren()) {//items of type
                    try {
                        ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                        shoppingExpenses.add(expenseTable);
                        //allExpenses.add(expenseTable);
                    } catch (Exception ex) {
                    }
                }
                break;
            case "Others":
                for (DataSnapshot child3 : child2.getChildren()) {//items of type
                    try {
                        ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                        othersExpenses.add(expenseTable);
                        //allExpenses.add(expenseTable);
                    } catch (Exception ex) {
                    }
                }
        }
    }

    public void add(ExpenseTable expense) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(EXPENSE_TABLE_APP);
        myRef.child(userName).child(EXPENSE_TABLE).child(expense.getCategory()).child("" + counter).setValue(expense);
        myRef.child(userName).child(EXPENSE_TABLE).child(EXPENSES_COUNTER).setValue("" + counter);

    }

    public void update(ExpenseTable expense) {

        //new UpdateNoteAsyncTask(expenseDao).execute(expense);
    }


    public void deleteAllNotes() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(EXPENSE_TABLE_APP);
        myRef.child(userName).child(EXPENSE_TABLE).child("Food").removeValue();
        //foodExpenses.clear();
        myRef.child(userName).child(EXPENSE_TABLE).child("Travel").removeValue();
        //travelExpenses.clear();
        myRef.child(userName).child(EXPENSE_TABLE).child("Utilities").removeValue();
       // utilitiesExpenses.clear();
        myRef.child(userName).child(EXPENSE_TABLE).child("Health").removeValue();
        //healthExpenses.clear();
        myRef.child(userName).child(EXPENSE_TABLE).child("Shopping").removeValue();
        //shoppingExpenses.clear();
        myRef.child(userName).child(EXPENSE_TABLE).child("Others").removeValue();
        //othersExpenses.clear();
        //new DeleteAllNotesAsyncTask(expenseDao).execute();
        allExpenses.clear();
        myRef.child(userName).child(EXPENSE_TABLE).child(EXPENSES_COUNTER).setValue(""+0);
        counter=0;
    }

    public List<ExpenseTable> getFoodExpenses() {
        return foodExpenses;
    }

    public List<ExpenseTable> getTravelExpenses() {
        return travelExpenses;
    }

    public List<ExpenseTable> getUtilitiesExpenses() {
        return utilitiesExpenses;
    }

    public List<ExpenseTable> getHealthExpenses() {
        return healthExpenses;
    }

    public List<ExpenseTable> getShoppingExpenses() {
        return shoppingExpenses;
    }

    public List<ExpenseTable> getOthersExpenses() {
        return othersExpenses;
    }


    public static List<ExpenseTable> getAllExpenses() {
        return allExpenses;
    }

    public ExpenseTable myDelete(int adapterPosition) {
        ExpenseTable e = allExpenses.get(adapterPosition);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(EXPENSE_TABLE_APP);
        myRef.child(userName).child(EXPENSE_TABLE).child(e.getCategory()).child("" + e.getId()).removeValue();
        //counter--;
        allExpenses.remove(adapterPosition);
        return e;
    }

/*
    public static class UpdateNoteAsyncTask extends AsyncTask<ExpenseTable, Void, Void> {
        private ExpenseDao expenseDao;

        private UpdateNoteAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(ExpenseTable... expenses) {
            expenseDao.updateExpense(expenses[0]);
            return null;
        }
    }

 */



}
