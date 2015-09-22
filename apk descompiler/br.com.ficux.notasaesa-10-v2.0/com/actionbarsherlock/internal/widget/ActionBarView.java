package com.actionbarsherlock.internal.widget;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.actionbarsherlock.C0029R;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.internal.ActionBarSherlockCompat;
import com.actionbarsherlock.internal.ResourcesCompat;
import com.actionbarsherlock.internal.view.menu.ActionMenuItem;
import com.actionbarsherlock.internal.view.menu.ActionMenuPresenter;
import com.actionbarsherlock.internal.view.menu.ActionMenuView;
import com.actionbarsherlock.internal.view.menu.MenuBuilder;
import com.actionbarsherlock.internal.view.menu.MenuItemImpl;
import com.actionbarsherlock.internal.view.menu.MenuPresenter;
import com.actionbarsherlock.internal.view.menu.MenuView;
import com.actionbarsherlock.internal.view.menu.SubMenuBuilder;
import com.actionbarsherlock.internal.widget.IcsAdapterView.OnItemSelectedListener;
import com.actionbarsherlock.view.CollapsibleActionView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window.Callback;

public class ActionBarView extends AbsActionBarView {
    private static final boolean DEBUG = false;
    private static final int DEFAULT_CUSTOM_GRAVITY = 19;
    public static final int DISPLAY_DEFAULT = 0;
    private static final int DISPLAY_RELAYOUT_MASK = 31;
    private static final String TAG = "ActionBarView";
    private OnNavigationListener mCallback;
    private ActionBarContextView mContextView;
    private View mCustomNavView;
    private int mDisplayOptions;
    View mExpandedActionView;
    private final OnClickListener mExpandedActionViewUpListener;
    private HomeView mExpandedHomeLayout;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private HomeView mHomeLayout;
    private Drawable mIcon;
    private boolean mIncludeTabs;
    private int mIndeterminateProgressStyle;
    private IcsProgressBar mIndeterminateProgressView;
    private boolean mIsCollapsable;
    private boolean mIsCollapsed;
    private int mItemPadding;
    private IcsLinearLayout mListNavLayout;
    private Drawable mLogo;
    private ActionMenuItem mLogoNavItem;
    private final OnItemSelectedListener mNavItemSelectedListener;
    private int mNavigationMode;
    private MenuBuilder mOptionsMenu;
    private int mProgressBarPadding;
    private int mProgressStyle;
    private IcsProgressBar mProgressView;
    private IcsSpinner mSpinner;
    private SpinnerAdapter mSpinnerAdapter;
    private CharSequence mSubtitle;
    private int mSubtitleStyleRes;
    private TextView mSubtitleView;
    private ScrollingTabContainerView mTabScrollView;
    private CharSequence mTitle;
    private LinearLayout mTitleLayout;
    private int mTitleStyleRes;
    private View mTitleUpView;
    private TextView mTitleView;
    private final OnClickListener mUpClickListener;
    private boolean mUserTitle;
    Callback mWindowCallback;

    /* renamed from: com.actionbarsherlock.internal.widget.ActionBarView.1 */
    class C00431 implements OnItemSelectedListener {
        C00431() {
        }

        public void onItemSelected(IcsAdapterView parent, View view, int position, long id) {
            if (ActionBarView.this.mCallback != null) {
                ActionBarView.this.mCallback.onNavigationItemSelected(position, id);
            }
        }

        public void onNothingSelected(IcsAdapterView parent) {
        }
    }

    /* renamed from: com.actionbarsherlock.internal.widget.ActionBarView.2 */
    class C00442 implements OnClickListener {
        C00442() {
        }

        public void onClick(View v) {
            MenuItemImpl item = ActionBarView.this.mExpandedMenuPresenter.mCurrentExpandedItem;
            if (item != null) {
                item.collapseActionView();
            }
        }
    }

    /* renamed from: com.actionbarsherlock.internal.widget.ActionBarView.3 */
    class C00453 implements OnClickListener {
        C00453() {
        }

        public void onClick(View v) {
            ActionBarView.this.mWindowCallback.onMenuItemSelected(ActionBarView.DISPLAY_DEFAULT, ActionBarView.this.mLogoNavItem);
        }
    }

    private class ExpandedActionViewMenuPresenter implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;

        private ExpandedActionViewMenuPresenter() {
        }

        public void initForMenu(Context context, MenuBuilder menu) {
            if (!(this.mMenu == null || this.mCurrentExpandedItem == null)) {
                this.mMenu.collapseItemActionView(this.mCurrentExpandedItem);
            }
            this.mMenu = menu;
        }

        public MenuView getMenuView(ViewGroup root) {
            return null;
        }

        public void updateMenuView(boolean cleared) {
            if (this.mCurrentExpandedItem != null) {
                boolean found = ActionBarView.DEBUG;
                if (this.mMenu != null) {
                    int count = this.mMenu.size();
                    for (int i = ActionBarView.DISPLAY_DEFAULT; i < count; i++) {
                        if (this.mMenu.getItem(i) == this.mCurrentExpandedItem) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
                }
            }
        }

        public void setCallback(MenuPresenter.Callback cb) {
        }

        public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
            return ActionBarView.DEBUG;
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        }

        public boolean flagActionItems() {
            return ActionBarView.DEBUG;
        }

        public boolean expandItemActionView(MenuBuilder menu, MenuItemImpl item) {
            ActionBarView.this.mExpandedActionView = item.getActionView();
            ActionBarView.this.mExpandedHomeLayout.setIcon(ActionBarView.this.mIcon.getConstantState().newDrawable());
            this.mCurrentExpandedItem = item;
            if (ActionBarView.this.mExpandedActionView.getParent() != ActionBarView.this) {
                ActionBarView.this.addView(ActionBarView.this.mExpandedActionView);
            }
            if (ActionBarView.this.mExpandedHomeLayout.getParent() != ActionBarView.this) {
                ActionBarView.this.addView(ActionBarView.this.mExpandedHomeLayout);
            }
            ActionBarView.this.mHomeLayout.setVisibility(8);
            if (ActionBarView.this.mTitleLayout != null) {
                ActionBarView.this.mTitleLayout.setVisibility(8);
            }
            if (ActionBarView.this.mTabScrollView != null) {
                ActionBarView.this.mTabScrollView.setVisibility(8);
            }
            if (ActionBarView.this.mSpinner != null) {
                ActionBarView.this.mSpinner.setVisibility(8);
            }
            if (ActionBarView.this.mCustomNavView != null) {
                ActionBarView.this.mCustomNavView.setVisibility(8);
            }
            ActionBarView.this.requestLayout();
            item.setActionViewExpanded(true);
            if (ActionBarView.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView) ActionBarView.this.mExpandedActionView).onActionViewExpanded();
            }
            return true;
        }

        public boolean collapseItemActionView(MenuBuilder menu, MenuItemImpl item) {
            if (ActionBarView.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView) ActionBarView.this.mExpandedActionView).onActionViewCollapsed();
            }
            ActionBarView.this.removeView(ActionBarView.this.mExpandedActionView);
            ActionBarView.this.removeView(ActionBarView.this.mExpandedHomeLayout);
            ActionBarView.this.mExpandedActionView = null;
            if ((ActionBarView.this.mDisplayOptions & 2) != 0) {
                ActionBarView.this.mHomeLayout.setVisibility(ActionBarView.DISPLAY_DEFAULT);
            }
            if ((ActionBarView.this.mDisplayOptions & 8) != 0) {
                if (ActionBarView.this.mTitleLayout == null) {
                    ActionBarView.this.initTitle();
                } else {
                    ActionBarView.this.mTitleLayout.setVisibility(ActionBarView.DISPLAY_DEFAULT);
                }
            }
            if (ActionBarView.this.mTabScrollView != null && ActionBarView.this.mNavigationMode == 2) {
                ActionBarView.this.mTabScrollView.setVisibility(ActionBarView.DISPLAY_DEFAULT);
            }
            if (ActionBarView.this.mSpinner != null && ActionBarView.this.mNavigationMode == 1) {
                ActionBarView.this.mSpinner.setVisibility(ActionBarView.DISPLAY_DEFAULT);
            }
            if (!(ActionBarView.this.mCustomNavView == null || (ActionBarView.this.mDisplayOptions & 16) == 0)) {
                ActionBarView.this.mCustomNavView.setVisibility(ActionBarView.DISPLAY_DEFAULT);
            }
            ActionBarView.this.mExpandedHomeLayout.setIcon(null);
            this.mCurrentExpandedItem = null;
            ActionBarView.this.requestLayout();
            item.setActionViewExpanded(ActionBarView.DEBUG);
            return true;
        }

        public int getId() {
            return ActionBarView.DISPLAY_DEFAULT;
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public void onRestoreInstanceState(Parcelable state) {
        }
    }

    public static class HomeView extends FrameLayout {
        private ImageView mIconView;
        private View mUpView;
        private int mUpWidth;

        public HomeView(Context context) {
            this(context, null);
        }

        public HomeView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public void setUp(boolean isUp) {
            this.mUpView.setVisibility(isUp ? ActionBarView.DISPLAY_DEFAULT : 8);
        }

        public void setIcon(Drawable icon) {
            this.mIconView.setImageDrawable(icon);
        }

        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
            onPopulateAccessibilityEvent(event);
            return true;
        }

        public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
            if (VERSION.SDK_INT >= 14) {
                super.onPopulateAccessibilityEvent(event);
            }
            CharSequence cdesc = getContentDescription();
            if (!TextUtils.isEmpty(cdesc)) {
                event.getText().add(cdesc);
            }
        }

        public boolean dispatchHoverEvent(MotionEvent event) {
            return onHoverEvent(event);
        }

        protected void onFinishInflate() {
            this.mUpView = findViewById(C0029R.id.abs__up);
            this.mIconView = (ImageView) findViewById(C0029R.id.abs__home);
        }

        public int getLeftOffset() {
            return this.mUpView.getVisibility() == 8 ? this.mUpWidth : ActionBarView.DISPLAY_DEFAULT;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            measureChildWithMargins(this.mUpView, widthMeasureSpec, ActionBarView.DISPLAY_DEFAULT, heightMeasureSpec, ActionBarView.DISPLAY_DEFAULT);
            LayoutParams upLp = (LayoutParams) this.mUpView.getLayoutParams();
            this.mUpWidth = (upLp.leftMargin + this.mUpView.getMeasuredWidth()) + upLp.rightMargin;
            int width = this.mUpView.getVisibility() == 8 ? ActionBarView.DISPLAY_DEFAULT : this.mUpWidth;
            int height = (upLp.topMargin + this.mUpView.getMeasuredHeight()) + upLp.bottomMargin;
            measureChildWithMargins(this.mIconView, widthMeasureSpec, width, heightMeasureSpec, ActionBarView.DISPLAY_DEFAULT);
            LayoutParams iconLp = (LayoutParams) this.mIconView.getLayoutParams();
            width += (iconLp.leftMargin + this.mIconView.getMeasuredWidth()) + iconLp.rightMargin;
            height = Math.max(height, (iconLp.topMargin + this.mIconView.getMeasuredHeight()) + iconLp.bottomMargin);
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            switch (widthMode) {
                case Integer.MIN_VALUE:
                    width = Math.min(width, widthSize);
                    break;
                case 1073741824:
                    width = widthSize;
                    break;
            }
            switch (heightMode) {
                case Integer.MIN_VALUE:
                    height = Math.min(height, heightSize);
                    break;
                case 1073741824:
                    height = heightSize;
                    break;
            }
            setMeasuredDimension(width, height);
        }

        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int vCenter = (b - t) / 2;
            int upOffset = ActionBarView.DISPLAY_DEFAULT;
            if (this.mUpView.getVisibility() != 8) {
                LayoutParams upLp = (LayoutParams) this.mUpView.getLayoutParams();
                int upHeight = this.mUpView.getMeasuredHeight();
                int upWidth = this.mUpView.getMeasuredWidth();
                int upTop = vCenter - (upHeight / 2);
                this.mUpView.layout(ActionBarView.DISPLAY_DEFAULT, upTop, upWidth, upTop + upHeight);
                upOffset = (upLp.leftMargin + upWidth) + upLp.rightMargin;
                l += upOffset;
            }
            LayoutParams iconLp = (LayoutParams) this.mIconView.getLayoutParams();
            int iconHeight = this.mIconView.getMeasuredHeight();
            int iconWidth = this.mIconView.getMeasuredWidth();
            int iconLeft = upOffset + Math.max(iconLp.leftMargin, ((r - l) / 2) - (iconWidth / 2));
            int iconTop = Math.max(iconLp.topMargin, vCenter - (iconHeight / 2));
            this.mIconView.layout(iconLeft, iconTop, iconLeft + iconWidth, iconTop + iconHeight);
        }
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;
        int expandedMenuItemId;
        boolean isOverflowOpen;

        /* renamed from: com.actionbarsherlock.internal.widget.ActionBarView.SavedState.1 */
        class C00461 implements Creator<SavedState> {
            C00461() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.expandedMenuItemId = in.readInt();
            this.isOverflowOpen = in.readInt() != 0 ? true : ActionBarView.DEBUG;
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.expandedMenuItemId);
            out.writeInt(this.isOverflowOpen ? 1 : ActionBarView.DISPLAY_DEFAULT);
        }

        static {
            CREATOR = new C00461();
        }
    }

    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDisplayOptions = -1;
        this.mNavItemSelectedListener = new C00431();
        this.mExpandedActionViewUpListener = new C00442();
        this.mUpClickListener = new C00453();
        setBackgroundResource(DISPLAY_DEFAULT);
        TypedArray a = context.obtainStyledAttributes(attrs, C0029R.styleable.SherlockActionBar, C0029R.attr.actionBarStyle, DISPLAY_DEFAULT);
        ApplicationInfo appInfo = context.getApplicationInfo();
        PackageManager pm = context.getPackageManager();
        this.mNavigationMode = a.getInt(6, DISPLAY_DEFAULT);
        this.mTitle = a.getText(8);
        this.mSubtitle = a.getText(9);
        this.mLogo = a.getDrawable(11);
        if (this.mLogo == null) {
            if (VERSION.SDK_INT >= 11) {
                if (context instanceof Activity) {
                    try {
                        this.mLogo = pm.getActivityLogo(((Activity) context).getComponentName());
                    } catch (NameNotFoundException e) {
                        Log.e(TAG, "Activity component name not found!", e);
                    }
                }
                if (this.mLogo == null) {
                    this.mLogo = appInfo.loadLogo(pm);
                }
            } else if (context instanceof Activity) {
                int resId = loadLogoFromManifest((Activity) context);
                if (resId != 0) {
                    this.mLogo = context.getResources().getDrawable(resId);
                }
            }
        }
        this.mIcon = a.getDrawable(10);
        if (this.mIcon == null) {
            if (context instanceof Activity) {
                try {
                    this.mIcon = pm.getActivityIcon(((Activity) context).getComponentName());
                } catch (NameNotFoundException e2) {
                    Log.e(TAG, "Activity component name not found!", e2);
                }
            }
            if (this.mIcon == null) {
                this.mIcon = appInfo.loadIcon(pm);
            }
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        int homeResId = a.getResourceId(14, C0029R.layout.abs__action_bar_home);
        this.mHomeLayout = (HomeView) inflater.inflate(homeResId, this, DEBUG);
        this.mExpandedHomeLayout = (HomeView) inflater.inflate(homeResId, this, DEBUG);
        this.mExpandedHomeLayout.setUp(true);
        this.mExpandedHomeLayout.setOnClickListener(this.mExpandedActionViewUpListener);
        this.mExpandedHomeLayout.setContentDescription(getResources().getText(C0029R.string.abs__action_bar_up_description));
        this.mTitleStyleRes = a.getResourceId(DISPLAY_DEFAULT, DISPLAY_DEFAULT);
        this.mSubtitleStyleRes = a.getResourceId(1, DISPLAY_DEFAULT);
        this.mProgressStyle = a.getResourceId(15, DISPLAY_DEFAULT);
        this.mIndeterminateProgressStyle = a.getResourceId(16, DISPLAY_DEFAULT);
        this.mProgressBarPadding = a.getDimensionPixelOffset(17, DISPLAY_DEFAULT);
        this.mItemPadding = a.getDimensionPixelOffset(18, DISPLAY_DEFAULT);
        setDisplayOptions(a.getInt(7, DISPLAY_DEFAULT));
        int customNavId = a.getResourceId(13, DISPLAY_DEFAULT);
        if (customNavId != 0) {
            this.mCustomNavView = inflater.inflate(customNavId, this, DEBUG);
            this.mNavigationMode = DISPLAY_DEFAULT;
            setDisplayOptions(this.mDisplayOptions | 16);
        }
        this.mContentHeight = a.getLayoutDimension(4, DISPLAY_DEFAULT);
        a.recycle();
        this.mLogoNavItem = new ActionMenuItem(context, DISPLAY_DEFAULT, 16908332, DISPLAY_DEFAULT, DISPLAY_DEFAULT, this.mTitle);
        this.mHomeLayout.setOnClickListener(this.mUpClickListener);
        this.mHomeLayout.setClickable(true);
        this.mHomeLayout.setFocusable(true);
    }

    private static int loadLogoFromManifest(Activity activity) {
        int logo = DISPLAY_DEFAULT;
        try {
            String thisPackage = activity.getClass().getName();
            String packageName = activity.getApplicationInfo().packageName;
            XmlResourceParser xml = activity.createPackageContext(packageName, DISPLAY_DEFAULT).getAssets().openXmlResourceParser("AndroidManifest.xml");
            for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.nextToken()) {
                if (eventType == 2) {
                    String name = xml.getName();
                    int i;
                    if ("application".equals(name)) {
                        for (i = xml.getAttributeCount() - 1; i >= 0; i--) {
                            if ("logo".equals(xml.getAttributeName(i))) {
                                logo = xml.getAttributeResourceValue(i, DISPLAY_DEFAULT);
                                break;
                            }
                        }
                    } else if ("activity".equals(name)) {
                        Integer activityLogo = null;
                        String activityPackage = null;
                        boolean isOurActivity = DEBUG;
                        for (i = xml.getAttributeCount() - 1; i >= 0; i--) {
                            String attrName = xml.getAttributeName(i);
                            if (!"logo".equals(attrName)) {
                                if ("name".equals(attrName)) {
                                    activityPackage = ActionBarSherlockCompat.cleanActivityName(packageName, xml.getAttributeValue(i));
                                    if (!thisPackage.equals(activityPackage)) {
                                        break;
                                    }
                                    isOurActivity = true;
                                }
                            } else {
                                activityLogo = Integer.valueOf(xml.getAttributeResourceValue(i, DISPLAY_DEFAULT));
                            }
                            if (!(activityLogo == null || activityPackage == null)) {
                                logo = activityLogo.intValue();
                            }
                        }
                        if (isOurActivity) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logo;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mTitleView = null;
        this.mSubtitleView = null;
        this.mTitleUpView = null;
        if (this.mTitleLayout != null && this.mTitleLayout.getParent() == this) {
            removeView(this.mTitleLayout);
        }
        this.mTitleLayout = null;
        if ((this.mDisplayOptions & 8) != 0) {
            initTitle();
        }
        if (this.mTabScrollView != null && this.mIncludeTabs) {
            ViewGroup.LayoutParams lp = this.mTabScrollView.getLayoutParams();
            if (lp != null) {
                lp.width = -2;
                lp.height = -1;
            }
            this.mTabScrollView.setAllowCollapse(true);
        }
    }

    public void setWindowCallback(Callback cb) {
        this.mWindowCallback = cb;
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.hideOverflowMenu();
            this.mActionMenuPresenter.hideSubMenus();
        }
    }

    public boolean shouldDelayChildPressedState() {
        return DEBUG;
    }

    public void initProgress() {
        this.mProgressView = new IcsProgressBar(this.mContext, null, DISPLAY_DEFAULT, this.mProgressStyle);
        this.mProgressView.setId(C0029R.id.abs__progress_horizontal);
        this.mProgressView.setMax(10000);
        addView(this.mProgressView);
    }

    public void initIndeterminateProgress() {
        this.mIndeterminateProgressView = new IcsProgressBar(this.mContext, null, DISPLAY_DEFAULT, this.mIndeterminateProgressStyle);
        this.mIndeterminateProgressView.setId(C0029R.id.abs__progress_circular);
        addView(this.mIndeterminateProgressView);
    }

    public void setSplitActionBar(boolean splitActionBar) {
        if (this.mSplitActionBar != splitActionBar) {
            if (this.mMenuView != null) {
                ViewGroup oldParent = (ViewGroup) this.mMenuView.getParent();
                if (oldParent != null) {
                    oldParent.removeView(this.mMenuView);
                }
                if (!splitActionBar) {
                    addView(this.mMenuView);
                } else if (this.mSplitView != null) {
                    this.mSplitView.addView(this.mMenuView);
                }
            }
            if (this.mSplitView != null) {
                this.mSplitView.setVisibility(splitActionBar ? DISPLAY_DEFAULT : 8);
            }
            super.setSplitActionBar(splitActionBar);
        }
    }

    public boolean isSplitActionBar() {
        return this.mSplitActionBar;
    }

    public boolean hasEmbeddedTabs() {
        return this.mIncludeTabs;
    }

    public void setEmbeddedTabView(ScrollingTabContainerView tabs) {
        if (this.mTabScrollView != null) {
            removeView(this.mTabScrollView);
        }
        this.mTabScrollView = tabs;
        this.mIncludeTabs = tabs != null ? true : DEBUG;
        if (this.mIncludeTabs && this.mNavigationMode == 2) {
            addView(this.mTabScrollView);
            ViewGroup.LayoutParams lp = this.mTabScrollView.getLayoutParams();
            lp.width = -2;
            lp.height = -1;
            tabs.setAllowCollapse(true);
        }
    }

    public void setCallback(OnNavigationListener callback) {
        this.mCallback = callback;
    }

    public void setMenu(Menu menu, MenuPresenter.Callback cb) {
        if (menu != this.mOptionsMenu) {
            ViewGroup oldParent;
            ActionMenuView menuView;
            if (this.mOptionsMenu != null) {
                this.mOptionsMenu.removeMenuPresenter(this.mActionMenuPresenter);
                this.mOptionsMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
            }
            MenuBuilder builder = (MenuBuilder) menu;
            this.mOptionsMenu = builder;
            if (this.mMenuView != null) {
                oldParent = (ViewGroup) this.mMenuView.getParent();
                if (oldParent != null) {
                    oldParent.removeView(this.mMenuView);
                }
            }
            if (this.mActionMenuPresenter == null) {
                this.mActionMenuPresenter = new ActionMenuPresenter(this.mContext);
                this.mActionMenuPresenter.setCallback(cb);
                this.mActionMenuPresenter.setId(C0029R.id.abs__action_menu_presenter);
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
            }
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -1);
            if (this.mSplitActionBar) {
                this.mActionMenuPresenter.setExpandedActionViewsExclusive(DEBUG);
                this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
                this.mActionMenuPresenter.setItemLimit(ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
                layoutParams.width = -1;
                configPresenters(builder);
                menuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                if (this.mSplitView != null) {
                    oldParent = (ViewGroup) menuView.getParent();
                    if (!(oldParent == null || oldParent == this.mSplitView)) {
                        oldParent.removeView(menuView);
                    }
                    menuView.setVisibility(getAnimatedVisibility());
                    this.mSplitView.addView(menuView, layoutParams);
                } else {
                    menuView.setLayoutParams(layoutParams);
                }
            } else {
                this.mActionMenuPresenter.setExpandedActionViewsExclusive(ResourcesCompat.getResources_getBoolean(getContext(), C0029R.bool.abs__action_bar_expanded_action_views_exclusive));
                configPresenters(builder);
                menuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                oldParent = (ViewGroup) menuView.getParent();
                if (!(oldParent == null || oldParent == this)) {
                    oldParent.removeView(menuView);
                }
                addView(menuView, layoutParams);
            }
            this.mMenuView = menuView;
        }
    }

    private void configPresenters(MenuBuilder builder) {
        if (builder != null) {
            builder.addMenuPresenter(this.mActionMenuPresenter);
            builder.addMenuPresenter(this.mExpandedMenuPresenter);
            return;
        }
        this.mActionMenuPresenter.initForMenu(this.mContext, null);
        this.mExpandedMenuPresenter.initForMenu(this.mContext, null);
        this.mActionMenuPresenter.updateMenuView(true);
        this.mExpandedMenuPresenter.updateMenuView(true);
    }

    public boolean hasExpandedActionView() {
        return (this.mExpandedMenuPresenter == null || this.mExpandedMenuPresenter.mCurrentExpandedItem == null) ? DEBUG : true;
    }

    public void collapseActionView() {
        MenuItemImpl item;
        if (this.mExpandedMenuPresenter == null) {
            item = null;
        } else {
            item = this.mExpandedMenuPresenter.mCurrentExpandedItem;
        }
        if (item != null) {
            item.collapseActionView();
        }
    }

    public void setCustomNavigationView(View view) {
        boolean showCustom = (this.mDisplayOptions & 16) != 0 ? true : DEBUG;
        if (this.mCustomNavView != null && showCustom) {
            removeView(this.mCustomNavView);
        }
        this.mCustomNavView = view;
        if (this.mCustomNavView != null && showCustom) {
            addView(this.mCustomNavView);
        }
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public void setTitle(CharSequence title) {
        this.mUserTitle = true;
        setTitleImpl(title);
    }

    public void setWindowTitle(CharSequence title) {
        if (!this.mUserTitle) {
            setTitleImpl(title);
        }
    }

    private void setTitleImpl(CharSequence title) {
        int i = DISPLAY_DEFAULT;
        this.mTitle = title;
        if (this.mTitleView != null) {
            this.mTitleView.setText(title);
            boolean visible = (this.mExpandedActionView != null || (this.mDisplayOptions & 8) == 0 || (TextUtils.isEmpty(this.mTitle) && TextUtils.isEmpty(this.mSubtitle))) ? DEBUG : true;
            LinearLayout linearLayout = this.mTitleLayout;
            if (!visible) {
                i = 8;
            }
            linearLayout.setVisibility(i);
        }
        if (this.mLogoNavItem != null) {
            this.mLogoNavItem.setTitle(title);
        }
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public void setSubtitle(CharSequence subtitle) {
        int i = DISPLAY_DEFAULT;
        this.mSubtitle = subtitle;
        if (this.mSubtitleView != null) {
            boolean visible;
            this.mSubtitleView.setText(subtitle);
            this.mSubtitleView.setVisibility(subtitle != null ? DISPLAY_DEFAULT : 8);
            if (this.mExpandedActionView != null || (this.mDisplayOptions & 8) == 0 || (TextUtils.isEmpty(this.mTitle) && TextUtils.isEmpty(this.mSubtitle))) {
                visible = DEBUG;
            } else {
                visible = true;
            }
            LinearLayout linearLayout = this.mTitleLayout;
            if (!visible) {
                i = 8;
            }
            linearLayout.setVisibility(i);
        }
    }

    public void setHomeButtonEnabled(boolean enable) {
        this.mHomeLayout.setEnabled(enable);
        this.mHomeLayout.setFocusable(enable);
        if (!enable) {
            this.mHomeLayout.setContentDescription(null);
        } else if ((this.mDisplayOptions & 4) != 0) {
            this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(C0029R.string.abs__action_bar_up_description));
        } else {
            this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(C0029R.string.abs__action_bar_home_description));
        }
    }

    public void setDisplayOptions(int options) {
        int i = 8;
        int flagsChanged = -1;
        boolean z = true;
        if (this.mDisplayOptions != -1) {
            flagsChanged = options ^ this.mDisplayOptions;
        }
        this.mDisplayOptions = options;
        if ((flagsChanged & DISPLAY_RELAYOUT_MASK) != 0) {
            boolean showHome;
            int vis;
            if ((options & 2) != 0) {
                showHome = true;
            } else {
                showHome = DEBUG;
            }
            if (showHome && this.mExpandedActionView == null) {
                vis = DISPLAY_DEFAULT;
            } else {
                vis = 8;
            }
            this.mHomeLayout.setVisibility(vis);
            if ((flagsChanged & 4) != 0) {
                boolean setUp;
                if ((options & 4) != 0) {
                    setUp = true;
                } else {
                    setUp = DEBUG;
                }
                this.mHomeLayout.setUp(setUp);
                if (setUp) {
                    setHomeButtonEnabled(true);
                }
            }
            if ((flagsChanged & 1) != 0) {
                boolean logoVis = (this.mLogo == null || (options & 1) == 0) ? DEBUG : true;
                this.mHomeLayout.setIcon(logoVis ? this.mLogo : this.mIcon);
            }
            if ((flagsChanged & 8) != 0) {
                if ((options & 8) != 0) {
                    initTitle();
                } else {
                    removeView(this.mTitleLayout);
                }
            }
            if (!(this.mTitleLayout == null || (flagsChanged & 6) == 0)) {
                boolean homeAsUp;
                if ((this.mDisplayOptions & 4) != 0) {
                    homeAsUp = true;
                } else {
                    homeAsUp = DEBUG;
                }
                View view = this.mTitleUpView;
                if (!showHome) {
                    i = homeAsUp ? DISPLAY_DEFAULT : 4;
                }
                view.setVisibility(i);
                LinearLayout linearLayout = this.mTitleLayout;
                if (showHome || !homeAsUp) {
                    z = DEBUG;
                }
                linearLayout.setEnabled(z);
            }
            if (!((flagsChanged & 16) == 0 || this.mCustomNavView == null)) {
                if ((options & 16) != 0) {
                    addView(this.mCustomNavView);
                } else {
                    removeView(this.mCustomNavView);
                }
            }
            requestLayout();
        } else {
            invalidate();
        }
        if (!this.mHomeLayout.isEnabled()) {
            this.mHomeLayout.setContentDescription(null);
        } else if ((options & 4) != 0) {
            this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(C0029R.string.abs__action_bar_up_description));
        } else {
            this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(C0029R.string.abs__action_bar_home_description));
        }
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
        if (icon == null) {
            return;
        }
        if ((this.mDisplayOptions & 1) == 0 || this.mLogo == null) {
            this.mHomeLayout.setIcon(icon);
        }
    }

    public void setIcon(int resId) {
        setIcon(this.mContext.getResources().getDrawable(resId));
    }

    public void setLogo(Drawable logo) {
        this.mLogo = logo;
        if (logo != null && (this.mDisplayOptions & 1) != 0) {
            this.mHomeLayout.setIcon(logo);
        }
    }

    public void setLogo(int resId) {
        setLogo(this.mContext.getResources().getDrawable(resId));
    }

    public void setNavigationMode(int mode) {
        int oldMode = this.mNavigationMode;
        if (mode != oldMode) {
            switch (oldMode) {
                case IcsSpinner.MODE_DROPDOWN /*1*/:
                    if (this.mListNavLayout != null) {
                        removeView(this.mListNavLayout);
                        break;
                    }
                    break;
                case IcsLinearLayout.SHOW_DIVIDER_MIDDLE /*2*/:
                    if (this.mTabScrollView != null && this.mIncludeTabs) {
                        removeView(this.mTabScrollView);
                        break;
                    }
            }
            switch (mode) {
                case IcsSpinner.MODE_DROPDOWN /*1*/:
                    if (this.mSpinner == null) {
                        this.mSpinner = new IcsSpinner(this.mContext, null, C0029R.attr.actionDropDownStyle);
                        this.mListNavLayout = (IcsLinearLayout) LayoutInflater.from(this.mContext).inflate(C0029R.layout.abs__action_bar_tab_bar_view, null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -1);
                        params.gravity = 17;
                        this.mListNavLayout.addView(this.mSpinner, params);
                    }
                    if (this.mSpinner.getAdapter() != this.mSpinnerAdapter) {
                        this.mSpinner.setAdapter(this.mSpinnerAdapter);
                    }
                    this.mSpinner.setOnItemSelectedListener(this.mNavItemSelectedListener);
                    addView(this.mListNavLayout);
                    break;
                case IcsLinearLayout.SHOW_DIVIDER_MIDDLE /*2*/:
                    if (this.mTabScrollView != null && this.mIncludeTabs) {
                        addView(this.mTabScrollView);
                        break;
                    }
            }
            this.mNavigationMode = mode;
            requestLayout();
        }
    }

    public void setDropdownAdapter(SpinnerAdapter adapter) {
        this.mSpinnerAdapter = adapter;
        if (this.mSpinner != null) {
            this.mSpinner.setAdapter(adapter);
        }
    }

    public SpinnerAdapter getDropdownAdapter() {
        return this.mSpinnerAdapter;
    }

    public void setDropdownSelectedPosition(int position) {
        this.mSpinner.setSelection(position);
    }

    public int getDropdownSelectedPosition() {
        return this.mSpinner.getSelectedItemPosition();
    }

    public View getCustomNavigationView() {
        return this.mCustomNavView;
    }

    public int getNavigationMode() {
        return this.mNavigationMode;
    }

    public int getDisplayOptions() {
        return this.mDisplayOptions;
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ActionBar.LayoutParams((int) DEFAULT_CUSTOM_GRAVITY);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        addView(this.mHomeLayout);
        if (this.mCustomNavView != null && (this.mDisplayOptions & 16) != 0) {
            ActionBarView parent = this.mCustomNavView.getParent();
            if (parent != this) {
                if (parent instanceof ViewGroup) {
                    parent.removeView(this.mCustomNavView);
                }
                addView(this.mCustomNavView);
            }
        }
    }

    private void initTitle() {
        boolean z = true;
        if (this.mTitleLayout == null) {
            boolean homeAsUp;
            boolean showHome;
            this.mTitleLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(C0029R.layout.abs__action_bar_title_item, this, DEBUG);
            this.mTitleView = (TextView) this.mTitleLayout.findViewById(C0029R.id.abs__action_bar_title);
            this.mSubtitleView = (TextView) this.mTitleLayout.findViewById(C0029R.id.abs__action_bar_subtitle);
            this.mTitleUpView = this.mTitleLayout.findViewById(C0029R.id.abs__up);
            this.mTitleLayout.setOnClickListener(this.mUpClickListener);
            if (this.mTitleStyleRes != 0) {
                this.mTitleView.setTextAppearance(this.mContext, this.mTitleStyleRes);
            }
            if (this.mTitle != null) {
                this.mTitleView.setText(this.mTitle);
            }
            if (this.mSubtitleStyleRes != 0) {
                this.mSubtitleView.setTextAppearance(this.mContext, this.mSubtitleStyleRes);
            }
            if (this.mSubtitle != null) {
                this.mSubtitleView.setText(this.mSubtitle);
                this.mSubtitleView.setVisibility(DISPLAY_DEFAULT);
            }
            if ((this.mDisplayOptions & 4) != 0) {
                homeAsUp = true;
            } else {
                homeAsUp = DEBUG;
            }
            if ((this.mDisplayOptions & 2) != 0) {
                showHome = true;
            } else {
                showHome = DEBUG;
            }
            View view = this.mTitleUpView;
            int i = !showHome ? homeAsUp ? DISPLAY_DEFAULT : 4 : 8;
            view.setVisibility(i);
            LinearLayout linearLayout = this.mTitleLayout;
            if (!homeAsUp || showHome) {
                z = DEBUG;
            }
            linearLayout.setEnabled(z);
        }
        addView(this.mTitleLayout);
        if (this.mExpandedActionView != null || (TextUtils.isEmpty(this.mTitle) && TextUtils.isEmpty(this.mSubtitle))) {
            this.mTitleLayout.setVisibility(8);
        }
    }

    public void setContextView(ActionBarContextView view) {
        this.mContextView = view;
    }

    public void setCollapsable(boolean collapsable) {
        this.mIsCollapsable = collapsable;
    }

    public boolean isCollapsed() {
        return this.mIsCollapsed;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int childCount = getChildCount();
        if (this.mIsCollapsable) {
            int visibleChildren = DISPLAY_DEFAULT;
            for (i = DISPLAY_DEFAULT; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != 8) {
                    View view = this.mMenuView;
                    if (child == r0) {
                        if (this.mMenuView.getChildCount() == 0) {
                        }
                    }
                    visibleChildren++;
                }
            }
            if (visibleChildren == 0) {
                setMeasuredDimension(DISPLAY_DEFAULT, DISPLAY_DEFAULT);
                this.mIsCollapsed = true;
                return;
            }
        }
        this.mIsCollapsed = DEBUG;
        if (MeasureSpec.getMode(widthMeasureSpec) != 1073741824) {
            throw new IllegalStateException(new StringBuilder(String.valueOf(getClass().getSimpleName())).append(" can only be used ").append("with android:layout_width=\"match_parent\" (or fill_parent)").toString());
        } else if (MeasureSpec.getMode(heightMeasureSpec) != Integer.MIN_VALUE) {
            throw new IllegalStateException(new StringBuilder(String.valueOf(getClass().getSimpleName())).append(" can only be used ").append("with android:layout_height=\"wrap_content\"").toString());
        } else {
            ViewGroup.LayoutParams lp;
            boolean showTitle;
            int itemPaddingSize;
            int listNavWidth;
            int tabWidth;
            View customView;
            ActionBar.LayoutParams ablp;
            int horizontalMargin;
            int verticalMargin;
            int customNavHeightMode;
            int i2;
            int customNavHeight;
            int customNavWidthMode;
            int customNavWidth;
            int measuredHeight;
            int paddedViewHeight;
            int contentWidth = MeasureSpec.getSize(widthMeasureSpec);
            int maxHeight = this.mContentHeight > 0 ? this.mContentHeight : MeasureSpec.getSize(heightMeasureSpec);
            int verticalPadding = getPaddingTop() + getPaddingBottom();
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            int height = maxHeight - verticalPadding;
            int childSpecHeight = MeasureSpec.makeMeasureSpec(height, Integer.MIN_VALUE);
            int availableWidth = (contentWidth - paddingLeft) - paddingRight;
            int leftOfCenter = availableWidth / 2;
            int rightOfCenter = leftOfCenter;
            HomeView homeLayout = this.mExpandedActionView != null ? this.mExpandedHomeLayout : this.mHomeLayout;
            if (homeLayout.getVisibility() != 8) {
                int homeWidthSpec;
                lp = homeLayout.getLayoutParams();
                if (lp.width < 0) {
                    homeWidthSpec = MeasureSpec.makeMeasureSpec(availableWidth, Integer.MIN_VALUE);
                } else {
                    homeWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, 1073741824);
                }
                homeLayout.measure(homeWidthSpec, MeasureSpec.makeMeasureSpec(height, 1073741824));
                int homeWidth = homeLayout.getMeasuredWidth() + homeLayout.getLeftOffset();
                availableWidth = Math.max(DISPLAY_DEFAULT, availableWidth - homeWidth);
                leftOfCenter = Math.max(DISPLAY_DEFAULT, availableWidth - homeWidth);
            }
            if (this.mMenuView != null) {
                if (this.mMenuView.getParent() == this) {
                    availableWidth = measureChildView(this.mMenuView, availableWidth, childSpecHeight, DISPLAY_DEFAULT);
                    rightOfCenter = Math.max(DISPLAY_DEFAULT, rightOfCenter - this.mMenuView.getMeasuredWidth());
                }
            }
            if (this.mIndeterminateProgressView != null) {
                if (this.mIndeterminateProgressView.getVisibility() != 8) {
                    availableWidth = measureChildView(this.mIndeterminateProgressView, availableWidth, childSpecHeight, DISPLAY_DEFAULT);
                    rightOfCenter = Math.max(DISPLAY_DEFAULT, rightOfCenter - this.mIndeterminateProgressView.getMeasuredWidth());
                }
            }
            if (this.mTitleLayout != null) {
                if (this.mTitleLayout.getVisibility() != 8) {
                    if ((this.mDisplayOptions & 8) != 0) {
                        showTitle = true;
                        if (this.mExpandedActionView == null) {
                            switch (this.mNavigationMode) {
                                case IcsSpinner.MODE_DROPDOWN /*1*/:
                                    if (this.mListNavLayout != null) {
                                        if (showTitle) {
                                            itemPaddingSize = this.mItemPadding;
                                        } else {
                                            itemPaddingSize = this.mItemPadding * 2;
                                        }
                                        availableWidth = Math.max(DISPLAY_DEFAULT, availableWidth - itemPaddingSize);
                                        leftOfCenter = Math.max(DISPLAY_DEFAULT, leftOfCenter - itemPaddingSize);
                                        this.mListNavLayout.measure(MeasureSpec.makeMeasureSpec(availableWidth, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(height, 1073741824));
                                        listNavWidth = this.mListNavLayout.getMeasuredWidth();
                                        availableWidth = Math.max(DISPLAY_DEFAULT, availableWidth - listNavWidth);
                                        leftOfCenter = Math.max(DISPLAY_DEFAULT, leftOfCenter - listNavWidth);
                                        break;
                                    }
                                    break;
                                case IcsLinearLayout.SHOW_DIVIDER_MIDDLE /*2*/:
                                    if (this.mTabScrollView != null) {
                                        if (showTitle) {
                                            itemPaddingSize = this.mItemPadding;
                                        } else {
                                            itemPaddingSize = this.mItemPadding * 2;
                                        }
                                        availableWidth = Math.max(DISPLAY_DEFAULT, availableWidth - itemPaddingSize);
                                        leftOfCenter = Math.max(DISPLAY_DEFAULT, leftOfCenter - itemPaddingSize);
                                        this.mTabScrollView.measure(MeasureSpec.makeMeasureSpec(availableWidth, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(height, 1073741824));
                                        tabWidth = this.mTabScrollView.getMeasuredWidth();
                                        availableWidth = Math.max(DISPLAY_DEFAULT, availableWidth - tabWidth);
                                        leftOfCenter = Math.max(DISPLAY_DEFAULT, leftOfCenter - tabWidth);
                                        break;
                                    }
                                    break;
                            }
                        }
                        customView = null;
                        if (this.mExpandedActionView == null) {
                            customView = this.mExpandedActionView;
                        } else {
                            if (!((this.mDisplayOptions & 16) == 0 || this.mCustomNavView == null)) {
                                customView = this.mCustomNavView;
                            }
                        }
                        if (customView != null) {
                            lp = generateLayoutParams(customView.getLayoutParams());
                            ablp = lp instanceof ActionBar.LayoutParams ? (ActionBar.LayoutParams) lp : null;
                            horizontalMargin = DISPLAY_DEFAULT;
                            verticalMargin = DISPLAY_DEFAULT;
                            if (ablp != null) {
                                horizontalMargin = ablp.leftMargin + ablp.rightMargin;
                                verticalMargin = ablp.topMargin + ablp.bottomMargin;
                            }
                            if (this.mContentHeight > 0) {
                                customNavHeightMode = Integer.MIN_VALUE;
                            } else {
                                i2 = lp.height;
                                customNavHeightMode = r0 == -2 ? 1073741824 : Integer.MIN_VALUE;
                            }
                            if (lp.height >= 0) {
                                height = Math.min(lp.height, height);
                            }
                            customNavHeight = Math.max(DISPLAY_DEFAULT, height - verticalMargin);
                            i2 = lp.width;
                            customNavWidthMode = r0 == -2 ? 1073741824 : Integer.MIN_VALUE;
                            if (lp.width < 0) {
                                i2 = Math.min(lp.width, availableWidth);
                            } else {
                                i2 = availableWidth;
                            }
                            customNavWidth = Math.max(DISPLAY_DEFAULT, i2 - horizontalMargin);
                            if (((ablp == null ? ablp.gravity : DEFAULT_CUSTOM_GRAVITY) & 7) == 1) {
                                i2 = lp.width;
                                if (r0 == -1) {
                                    customNavWidth = Math.min(leftOfCenter, rightOfCenter) * 2;
                                }
                            }
                            customView.measure(MeasureSpec.makeMeasureSpec(customNavWidth, customNavWidthMode), MeasureSpec.makeMeasureSpec(customNavHeight, customNavHeightMode));
                            availableWidth -= customView.getMeasuredWidth() + horizontalMargin;
                        }
                        if (this.mExpandedActionView == null && showTitle) {
                            availableWidth = measureChildView(this.mTitleLayout, availableWidth, MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824), DISPLAY_DEFAULT);
                            leftOfCenter = Math.max(DISPLAY_DEFAULT, leftOfCenter - this.mTitleLayout.getMeasuredWidth());
                        }
                        if (this.mContentHeight > 0) {
                            measuredHeight = DISPLAY_DEFAULT;
                            for (i = DISPLAY_DEFAULT; i < childCount; i++) {
                                paddedViewHeight = getChildAt(i).getMeasuredHeight() + verticalPadding;
                                if (paddedViewHeight > measuredHeight) {
                                    measuredHeight = paddedViewHeight;
                                }
                            }
                            setMeasuredDimension(contentWidth, measuredHeight);
                        } else {
                            setMeasuredDimension(contentWidth, maxHeight);
                        }
                        if (this.mContextView != null) {
                            this.mContextView.setContentHeight(getMeasuredHeight());
                        }
                        if (this.mProgressView != null) {
                            if (this.mProgressView.getVisibility() != 8) {
                                this.mProgressView.measure(MeasureSpec.makeMeasureSpec(contentWidth - (this.mProgressBarPadding * 2), 1073741824), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), Integer.MIN_VALUE));
                            }
                        }
                    }
                }
            }
            showTitle = DEBUG;
            if (this.mExpandedActionView == null) {
                switch (this.mNavigationMode) {
                    case IcsSpinner.MODE_DROPDOWN /*1*/:
                        if (this.mListNavLayout != null) {
                            if (showTitle) {
                                itemPaddingSize = this.mItemPadding;
                            } else {
                                itemPaddingSize = this.mItemPadding * 2;
                            }
                            availableWidth = Math.max(DISPLAY_DEFAULT, availableWidth - itemPaddingSize);
                            leftOfCenter = Math.max(DISPLAY_DEFAULT, leftOfCenter - itemPaddingSize);
                            this.mListNavLayout.measure(MeasureSpec.makeMeasureSpec(availableWidth, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(height, 1073741824));
                            listNavWidth = this.mListNavLayout.getMeasuredWidth();
                            availableWidth = Math.max(DISPLAY_DEFAULT, availableWidth - listNavWidth);
                            leftOfCenter = Math.max(DISPLAY_DEFAULT, leftOfCenter - listNavWidth);
                            break;
                        }
                        break;
                    case IcsLinearLayout.SHOW_DIVIDER_MIDDLE /*2*/:
                        if (this.mTabScrollView != null) {
                            if (showTitle) {
                                itemPaddingSize = this.mItemPadding;
                            } else {
                                itemPaddingSize = this.mItemPadding * 2;
                            }
                            availableWidth = Math.max(DISPLAY_DEFAULT, availableWidth - itemPaddingSize);
                            leftOfCenter = Math.max(DISPLAY_DEFAULT, leftOfCenter - itemPaddingSize);
                            this.mTabScrollView.measure(MeasureSpec.makeMeasureSpec(availableWidth, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(height, 1073741824));
                            tabWidth = this.mTabScrollView.getMeasuredWidth();
                            availableWidth = Math.max(DISPLAY_DEFAULT, availableWidth - tabWidth);
                            leftOfCenter = Math.max(DISPLAY_DEFAULT, leftOfCenter - tabWidth);
                            break;
                        }
                        break;
                }
            }
            customView = null;
            if (this.mExpandedActionView == null) {
                customView = this.mCustomNavView;
            } else {
                customView = this.mExpandedActionView;
            }
            if (customView != null) {
                lp = generateLayoutParams(customView.getLayoutParams());
                if (lp instanceof ActionBar.LayoutParams) {
                }
                horizontalMargin = DISPLAY_DEFAULT;
                verticalMargin = DISPLAY_DEFAULT;
                if (ablp != null) {
                    horizontalMargin = ablp.leftMargin + ablp.rightMargin;
                    verticalMargin = ablp.topMargin + ablp.bottomMargin;
                }
                if (this.mContentHeight > 0) {
                    i2 = lp.height;
                    if (r0 == -2) {
                    }
                } else {
                    customNavHeightMode = Integer.MIN_VALUE;
                }
                if (lp.height >= 0) {
                    height = Math.min(lp.height, height);
                }
                customNavHeight = Math.max(DISPLAY_DEFAULT, height - verticalMargin);
                i2 = lp.width;
                if (r0 == -2) {
                }
                if (lp.width < 0) {
                    i2 = availableWidth;
                } else {
                    i2 = Math.min(lp.width, availableWidth);
                }
                customNavWidth = Math.max(DISPLAY_DEFAULT, i2 - horizontalMargin);
                if (ablp == null) {
                }
                if (((ablp == null ? ablp.gravity : DEFAULT_CUSTOM_GRAVITY) & 7) == 1) {
                    i2 = lp.width;
                    if (r0 == -1) {
                        customNavWidth = Math.min(leftOfCenter, rightOfCenter) * 2;
                    }
                }
                customView.measure(MeasureSpec.makeMeasureSpec(customNavWidth, customNavWidthMode), MeasureSpec.makeMeasureSpec(customNavHeight, customNavHeightMode));
                availableWidth -= customView.getMeasuredWidth() + horizontalMargin;
            }
            availableWidth = measureChildView(this.mTitleLayout, availableWidth, MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824), DISPLAY_DEFAULT);
            leftOfCenter = Math.max(DISPLAY_DEFAULT, leftOfCenter - this.mTitleLayout.getMeasuredWidth());
            if (this.mContentHeight > 0) {
                setMeasuredDimension(contentWidth, maxHeight);
            } else {
                measuredHeight = DISPLAY_DEFAULT;
                for (i = DISPLAY_DEFAULT; i < childCount; i++) {
                    paddedViewHeight = getChildAt(i).getMeasuredHeight() + verticalPadding;
                    if (paddedViewHeight > measuredHeight) {
                        measuredHeight = paddedViewHeight;
                    }
                }
                setMeasuredDimension(contentWidth, measuredHeight);
            }
            if (this.mContextView != null) {
                this.mContextView.setContentHeight(getMeasuredHeight());
            }
            if (this.mProgressView != null) {
                if (this.mProgressView.getVisibility() != 8) {
                    this.mProgressView.measure(MeasureSpec.makeMeasureSpec(contentWidth - (this.mProgressBarPadding * 2), 1073741824), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), Integer.MIN_VALUE));
                }
            }
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int x = getPaddingLeft();
        int y = getPaddingTop();
        int contentHeight = ((b - t) - getPaddingTop()) - getPaddingBottom();
        if (contentHeight > 0) {
            HomeView homeLayout = this.mExpandedActionView != null ? this.mExpandedHomeLayout : this.mHomeLayout;
            if (homeLayout.getVisibility() != 8) {
                int leftOffset = homeLayout.getLeftOffset();
                x += positionChild(homeLayout, x + leftOffset, y, contentHeight) + leftOffset;
            }
            if (this.mExpandedActionView == null) {
                boolean showTitle;
                if (this.mTitleLayout != null) {
                    if (this.mTitleLayout.getVisibility() != 8) {
                        if ((this.mDisplayOptions & 8) != 0) {
                            showTitle = true;
                            if (showTitle) {
                                x += positionChild(this.mTitleLayout, x, y, contentHeight);
                            }
                            switch (this.mNavigationMode) {
                                case IcsSpinner.MODE_DROPDOWN /*1*/:
                                    if (this.mListNavLayout != null) {
                                        if (showTitle) {
                                            x += this.mItemPadding;
                                        }
                                        x += positionChild(this.mListNavLayout, x, y, contentHeight) + this.mItemPadding;
                                        break;
                                    }
                                    break;
                                case IcsLinearLayout.SHOW_DIVIDER_MIDDLE /*2*/:
                                    if (this.mTabScrollView != null) {
                                        if (showTitle) {
                                            x += this.mItemPadding;
                                        }
                                        x += positionChild(this.mTabScrollView, x, y, contentHeight) + this.mItemPadding;
                                        break;
                                    }
                                    break;
                            }
                        }
                    }
                }
                showTitle = DEBUG;
                if (showTitle) {
                    x += positionChild(this.mTitleLayout, x, y, contentHeight);
                }
                switch (this.mNavigationMode) {
                    case IcsSpinner.MODE_DROPDOWN /*1*/:
                        if (this.mListNavLayout != null) {
                            if (showTitle) {
                                x += this.mItemPadding;
                            }
                            x += positionChild(this.mListNavLayout, x, y, contentHeight) + this.mItemPadding;
                            break;
                        }
                        break;
                    case IcsLinearLayout.SHOW_DIVIDER_MIDDLE /*2*/:
                        if (this.mTabScrollView != null) {
                            if (showTitle) {
                                x += this.mItemPadding;
                            }
                            x += positionChild(this.mTabScrollView, x, y, contentHeight) + this.mItemPadding;
                            break;
                        }
                        break;
                }
            }
            int menuLeft = (r - l) - getPaddingRight();
            if (this.mMenuView != null) {
                if (this.mMenuView.getParent() == this) {
                    positionChildInverse(this.mMenuView, menuLeft, y, contentHeight);
                    menuLeft -= this.mMenuView.getMeasuredWidth();
                }
            }
            if (this.mIndeterminateProgressView != null) {
                if (this.mIndeterminateProgressView.getVisibility() != 8) {
                    positionChildInverse(this.mIndeterminateProgressView, menuLeft, y, contentHeight);
                    menuLeft -= this.mIndeterminateProgressView.getMeasuredWidth();
                }
            }
            View customView = null;
            if (this.mExpandedActionView != null) {
                customView = this.mExpandedActionView;
            } else {
                if (!((this.mDisplayOptions & 16) == 0 || this.mCustomNavView == null)) {
                    customView = this.mCustomNavView;
                }
            }
            if (customView != null) {
                ViewGroup.LayoutParams lp = customView.getLayoutParams();
                ActionBar.LayoutParams ablp = lp instanceof ActionBar.LayoutParams ? (ActionBar.LayoutParams) lp : null;
                int gravity = ablp != null ? ablp.gravity : DEFAULT_CUSTOM_GRAVITY;
                int navWidth = customView.getMeasuredWidth();
                int topMargin = DISPLAY_DEFAULT;
                int bottomMargin = DISPLAY_DEFAULT;
                if (ablp != null) {
                    x += ablp.leftMargin;
                    menuLeft -= ablp.rightMargin;
                    topMargin = ablp.topMargin;
                    bottomMargin = ablp.bottomMargin;
                }
                int hgravity = gravity & 7;
                if (hgravity == 1) {
                    int centeredLeft = ((getRight() - getLeft()) - navWidth) / 2;
                    if (centeredLeft < x) {
                        hgravity = 3;
                    } else if (centeredLeft + navWidth > menuLeft) {
                        hgravity = 5;
                    }
                } else if (gravity == -1) {
                    hgravity = 3;
                }
                int xpos = DISPLAY_DEFAULT;
                switch (hgravity) {
                    case IcsSpinner.MODE_DROPDOWN /*1*/:
                        xpos = ((getRight() - getLeft()) - navWidth) / 2;
                        break;
                    case C0029R.styleable.SherlockTheme_actionBarTabTextStyle /*3*/:
                        xpos = x;
                        break;
                    case C0029R.styleable.SherlockTheme_actionBarStyle /*5*/:
                        xpos = menuLeft - navWidth;
                        break;
                }
                int vgravity = gravity & 112;
                if (gravity == -1) {
                    vgravity = 16;
                }
                int ypos = DISPLAY_DEFAULT;
                switch (vgravity) {
                    case Menu.CATEGORY_SHIFT /*16*/:
                        ypos = ((((getBottom() - getTop()) - getPaddingBottom()) - getPaddingTop()) - customView.getMeasuredHeight()) / 2;
                        break;
                    case C0029R.styleable.SherlockTheme_windowSplitActionBar /*48*/:
                        ypos = getPaddingTop() + topMargin;
                        break;
                    case 80:
                        ypos = ((getHeight() - getPaddingBottom()) - customView.getMeasuredHeight()) - bottomMargin;
                        break;
                }
                int customWidth = customView.getMeasuredWidth();
                customView.layout(xpos, ypos, xpos + customWidth, customView.getMeasuredHeight() + ypos);
                x += customWidth;
            }
            if (this.mProgressView != null) {
                this.mProgressView.bringToFront();
                int halfProgressHeight = this.mProgressView.getMeasuredHeight() / 2;
                this.mProgressView.layout(this.mProgressBarPadding, -halfProgressHeight, this.mProgressBarPadding + this.mProgressView.getMeasuredWidth(), halfProgressHeight);
            }
        }
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ActionBar.LayoutParams(getContext(), attrs);
    }

    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp == null) {
            return generateDefaultLayoutParams();
        }
        return lp;
    }

    public Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        if (!(this.mExpandedMenuPresenter == null || this.mExpandedMenuPresenter.mCurrentExpandedItem == null)) {
            state.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        state.isOverflowOpen = isOverflowMenuShowing();
        return state;
    }

    public void onRestoreInstanceState(Parcelable p) {
        SavedState state = (SavedState) p;
        super.onRestoreInstanceState(state.getSuperState());
        if (!(state.expandedMenuItemId == 0 || this.mExpandedMenuPresenter == null || this.mOptionsMenu == null)) {
            MenuItem item = this.mOptionsMenu.findItem(state.expandedMenuItemId);
            if (item != null) {
                item.expandActionView();
            }
        }
        if (state.isOverflowOpen) {
            postShowOverflowMenu();
        }
    }
}
