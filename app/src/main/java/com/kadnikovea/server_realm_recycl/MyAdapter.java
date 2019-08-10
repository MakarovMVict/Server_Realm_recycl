package com.kadnikovea.server_realm_recycl;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<String> mtitles;
    Context context;
    private ClickRecyclerListener clickRecyclerListener;

    public MyAdapter(List<String> mtitles, Context context,ClickRecyclerListener clickRecyclerListener) {
        this.mtitles = mtitles;
        this.context = context;
        this.clickRecyclerListener =clickRecyclerListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_layout, parent, false);


        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //if(!mtitles.isEmpty()){
            holder.nameText.setText(mtitles.get(position));
            holder.nameText.setOnClickListener(view -> {
                holder.nameText.setBackgroundColor(Color.parseColor("#AD4242"));
                clickRecyclerListener.addViewListener(mtitles.get(position));
            });


        //}
    }

    @Override
    public int getItemCount() {
        return mtitles.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText=itemView.findViewById(R.id.name_View_holder_id);
        }
    }





    interface ClickRecyclerListener{
        //String selectedName = null;
        public void addViewListener(String name);
    }

}
