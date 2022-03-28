 package com.example.filesharing.Fragments;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.filesharing.Activity.DiscoverList;
import com.example.filesharing.Activity.sendScreen;
import com.example.filesharing.Adapter.Adapter;
import com.example.filesharing.Model.ItemList;
import com.example.filesharing.Model.ItemsList;
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


 public class ImageFragment extends Fragment{
     RecyclerView recyclerView;

     public ItemAdapter<Adapter> itemAdapter;
     public FastAdapter<Adapter> fastAdapter;

     private ActionModeHelper<Adapter> mActionModeHelper;

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
          View view= inflater.inflate(R.layout.fragment_document, container, false);
         recyclerView=view.findViewById(R.id.rv);

         Log.e("FRGMENT","VIDEOS cREATE");
         recyclerView.setLayoutManager(new GridLayoutManager( ImageFragment.this.getActivity(),3));
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
         recyclerView.setAdapter( fastAdapter);


         fastAdapter.withSelectionListener(new ISelectionListener<Adapter>() {
             @Override
             public void onSelectionChanged(Adapter item, boolean selected) {

                 DiscoverList.PasItem(getSelectedItems());

             }
         });

         fastAdapter.withOnClickListener(new OnClickListener<Adapter>() {

             @Override
             public boolean onClick(@Nullable View v, IAdapter<Adapter> adapter, Adapter item, int position) {
                 ActionMode actionMode = mActionModeHelper.onLongClick( (AppCompatActivity) getActivity(), position);

                 if (actionMode != null) {

                 }
                 return actionMode != null;
             }
         });
         fastAdapter.withOnPreClickListener(new OnClickListener<Adapter>() {
             @Override
             public boolean onClick(View v, IAdapter<Adapter> adapter, @NonNull Adapter item, int position) {
                 Boolean res = mActionModeHelper.onClick(item);
                 return res != null ? res : false;
             }
         });



         mActionModeHelper = new ActionModeHelper<>(fastAdapter, R.menu.cab, new ActionBarCallBack());


         ContentResolver contentResolver= getActivity().getContentResolver();
         Uri uri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
         Cursor cursor=contentResolver.query(uri,null,MediaStore.Images.Media.DATA+" LIKE?", new String[]{"%.jpg%"},null);


         if(cursor==null){
             Toast.makeText(getContext(),"somthing wnt wrng",Toast.LENGTH_SHORT).show();

         }

         else  if(!cursor.moveToNext()){
             Toast.makeText(getContext(),"no music found",Toast.LENGTH_SHORT).show();

         }else{
             while (cursor.moveToNext()){
                 @SuppressLint("Range")  String getMusicFileName= cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
                 @SuppressLint("Range") long cursorId=cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                 Uri musicFileUri= ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cursorId);
                 //    @SuppressLint("Range") double size= Double.parseDouble(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE)));
                 @SuppressLint("Range") String data=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
                 @SuppressLint("Range") long size=cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                 @SuppressLint("Range") String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA) );


                 itemAdapter.add(new Adapter().withAdapter(new ItemsList(getMusicFileName, musicFileUri,size,data,path)));

             }

         }
         return  view;
     }

     public List<ItemList> getSelectedItems() {
         final List<ItemList> items = new ArrayList<>();
         fastAdapter.recursive(new AdapterPredicate<Adapter>() {
             @Override
             public boolean apply(@NonNull IAdapter<Adapter> lastParentAdapter, int lastParentPosition, Adapter item, int position) {
                 if (item.isSelected()) {
                     String name=item.itemsList.getName();
                     String path=item.itemsList.getPath();
                     long size=item.itemsList.getSize();
                     items.add(new ItemList(name,size,path));

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