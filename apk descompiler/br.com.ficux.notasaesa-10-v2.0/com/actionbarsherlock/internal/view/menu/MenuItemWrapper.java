package com.actionbarsherlock.internal.view.menu;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import com.actionbarsherlock.internal.view.ActionProviderWrapper;
import com.actionbarsherlock.view.ActionProvider;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.SubMenu;

public class MenuItemWrapper implements MenuItem, OnMenuItemClickListener {
    private OnActionExpandListener mActionExpandListener;
    private MenuItem.OnMenuItemClickListener mMenuItemClickListener;
    private android.view.MenuItem.OnActionExpandListener mNativeActionExpandListener;
    private final android.view.MenuItem mNativeItem;
    private SubMenu mSubMenu;

    /* renamed from: com.actionbarsherlock.internal.view.menu.MenuItemWrapper.1 */
    class C00401 implements android.view.MenuItem.OnActionExpandListener {
        C00401() {
        }

        public boolean onMenuItemActionExpand(android.view.MenuItem menuItem) {
            if (MenuItemWrapper.this.mActionExpandListener != null) {
                return MenuItemWrapper.this.mActionExpandListener.onMenuItemActionExpand(MenuItemWrapper.this);
            }
            return false;
        }

        public boolean onMenuItemActionCollapse(android.view.MenuItem menuItem) {
            if (MenuItemWrapper.this.mActionExpandListener != null) {
                return MenuItemWrapper.this.mActionExpandListener.onMenuItemActionCollapse(MenuItemWrapper.this);
            }
            return false;
        }
    }

    public MenuItemWrapper(android.view.MenuItem nativeItem) {
        this.mSubMenu = null;
        this.mMenuItemClickListener = null;
        this.mActionExpandListener = null;
        this.mNativeActionExpandListener = null;
        if (nativeItem == null) {
            throw new IllegalStateException("Wrapped menu item cannot be null.");
        }
        this.mNativeItem = nativeItem;
    }

    public int getItemId() {
        return this.mNativeItem.getItemId();
    }

    public int getGroupId() {
        return this.mNativeItem.getGroupId();
    }

    public int getOrder() {
        return this.mNativeItem.getOrder();
    }

    public MenuItem setTitle(CharSequence title) {
        this.mNativeItem.setTitle(title);
        return this;
    }

    public MenuItem setTitle(int title) {
        this.mNativeItem.setTitle(title);
        return this;
    }

    public CharSequence getTitle() {
        return this.mNativeItem.getTitle();
    }

    public MenuItem setTitleCondensed(CharSequence title) {
        this.mNativeItem.setTitleCondensed(title);
        return this;
    }

    public CharSequence getTitleCondensed() {
        return this.mNativeItem.getTitleCondensed();
    }

    public MenuItem setIcon(Drawable icon) {
        this.mNativeItem.setIcon(icon);
        return this;
    }

    public MenuItem setIcon(int iconRes) {
        this.mNativeItem.setIcon(iconRes);
        return this;
    }

    public Drawable getIcon() {
        return this.mNativeItem.getIcon();
    }

    public MenuItem setIntent(Intent intent) {
        this.mNativeItem.setIntent(intent);
        return this;
    }

    public Intent getIntent() {
        return this.mNativeItem.getIntent();
    }

    public MenuItem setShortcut(char numericChar, char alphaChar) {
        this.mNativeItem.setShortcut(numericChar, alphaChar);
        return this;
    }

    public MenuItem setNumericShortcut(char numericChar) {
        this.mNativeItem.setNumericShortcut(numericChar);
        return this;
    }

    public char getNumericShortcut() {
        return this.mNativeItem.getNumericShortcut();
    }

    public MenuItem setAlphabeticShortcut(char alphaChar) {
        this.mNativeItem.setAlphabeticShortcut(alphaChar);
        return this;
    }

    public char getAlphabeticShortcut() {
        return this.mNativeItem.getAlphabeticShortcut();
    }

    public MenuItem setCheckable(boolean checkable) {
        this.mNativeItem.setCheckable(checkable);
        return this;
    }

    public boolean isCheckable() {
        return this.mNativeItem.isCheckable();
    }

    public MenuItem setChecked(boolean checked) {
        this.mNativeItem.setChecked(checked);
        return this;
    }

    public boolean isChecked() {
        return this.mNativeItem.isChecked();
    }

    public MenuItem setVisible(boolean visible) {
        this.mNativeItem.setVisible(visible);
        return this;
    }

    public boolean isVisible() {
        return this.mNativeItem.isVisible();
    }

    public MenuItem setEnabled(boolean enabled) {
        this.mNativeItem.setEnabled(enabled);
        return this;
    }

    public boolean isEnabled() {
        return this.mNativeItem.isEnabled();
    }

    public boolean hasSubMenu() {
        return this.mNativeItem.hasSubMenu();
    }

    public SubMenu getSubMenu() {
        if (hasSubMenu() && this.mSubMenu == null) {
            this.mSubMenu = new SubMenuWrapper(this.mNativeItem.getSubMenu());
        }
        return this.mSubMenu;
    }

    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener menuItemClickListener) {
        this.mMenuItemClickListener = menuItemClickListener;
        this.mNativeItem.setOnMenuItemClickListener(this);
        return this;
    }

    public boolean onMenuItemClick(android.view.MenuItem item) {
        if (this.mMenuItemClickListener != null) {
            return this.mMenuItemClickListener.onMenuItemClick(this);
        }
        return false;
    }

    public ContextMenuInfo getMenuInfo() {
        return this.mNativeItem.getMenuInfo();
    }

    public void setShowAsAction(int actionEnum) {
        this.mNativeItem.setShowAsAction(actionEnum);
    }

    public MenuItem setShowAsActionFlags(int actionEnum) {
        this.mNativeItem.setShowAsActionFlags(actionEnum);
        return this;
    }

    public MenuItem setActionView(View view) {
        this.mNativeItem.setActionView(view);
        return this;
    }

    public MenuItem setActionView(int resId) {
        this.mNativeItem.setActionView(resId);
        return this;
    }

    public View getActionView() {
        return this.mNativeItem.getActionView();
    }

    public MenuItem setActionProvider(ActionProvider actionProvider) {
        this.mNativeItem.setActionProvider(new ActionProviderWrapper(actionProvider));
        return this;
    }

    public ActionProvider getActionProvider() {
        android.view.ActionProvider nativeProvider = this.mNativeItem.getActionProvider();
        if (nativeProvider == null || !(nativeProvider instanceof ActionProviderWrapper)) {
            return null;
        }
        return ((ActionProviderWrapper) nativeProvider).unwrap();
    }

    public boolean expandActionView() {
        return this.mNativeItem.expandActionView();
    }

    public boolean collapseActionView() {
        return this.mNativeItem.collapseActionView();
    }

    public boolean isActionViewExpanded() {
        return this.mNativeItem.isActionViewExpanded();
    }

    public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
        this.mActionExpandListener = listener;
        if (this.mNativeActionExpandListener == null) {
            this.mNativeActionExpandListener = new C00401();
            this.mNativeItem.setOnActionExpandListener(this.mNativeActionExpandListener);
        }
        return this;
    }
}
