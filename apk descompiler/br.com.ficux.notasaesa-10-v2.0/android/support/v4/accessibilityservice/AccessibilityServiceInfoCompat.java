package android.support.v4.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import com.actionbarsherlock.internal.widget.IcsLinearLayout;
import com.actionbarsherlock.internal.widget.IcsSpinner;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class AccessibilityServiceInfoCompat {
    public static final int FEEDBACK_ALL_MASK = -1;
    private static final AccessibilityServiceInfoVersionImpl IMPL;

    interface AccessibilityServiceInfoVersionImpl {
        boolean getCanRetrieveWindowContent(AccessibilityServiceInfo accessibilityServiceInfo);

        String getDescription(AccessibilityServiceInfo accessibilityServiceInfo);

        String getId(AccessibilityServiceInfo accessibilityServiceInfo);

        ResolveInfo getResolveInfo(AccessibilityServiceInfo accessibilityServiceInfo);

        String getSettingsActivityName(AccessibilityServiceInfo accessibilityServiceInfo);
    }

    static class AccessibilityServiceInfoStubImpl implements AccessibilityServiceInfoVersionImpl {
        AccessibilityServiceInfoStubImpl() {
        }

        public boolean getCanRetrieveWindowContent(AccessibilityServiceInfo info) {
            return false;
        }

        public String getDescription(AccessibilityServiceInfo info) {
            return null;
        }

        public String getId(AccessibilityServiceInfo info) {
            return null;
        }

        public ResolveInfo getResolveInfo(AccessibilityServiceInfo info) {
            return null;
        }

        public String getSettingsActivityName(AccessibilityServiceInfo info) {
            return null;
        }
    }

    static class AccessibilityServiceInfoIcsImpl extends AccessibilityServiceInfoStubImpl {
        AccessibilityServiceInfoIcsImpl() {
        }

        public boolean getCanRetrieveWindowContent(AccessibilityServiceInfo info) {
            return AccessibilityServiceInfoCompatIcs.getCanRetrieveWindowContent(info);
        }

        public String getDescription(AccessibilityServiceInfo info) {
            return AccessibilityServiceInfoCompatIcs.getDescription(info);
        }

        public String getId(AccessibilityServiceInfo info) {
            return AccessibilityServiceInfoCompatIcs.getId(info);
        }

        public ResolveInfo getResolveInfo(AccessibilityServiceInfo info) {
            return AccessibilityServiceInfoCompatIcs.getResolveInfo(info);
        }

        public String getSettingsActivityName(AccessibilityServiceInfo info) {
            return AccessibilityServiceInfoCompatIcs.getSettingsActivityName(info);
        }
    }

    static {
        if (VERSION.SDK_INT >= 14) {
            IMPL = new AccessibilityServiceInfoIcsImpl();
        } else {
            IMPL = new AccessibilityServiceInfoStubImpl();
        }
    }

    private AccessibilityServiceInfoCompat() {
    }

    public static String getId(AccessibilityServiceInfo info) {
        return IMPL.getId(info);
    }

    public static ResolveInfo getResolveInfo(AccessibilityServiceInfo info) {
        return IMPL.getResolveInfo(info);
    }

    public static String getSettingsActivityName(AccessibilityServiceInfo info) {
        return IMPL.getSettingsActivityName(info);
    }

    public static boolean getCanRetrieveWindowContent(AccessibilityServiceInfo info) {
        return IMPL.getCanRetrieveWindowContent(info);
    }

    public static String getDescription(AccessibilityServiceInfo info) {
        return IMPL.getDescription(info);
    }

    public static String feedbackTypeToString(int feedbackType) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        while (feedbackType > 0) {
            int feedbackTypeFlag = 1 << Integer.numberOfTrailingZeros(feedbackType);
            feedbackType &= feedbackTypeFlag ^ FEEDBACK_ALL_MASK;
            if (builder.length() > 1) {
                builder.append(", ");
            }
            switch (feedbackTypeFlag) {
                case IcsSpinner.MODE_DROPDOWN /*1*/:
                    builder.append("FEEDBACK_SPOKEN");
                    break;
                case IcsLinearLayout.SHOW_DIVIDER_MIDDLE /*2*/:
                    builder.append("FEEDBACK_HAPTIC");
                    break;
                case ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_DEFAULT /*4*/:
                    builder.append("FEEDBACK_AUDIBLE");
                    break;
                case MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW /*8*/:
                    builder.append("FEEDBACK_VISUAL");
                    break;
                case Menu.CATEGORY_SHIFT /*16*/:
                    builder.append("FEEDBACK_GENERIC");
                    break;
                default:
                    break;
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public static String flagToString(int flag) {
        switch (flag) {
            case IcsSpinner.MODE_DROPDOWN /*1*/:
                return "DEFAULT";
            default:
                return null;
        }
    }
}
