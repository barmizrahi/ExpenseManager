package com.example.finalprojectexpensemanager.Repository;

import android.app.Application;
import androidx.annotation.NonNull;
import java.util.List;
import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExpenseRepository {
    public final String EXPENSE_TABLE_APP = "myExpenseApp";
    public final String EXPENSE_TABLE = "expenses";
    public final String EXPENSES_COUNTER = "counter";
    public static int counter;
    public static String userName;
    public static String coin;
    public static int reset;
    private List<ExpenseTable> foodExpenses;
    private List<ExpenseTable> travelExpenses;
    private List<ExpenseTable> utilitiesExpenses;
    private List<ExpenseTable> healthExpenses;
    private List<ExpenseTable> shoppingExpenses;
    private List<ExpenseTable> othersExpenses;
    public static List<ExpenseTable> allExpenses;

    public void setAllExpenses(List<ExpenseTable> allExpenses) {
        this.allExpenses = allExpenses;
    }

    public ExpenseRepository(Application application) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(EXPENSE_TABLE_APP);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getKey().equals(userName)) {//then enter to the user now
                        for (DataSnapshot child1 : child.getChildren()) {//expneses
                            for (DataSnapshot child2 : child1.getChildren()) {
                                String key = child2.getKey();
                                myswichCase(child2, key);
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

    private void myswichCase(DataSnapshot child2, String key) {
        for (DataSnapshot child3 : child2.getChildren()) {//items of type
            insertExpensesIntoList(key, child3);
        }
    }

    private void insertExpensesIntoList(String key, DataSnapshot child3) {
        switch (key) {
            case "Food":
                try {
                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                    foodExpenses.add(expenseTable);
                } catch (Exception ex) {
                }

                break;
            case "Travel":
                try {
                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                    travelExpenses.add(expenseTable);
                } catch (Exception ex) {
                }

                break;
            case "Utilities":
                try {
                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                    utilitiesExpenses.add(expenseTable);
                } catch (Exception ex) {
                }

                break;
            case "Health":
                try {
                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                    healthExpenses.add(expenseTable);
                } catch (Exception ex) {
                }

                break;
            case "Shopping":
                try {
                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                    shoppingExpenses.add(expenseTable);
                } catch (Exception ex) {
                }

                break;
            case "Others":
                try {
                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                    othersExpenses.add(expenseTable);
                } catch (Exception ex) {
                }

                break;
        }
    }

    public void add(ExpenseTable expense, DatabaseReference myRef) {
        myRef.child(userName).child(EXPENSE_TABLE).child(expense.getCategory()).child("" + counter).setValue(expense);
        myRef.child(userName).child(EXPENSE_TABLE).child(EXPENSES_COUNTER).setValue("" + counter);
    }

    public void update(ExpenseTable expense) {

    }


    public void deleteAllNotes() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(EXPENSE_TABLE_APP);
        myRef.child(userName).child(EXPENSE_TABLE).child("Food").removeValue();
        myRef.child(userName).child(EXPENSE_TABLE).child("Travel").removeValue();
        myRef.child(userName).child(EXPENSE_TABLE).child("Utilities").removeValue();
        myRef.child(userName).child(EXPENSE_TABLE).child("Health").removeValue();
        myRef.child(userName).child(EXPENSE_TABLE).child("Shopping").removeValue();
        myRef.child(userName).child(EXPENSE_TABLE).child("Others").removeValue();
        allExpenses.clear();
        myRef.child(userName).child(EXPENSE_TABLE).child(EXPENSES_COUNTER).setValue("" + 0);
        counter = 0;
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
        allExpenses.remove(adapterPosition);
        return e;
    }

}
