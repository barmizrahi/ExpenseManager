package com.example.finalprojectexpensemanager.AllFragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.finalprojectexpensemanager.R;

public class InfoLogInFragment extends Fragment {
    private View view;
    private TextView Tv_info;
    private ImageButton back_info;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info_log_in, container, false);
        Tv_info = view.findViewById(R.id.Tv_info);
        getActivity().setTitle("Log In Info");
        back_info = view.findViewById(R.id.back_info);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentInfoLogIn_to_loginFragment);
            }
        });
    }
}
