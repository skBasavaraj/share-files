package com.example.filesharing.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
 import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
 import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.filesharing.Fragments.ActionModeHelper;
import com.example.filesharing.Model.ItemsList;
import com.example.filesharing.R;
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialize.util.UIUtils;

import java.util.List;

public class Adapter extends AbstractItem<Adapter,Adapter.ViewHolder> {
 public  ItemsList itemsList;
     public Adapter withAdapter(ItemsList itemsList) {
        this.itemsList = itemsList;
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
        return R.layout.layout;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
      ///storage/emulated/0/DCIM/Camera/VID_20220305_151741.mp4
       // Log.e("uri","uri"+itemsList.getPath());

        Context context = holder.itemView.getContext();
         holder.name.setText(itemsList.getName());
        holder.name.setMarqueeRepeatLimit(3);
        holder.name.setSelected(true);
        holder.name.setSingleLine(true);
      //  holder.size.setText(android.text.format.Formatter.formatShortFileSize(context,itemsList.getSize()));

         String path = String.valueOf(itemsList.getName());
         String match = itemsList.getDate();

         if (match.equals("video/mp4")) {
             Glide.with(holder.Apic).load(itemsList.getUri()).placeholder(R.drawable.ic_baseline_movie_24).thumbnail(0.33f)
                     .centerCrop().into(holder.Apic);
        } else if(match.equals("image/jpeg")){
             Glide.with(holder.Apic).load(itemsList.getUri()).placeholder(R.drawable.ic_baseline_image).thumbnail(0.33f)
                     .centerCrop().into(holder.Apic);

         }
        else if(match.equals("audio/mpeg")){

            Glide.with(context).load(itemsList.getUri()) .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(125,120);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    holder.Apic.setLayoutParams(layoutParams);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(180,120);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                    holder.Apic.setLayoutParams(layoutParams);
                    holder.Apic.setScaleType(ImageView.ScaleType.FIT_XY);
                    return false;
                }
            }).apply(new RequestOptions().placeholder(R.drawable.ic_muisc).dontAnimate()).into(holder.Apic);
        }else if(path.toLowerCase().endsWith(".pdf")){
            Glide.with(context).load(itemsList.getUri()) .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(125,120);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    holder.Apic.setLayoutParams(layoutParams);
                     return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(180,120);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                    holder.Apic.setLayoutParams(layoutParams);
                    holder.Apic.setScaleType(ImageView.ScaleType.FIT_XY);
                    return false;
                }
            }).apply(new RequestOptions().placeholder(R.drawable.ic_pdf_24).dontAnimate()).into(holder.Apic);
        }else if(path.toLowerCase().endsWith(".txt")){
            Glide.with(context).load(itemsList.getUri()) .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(120,120);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    holder.Apic.setLayoutParams(layoutParams);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(180,120);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                    holder.Apic.setLayoutParams(layoutParams);
                    holder.Apic.setScaleType(ImageView.ScaleType.FIT_XY);
                    return false;
                }
            }).apply(new RequestOptions().placeholder(R.drawable.ic_drive).dontAnimate()).into(holder.Apic);        }else if(path.toLowerCase().endsWith(".docx")){
        }else if(path.toLowerCase().endsWith(".doc")){
            Glide.with(context).load(itemsList.getUri()) .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(120,120);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    holder.Apic.setLayoutParams(layoutParams);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(180,120);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                    holder.Apic.setLayoutParams(layoutParams);
                    holder.Apic.setScaleType(ImageView.ScaleType.FIT_XY);
                    return false;
                }
            }).apply(new RequestOptions().placeholder(R.drawable.ic_baseline_zip).dontAnimate()).into(holder.Apic);
        }

        else if(path.toLowerCase().endsWith(".docx")){
            Glide.with(context).load(itemsList.getUri()) .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(120,120);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    holder.Apic.setLayoutParams(layoutParams);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(180,120);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                    holder.Apic.setLayoutParams(layoutParams);
                    holder.Apic.setScaleType(ImageView.ScaleType.FIT_XY);
                    return false;
                }
            }).apply(new RequestOptions().placeholder(R.drawable.ic_drive).dontAnimate()).into(holder.Apic);
        }

        UIUtils.setBackground(holder.itemView,  FastAdapterUIUtils.getSelectableBackground(context, Color.rgb(102, 140, 255), true));
       }


       class ViewHolder extends RecyclerView.ViewHolder {
        public  View v;

         TextView name;
        ImageView Apic;

        public ViewHolder(View itemView) {
            super(itemView);
            Apic = itemView.findViewById(R.id.audioPic);
            name = itemView.findViewById(R.id.audioName);
           this.v=itemView;

        }

    }
}