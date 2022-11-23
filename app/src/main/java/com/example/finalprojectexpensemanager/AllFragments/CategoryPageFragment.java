package com.example.finalprojectexpensemanager.AllFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.finalprojectexpensemanager.R;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class CategoryPageFragment extends Fragment {
    private CardView food_Cat, travel_cat, utilities_cat, health_cat, shopping_cat, others_cat;
    private Context context;
    private View view;
    private ImageButton categoty_back_btn;
    private Activity activity;
    private GridLayout mainGrid;
    private boolean BuyCategoty;
    private MaterialDialog mDidNotPurchase;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        this.view = inflater.inflate(R.layout.fragment_category_page, container, false);
        init(view);
        activity.setTitle("Select Category");
        categoty_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_category_page_to_fragment_main);
            }
        });
        if(!BuyCategoty) {
            mainGrid.setVisibility(View.GONE);
            initmDidNotPurchase();
            mDidNotPurchase.show();
        }
        food_Cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate("Food");
            }
        });
        travel_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate("Travel");
            }
        });
        health_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate("Health");
            }
        });
        utilities_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate("Utilities");
            }
        });
        shopping_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate("Shopping");
            }
        });
        others_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate("Others");
            }
        });
        return view;
    }

    private void initmDidNotPurchase() {
        mDidNotPurchase = new MaterialDialog.Builder(activity)
                .setTitle("Error")
                .setMessage("In Order To Watch Expenses By Category You Will Need To Purchase The Pro Version")
                .setCancelable(false)
                .setPositiveButton("Ok", new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        Navigation.findNavController(view).navigate(R.id.action_fragment_category_page_to_fragment_main);
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


    private void navigate(String val) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.EXTRA_CATEGORY), val);
        getParentFragmentManager().setFragmentResult("dataFromCatagory", bundle);
        Navigation.findNavController(view).navigate(R.id.action_fragment_category_page_to_fragmentMyCategotyDetalis);
    }

    private void init(View view) {
        categoty_back_btn = view.findViewById(R.id.categoty_back_btn);
        food_Cat = view.findViewById(R.id.food_cat);
        travel_cat = view.findViewById(R.id.travel_cat);
        utilities_cat = view.findViewById(R.id.Utilities_cat);
        health_cat = view.findViewById(R.id.Health_cat);
        shopping_cat = view.findViewById(R.id.Shopping_cat);
        others_cat = view.findViewById(R.id.Others_cat);
        activity = getActivity();
        mainGrid = view.findViewById(R.id.mainGrid);
    }

}