package com.actionbarsherlock.internal.view.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.actionbarsherlock.C0029R;
import com.actionbarsherlock.internal.view.menu.MenuView.ItemView;

public class ListMenuItemView extends LinearLayout implements ItemView {
    private Drawable mBackground;
    private CheckBox mCheckBox;
    final Context mContext;
    private boolean mForceShowIcon;
    private ImageView mIconView;
    private LayoutInflater mInflater;
    private MenuItemImpl mItemData;
    private boolean mPreserveIconSpacing;
    private RadioButton mRadioButton;
    private TextView mShortcutView;
    private int mTextAppearance;
    private Context mTextAppearanceContext;
    private TextView mTitleView;

    public ListMenuItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, C0029R.styleable.SherlockMenuView, defStyle, 0);
        this.mBackground = a.getDrawable(4);
        this.mTextAppearance = a.getResourceId(0, -1);
        this.mPreserveIconSpacing = a.getBoolean(7, false);
        this.mTextAppearanceContext = context;
        a.recycle();
    }

    public ListMenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        setBackgroundDrawable(this.mBackground);
        this.mTitleView = (TextView) findViewById(C0029R.id.abs__title);
        if (this.mTextAppearance != -1) {
            this.mTitleView.setTextAppearance(this.mTextAppearanceContext, this.mTextAppearance);
        }
        this.mShortcutView = (TextView) findViewById(C0029R.id.abs__shortcut);
    }

    public void initialize(MenuItemImpl itemData, int menuType) {
        this.mItemData = itemData;
        setVisibility(itemData.isVisible() ? 0 : 8);
        setTitle(itemData.getTitleForItemView(this));
        setCheckable(itemData.isCheckable());
        setShortcut(itemData.shouldShowShortcut(), itemData.getShortcut());
        setIcon(itemData.getIcon());
        setEnabled(itemData.isEnabled());
    }

    public void setForceShowIcon(boolean forceShow) {
        this.mForceShowIcon = forceShow;
        this.mPreserveIconSpacing = forceShow;
    }

    public void setTitle(CharSequence title) {
        if (title != null) {
            this.mTitleView.setText(title);
            if (this.mTitleView.getVisibility() != 0) {
                this.mTitleView.setVisibility(0);
            }
        } else if (this.mTitleView.getVisibility() != 8) {
            this.mTitleView.setVisibility(8);
        }
    }

    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public void setCheckable(boolean checkable) {
        if (checkable || this.mRadioButton != null || this.mCheckBox != null) {
            CompoundButton compoundButton;
            CompoundButton otherCompoundButton;
            if (this.mRadioButton == null) {
                insertRadioButton();
            }
            if (this.mCheckBox == null) {
                insertCheckBox();
            }
            if (this.mItemData.isExclusiveCheckable()) {
                compoundButton = this.mRadioButton;
                otherCompoundButton = this.mCheckBox;
            } else {
                compoundButton = this.mCheckBox;
                otherCompoundButton = this.mRadioButton;
            }
            if (checkable) {
                int newVisibility;
                compoundButton.setChecked(this.mItemData.isChecked());
                if (checkable) {
                    newVisibility = 0;
                } else {
                    newVisibility = 8;
                }
                if (compoundButton.getVisibility() != newVisibility) {
                    compoundButton.setVisibility(newVisibility);
                }
                if (otherCompoundButton.getVisibility() != 8) {
                    otherCompoundButton.setVisibility(8);
                    return;
                }
                return;
            }
            this.mCheckBox.setVisibility(8);
            this.mRadioButton.setVisibility(8);
        }
    }

    public void setChecked(boolean checked) {
        CompoundButton compoundButton;
        if (this.mItemData.isExclusiveCheckable()) {
            if (this.mRadioButton == null) {
                insertRadioButton();
            }
            compoundButton = this.mRadioButton;
        } else {
            if (this.mCheckBox == null) {
                insertCheckBox();
            }
            compoundButton = this.mCheckBox;
        }
        compoundButton.setChecked(checked);
    }

    public void setShortcut(boolean showShortcut, char shortcutKey) {
        int newVisibility = (showShortcut && this.mItemData.shouldShowShortcut()) ? 0 : 8;
        if (newVisibility == 0) {
            this.mShortcutView.setText(this.mItemData.getShortcutLabel());
        }
        if (this.mShortcutView.getVisibility() != newVisibility) {
            this.mShortcutView.setVisibility(newVisibility);
        }
    }

    public void setIcon(Drawable icon) {
        boolean showIcon = this.mItemData.shouldShowIcon() || this.mForceShowIcon;
        if (!showIcon && !this.mPreserveIconSpacing) {
            return;
        }
        if (this.mIconView != null || icon != null || this.mPreserveIconSpacing) {
            if (this.mIconView == null) {
                insertIconView();
            }
            if (icon != null || this.mPreserveIconSpacing) {
                ImageView imageView = this.mIconView;
                if (!showIcon) {
                    icon = null;
                }
                imageView.setImageDrawable(icon);
                if (this.mIconView.getVisibility() != 0) {
                    this.mIconView.setVisibility(0);
                    return;
                }
                return;
            }
            this.mIconView.setVisibility(8);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mIconView != null && this.mPreserveIconSpacing) {
            LayoutParams lp = getLayoutParams();
            LinearLayout.LayoutParams iconLp = (LinearLayout.LayoutParams) this.mIconView.getLayoutParams();
            if (lp.height > 0 && iconLp.width <= 0) {
                iconLp.width = lp.height;
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void insertIconView() {
        this.mIconView = (ImageView) getInflater().inflate(C0029R.layout.abs__list_menu_item_icon, this, false);
        addView(this.mIconView, 0);
    }

    private void insertRadioButton() {
        this.mRadioButton = (RadioButton) getInflater().inflate(C0029R.layout.abs__list_menu_item_radio, this, false);
        addView(this.mRadioButton);
    }

    private void insertCheckBox() {
        this.mCheckBox = (CheckBox) getInflater().inflate(C0029R.layout.abs__list_menu_item_checkbox, this, false);
        addView(this.mCheckBox);
    }

    public boolean prefersCondensedTitle() {
        return false;
    }

    public boolean showsIcon() {
        return this.mForceShowIcon;
    }

    private LayoutInflater getInflater() {
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(this.mContext);
        }
        return this.mInflater;
    }
}