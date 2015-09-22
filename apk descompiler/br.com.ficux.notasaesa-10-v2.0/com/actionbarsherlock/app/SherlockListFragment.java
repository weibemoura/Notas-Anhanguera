package com.actionbarsherlock.app;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.support.v4.app._ActionBarSherlockTrojanHorse.OnCreateOptionsMenuListener;
import android.support.v4.app._ActionBarSherlockTrojanHorse.OnOptionsItemSelectedListener;
import android.support.v4.app._ActionBarSherlockTrojanHorse.OnPrepareOptionsMenuListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.actionbarsherlock.internal.view.menu.MenuItemWrapper;
import com.actionbarsherlock.internal.view.menu.MenuWrapper;

public class SherlockListFragment extends ListFragment implements OnCreateOptionsMenuListener, OnPrepareOptionsMenuListener, OnOptionsItemSelectedListener {
    private SherlockFragmentActivity mActivity;

    public SherlockFragmentActivity getSherlockActivity() {
        return this.mActivity;
    }

    public void onAttach(Activity activity) {
        if (activity instanceof SherlockFragmentActivity) {
            this.mActivity = (SherlockFragmentActivity) activity;
            super.onAttach(activity);
            return;
        }
        throw new IllegalStateException(new StringBuilder(String.valueOf(getClass().getSimpleName())).append(" must be attached to a SherlockFragmentActivity.").toString());
    }

    public void onDetach() {
        this.mActivity = null;
        super.onDetach();
    }

    public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        onCreateOptionsMenu(new MenuWrapper(menu), this.mActivity.getSupportMenuInflater());
    }

    public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
    }

    public final void onPrepareOptionsMenu(Menu menu) {
        onPrepareOptionsMenu(new MenuWrapper(menu));
    }

    public void onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
    }

    public final boolean onOptionsItemSelected(MenuItem item) {
        return onOptionsItemSelected(new MenuItemWrapper(item));
    }

    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        return false;
    }
}
