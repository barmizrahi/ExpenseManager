package com.example.finalprojectexpensemanager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class CategoryPage extends Fragment {
    private CardView food_Cat, travel_cat, utilities_cat, health_cat, shopping_cat, others_cat;
    private Context context;
    private View view;
    private ImageButton categoty_back_btn;
    private Activity activity;


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
    }

}