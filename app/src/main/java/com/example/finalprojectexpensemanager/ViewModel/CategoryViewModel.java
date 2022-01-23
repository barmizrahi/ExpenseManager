package com.example.finalprojectexpensemanager.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
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

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
        FoodExpenses = expenseRepository.getFoodExpenses();
        TravelExpenses = expenseRepository.getTravelExpenses();
        UtilityExpenses = expenseRepository.getUtilitiesExpenses();
        HealthExpenses = expenseRepository.getHealthExpenses();
        ShoppingExpenses = expenseRepository.getShoppingExpenses();
        OthersExpenses = expenseRepository.getOthersExpenses();
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

}
