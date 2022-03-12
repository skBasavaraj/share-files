package com.example.filesharing.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
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
import com.example.filesharing.Model.App;
import com.example.filesharing.R;
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialize.util.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AppAdapter extends AbstractItem<AppAdapter,AppAdapter.ViewHolder> {
  public  App app;

    public AppAdapter withApp(App app){
        this.app=app;
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
        Context context = holder.itemView.getContext();

        holder.name.setText(app.getAname());
        holder.name.setMarqueeRepeatLimit(3);
        holder.name.setSelected(true);
        holder.name.setSingleLine(true);
         Glide.with(context).load(app.getIcon()) .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(125,80);
                 layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
                holder.Apic.setLayoutParams(layoutParams);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(125,120);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                holder.Apic.setLayoutParams(layoutParams);
                holder.Apic.setScaleType(ImageView.ScaleType.FIT_XY);
                return false;
            }
        }).apply(new RequestOptions().placeholder(R.drawable.ic_baseline_movie_24).dontAnimate()).into(holder.Apic);
         UIUtils.setBackground(holder.itemView,  FastAdapterUIUtils.getSelectableBackground(context, Color.rgb(102, 140, 255), true));

    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView Apic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Apic=itemView.findViewById(R.id.audioPic);
            name=itemView.findViewById(R.id.audioName);

        }
    }
}
