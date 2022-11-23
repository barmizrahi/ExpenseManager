package com.example.finalprojectexpensemanager.AllFragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprojectexpensemanager.MSPV3;
import com.example.finalprojectexpensemanager.MainActivity;
import com.example.finalprojectexpensemanager.R;
import com.example.finalprojectexpensemanager.Repository.ExpenseRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.example.finalprojectexpensemanager.Adapters.ExpenseAdapter;
import com.example.finalprojectexpensemanager.Entity.ExpenseTable;
import com.example.finalprojectexpensemanager.ViewModel.ExpenseViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class MainFragment extends Fragment {
    private Button category_button;
    public static ExpenseViewModel expenseViewModel;
    private MaterialDialog mDialogDeleteAll;
    private boolean boolPayForExport=true;
    private TextView deleteAll;
    private TextView no_expense;
    private TextView ExportData;
    private TextView viewAllTransaction;
    private ImageView img_refresh_balance;
    private LinearLayoutManager HorizontalLayout;
    private Activity activity;
    private TextView amount_remaining2;
    private TextView name_text;
    private FloatingActionButton buttonAddNote;
    private View view;
    private MaterialDialog mDialogRefresh;
    public static List<ExpenseTable> finalE;
    private String userName;
    private RecyclerView recyclerView;
    private ImageView Iv_exit;
    private ImageView info;
    private Sheet sheet = null;
    private Cell cell = null;
    private File filePath;
    private Context context;
    private final static String EXCEL_SHEET_NAME = "Expenses";
    private final int REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 902;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MSPV3.getMe().putString(getString(R.string.UserName), ExpenseRepository.userName);
        MSPV3.getMe().putString(getString(R.string.LogInBolean), "true");
        context = container.getContext();
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        final ExpenseAdapter adapter = new ExpenseAdapter();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(getString(R.string.EXPENSE_TABLE_APP));
        //insert into name_text the name of user
        editName(myRef);
        //insert into amount_remaining2 a value
        editBudget(myRef);
        //insert into recyclerView some expneses
        editLastItems(myRef, recyclerView, adapter);
        recyclerView.setHasFixedSize(true);
        HorizontalLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        //add an expenses
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_main_to_fragment_add_expense);
            }
        });
        //to show all expneses
        viewAllTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_main_to_fragmentViewAllExpense);
            }
        });
        ExportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExportDataToExcel();
            }
        });
        //To delete all Expenses
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogDeleteAll.show();
            }
        });
        //Go to category page
        category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_main_to_fragment_category_page);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_main_to_fragmentInfoMain);
            }
        });

        initMDialog(recyclerView, adapter);
        initMDialogRefresh(myRef);
        //to set the balance to default
        img_refresh_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogRefresh.show();
            }
        });
        //exit from the account
        Iv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MSPV3.getMe().putString("Start", "false");
                MSPV3.getMe().putString(getString(R.string.UserName), "");
                MSPV3.getMe().putString(getString(R.string.LogInBolean), "false");
                ExpenseRepository.counter = 0;
                Navigation.findNavController(view).navigate(R.id.action_fragment_main_to_firebaseGoogleLoginJavaFragment);
            }
        });
        if (MSPV3.getMe().getString("Start", "").equals("true")) {
            myRef.child(userName).child(getString(R.string.RESET)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ExpenseRepository.reset = Integer.parseInt(snapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            MSPV3.getMe().putString("Start", "false");
        }

        return view;
    }
    class SortByDate implements Comparator<ExpenseTable> {
        @Override
        public int compare(ExpenseTable a, ExpenseTable b) {
            return b.getDate().compareTo(a.getDate());
        }
    }

    private void ExportDataToExcel() {
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE); //request for contact
        Collections.sort(finalE, new SortByDate()); //sort by Date
        Workbook workbook = new HSSFWorkbook();
        sheet = workbook.createSheet(EXCEL_SHEET_NAME);
        Row row = sheet.createRow(0);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);

// Creating a cell and assigning it to a row
// Setting Value and Style to the cell
        cell = row.createCell(0);
        cell.setCellValue("Date");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("Expense Name");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(2);
        cell.setCellValue("Amount");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(3);
        cell.setCellValue("Category");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(4);
        cell.setCellValue("Description");
        cell.setCellStyle(cellStyle);

        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setWrapText(true);
        cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle1.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
        for(int i=0;i<finalE.size();i++){
            row = sheet.createRow(i+1);
            cell = row.createCell(0);
            cell.setCellValue(finalE.get(i).getDate());
            cell.setCellStyle(cellStyle1);

            cell = row.createCell(1);
            cell.setCellValue(finalE.get(i).getExpenseName());
            cell.setCellStyle(cellStyle1);

            cell = row.createCell(2);
            cell.setCellValue(finalE.get(i).getAmount());
            cell.setCellStyle(cellStyle1);

            cell = row.createCell(3);
            cell.setCellValue(finalE.get(i).getCategory());
            cell.setCellStyle(cellStyle1);

            cell = row.createCell(4);
            cell.setCellValue(finalE.get(i).getDescription());
            cell.setCellStyle(cellStyle1);
        }
        //java.io.IOException: Operation not permitted
        filePath = (MainActivity.path);
        try{
            /*
            if(!filePath.exists()){
                filePath.createNewFile();
            }
*/

            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            int duration = Toast.LENGTH_SHORT;
            CharSequence text = "Data Export Was Performed Successfully";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            if(fos!=null){
                fos.flush();
                fos.close();
            }
        }
        catch (Exception e){
            int duration = Toast.LENGTH_SHORT;
            CharSequence text = "Data Export Was Not Performed Successfully";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  Toast.makeText(, "Contacts ok", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
            }
    private void initMDialogRefresh(DatabaseReference myRef) {
        mDialogRefresh = new MaterialDialog.Builder(activity)
                .setTitle("Refresh?")
                .setMessage("Are You Sure You Want To Refresh Your Balance?")
                .setCancelable(false)
                .setPositiveButton("Refresh", R.drawable.ic_delete, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        myRef.child(userName).child(getString(R.string.RESET)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String helper = snapshot.getValue(String.class);
                                myRef.child(userName).child(getString(R.string.BUDGETDB)).setValue("" + helper);
                                MSPV3.getMe().putString("MPAmount", helper);
                                ExpenseRepository.amount = Integer.parseInt(helper);
                                if (ExpenseRepository.coin.equals("null")) {
                                    amount_remaining2.setText("" + helper);
                                } else {
                                    amount_remaining2.setText("" + helper + ExpenseRepository.coin);
                                }
                                dialogInterface.dismiss();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();
    }

    private void initMDialog(RecyclerView recyclerView, ExpenseAdapter adapter) {
        mDialogDeleteAll = new MaterialDialog.Builder(activity)
                .setTitle("Delete?")
                .setMessage("Are You Sure You Want To Delete All The Expenses?")
                .setCancelable(false)
                .setPositiveButton("Delete", R.drawable.ic_delete, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        expenseViewModel.deleteAllExpense();
                        adapter.setNotes(expenseViewModel.getAllExpense());
                        recyclerView.setAdapter(adapter);
                        dialogInterface.dismiss();
                        no_expense.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        deleteAll.setVisibility(View.INVISIBLE);
                     //   ExportData.setVisibility(View.INVISIBLE);
                        viewAllTransaction.setVisibility(View.INVISIBLE);
                    }
                })
                .setNegativeButton("Cancel", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();
    }

    private void editLastItems(DatabaseReference myRef, RecyclerView recyclerView, ExpenseAdapter adapter) {
        finalE = new ArrayList<>();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (child.getKey().equals(userName)) {//then enter to the user now
                                for (DataSnapshot child1 : child.getChildren()) {//expneses
                                    if (child1.getKey().equals(getString(R.string.EXPENSE_TABLE))) {
                                        for (DataSnapshot child2 : child1.getChildren()) {
                                            for (DataSnapshot child3 : child2.getChildren()) {//items of type
                                                try {
                                                    ExpenseTable expenseTable = child3.getValue(ExpenseTable.class);
                                                    finalE.add(expenseTable);
                                                } catch (Exception ex) {
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        showOrNot(finalE, adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void showOrNot(List<ExpenseTable> finalE, ExpenseAdapter adapter) {
        if (finalE.isEmpty()) {
            no_expense.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            deleteAll.setVisibility(View.INVISIBLE);
           //
            viewAllTransaction.setVisibility(View.INVISIBLE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            deleteAll.setVisibility(View.VISIBLE);
          //
            no_expense.setVisibility(View.INVISIBLE);
            viewAllTransaction.setVisibility(View.VISIBLE);
        }

        if(boolPayForExport)
            ExportData.setVisibility(View.VISIBLE);
        else
            ExportData.setVisibility(View.INVISIBLE);
        expenseViewModel.setAllExpenses(finalE);
        adapter.setNotes(expenseViewModel.getAllExpense());
        recyclerView.setAdapter(adapter);
    }

    private void editBudget(DatabaseReference myRef) {
        String helper = MSPV3.getMe().getString("MPAmount", "");
        int y;
        try {
            y = Integer.parseInt(MSPV3.getMe().getString("newAmount", ""));
        } catch (Exception e) {
            y = 0;
        }
        MSPV3.getMe().putString("newAmount", "0");
        int x = Integer.parseInt(helper) - y;
        if(x<0){
            x=0;
        }
        else if(x>ExpenseRepository.reset){
            x = ExpenseRepository.reset;
        }
        myRef.child(userName).child(getString(R.string.BUDGETDB)).setValue("" + x);
        MSPV3.getMe().putString("MPAmount", "" + x);
        ExpenseRepository.amount = x;
        amount_remaining2.setText("" + x + ExpenseRepository.coin);
    }

    private void editName(DatabaseReference myRef) {
        myRef.child(userName).child(getString(R.string.NAMEDB)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name_text.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initView(View view) {
        activity = this.getActivity();
        activity.setTitle("Expense Manager");
        img_refresh_balance = view.findViewById(R.id.img_refresh_balance);
        buttonAddNote = view.findViewById(R.id.button_add_note);
        deleteAll = view.findViewById(R.id.deleteAllExpense);
        Iv_exit = view.findViewById(R.id.Iv_exit);
        no_expense = view.findViewById(R.id.no_expense);
        info = view.findViewById(R.id.info);
        ExportData = view.findViewById(R.id.ExportData);
        viewAllTransaction = view.findViewById(R.id.viewAllTransaction);
        category_button = view.findViewById(R.id.category_button);
        amount_remaining2 = view.findViewById(R.id.amount_remaining2);
        name_text = view.findViewById(R.id.name_text);
        userName = ExpenseRepository.userName;
        recyclerView = view.findViewById(R.id.recycler_view);
        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = activity.getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                expenseViewModel.deleteAllExpense();
                Toast.makeText(activity, "All Expenses Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void callParentMethod() {
        activity.onBackPressed();
    }

}
