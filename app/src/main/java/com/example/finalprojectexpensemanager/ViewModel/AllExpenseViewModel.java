package com.example.finalprojectexpensemanager.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;

public class AllExpenseViewModel extends AndroidViewModel {
    private ExpenseRepository expenseRepository;
    private LiveData<List<ExpenseTable>> getAllExpense;
    private List<ExpenseTable> AllExpense;

    public AllExpenseViewModel(@NonNull Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
        //getAllExpense = expenseRepository.getAllExpense();
        AllExpense = expenseRepository.getAllExpenses();
    }/*
    public LiveData<List<ExpenseTable>> getAllExpense(){
        return getAllExpense;
    }

    public void delete(ExpenseTable expense) {
        expenseRepository.delete(expense);
    }
    */
    public List<ExpenseTable> getAllExpense(){
        AllExpense = ExpenseRepository.getAllExpenses();
        return AllExpense;
    }

    public ExpenseTable myDelete(int adapterPosition) {
        return expenseRepository.myDelete(adapterPosition);
    }
}
