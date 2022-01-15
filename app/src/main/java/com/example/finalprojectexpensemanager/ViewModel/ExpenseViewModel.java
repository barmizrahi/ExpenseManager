package com.example.finalprojectexpensemanager.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import java.util.List;
import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.firebase.database.DatabaseReference;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseRepository expenseRepository;

    private List<ExpenseTable> AllExpense;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);

        AllExpense = expenseRepository.getAllExpenses();
    }
    public void insert(ExpenseTable expense, DatabaseReference myRef) {
        expenseRepository.add(expense,myRef);
    }
    public void update(ExpenseTable expense) {
        expenseRepository.update(expense);
    }

    public void deleteAllExpense() {
        expenseRepository.deleteAllNotes();
    }

    public List<ExpenseTable> getAllExpense(){
        AllExpense = expenseRepository.getAllExpenses();
        return AllExpense;
    }

    public void setAllExpenses(List<ExpenseTable> finalE) {
        expenseRepository.setAllExpenses(finalE);
    }
}
