package com.example.filesharing.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Handler;
 import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filesharing.model.ItemList;
import com.example.filesharing.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.xiaochen.progressroundbutton.AnimDownloadProgressButton;

import java.util.List;


public class sendReceive extends AbstractItem<sendReceive, sendReceive.ViewHolder> {

    public ItemList itemLists;
    public Handler hdlr = new Handler();
    public float i;
    public Context ctx;
      public sendReceive(ItemList itemLists ,Context contxt) {
        this.itemLists =  itemLists;
        this.ctx=contxt;
     }



    @NonNull
    @Override
    public  ViewHolder getViewHolder(View v) {
        return new  ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.fastadapter_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout2;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        // holder.file_title.setText(itemLists.getName());
        holder.mAnimDownloadProgressButton.setTextSize(30f);
//        holder.file_title.setMarqueeRepeatLimit(5);
//        holder.file_title.setSelected(true);
//        holder.file_title.setSingleLine(true);
         holder.mAnimDownloadProgressButton.setButtonRadius(0.500f);
        holder.mAnimDownloadProgressButton.postInvalidate();


       // i = holder.mAnimDownloadProgressButton.getProgress();

        new Thread(new Runnable() {
            public void run() {

                while (i <100) {

                    i += 1;
                    hdlr.post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        public void run() {
                            holder.mAnimDownloadProgressButton.setProgressText( "" , i);
                            holder. mAnimDownloadProgressButton.setState(AnimDownloadProgressButton.DOWNLOADING);
                         /*   if(i==100){
                                holder.mAnimDownloadProgressButton.setState(AnimDownloadProgressButton.INSTALLING);
                                holder.mAnimDownloadProgressButton.setCurrentText("Completed");
                                holder.imageView1.setBackgroundResource(R.drawable.ic_chek);
                                holder.rmv.setVisibility(View.INVISIBLE);

                            }  else if(i>85) {

                                holder.mAnimDownloadProgressButton.setState(AnimDownloadProgressButton.INSTALLING);
                                holder.mAnimDownloadProgressButton.setCurrentText("Completed");
                                holder.imageView1.setBackgroundResource(R.drawable.ic_chek);
                                holder.rmv.setVisibility(View.INVISIBLE);

                            }*/

                        }
                    });

                    try {
                        Thread.sleep(50);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
        ).start();


        /*if (itemLists.getUriPath().endsWith(".mp4")) {

             Glide.with(ctx).load(itemLists.getPath()).placeholder(R.drawable.ic_baseline_movie_24).error(R.drawable.ic_baseline_movie_24).into(holder.imageView);

        } else if(itemLists.getPath().toLowerCase().endsWith(".jpg")){
             Glide.with(ctx).load(itemLists.getPath()).placeholder(R.drawable.ic_baseline_image).error(R.drawable.ic_baseline_image).into(holder.imageView);

        } else if (itemLists.getPath().endsWith(".pdf")) {
            holder.imageView.setImageResource(R.drawable.ic_pdf_24);
        }
        else if (itemLists .getPath().endsWith(".mp3")) {
            holder.imageView.setImageResource(R.drawable.ic_muisc);

        }*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView file_title ;
        ImageView imageView,imageView1,rmv;
       public LinearLayout cardView;
        AnimDownloadProgressButton mAnimDownloadProgressButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
         cardView=itemView.findViewById(R.id.card);
           /* imageView=itemView.findViewById(R.id.pic);
            imageView1=itemView.findViewById(R.id.Xmark);*/
            mAnimDownloadProgressButton = itemView.findViewById(R.id.btn);
           /* file_title=itemView.findViewById(R.id.file_title);
            rmv=itemView.findViewById(R.id.Remove);*/
        }
    }

}
