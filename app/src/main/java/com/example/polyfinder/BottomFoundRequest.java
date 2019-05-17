package com.example.polyfinder;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class BottomFoundRequest extends Fragment {

    private View view;
    private EditText mTitle;
    private EditText mDescription;
    //private Type type;
    //private Image image;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.found_fragment, container, false);

        findAllViews();

        return view;
    }

    private void findAllViews() {
        mTitle = view.findViewById(R.id.title);
        mDescription = view.findViewById(R.id.description);
    }

}
