package com.actionbarsherlock.internal.app;

import android.app.ActionBar.OnMenuVisibilityListener;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.SpinnerAdapter;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.LayoutParams;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import java.util.HashSet;
import java.util.Set;

public class ActionBarWrapper extends ActionBar implements OnNavigationListener, OnMenuVisibilityListener {
    private final android.app.ActionBar mActionBar;
    private final Activity mActivity;
    private FragmentTransaction mFragmentTransaction;
    private Set<ActionBar.OnMenuVisibilityListener> mMenuVisibilityListeners;
    private ActionBar.OnNavigationListener mNavigationListener;

    public class TabWrapper extends Tab implements TabListener {
        private ActionBar.TabListener mListener;
        final android.app.ActionBar.Tab mNativeTab;
        private Object mTag;

        public TabWrapper(android.app.ActionBar.Tab nativeTab) {
            this.mNativeTab = nativeTab;
            this.mNativeTab.setTag(this);
        }

        public int getPosition() {
            return this.mNativeTab.getPosition();
        }

        public Drawable getIcon() {
            return this.mNativeTab.getIcon();
        }

        public CharSequence getText() {
            return this.mNativeTab.getText();
        }

        public Tab setIcon(Drawable icon) {
            this.mNativeTab.setIcon(icon);
            return this;
        }

        public Tab setIcon(int resId) {
            this.mNativeTab.setIcon(resId);
            return this;
        }

        public Tab setText(CharSequence text) {
            this.mNativeTab.setText(text);
            return this;
        }

        public Tab setText(int resId) {
            this.mNativeTab.setText(resId);
            return this;
        }

        public Tab setCustomView(View view) {
            this.mNativeTab.setCustomView(view);
            return this;
        }

        public Tab setCustomView(int layoutResId) {
            this.mNativeTab.setCustomView(layoutResId);
            return this;
        }

        public View getCustomView() {
            return this.mNativeTab.getCustomView();
        }

        public Tab setTag(Object obj) {
            this.mTag = obj;
            return this;
        }

        public Object getTag() {
            return this.mTag;
        }

        public Tab setTabListener(ActionBar.TabListener listener) {
            this.mNativeTab.setTabListener(listener != null ? this : null);
            this.mListener = listener;
            return this;
        }

        public void select() {
            this.mNativeTab.select();
        }

        public Tab setContentDescription(int resId) {
            this.mNativeTab.setContentDescription(resId);
            return this;
        }

        public Tab setContentDescription(CharSequence contentDesc) {
            this.mNativeTab.setContentDescription(contentDesc);
            return this;
        }

        public CharSequence getContentDescription() {
            return this.mNativeTab.getContentDescription();
        }

        public void onTabReselected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction ft) {
            if (this.mListener != null) {
                FragmentTransaction trans = null;
                if (ActionBarWrapper.this.mActivity instanceof SherlockFragmentActivity) {
                    trans = ((SherlockFragmentActivity) ActionBarWrapper.this.mActivity).getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
                }
                this.mListener.onTabReselected(this, trans);
                if (trans != null && !trans.isEmpty()) {
                    trans.commit();
                }
            }
        }

        public void onTabSelected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction ft) {
            if (this.mListener != null) {
                if (ActionBarWrapper.this.mFragmentTransaction == null && (ActionBarWrapper.this.mActivity instanceof SherlockFragmentActivity)) {
                    ActionBarWrapper.this.mFragmentTransaction = ((SherlockFragmentActivity) ActionBarWrapper.this.mActivity).getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
                }
                this.mListener.onTabSelected(this, ActionBarWrapper.this.mFragmentTransaction);
                if (ActionBarWrapper.this.mFragmentTransaction != null) {
                    if (!ActionBarWrapper.this.mFragmentTransaction.isEmpty()) {
                        ActionBarWrapper.this.mFragmentTransaction.commit();
                    }
                    ActionBarWrapper.this.mFragmentTransaction = null;
                }
            }
        }

        public void onTabUnselected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction ft) {
            if (this.mListener != null) {
                FragmentTransaction trans = null;
                if (ActionBarWrapper.this.mActivity instanceof SherlockFragmentActivity) {
                    trans = ((SherlockFragmentActivity) ActionBarWrapper.this.mActivity).getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
                    ActionBarWrapper.this.mFragmentTransaction = trans;
                }
                this.mListener.onTabUnselected(this, trans);
            }
        }
    }

    public ActionBarWrapper(Activity activity) {
        this.mMenuVisibilityListeners = new HashSet(1);
        this.mActivity = activity;
        this.mActionBar = activity.getActionBar();
        if (this.mActionBar != null) {
            this.mActionBar.addOnMenuVisibilityListener(this);
        }
    }

    public void setHomeButtonEnabled(boolean enabled) {
        this.mActionBar.setHomeButtonEnabled(enabled);
    }

    public Context getThemedContext() {
        return this.mActionBar.getThemedContext();
    }

    public void setCustomView(View view) {
        this.mActionBar.setCustomView(view);
    }

    public void setCustomView(View view, LayoutParams layoutParams) {
        android.app.ActionBar.LayoutParams lp = new android.app.ActionBar.LayoutParams(layoutParams);
        lp.gravity = layoutParams.gravity;
        lp.bottomMargin = layoutParams.bottomMargin;
        lp.topMargin = layoutParams.topMargin;
        lp.leftMargin = layoutParams.leftMargin;
        lp.rightMargin = layoutParams.rightMargin;
        this.mActionBar.setCustomView(view, lp);
    }

    public void setCustomView(int resId) {
        this.mActionBar.setCustomView(resId);
    }

    public void setIcon(int resId) {
        this.mActionBar.setIcon(resId);
    }

    public void setIcon(Drawable icon) {
        this.mActionBar.setIcon(icon);
    }

    public void setLogo(int resId) {
        this.mActionBar.setLogo(resId);
    }

    public void setLogo(Drawable logo) {
        this.mActionBar.setLogo(logo);
    }

    public void setListNavigationCallbacks(SpinnerAdapter adapter, ActionBar.OnNavigationListener callback) {
        OnNavigationListener onNavigationListener;
        this.mNavigationListener = callback;
        android.app.ActionBar actionBar = this.mActionBar;
        if (callback == null) {
            onNavigationListener = null;
        }
        actionBar.setListNavigationCallbacks(adapter, onNavigationListener);
    }

    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return this.mNavigationListener.onNavigationItemSelected(itemPosition, itemId);
    }

    public void setSelectedNavigationItem(int position) {
        this.mActionBar.setSelectedNavigationItem(position);
    }

    public int getSelectedNavigationIndex() {
        return this.mActionBar.getSelectedNavigationIndex();
    }

    public int getNavigationItemCount() {
        return this.mActionBar.getNavigationItemCount();
    }

    public void setTitle(CharSequence title) {
        this.mActionBar.setTitle(title);
    }

    public void setTitle(int resId) {
        this.mActionBar.setTitle(resId);
    }

    public void setSubtitle(CharSequence subtitle) {
        this.mActionBar.setSubtitle(subtitle);
    }

    public void setSubtitle(int resId) {
        this.mActionBar.setSubtitle(resId);
    }

    public void setDisplayOptions(int options) {
        this.mActionBar.setDisplayOptions(options);
    }

    public void setDisplayOptions(int options, int mask) {
        this.mActionBar.setDisplayOptions(options, mask);
    }

    public void setDisplayUseLogoEnabled(boolean useLogo) {
        this.mActionBar.setDisplayUseLogoEnabled(useLogo);
    }

    public void setDisplayShowHomeEnabled(boolean showHome) {
        this.mActionBar.setDisplayShowHomeEnabled(showHome);
    }

    public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        this.mActionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
    }

    public void setDisplayShowTitleEnabled(boolean showTitle) {
        this.mActionBar.setDisplayShowTitleEnabled(showTitle);
    }

    public void setDisplayShowCustomEnabled(boolean showCustom) {
        this.mActionBar.setDisplayShowCustomEnabled(showCustom);
    }

    public void setBackgroundDrawable(Drawable d) {
        this.mActionBar.setBackgroundDrawable(d);
    }

    public void setStackedBackgroundDrawable(Drawable d) {
        this.mActionBar.setStackedBackgroundDrawable(d);
    }

    public void setSplitBackgroundDrawable(Drawable d) {
        this.mActionBar.setSplitBackgroundDrawable(d);
    }

    public View getCustomView() {
        return this.mActionBar.getCustomView();
    }

    public CharSequence getTitle() {
        return this.mActionBar.getTitle();
    }

    public CharSequence getSubtitle() {
        return this.mActionBar.getSubtitle();
    }

    public int getNavigationMode() {
        return this.mActionBar.getNavigationMode();
    }

    public void setNavigationMode(int mode) {
        this.mActionBar.setNavigationMode(mode);
    }

    public int getDisplayOptions() {
        return this.mActionBar.getDisplayOptions();
    }

    public Tab newTab() {
        return new TabWrapper(this.mActionBar.newTab());
    }

    public void addTab(Tab tab) {
        this.mActionBar.addTab(((TabWrapper) tab).mNativeTab);
    }

    public void addTab(Tab tab, boolean setSelected) {
        this.mActionBar.addTab(((TabWrapper) tab).mNativeTab, setSelected);
    }

    public void addTab(Tab tab, int position) {
        this.mActionBar.addTab(((TabWrapper) tab).mNativeTab, position);
    }

    public void addTab(Tab tab, int position, boolean setSelected) {
        this.mActionBar.addTab(((TabWrapper) tab).mNativeTab, position, setSelected);
    }

    public void removeTab(Tab tab) {
        this.mActionBar.removeTab(((TabWrapper) tab).mNativeTab);
    }

    public void removeTabAt(int position) {
        this.mActionBar.removeTabAt(position);
    }

    public void removeAllTabs() {
        this.mActionBar.removeAllTabs();
    }

    public void selectTab(Tab tab) {
        this.mActionBar.selectTab(((TabWrapper) tab).mNativeTab);
    }

    public Tab getSelectedTab() {
        android.app.ActionBar.Tab selected = this.mActionBar.getSelectedTab();
        return selected != null ? (Tab) selected.getTag() : null;
    }

    public Tab getTabAt(int index) {
        android.app.ActionBar.Tab selected = this.mActionBar.getTabAt(index);
        return selected != null ? (Tab) selected.getTag() : null;
    }

    public int getTabCount() {
        return this.mActionBar.getTabCount();
    }

    public int getHeight() {
        return this.mActionBar.getHeight();
    }

    public void show() {
        this.mActionBar.show();
    }

    public void hide() {
        this.mActionBar.hide();
    }

    public boolean isShowing() {
        return this.mActionBar.isShowing();
    }

    public void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener listener) {
        this.mMenuVisibilityListeners.add(listener);
    }

    public void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener listener) {
        this.mMenuVisibilityListeners.remove(listener);
    }

    public void onMenuVisibilityChanged(boolean isVisible) {
        for (ActionBar.OnMenuVisibilityListener listener : this.mMenuVisibilityListeners) {
            listener.onMenuVisibilityChanged(isVisible);
        }
    }
}
