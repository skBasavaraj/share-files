package com.example.filesharing.adapters;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.filesharing.model.ListItem;
import com.example.filesharing.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class listAdapter extends AbstractItem<listAdapter,listAdapter.ViewHolder> {
public ListItem listItem;
 public static ViewHolder abc;
 public static listAdapter instance;
    public listAdapter withadapter(ListItem listItem) {
        this.listItem = listItem;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.fastadapter_sampleitem_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout3;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
         abc = holder;
        String str=listItem.getLname();

        int index=str.lastIndexOf('/');
        String last=str.substring(index+1);

        holder.name.setText(last);
         Glide.with(holder.pic).load(listItem.getLpath()).placeholder(R.drawable.ic_baseline_image).thumbnail(0.33f)
                .centerCrop().into(holder.pic);
//          if(setC.equals("check")) {
//            holder.icon.setImageResource(R.drawable.ic_chek);
//        }else{
//            holder.icon.setImageResource(R.drawable.ic_check_circle_24);
//
//        }

    }

    class ViewHolder extends RecyclerView.ViewHolder{
    TextView name;
    ImageView pic,icon;
     public ViewHolder(@NonNull View itemView) {
         super(itemView);
         name=itemView.findViewById(R.id.lname);
         pic=itemView.findViewById(R.id.lpic);
         icon=itemView.findViewById(R.id.Icon);
     }
 }
 public static listAdapter  Icon(String check){
     String set = "check";
     if(set.equals(check)) {
           abc.icon.setImageResource(R.drawable.ic_chek);
        }
        return instance;
 }
}
