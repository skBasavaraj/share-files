package com.example.filesharing.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.filesharing.Adapter.sendReceive;
import com.example.filesharing.Model.ItemList;
import com.example.filesharing.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.fastadapter.utils.EventHookUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.filesharing.Activity.sendScreen.Remove;

public class ReceiveScreen extends AppCompatActivity {
    public static FastAdapter<sendReceive> fastAdapter;
    public static  ItemAdapter<sendReceive> itemAdapter;
    public static  ReceiveScreen rvScreen;

    public  List<String> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_screen);
        RecyclerView rv=findViewById(R.id.rrv);

        rv.setLayoutManager(new LinearLayoutManager(this));
         rv.setHasFixedSize(true);

        itemAdapter = itemAdapter.items();

        itemAdapter = new ItemAdapter();
        fastAdapter = FastAdapter.with(itemAdapter);
        fastAdapter.withEventHook(new ClickEvent());

        rv.setAdapter(fastAdapter);
        list.add("File1");
        list.add("File2");
        list.add("File2");
        list.add("File3");
        list.add("File4");


    for(String s:list) {
    itemAdapter.add(new sendReceive( new ItemList(s,"",0),this));
       }
     }
public  static ReceiveScreen rmv(int position){
        itemAdapter.remove(position);
        fastAdapter.notifyAdapterDataSetChanged();
    return rvScreen;
}
    public static class ClickEvent extends ClickEventHook<sendReceive> {
        @Override
        public void onClick(View v, int position, FastAdapter<sendReceive> fastAdapter, sendReceive item) {
             rmv(position);
        }



        @Nullable
        @Override
        public List<View> onBindMany(@NonNull RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof sendReceive.ViewHolder) {
                return EventHookUtil.toList(((sendReceive.ViewHolder) viewHolder).cardView);
            }
            return super.onBindMany(viewHolder);
        }


    }
    }
