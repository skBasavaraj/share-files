 package com.example.filesharing.fragments;

 import android.view.Menu;
 import android.view.MenuItem;

 import androidx.annotation.MenuRes;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.appcompat.view.ActionMode;

 import com.mikepenz.fastadapter.FastAdapter;
 import com.mikepenz.fastadapter.IItem;
 import com.mikepenz.fastadapter.select.SelectExtension;

public class ActionModeHelper <Item extends IItem> {
    private FastAdapter<Item> mFastAdapter;
    private SelectExtension<Item> mSelectExtension;
     @MenuRes
    private int mCabMenu;
    public long Total=0;
    private ActionMode.Callback mInternalCallback;
    private ActionMode.Callback mCallback;
    private ActionMode mActionMode;
     private boolean mAutoDeselect = true;

    private ActionModeTitleProvider mTitleProvider;

    private ActionItemClickedListener actionItemClickedListener = null;

    public ActionModeHelper(FastAdapter<Item> fastAdapter, int cabMenu) {
        this(fastAdapter, cabMenu, (ActionItemClickedListener) null);
    }



    public ActionModeHelper(FastAdapter<Item> fastAdapter, int cabMenu, ActionItemClickedListener actionItemClickedListener) {
        this.mFastAdapter = fastAdapter;
        this.mCabMenu = cabMenu;
        this.mInternalCallback = new ActionBarCallBack();
        this.actionItemClickedListener = actionItemClickedListener;

        this.mSelectExtension = fastAdapter.getExtension(SelectExtension.class);
        if (mSelectExtension == null) {
            throw new IllegalStateException("The provided FastAdapter requires the `SelectExtension` or `withSelectable(true)`");
        }
    }


    public ActionModeHelper(FastAdapter<Item> fastAdapter, int cabMenu, ActionMode.Callback callback) {
        this.mFastAdapter = fastAdapter;
        this.mCabMenu = cabMenu;
        this.mCallback = callback;
        this.mInternalCallback = new ActionBarCallBack();

        this.mSelectExtension = fastAdapter.getExtension(SelectExtension.class);
        if (mSelectExtension == null) {
            throw new IllegalStateException("The provided FastAdapter requires the `SelectExtension` or `withSelectable(true)`");
        }
    }

    public ActionModeHelper<Item> withTitleProvider(ActionModeTitleProvider titleProvider) {
        this.mTitleProvider = titleProvider;
        return this;
    }

    public ActionModeHelper<Item> withAutoDeselect(boolean enabled) {
        this.mAutoDeselect = enabled;
        return this;
    }




    public Boolean onClick(IItem item) {
        return onClick(null, item);
    }


    public Boolean onClick(AppCompatActivity act, IItem item) {
         if (mActionMode != null && (mSelectExtension.getSelectedItems().size() == 1) && item.isSelected()) {
            mActionMode.finish();
            mSelectExtension.deselect();
            return true;
        }

        if (mActionMode != null) {

            int selected = mSelectExtension.getSelectedItems().size();
            if (item.isSelected()) {

                selected--;
             }
            else if (item.isSelectable())
                selected++;

             checkActionMode(act, selected);
        }

        return null;
    }


    public ActionMode onLongClick(AppCompatActivity act, int position) {
        if (mActionMode == null && mFastAdapter.getItem(position).isSelectable()) {
             mActionMode = act.startSupportActionMode(mInternalCallback);
             mSelectExtension.select(position);
             checkActionMode(act, 1);
             return mActionMode;
        }
        return mActionMode;
    }






    private ActionMode checkActionMode(AppCompatActivity act, int selected) {
        if (selected == 0) {
            if (mActionMode != null) {
                mActionMode.finish();
                mActionMode = null;
            }
        } else if (mActionMode == null) {
            if (act != null)
                mActionMode = act.startSupportActionMode(mInternalCallback);
        }
        updateTitle(selected);
        return mActionMode;
    }


    private void updateTitle(int selected) {

        if (mActionMode != null) {
            if (mTitleProvider != null)
                mActionMode.setTitle(mTitleProvider.getTitle(selected));
            else
                mActionMode.setTitle(selected + " selected");
         }
    }


    private class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            boolean consumed = false;
            if (mCallback != null) {
                consumed = mCallback.onActionItemClicked(mode, item);
            }

            if (!consumed && actionItemClickedListener != null) {
                consumed = actionItemClickedListener.onClick(mode, item);
            }

            if (!consumed) {
                mSelectExtension.deleteAllSelectedItems();
                 mode.finish();
            }
            return consumed;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(mCabMenu, menu);

             mFastAdapter.withSelectOnLongClick(false);

            return mCallback == null || mCallback.onCreateActionMode(mode, menu);
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;

             mFastAdapter.withSelectOnLongClick(true);

             if (mAutoDeselect)
                mSelectExtension.deselect();

            if (mCallback != null) {
                 mCallback.onDestroyActionMode(mode);
            }
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return mCallback != null && mCallback.onPrepareActionMode(mode, menu);
        }
    }


    public interface ActionModeTitleProvider {
        String getTitle(int selected);
    }

    public interface ActionItemClickedListener {
        boolean onClick(ActionMode mode, MenuItem item);
    }
}
