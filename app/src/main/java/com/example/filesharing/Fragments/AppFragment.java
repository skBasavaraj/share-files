package com.example.filesharing.Fragments;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filesharing.Activity.sendScreen;
import com.example.filesharing.Adapter.AppAdapter;
import com.example.filesharing.Model.App;
import com.example.filesharing.Model.ItemList;
import com.example.filesharing.R;
import com.example.filesharing.SpacesItemDecoration;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.ISelectionListener;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter.utils.AdapterPredicate;

import java.util.ArrayList;
import java.util.List;


public class AppFragment extends Fragment {

    String name;
    String path;
    RecyclerView recyclerView;

    public ItemAdapter<AppAdapter> itemAdapter;
    public FastAdapter<AppAdapter> fastAdapter;
    private ActionModeHelper<AppAdapter> mActionModeHelper;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
          View view= inflater.inflate(R.layout.fragment_app, container, false);
        recyclerView=view.findViewById(R.id.rv);

         recyclerView.setLayoutManager(new GridLayoutManager(AppFragment.this.getActivity(),3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration(3));

        recyclerView.setHasFixedSize(true);

        itemAdapter = itemAdapter.items();

        itemAdapter = new ItemAdapter();
        fastAdapter = FastAdapter.with(itemAdapter);
        fastAdapter.setHasStableIds(true);
        fastAdapter.withSelectable(true);
        fastAdapter.withMultiSelect(true);
        fastAdapter.withSelectOnLongClick(true);
        recyclerView.setAdapter(fastAdapter);
        fastAdapter.withSelectionListener(new ISelectionListener<AppAdapter>() {
            @Override
            public void onSelectionChanged(AppAdapter item, boolean selected) {
                sendScreen.PasItem(getSelectedItems());

            }
        });

        fastAdapter.withOnClickListener(new OnClickListener<AppAdapter>() {
            @Override
            public boolean onClick(@Nullable View v, IAdapter<AppAdapter> adapter, AppAdapter item, int position) {
                ActionMode actionMode = mActionModeHelper.onLongClick( (AppCompatActivity) getActivity(), position);

                if (actionMode != null) {

                }

                return actionMode != null;            }
        });
        fastAdapter.withOnPreClickListener(new OnClickListener<AppAdapter>() {
            @Override
            public boolean onClick(@Nullable View v, IAdapter<AppAdapter> adapter, AppAdapter item, int position) {
                     Boolean res = mActionModeHelper.onClick(item);
                    return res != null ? res : false;
             }
        });

        mActionModeHelper = new ActionModeHelper<>(fastAdapter, R.menu.cab, new  ActionBarCallBack());

        PackageManager pm=getContext().getPackageManager();
        List<ApplicationInfo> packages=pm.getInstalledApplications(PackageManager.GET_META_DATA);

             for(ApplicationInfo packInfo:packages){
                 if ((packInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
                     name = packInfo.packageName;
                     path = packInfo.sourceDir;
                     Drawable icon = pm.getApplicationIcon(packInfo);
                     itemAdapter.add(new AppAdapter().withApp(new App(name, icon, path)));
                 }
         }

        

         return view;
     }

 public List<ItemList> getSelectedItems() {
        final List<ItemList> items = new ArrayList<>();
       fastAdapter.recursive(new AdapterPredicate<AppAdapter>() {
            @Override
           public boolean apply(IAdapter<AppAdapter> lastParentAdapter, int lastParentPosition, AppAdapter item, int position) {
               if (item.isSelected()) {
                   String path= item.app.getApath();
                   String name=item.app.getAname();
                   items.add(new ItemList(name,path,0));

                }
                return false;
            }


       }, false);
     return items;

 }






    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            mode.finish();
            return true;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    }
}