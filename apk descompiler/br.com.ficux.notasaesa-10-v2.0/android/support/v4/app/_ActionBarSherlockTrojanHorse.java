package android.support.v4.app;

import android.view.View;
import com.actionbarsherlock.ActionBarSherlock.OnCreatePanelMenuListener;
import com.actionbarsherlock.ActionBarSherlock.OnMenuItemSelectedListener;
import com.actionbarsherlock.ActionBarSherlock.OnPreparePanelListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import java.util.ArrayList;

public abstract class _ActionBarSherlockTrojanHorse extends FragmentActivity implements OnCreatePanelMenuListener, OnPreparePanelListener, OnMenuItemSelectedListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "_ActionBarSherlockTrojanHorse";
    private ArrayList<Fragment> mCreatedMenus;

    public interface OnCreateOptionsMenuListener {
        void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater);
    }

    public interface OnOptionsItemSelectedListener {
        boolean onOptionsItemSelected(MenuItem menuItem);
    }

    public interface OnPrepareOptionsMenuListener {
        void onPrepareOptionsMenu(Menu menu);
    }

    public abstract MenuInflater getSupportMenuInflater();

    public abstract boolean onCreateOptionsMenu(Menu menu);

    public abstract boolean onOptionsItemSelected(MenuItem menuItem);

    public abstract boolean onPrepareOptionsMenu(Menu menu);

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId != 0) {
            return DEBUG;
        }
        int i;
        Fragment f;
        boolean result = onCreateOptionsMenu(menu);
        MenuInflater inflater = getSupportMenuInflater();
        boolean show = DEBUG;
        ArrayList<Fragment> newMenus = null;
        if (this.mFragments.mActive != null) {
            for (i = 0; i < this.mFragments.mAdded.size(); i++) {
                f = (Fragment) this.mFragments.mAdded.get(i);
                if (f != null && !f.mHidden && f.mHasMenu && f.mMenuVisible && (f instanceof OnCreateOptionsMenuListener)) {
                    show = true;
                    ((OnCreateOptionsMenuListener) f).onCreateOptionsMenu(menu, inflater);
                    if (newMenus == null) {
                        newMenus = new ArrayList();
                    }
                    newMenus.add(f);
                }
            }
        }
        if (this.mCreatedMenus != null) {
            for (i = 0; i < this.mCreatedMenus.size(); i++) {
                f = (Fragment) this.mCreatedMenus.get(i);
                if (newMenus == null || !newMenus.contains(f)) {
                    f.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = newMenus;
        return result | show;
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (featureId != 0) {
            return DEBUG;
        }
        boolean result = onPrepareOptionsMenu(menu);
        boolean show = DEBUG;
        if (this.mFragments.mActive != null) {
            for (int i = 0; i < this.mFragments.mAdded.size(); i++) {
                Fragment f = (Fragment) this.mFragments.mAdded.get(i);
                if (f != null && !f.mHidden && f.mHasMenu && f.mMenuVisible && (f instanceof OnPrepareOptionsMenuListener)) {
                    show = true;
                    ((OnPrepareOptionsMenuListener) f).onPrepareOptionsMenu(menu);
                }
            }
        }
        return (result | show) & menu.hasVisibleItems();
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == 0) {
            if (onOptionsItemSelected(item)) {
                return true;
            }
            if (this.mFragments.mActive != null) {
                for (int i = 0; i < this.mFragments.mAdded.size(); i++) {
                    Fragment f = (Fragment) this.mFragments.mAdded.get(i);
                    if (f != null && !f.mHidden && f.mHasMenu && f.mMenuVisible && (f instanceof OnOptionsItemSelectedListener) && ((OnOptionsItemSelectedListener) f).onOptionsItemSelected(item)) {
                        return true;
                    }
                }
            }
        }
        return DEBUG;
    }
}
