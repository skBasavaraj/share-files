package com.example.filesharing.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.filesharing.Adapter.sendReceive;
import com.example.filesharing.Model.ProgressModel;
import com.example.filesharing.R;
import com.example.filesharing.sharing_backend.WifiClientTask;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.fastadapter.utils.EventHookUtil;

import java.util.ArrayList;
import java.util.List;

public  class sendScreen extends AppCompatActivity {

 public static List<ProgressModel> list=new ArrayList<>();
 public static sendScreen instance;
    public static FastAdapter<sendReceive>fastAdapter;
    public static ItemAdapter<sendReceive>itemAdapter;
 public DiscoverList discoverList =new DiscoverList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_progress);
        RecyclerView rv=findViewById(R.id.srv);

        rv.setLayoutManager(new LinearLayoutManager(this));
        instance=this;
        rv.setHasFixedSize(true);
        itemAdapter = itemAdapter.items();
        itemAdapter = new ItemAdapter();
        fastAdapter = FastAdapter.with(itemAdapter);
        rv.setAdapter(fastAdapter);

      fastAdapter.withEventHook(new  ClickEvent());

        /*for(ProgressModel s:list) {
            Uri imageUri =Uri.parse(s.getPath() );
            // Log.e(TAG, "file path：" + imageUri);
            if (discoverList.wifiP2pInfo != null) {
                 new WifiClientTask(this).execute(discoverList.wifiP2pInfo.groupOwnerAddress.getHostAddress(), imageUri);
            }
            //itemAdapter.add(new sendReceive(new ProgressModel(s.getName(),s.getPath(),s.getSize()),this));
        }*/
       fastAdapter.notifyAdapterDataSetChanged();
      /*  Uri imageUri =;
       // Log.e(TAG, "file path：" + imageUri);
        if (discoverList.wifiP2pInfo != null) {
            new WifiClientTask(this).execute(discoverList.wifiP2pInfo.groupOwnerAddress.getHostAddress(), imageUri);
        }*/

    }

    public static sendScreen PasItem(int select) {
        itemAdapter.add( new sendReceive(new ProgressModel(select)) );
       return instance;
    }


    public static sendScreen Remove(int position) {
        itemAdapter.remove(position);
        fastAdapter.notifyAdapterDataSetChanged();
        return instance;
    }
    public static class ClickEvent extends ClickEventHook<sendReceive> {
        @Override
        public void onClick(View v, int position, FastAdapter<sendReceive> fastAdapter, sendReceive item) {
            Remove(position);
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