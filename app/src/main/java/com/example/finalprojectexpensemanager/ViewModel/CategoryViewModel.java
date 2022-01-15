package com.example.finalprojectexpensemanager.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;

public class CategoryViewModel extends AndroidViewModel {
    private ExpenseRepository expenseRepository;
    private List<ExpenseTable> FoodExpenses;
    private List<ExpenseTable> TravelExpenses;
    private List<ExpenseTable> UtilityExpenses;
    private List<ExpenseTable> HealthExpenses;
    private List<ExpenseTable> ShoppingExpenses;
    private List<ExpenseTable> OthersExpenses;
    private LiveData<List<ExpenseTable>> getFoodCategory;
    private LiveData<List<ExpenseTable>> getTravelCategory;
    private LiveData<List<ExpenseTable>> getUtilityCategory;
    private LiveData<List<ExpenseTable>> getHealthCategory;
    private LiveData<List<ExpenseTable>> getShoppingCategory;
    private LiveData<List<ExpenseTable>> getOthersCategory;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
        FoodExpenses = expenseRepository.getFoodExpenses();
        TravelExpenses = expenseRepository.getTravelExpenses();
        UtilityExpenses = expenseRepository.getUtilitiesExpenses();
        HealthExpenses = expenseRepository.getHealthExpenses();
        ShoppingExpenses = expenseRepository.getShoppingExpenses();
        OthersExpenses = expenseRepository.getOthersExpenses();
        //getFoodCategory = expenseRepository.getFoodCategory();
        //getTravelCategory = expenseRepository.getTravelCategory();
        //getUtilityCategory = expenseRepository.getUtilitiesCategory();
        //getHealthCategory = expenseRepository.getHealthCategory();
        //getShoppingCategory = expenseRepository.getShoppingCategory();
        //getOthersCategory = expenseRepository.getOthersCategory();
    }

    public List<ExpenseTable> getTravelExpenses() {
        return TravelExpenses;
    }

    public List<ExpenseTable> getUtilityExpenses() {
        return UtilityExpenses;
    }

    public List<ExpenseTable> getHealthExpenses() {
        return HealthExpenses;
    }

    public List<ExpenseTable> getShoppingExpenses() {
        return ShoppingExpenses;
    }

    public List<ExpenseTable> getOthersExpenses() {
        return OthersExpenses;
    }

    public List<ExpenseTable> getFoodExpenses(){
        return FoodExpenses;
    }
    public LiveData<List<ExpenseTable>> getFoodCategory(){
        return getFoodCategory;
    }
    public LiveData<List<ExpenseTable>> getTravelCategory(){
        return getTravelCategory;
    }
    public LiveData<List<ExpenseTable>> getUtilityCategory(){
        return getUtilityCategory;
    }
    public LiveData<List<ExpenseTable>> getHealthCategory(){
        return getHealthCategory;
    }
    public LiveData<List<ExpenseTable>> getShoppingCategory(){
        return getShoppingCategory;
    }
    public LiveData<List<ExpenseTable>> getOthersCategory(){
        return getOthersCategory;
    }
}
