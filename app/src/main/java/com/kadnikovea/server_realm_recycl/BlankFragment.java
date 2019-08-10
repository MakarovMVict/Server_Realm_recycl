package com.kadnikovea.server_realm_recycl;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.net.URI;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    ImageView imageView;


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_blank, container, false);
        imageView = v.findViewById(R.id.imagr_empty_id);
        imageView.setImageResource(R.drawable.ic_launcher_foreground);
        // Inflate the layout for this fragment
        return v;
    }

}
