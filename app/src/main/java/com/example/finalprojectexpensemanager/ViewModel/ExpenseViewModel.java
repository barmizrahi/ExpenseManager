package com.example.finalprojectexpensemanager.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseRepository expenseRepository;
    private LiveData<List<ExpenseTable>> getAllExpense;
    private List<ExpenseTable> AllExpense;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
        //getAllExpense = expenseRepository.getAllExpense();
        //expenseRepository.insertIntoAllExp();
        AllExpense = expenseRepository.getAllExpenses();
    }
    public void insert(ExpenseTable expense) {
        expenseRepository.add(expense);
    }
    public void update(ExpenseTable expense) {
        expenseRepository.update(expense);
    }
   /* public void delete(ExpenseTable expense) {
        expenseRepository.delete(expense);
    }

    */
    public void deleteAllExpense() {
        expenseRepository.deleteAllNotes();
    }
   /*
    public LiveData<List<ExpenseTable>> getAllExpense(){
        return getAllExpense;
    }
    */
    public List<ExpenseTable> getAllExpense(){
        //expenseRepository.insertIntoAllExp();
        AllExpense = expenseRepository.getAllExpenses();
        return AllExpense;
    }

    public void setAllExpenses(List<ExpenseTable> finalE) {
        expenseRepository.setAllExpenses(finalE);
    }
}
