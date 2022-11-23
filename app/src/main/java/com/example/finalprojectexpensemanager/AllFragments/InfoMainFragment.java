package com.example.finalprojectexpensemanager.AllFragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.finalprojectexpensemanager.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.IOException;
import java.io.InputStream;


public class InfoMainFragment extends Fragment {
    private View view;
    private TextView Tv_info;
    private ImageButton back_info;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info_main, container, false);
        Tv_info = view.findViewById(R.id.Tv_info);
        back_info = view.findViewById(R.id.back_info);
        Tv_info.setText(Tv_info.getText() + "");
       //active when click on Privacy_Policy
        //https://sites.google.com/view/my-expense-manager-app/%D7%91%D7%99%D7%AA
        openHtmlTextDialog(this.getActivity(),"Privacy_Policy.html");
        //active when click on Terms_of_use
        //https://sites.google.com/view/my-expnese-manager/%D7%91%D7%99%D7%AA
        openHtmlTextDialog(this.getActivity(),"Terms_of_use.html");
        getActivity().setTitle("Main Info");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentInfoMain_to_fragment_main);
            }
        });
    }

    public static void openHtmlTextDialog(Activity activity, String fileNameInAssets) {
        String str = "";
        InputStream is = null;

        try {
            is = activity.getAssets().open(fileNameInAssets);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(activity);
        materialAlertDialogBuilder.setPositiveButton("Close", null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            materialAlertDialogBuilder.setMessage(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
        } else {
            materialAlertDialogBuilder.setMessage(Html.fromHtml(str));
        }

        AlertDialog al = materialAlertDialogBuilder.show();
        TextView alertTextView = al.findViewById(android.R.id.message);
        alertTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}