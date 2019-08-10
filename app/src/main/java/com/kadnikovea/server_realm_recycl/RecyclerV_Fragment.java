package com.kadnikovea.server_realm_recycl;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerV_Fragment extends Fragment /*implements MyAdapter.ClickRecyclerListener*/{
    List<String>titles=new ArrayList<>();
    RecyclerView recyclerView;
    MyAdapter myAdapter;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_recycler_v,  null);
        /*titles.add("maxim");
        //recyclerView
        recyclerView=v.findViewById(R.id.title_recyclerview_id);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        myAdapter=new MyAdapter(titles,getContext(),this);
        //


        //
        recyclerView.setAdapter(myAdapter);*/



        // recyclerView.setItemAnimator(new DefaultItemAnimator());


        return v;
    }


    /*@Override
    public void addViewListener(String name) {
        Bundle bundle=new Bundle();
        bundle.putString("name",name);

        System.out.println("name in booktext "+name);


    }*/
}
