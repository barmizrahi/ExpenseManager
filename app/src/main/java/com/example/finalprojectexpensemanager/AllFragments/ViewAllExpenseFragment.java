package com.example.finalprojectexpensemanager.AllFragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.finalprojectexpensemanager.Adapters.AllTransactionAdapter;
import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.example.finalprojectexpensemanager.MSPV3;
import com.example.finalprojectexpensemanager.R;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.example.finalprojectexpensemanager.ViewModel.AllExpenseViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class ViewAllExpenseFragment extends Fragment {
    private RecyclerView expenseView;
    private AllExpenseViewModel expenseViewModel;
    private ImageButton back;
    private Activity activity;
    private List<ExpenseTable> expenseTables = new ArrayList<>();
    private MaterialDialog mDialog;
    private RecyclerView.ViewHolder viewHolder1;
    private Context context;
    private final AllTransactionAdapter adapter = new AllTransactionAdapter();
    private PieChart pieChart;
    private View view;
    private ArrayList<PieEntry> entries;
    private List<ExpenseTable> ExpenseToPie;

    class SortByDate implements Comparator<ExpenseTable> {
        @Override
        public int compare(ExpenseTable a, ExpenseTable b) {
            return b.getDate().compareTo(a.getDate());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_all_expense, container, false);
        context = container.getContext();
        initVeiw();
        activity = getActivity();
        activity.setTitle("All Expenses");
        addExpnesesToView();
        setUpPieChart();
        loadPieChartData();
        mDialog = new MaterialDialog.Builder(activity)
                .setTitle("Delete?")
                .setMessage("Do You Want A Refund?")
                .setCancelable(false)
                .setPositiveButton("Refund", R.drawable.ic_delete, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        refundAction(dialogInterface);
                        loadPieChartData();
                    }
                })
                .setNegativeButton("No Refund", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        expenseViewModel.myDelete(viewHolder1.getAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder1.getAdapterPosition());
                        adapter.notifyItemRangeChanged(viewHolder1.getAdapterPosition(), ExpenseRepository.allExpenses.size());
                        viewHolder1.itemView.setVisibility(View.GONE);
                        dialogInterface.dismiss();
                        loadPieChartData();
                    }
                })
                .build();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                viewHolder1 = viewHolder;
                if (direction == ItemTouchHelper.RIGHT) {
                    mDialog.show();
                } else if (direction == ItemTouchHelper.LEFT) {
                    EditExpense();
                }

            }


        }).attachToRecyclerView(expenseView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentViewAllExpense_to_fragment_main);
            }
        });
        return view;
    }

    private void EditExpense() {
        ExpenseTable e = expenseViewModel.editExpense(viewHolder1.getAdapterPosition());
        adapter.notifyItemRemoved(viewHolder1.getAdapterPosition());
        adapter.notifyItemRangeChanged(viewHolder1.getAdapterPosition(), ExpenseRepository.allExpenses.size());
        viewHolder1.itemView.setVisibility(View.GONE);
        MSPV3.getMe().putString("editExp", "true");
        MSPV3.getMe().putObject("expense", e);
        Navigation.findNavController(view).navigate(R.id.action_fragmentViewAllExpense_to_fragment_add_expense);
    }

    private void setUpPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(11);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        entries = new ArrayList<>();
        ExpenseToPie = expenseViewModel.getAllExpense();
        insetExpenses(entries, ExpenseToPie);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }
        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }
        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);


    }

    private void insetExpenses(ArrayList<PieEntry> entries, List<ExpenseTable> ExpenseToPie) {
        insetNewValueToEntries(0, entries, ExpenseToPie, "Food");
        insetNewValueToEntries(0, entries, ExpenseToPie, "Travel");
        insetNewValueToEntries(0, entries, ExpenseToPie, "Utilities");
        insetNewValueToEntries(0, entries, ExpenseToPie, "Health");
        insetNewValueToEntries(0, entries, ExpenseToPie, "Shopping");
        insetNewValueToEntries(0, entries, ExpenseToPie, "Others");
    }

    private void insetNewValueToEntries(int counter, ArrayList<PieEntry> entries, List<ExpenseTable> ExpenseToPie, String category) {
        int j;
        for (j = 0; j < ExpenseToPie.size(); j++) {
            if (ExpenseToPie.get(j).getCategory().equals(category)) {
                counter = counter + Integer.parseInt(ExpenseToPie.get(j).getAmount());
            }
        }
        if (counter != 0) {
            entries.add(new PieEntry(counter, category));
        }
    }

    private void refundAction(DialogInterface dialogInterface) {
        ExpenseTable e = expenseViewModel.myDelete(viewHolder1.getAdapterPosition());
        adapter.notifyItemRemoved(viewHolder1.getAdapterPosition());
        adapter.notifyItemRangeChanged(viewHolder1.getAdapterPosition(), ExpenseRepository.allExpenses.size());
        viewHolder1.itemView.setVisibility(View.GONE);
        dialogInterface.dismiss();
        MSPV3.getMe().putString("newAmount", "-" + e.getAmount());
    }

    private void addExpnesesToView() {
        expenseView.setLayoutManager(new LinearLayoutManager(context));
        expenseView.setAdapter(adapter);
        expenseViewModel = new ViewModelProvider(this).get(AllExpenseViewModel.class);
        expenseTables = expenseViewModel.getAllExpense();
        if (expenseTables != null) {
            Collections.sort(expenseTables, new SortByDate());
            adapter.setNotes(expenseTables);
        }
    }

    private void initVeiw() {
        expenseView = view.findViewById(R.id.recycler_view2);
        back = view.findViewById(R.id.back_all);
        pieChart = view.findViewById(R.id.pie_chart);
    }
}


