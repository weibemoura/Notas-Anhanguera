package com.actionbarsherlock;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.internal.ActionBarSherlockCompat;
import com.actionbarsherlock.internal.ActionBarSherlockNative;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.ActionMode.Callback;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;

public abstract class ActionBarSherlock {
    private static final Class<?>[] CONSTRUCTOR_ARGS;
    protected static final boolean DEBUG = false;
    public static final int FLAG_DELEGATE = 1;
    private static final HashMap<Implementation, Class<? extends ActionBarSherlock>> IMPLEMENTATIONS;
    protected static final String TAG = "ActionBarSherlock";
    protected final Activity mActivity;
    protected final boolean mIsDelegate;
    protected MenuInflater mMenuInflater;

    public interface OnCreatePanelMenuListener {
        boolean onCreatePanelMenu(int i, Menu menu);
    }

    public interface OnPreparePanelListener {
        boolean onPreparePanel(int i, View view, Menu menu);
    }

    public interface OnMenuItemSelectedListener {
        boolean onMenuItemSelected(int i, MenuItem menuItem);
    }

    public interface OnActionModeStartedListener {
        void onActionModeStarted(ActionMode actionMode);
    }

    public interface OnActionModeFinishedListener {
        void onActionModeFinished(ActionMode actionMode);
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Implementation {
        public static final int DEFAULT_API = -1;
        public static final int DEFAULT_DPI = -1;

        int api() default -1;

        int dpi() default -1;
    }

    public interface OnCreateOptionsMenuListener {
        boolean onCreateOptionsMenu(Menu menu);
    }

    public interface OnOptionsItemSelectedListener {
        boolean onOptionsItemSelected(MenuItem menuItem);
    }

    public interface OnPrepareOptionsMenuListener {
        boolean onPrepareOptionsMenu(Menu menu);
    }

    public abstract void addContentView(View view, LayoutParams layoutParams);

    public abstract boolean dispatchCreateOptionsMenu(android.view.Menu menu);

    public abstract void dispatchInvalidateOptionsMenu();

    public abstract boolean dispatchOptionsItemSelected(android.view.MenuItem menuItem);

    public abstract boolean dispatchPrepareOptionsMenu(android.view.Menu menu);

    public abstract ActionBar getActionBar();

    protected abstract Context getThemedContext();

    public abstract boolean hasFeature(int i);

    public abstract boolean requestFeature(int i);

    public abstract void setContentView(int i);

    public abstract void setContentView(View view, LayoutParams layoutParams);

    public abstract void setProgress(int i);

    public abstract void setProgressBarIndeterminate(boolean z);

    public abstract void setProgressBarIndeterminateVisibility(boolean z);

    public abstract void setProgressBarVisibility(boolean z);

    public abstract void setSecondaryProgress(int i);

    public abstract void setTitle(CharSequence charSequence);

    public abstract void setUiOptions(int i);

    public abstract void setUiOptions(int i, int i2);

    public abstract ActionMode startActionMode(Callback callback);

    static {
        CONSTRUCTOR_ARGS = new Class[]{Activity.class, Integer.TYPE};
        IMPLEMENTATIONS = new HashMap();
        registerImplementation(ActionBarSherlockCompat.class);
        registerImplementation(ActionBarSherlockNative.class);
    }

    public static void registerImplementation(Class<? extends ActionBarSherlock> implementationClass) {
        if (!implementationClass.isAnnotationPresent(Implementation.class)) {
            throw new IllegalArgumentException("Class " + implementationClass.getSimpleName() + " is not annotated with @Implementation");
        } else if (!IMPLEMENTATIONS.containsValue(implementationClass)) {
            IMPLEMENTATIONS.put((Implementation) implementationClass.getAnnotation(Implementation.class), implementationClass);
        }
    }

    public static boolean unregisterImplementation(Class<? extends ActionBarSherlock> implementationClass) {
        return IMPLEMENTATIONS.values().remove(implementationClass);
    }

    public static ActionBarSherlock wrap(Activity activity) {
        return wrap(activity, 0);
    }

    public static ActionBarSherlock wrap(Activity activity, int flags) {
        Iterator<Implementation> keys;
        HashMap<Implementation, Class<? extends ActionBarSherlock>> impls = new HashMap(IMPLEMENTATIONS);
        boolean hasQualfier = DEBUG;
        for (Implementation key : impls.keySet()) {
            if (key.dpi() == 213) {
                hasQualfier = true;
                break;
            }
        }
        if (hasQualfier) {
            boolean isTvDpi = activity.getResources().getDisplayMetrics().densityDpi == 213 ? true : DEBUG;
            keys = impls.keySet().iterator();
            while (keys.hasNext()) {
                int keyDpi = ((Implementation) keys.next()).dpi();
                if ((isTvDpi && keyDpi != 213) || (!isTvDpi && keyDpi == 213)) {
                    keys.remove();
                }
            }
        }
        hasQualfier = DEBUG;
        for (Implementation key2 : impls.keySet()) {
            if (key2.api() != -1) {
                hasQualfier = true;
                break;
            }
        }
        if (hasQualfier) {
            int runtimeApi = VERSION.SDK_INT;
            int bestApi = 0;
            keys = impls.keySet().iterator();
            while (keys.hasNext()) {
                int keyApi = ((Implementation) keys.next()).api();
                if (keyApi > runtimeApi) {
                    keys.remove();
                } else if (keyApi > bestApi) {
                    bestApi = keyApi;
                }
            }
            keys = impls.keySet().iterator();
            while (keys.hasNext()) {
                if (((Implementation) keys.next()).api() != bestApi) {
                    keys.remove();
                }
            }
        }
        if (impls.size() > FLAG_DELEGATE) {
            throw new IllegalStateException("More than one implementation matches configuration.");
        } else if (impls.isEmpty()) {
            throw new IllegalStateException("No implementations match configuration.");
        } else {
            try {
                return (ActionBarSherlock) ((Class) impls.values().iterator().next()).getConstructor(CONSTRUCTOR_ARGS).newInstance(new Object[]{activity, Integer.valueOf(flags)});
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e2) {
                throw new RuntimeException(e2);
            } catch (InstantiationException e3) {
                throw new RuntimeException(e3);
            } catch (IllegalAccessException e4) {
                throw new RuntimeException(e4);
            } catch (InvocationTargetException e5) {
                throw new RuntimeException(e5);
            }
        }
    }

    protected ActionBarSherlock(Activity activity, int flags) {
        this.mActivity = activity;
        this.mIsDelegate = (flags & FLAG_DELEGATE) != 0 ? true : DEBUG;
    }

    public void dispatchConfigurationChanged(Configuration newConfig) {
    }

    public void dispatchPostResume() {
    }

    public void dispatchPause() {
    }

    public void dispatchStop() {
    }

    public boolean dispatchOpenOptionsMenu() {
        return DEBUG;
    }

    public boolean dispatchCloseOptionsMenu() {
        return DEBUG;
    }

    public void dispatchPostCreate(Bundle savedInstanceState) {
    }

    public void dispatchTitleChanged(CharSequence title, int color) {
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return DEBUG;
    }

    public boolean dispatchMenuOpened(int featureId, android.view.Menu menu) {
        return DEBUG;
    }

    public void dispatchPanelClosed(int featureId, android.view.Menu menu) {
    }

    public void dispatchDestroy() {
    }

    protected final boolean callbackCreateOptionsMenu(Menu menu) {
        if (this.mActivity instanceof OnCreatePanelMenuListener) {
            return this.mActivity.onCreatePanelMenu(0, menu);
        }
        if (this.mActivity instanceof OnCreateOptionsMenuListener) {
            return this.mActivity.onCreateOptionsMenu(menu);
        }
        return true;
    }

    protected final boolean callbackPrepareOptionsMenu(Menu menu) {
        if (this.mActivity instanceof OnPreparePanelListener) {
            return this.mActivity.onPreparePanel(0, null, menu);
        }
        if (this.mActivity instanceof OnPrepareOptionsMenuListener) {
            return this.mActivity.onPrepareOptionsMenu(menu);
        }
        return true;
    }

    protected final boolean callbackOptionsItemSelected(MenuItem item) {
        if (this.mActivity instanceof OnMenuItemSelectedListener) {
            return this.mActivity.onMenuItemSelected(0, item);
        }
        if (this.mActivity instanceof OnOptionsItemSelectedListener) {
            return this.mActivity.onOptionsItemSelected(item);
        }
        return DEBUG;
    }

    public void setContentView(View view) {
        setContentView(view, new LayoutParams(-1, -1));
    }

    public void setTitle(int resId) {
        setTitle(this.mActivity.getString(resId));
    }

    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            if (getActionBar() != null) {
                this.mMenuInflater = new MenuInflater(getThemedContext());
            } else {
                this.mMenuInflater = new MenuInflater(this.mActivity);
            }
        }
        return this.mMenuInflater;
    }
}
