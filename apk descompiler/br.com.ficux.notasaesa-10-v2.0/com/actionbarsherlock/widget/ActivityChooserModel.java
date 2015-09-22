package com.actionbarsherlock.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import org.xmlpull.v1.XmlSerializer;

class ActivityChooserModel extends DataSetObservable {
    private static final String ATTRIBUTE_ACTIVITY = "activity";
    private static final String ATTRIBUTE_TIME = "time";
    private static final String ATTRIBUTE_WEIGHT = "weight";
    private static final boolean DEBUG = false;
    private static final int DEFAULT_ACTIVITY_INFLATION = 5;
    private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0f;
    public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
    public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
    private static final String HISTORY_FILE_EXTENSION = ".xml";
    private static final int INVALID_INDEX = -1;
    private static final String LOG_TAG;
    private static final SerialExecutor SERIAL_EXECUTOR;
    private static final String TAG_HISTORICAL_RECORD = "historical-record";
    private static final String TAG_HISTORICAL_RECORDS = "historical-records";
    private static final Map<String, ActivityChooserModel> sDataModelRegistry;
    private static final Object sRegistryLock;
    private final List<ActivityResolveInfo> mActivites;
    private OnChooseActivityListener mActivityChoserModelPolicy;
    private ActivitySorter mActivitySorter;
    private boolean mCanReadHistoricalData;
    private final Context mContext;
    private final Handler mHandler;
    private final List<HistoricalRecord> mHistoricalRecords;
    private boolean mHistoricalRecordsChanged;
    private final String mHistoryFileName;
    private int mHistoryMaxSize;
    private final Object mInstanceLock;
    private Intent mIntent;
    private boolean mReadShareHistoryCalled;

    public interface ActivityChooserModelClient {
        void setActivityChooserModel(ActivityChooserModel activityChooserModel);
    }

    public final class ActivityResolveInfo implements Comparable<ActivityResolveInfo> {
        public final ResolveInfo resolveInfo;
        public float weight;

        public ActivityResolveInfo(ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
        }

        public int hashCode() {
            return Float.floatToIntBits(this.weight) + 31;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return ActivityChooserModel.DEBUG;
            }
            if (getClass() != obj.getClass()) {
                return ActivityChooserModel.DEBUG;
            }
            if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(((ActivityResolveInfo) obj).weight)) {
                return ActivityChooserModel.DEBUG;
            }
            return true;
        }

        public int compareTo(ActivityResolveInfo another) {
            return Float.floatToIntBits(another.weight) - Float.floatToIntBits(this.weight);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            builder.append("resolveInfo:").append(this.resolveInfo.toString());
            builder.append("; weight:").append(new BigDecimal((double) this.weight));
            builder.append("]");
            return builder.toString();
        }
    }

    public interface ActivitySorter {
        void sort(Intent intent, List<ActivityResolveInfo> list, List<HistoricalRecord> list2);
    }

    private final class DefaultSorter implements ActivitySorter {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95f;
        private final Map<String, ActivityResolveInfo> mPackageNameToActivityMap;

        private DefaultSorter() {
            this.mPackageNameToActivityMap = new HashMap();
        }

        public void sort(Intent intent, List<ActivityResolveInfo> activities, List<HistoricalRecord> historicalRecords) {
            int i;
            Map<String, ActivityResolveInfo> packageNameToActivityMap = this.mPackageNameToActivityMap;
            packageNameToActivityMap.clear();
            int activityCount = activities.size();
            for (i = 0; i < activityCount; i++) {
                ActivityResolveInfo activity = (ActivityResolveInfo) activities.get(i);
                activity.weight = 0.0f;
                packageNameToActivityMap.put(activity.resolveInfo.activityInfo.packageName, activity);
            }
            int lastShareIndex = historicalRecords.size() + ActivityChooserModel.INVALID_INDEX;
            float nextRecordWeight = ActivityChooserModel.DEFAULT_HISTORICAL_RECORD_WEIGHT;
            for (i = lastShareIndex; i >= 0; i += ActivityChooserModel.INVALID_INDEX) {
                HistoricalRecord historicalRecord = (HistoricalRecord) historicalRecords.get(i);
                activity = (ActivityResolveInfo) packageNameToActivityMap.get(historicalRecord.activity.getPackageName());
                if (activity != null) {
                    activity.weight += historicalRecord.weight * nextRecordWeight;
                    nextRecordWeight *= WEIGHT_DECAY_COEFFICIENT;
                }
            }
            Collections.sort(activities);
        }
    }

    public static final class HistoricalRecord {
        public final ComponentName activity;
        public final long time;
        public final float weight;

        public HistoricalRecord(String activityName, long time, float weight) {
            this(ComponentName.unflattenFromString(activityName), time, weight);
        }

        public HistoricalRecord(ComponentName activityName, long time, float weight) {
            this.activity = activityName;
            this.time = time;
            this.weight = weight;
        }

        public int hashCode() {
            return (((((this.activity == null ? 0 : this.activity.hashCode()) + 31) * 31) + ((int) (this.time ^ (this.time >>> 32)))) * 31) + Float.floatToIntBits(this.weight);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return ActivityChooserModel.DEBUG;
            }
            if (getClass() != obj.getClass()) {
                return ActivityChooserModel.DEBUG;
            }
            HistoricalRecord other = (HistoricalRecord) obj;
            if (this.activity == null) {
                if (other.activity != null) {
                    return ActivityChooserModel.DEBUG;
                }
            } else if (!this.activity.equals(other.activity)) {
                return ActivityChooserModel.DEBUG;
            }
            if (this.time != other.time) {
                return ActivityChooserModel.DEBUG;
            }
            if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(other.weight)) {
                return ActivityChooserModel.DEBUG;
            }
            return true;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            builder.append("; activity:").append(this.activity);
            builder.append("; time:").append(this.time);
            builder.append("; weight:").append(new BigDecimal((double) this.weight));
            builder.append("]");
            return builder.toString();
        }
    }

    private final class HistoryLoader implements Runnable {

        /* renamed from: com.actionbarsherlock.widget.ActivityChooserModel.HistoryLoader.1 */
        class C00521 implements Runnable {
            C00521() {
            }

            public void run() {
                ActivityChooserModel.this.pruneExcessiveHistoricalRecordsLocked();
                ActivityChooserModel.this.sortActivities();
            }
        }

        private HistoryLoader() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r24 = this;
            r4 = 0;
            r0 = r24;
            r0 = com.actionbarsherlock.widget.ActivityChooserModel.this;	 Catch:{ FileNotFoundException -> 0x0077 }
            r21 = r0;
            r21 = r21.mContext;	 Catch:{ FileNotFoundException -> 0x0077 }
            r0 = r24;
            r0 = com.actionbarsherlock.widget.ActivityChooserModel.this;	 Catch:{ FileNotFoundException -> 0x0077 }
            r22 = r0;
            r22 = r22.mHistoryFileName;	 Catch:{ FileNotFoundException -> 0x0077 }
            r4 = r21.openFileInput(r22);	 Catch:{ FileNotFoundException -> 0x0077 }
            r12 = android.util.Xml.newPullParser();	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r21 = 0;
            r0 = r21;
            r12.setInput(r4, r0);	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r17 = 0;
        L_0x0026:
            r21 = 1;
            r0 = r17;
            r1 = r21;
            if (r0 == r1) goto L_0x0036;
        L_0x002e:
            r21 = 2;
            r0 = r17;
            r1 = r21;
            if (r0 != r1) goto L_0x0079;
        L_0x0036:
            r21 = "historical-records";
            r22 = r12.getName();	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r21 = r21.equals(r22);	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            if (r21 != 0) goto L_0x007e;
        L_0x0042:
            r21 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r22 = "Share records file does not start with historical-records tag.";
            r21.<init>(r22);	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            throw r21;	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
        L_0x004a:
            r20 = move-exception;
            r21 = com.actionbarsherlock.widget.ActivityChooserModel.LOG_TAG;	 Catch:{ all -> 0x0158 }
            r22 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0158 }
            r23 = "Error reading historical recrod file: ";
            r22.<init>(r23);	 Catch:{ all -> 0x0158 }
            r0 = r24;
            r0 = com.actionbarsherlock.widget.ActivityChooserModel.this;	 Catch:{ all -> 0x0158 }
            r23 = r0;
            r23 = r23.mHistoryFileName;	 Catch:{ all -> 0x0158 }
            r22 = r22.append(r23);	 Catch:{ all -> 0x0158 }
            r22 = r22.toString();	 Catch:{ all -> 0x0158 }
            r0 = r21;
            r1 = r22;
            r2 = r20;
            android.util.Log.e(r0, r1, r2);	 Catch:{ all -> 0x0158 }
            if (r4 == 0) goto L_0x0076;
        L_0x0073:
            r4.close();	 Catch:{ IOException -> 0x01a9 }
        L_0x0076:
            return;
        L_0x0077:
            r5 = move-exception;
            goto L_0x0076;
        L_0x0079:
            r17 = r12.next();	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            goto L_0x0026;
        L_0x007e:
            r14 = new java.util.ArrayList;	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r14.<init>();	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
        L_0x0083:
            r17 = r12.next();	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r21 = 1;
            r0 = r17;
            r1 = r21;
            if (r0 != r1) goto L_0x00ca;
        L_0x008f:
            r0 = r24;
            r0 = com.actionbarsherlock.widget.ActivityChooserModel.this;	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r21 = r0;
            r22 = r21.mInstanceLock;	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            monitor-enter(r22);	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r18 = new java.util.LinkedHashSet;	 Catch:{ all -> 0x01a6 }
            r0 = r18;
            r0.<init>(r14);	 Catch:{ all -> 0x01a6 }
            r0 = r24;
            r0 = com.actionbarsherlock.widget.ActivityChooserModel.this;	 Catch:{ all -> 0x01a6 }
            r21 = r0;
            r7 = r21.mHistoricalRecords;	 Catch:{ all -> 0x01a6 }
            r8 = r7.size();	 Catch:{ all -> 0x01a6 }
            r9 = r8 + -1;
        L_0x00b1:
            if (r9 >= 0) goto L_0x015f;
        L_0x00b3:
            r21 = r7.size();	 Catch:{ all -> 0x01a6 }
            r23 = r18.size();	 Catch:{ all -> 0x01a6 }
            r0 = r21;
            r1 = r23;
            if (r0 != r1) goto L_0x016e;
        L_0x00c1:
            monitor-exit(r22);	 Catch:{ all -> 0x01a6 }
            if (r4 == 0) goto L_0x0076;
        L_0x00c4:
            r4.close();	 Catch:{ IOException -> 0x00c8 }
            goto L_0x0076;
        L_0x00c8:
            r21 = move-exception;
            goto L_0x0076;
        L_0x00ca:
            r21 = 3;
            r0 = r17;
            r1 = r21;
            if (r0 == r1) goto L_0x0083;
        L_0x00d2:
            r21 = 4;
            r0 = r17;
            r1 = r21;
            if (r0 == r1) goto L_0x0083;
        L_0x00da:
            r11 = r12.getName();	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r21 = "historical-record";
            r0 = r21;
            r21 = r0.equals(r11);	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            if (r21 != 0) goto L_0x011f;
        L_0x00e8:
            r21 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r22 = "Share records file not well-formed.";
            r21.<init>(r22);	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            throw r21;	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
        L_0x00f0:
            r10 = move-exception;
            r21 = com.actionbarsherlock.widget.ActivityChooserModel.LOG_TAG;	 Catch:{ all -> 0x0158 }
            r22 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0158 }
            r23 = "Error reading historical recrod file: ";
            r22.<init>(r23);	 Catch:{ all -> 0x0158 }
            r0 = r24;
            r0 = com.actionbarsherlock.widget.ActivityChooserModel.this;	 Catch:{ all -> 0x0158 }
            r23 = r0;
            r23 = r23.mHistoryFileName;	 Catch:{ all -> 0x0158 }
            r22 = r22.append(r23);	 Catch:{ all -> 0x0158 }
            r22 = r22.toString();	 Catch:{ all -> 0x0158 }
            r0 = r21;
            r1 = r22;
            android.util.Log.e(r0, r1, r10);	 Catch:{ all -> 0x0158 }
            if (r4 == 0) goto L_0x0076;
        L_0x0117:
            r4.close();	 Catch:{ IOException -> 0x011c }
            goto L_0x0076;
        L_0x011c:
            r21 = move-exception;
            goto L_0x0076;
        L_0x011f:
            r21 = 0;
            r22 = "activity";
            r0 = r21;
            r1 = r22;
            r3 = r12.getAttributeValue(r0, r1);	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r21 = 0;
            r22 = "time";
            r0 = r21;
            r1 = r22;
            r21 = r12.getAttributeValue(r0, r1);	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r15 = java.lang.Long.parseLong(r21);	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r21 = 0;
            r22 = "weight";
            r0 = r21;
            r1 = r22;
            r21 = r12.getAttributeValue(r0, r1);	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r19 = java.lang.Float.parseFloat(r21);	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r13 = new com.actionbarsherlock.widget.ActivityChooserModel$HistoricalRecord;	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r0 = r15;
            r2 = r19;
            r13.<init>(r3, r0, r2);	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            r14.add(r13);	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
            goto L_0x0083;
        L_0x0158:
            r21 = move-exception;
            if (r4 == 0) goto L_0x015e;
        L_0x015b:
            r4.close();	 Catch:{ IOException -> 0x01ac }
        L_0x015e:
            throw r21;
        L_0x015f:
            r6 = r7.get(r9);	 Catch:{ all -> 0x01a6 }
            r6 = (com.actionbarsherlock.widget.ActivityChooserModel.HistoricalRecord) r6;	 Catch:{ all -> 0x01a6 }
            r0 = r18;
            r0.add(r6);	 Catch:{ all -> 0x01a6 }
            r9 = r9 + -1;
            goto L_0x00b1;
        L_0x016e:
            r7.clear();	 Catch:{ all -> 0x01a6 }
            r0 = r18;
            r7.addAll(r0);	 Catch:{ all -> 0x01a6 }
            r0 = r24;
            r0 = com.actionbarsherlock.widget.ActivityChooserModel.this;	 Catch:{ all -> 0x01a6 }
            r21 = r0;
            r23 = 1;
            r0 = r21;
            r1 = r23;
            r0.mHistoricalRecordsChanged = r1;	 Catch:{ all -> 0x01a6 }
            r0 = r24;
            r0 = com.actionbarsherlock.widget.ActivityChooserModel.this;	 Catch:{ all -> 0x01a6 }
            r21 = r0;
            r21 = r21.mHandler;	 Catch:{ all -> 0x01a6 }
            r23 = new com.actionbarsherlock.widget.ActivityChooserModel$HistoryLoader$1;	 Catch:{ all -> 0x01a6 }
            r23.<init>();	 Catch:{ all -> 0x01a6 }
            r0 = r21;
            r1 = r23;
            r0.post(r1);	 Catch:{ all -> 0x01a6 }
            monitor-exit(r22);	 Catch:{ all -> 0x01a6 }
            if (r4 == 0) goto L_0x0076;
        L_0x019e:
            r4.close();	 Catch:{ IOException -> 0x01a3 }
            goto L_0x0076;
        L_0x01a3:
            r21 = move-exception;
            goto L_0x0076;
        L_0x01a6:
            r21 = move-exception;
            monitor-exit(r22);	 Catch:{ all -> 0x01a6 }
            throw r21;	 Catch:{ XmlPullParserException -> 0x004a, IOException -> 0x00f0 }
        L_0x01a9:
            r21 = move-exception;
            goto L_0x0076;
        L_0x01ac:
            r22 = move-exception;
            goto L_0x015e;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.actionbarsherlock.widget.ActivityChooserModel.HistoryLoader.run():void");
        }
    }

    private final class HistoryPersister implements Runnable {
        private HistoryPersister() {
        }

        public void run() {
            Throwable th;
            synchronized (ActivityChooserModel.this.mInstanceLock) {
                try {
                    List<HistoricalRecord> records = new ArrayList(ActivityChooserModel.this.mHistoricalRecords);
                    try {
                        try {
                            FileOutputStream fos = ActivityChooserModel.this.mContext.openFileOutput(ActivityChooserModel.this.mHistoryFileName, 0);
                            XmlSerializer serializer = Xml.newSerializer();
                            try {
                                serializer.setOutput(fos, null);
                                serializer.startDocument("UTF-8", Boolean.valueOf(true));
                                serializer.startTag(null, ActivityChooserModel.TAG_HISTORICAL_RECORDS);
                                int recordCount = records.size();
                                for (int i = 0; i < recordCount; i++) {
                                    HistoricalRecord record = (HistoricalRecord) records.remove(0);
                                    serializer.startTag(null, ActivityChooserModel.TAG_HISTORICAL_RECORD);
                                    serializer.attribute(null, ActivityChooserModel.ATTRIBUTE_ACTIVITY, record.activity.flattenToString());
                                    serializer.attribute(null, ActivityChooserModel.ATTRIBUTE_TIME, String.valueOf(record.time));
                                    serializer.attribute(null, ActivityChooserModel.ATTRIBUTE_WEIGHT, String.valueOf(record.weight));
                                    serializer.endTag(null, ActivityChooserModel.TAG_HISTORICAL_RECORD);
                                }
                                serializer.endTag(null, ActivityChooserModel.TAG_HISTORICAL_RECORDS);
                                serializer.endDocument();
                                if (fos != null) {
                                    try {
                                        fos.close();
                                    } catch (IOException e) {
                                    }
                                }
                            } catch (IllegalArgumentException iae) {
                                Log.e(ActivityChooserModel.LOG_TAG, "Error writing historical recrod file: " + ActivityChooserModel.this.mHistoryFileName, iae);
                                if (fos != null) {
                                    try {
                                        fos.close();
                                    } catch (IOException e2) {
                                    }
                                }
                            } catch (IllegalStateException ise) {
                                Log.e(ActivityChooserModel.LOG_TAG, "Error writing historical recrod file: " + ActivityChooserModel.this.mHistoryFileName, ise);
                                if (fos != null) {
                                    try {
                                        fos.close();
                                    } catch (IOException e3) {
                                    }
                                }
                            } catch (IOException ioe) {
                                Log.e(ActivityChooserModel.LOG_TAG, "Error writing historical recrod file: " + ActivityChooserModel.this.mHistoryFileName, ioe);
                                if (fos != null) {
                                    try {
                                        fos.close();
                                    } catch (IOException e4) {
                                    }
                                }
                            } catch (Throwable th2) {
                                if (fos != null) {
                                    try {
                                        fos.close();
                                    } catch (IOException e5) {
                                    }
                                }
                            }
                        } catch (FileNotFoundException fnfe) {
                            Log.e(ActivityChooserModel.LOG_TAG, "Error writing historical recrod file: " + ActivityChooserModel.this.mHistoryFileName, fnfe);
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        List<HistoricalRecord> list = records;
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    throw th;
                }
            }
        }
    }

    public interface OnChooseActivityListener {
        boolean onChooseActivity(ActivityChooserModel activityChooserModel, Intent intent);
    }

    private static class SerialExecutor implements Executor {
        Runnable mActive;
        final LinkedList<Runnable> mTasks;

        /* renamed from: com.actionbarsherlock.widget.ActivityChooserModel.SerialExecutor.1 */
        class C00531 implements Runnable {
            private final /* synthetic */ Runnable val$r;

            C00531(Runnable runnable) {
                this.val$r = runnable;
            }

            public void run() {
                try {
                    this.val$r.run();
                } finally {
                    SerialExecutor.this.scheduleNext();
                }
            }
        }

        private SerialExecutor() {
            this.mTasks = new LinkedList();
        }

        public synchronized void execute(Runnable r) {
            this.mTasks.offer(new C00531(r));
            if (this.mActive == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            Runnable runnable = (Runnable) this.mTasks.poll();
            this.mActive = runnable;
            if (runnable != null) {
                this.mActive.run();
            }
        }
    }

    static {
        LOG_TAG = ActivityChooserModel.class.getSimpleName();
        sRegistryLock = new Object();
        sDataModelRegistry = new HashMap();
        SERIAL_EXECUTOR = new SerialExecutor();
    }

    public static ActivityChooserModel get(Context context, String historyFileName) {
        ActivityChooserModel dataModel;
        synchronized (sRegistryLock) {
            dataModel = (ActivityChooserModel) sDataModelRegistry.get(historyFileName);
            if (dataModel == null) {
                dataModel = new ActivityChooserModel(context, historyFileName);
                sDataModelRegistry.put(historyFileName, dataModel);
            }
            dataModel.readHistoricalData();
        }
        return dataModel;
    }

    private ActivityChooserModel(Context context, String historyFileName) {
        this.mInstanceLock = new Object();
        this.mActivites = new ArrayList();
        this.mHistoricalRecords = new ArrayList();
        this.mActivitySorter = new DefaultSorter();
        this.mHistoryMaxSize = DEFAULT_HISTORY_MAX_LENGTH;
        this.mCanReadHistoricalData = true;
        this.mReadShareHistoryCalled = DEBUG;
        this.mHistoricalRecordsChanged = true;
        this.mHandler = new Handler();
        this.mContext = context.getApplicationContext();
        if (TextUtils.isEmpty(historyFileName) || historyFileName.endsWith(HISTORY_FILE_EXTENSION)) {
            this.mHistoryFileName = historyFileName;
        } else {
            this.mHistoryFileName = new StringBuilder(String.valueOf(historyFileName)).append(HISTORY_FILE_EXTENSION).toString();
        }
    }

    public void setIntent(Intent intent) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == intent) {
                return;
            }
            this.mIntent = intent;
            loadActivitiesLocked();
        }
    }

    public Intent getIntent() {
        Intent intent;
        synchronized (this.mInstanceLock) {
            intent = this.mIntent;
        }
        return intent;
    }

    public int getActivityCount() {
        int size;
        synchronized (this.mInstanceLock) {
            size = this.mActivites.size();
        }
        return size;
    }

    public ResolveInfo getActivity(int index) {
        ResolveInfo resolveInfo;
        synchronized (this.mInstanceLock) {
            resolveInfo = ((ActivityResolveInfo) this.mActivites.get(index)).resolveInfo;
        }
        return resolveInfo;
    }

    public int getActivityIndex(ResolveInfo activity) {
        List<ActivityResolveInfo> activities = this.mActivites;
        int activityCount = activities.size();
        for (int i = 0; i < activityCount; i++) {
            if (((ActivityResolveInfo) activities.get(i)).resolveInfo == activity) {
                return i;
            }
        }
        return INVALID_INDEX;
    }

    public Intent chooseActivity(int index) {
        ActivityResolveInfo chosenActivity = (ActivityResolveInfo) this.mActivites.get(index);
        ComponentName chosenName = new ComponentName(chosenActivity.resolveInfo.activityInfo.packageName, chosenActivity.resolveInfo.activityInfo.name);
        Intent choiceIntent = new Intent(this.mIntent);
        choiceIntent.setComponent(chosenName);
        if (this.mActivityChoserModelPolicy != null) {
            if (this.mActivityChoserModelPolicy.onChooseActivity(this, new Intent(choiceIntent))) {
                return null;
            }
        }
        addHisoricalRecord(new HistoricalRecord(chosenName, System.currentTimeMillis(), (float) DEFAULT_HISTORICAL_RECORD_WEIGHT));
        return choiceIntent;
    }

    public void setOnChooseActivityListener(OnChooseActivityListener listener) {
        this.mActivityChoserModelPolicy = listener;
    }

    public ResolveInfo getDefaultActivity() {
        synchronized (this.mInstanceLock) {
            if (this.mActivites.isEmpty()) {
                return null;
            }
            ResolveInfo resolveInfo = ((ActivityResolveInfo) this.mActivites.get(0)).resolveInfo;
            return resolveInfo;
        }
    }

    public void setDefaultActivity(int index) {
        float weight;
        ActivityResolveInfo newDefaultActivity = (ActivityResolveInfo) this.mActivites.get(index);
        ActivityResolveInfo oldDefaultActivity = (ActivityResolveInfo) this.mActivites.get(0);
        if (oldDefaultActivity != null) {
            weight = (oldDefaultActivity.weight - newDefaultActivity.weight) + 5.0f;
        } else {
            weight = DEFAULT_HISTORICAL_RECORD_WEIGHT;
        }
        addHisoricalRecord(new HistoricalRecord(new ComponentName(newDefaultActivity.resolveInfo.activityInfo.packageName, newDefaultActivity.resolveInfo.activityInfo.name), System.currentTimeMillis(), weight));
    }

    private void readHistoricalData() {
        synchronized (this.mInstanceLock) {
            if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged) {
                this.mCanReadHistoricalData = DEBUG;
                this.mReadShareHistoryCalled = true;
                if (!TextUtils.isEmpty(this.mHistoryFileName)) {
                    SERIAL_EXECUTOR.execute(new HistoryLoader());
                }
                return;
            }
        }
    }

    private void persistHistoricalData() {
        synchronized (this.mInstanceLock) {
            if (!this.mReadShareHistoryCalled) {
                throw new IllegalStateException("No preceding call to #readHistoricalData");
            } else if (this.mHistoricalRecordsChanged) {
                this.mHistoricalRecordsChanged = DEBUG;
                this.mCanReadHistoricalData = true;
                if (!TextUtils.isEmpty(this.mHistoryFileName)) {
                    SERIAL_EXECUTOR.execute(new HistoryPersister());
                }
            }
        }
    }

    public void setActivitySorter(ActivitySorter activitySorter) {
        synchronized (this.mInstanceLock) {
            if (this.mActivitySorter == activitySorter) {
                return;
            }
            this.mActivitySorter = activitySorter;
            sortActivities();
        }
    }

    private void sortActivities() {
        synchronized (this.mInstanceLock) {
            if (!(this.mActivitySorter == null || this.mActivites.isEmpty())) {
                this.mActivitySorter.sort(this.mIntent, this.mActivites, Collections.unmodifiableList(this.mHistoricalRecords));
                notifyChanged();
            }
        }
    }

    public void setHistoryMaxSize(int historyMaxSize) {
        synchronized (this.mInstanceLock) {
            if (this.mHistoryMaxSize == historyMaxSize) {
                return;
            }
            this.mHistoryMaxSize = historyMaxSize;
            pruneExcessiveHistoricalRecordsLocked();
            sortActivities();
        }
    }

    public int getHistoryMaxSize() {
        int i;
        synchronized (this.mInstanceLock) {
            i = this.mHistoryMaxSize;
        }
        return i;
    }

    public int getHistorySize() {
        int size;
        synchronized (this.mInstanceLock) {
            size = this.mHistoricalRecords.size();
        }
        return size;
    }

    private boolean addHisoricalRecord(HistoricalRecord historicalRecord) {
        boolean added;
        synchronized (this.mInstanceLock) {
            added = this.mHistoricalRecords.add(historicalRecord);
            if (added) {
                this.mHistoricalRecordsChanged = true;
                pruneExcessiveHistoricalRecordsLocked();
                persistHistoricalData();
                sortActivities();
            }
        }
        return added;
    }

    private void pruneExcessiveHistoricalRecordsLocked() {
        List<HistoricalRecord> choiceRecords = this.mHistoricalRecords;
        int pruneCount = choiceRecords.size() - this.mHistoryMaxSize;
        if (pruneCount > 0) {
            this.mHistoricalRecordsChanged = true;
            for (int i = 0; i < pruneCount; i++) {
                HistoricalRecord historicalRecord = (HistoricalRecord) choiceRecords.remove(0);
            }
        }
    }

    private void loadActivitiesLocked() {
        this.mActivites.clear();
        if (this.mIntent != null) {
            List<ResolveInfo> resolveInfos = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
            int resolveInfoCount = resolveInfos.size();
            for (int i = 0; i < resolveInfoCount; i++) {
                this.mActivites.add(new ActivityResolveInfo((ResolveInfo) resolveInfos.get(i)));
            }
            sortActivities();
            return;
        }
        notifyChanged();
    }
}
