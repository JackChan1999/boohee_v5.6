package com.squareup.leakcanary.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.HeapDump;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@TargetApi(11)
public final class DisplayLeakActivity extends Activity {
    private static final String SHOW_LEAK_EXTRA = "show_latest";
    private static final String TAG             = "DisplayLeakActivity";
    private Button     actionButton;
    private TextView   failureView;
    private List<Leak> leaks;
    private ListView   listView;
    private int        maxStoredLeaks;
    private String     visibleLeakRefKey;

    static class Leak {
        final HeapDump       heapDump;
        final AnalysisResult result;

        Leak(HeapDump heapDump, AnalysisResult result) {
            this.heapDump = heapDump;
            this.result = result;
        }
    }

    class LeakListAdapter extends BaseAdapter {
        LeakListAdapter() {
        }

        public int getCount() {
            return DisplayLeakActivity.this.leaks.size();
        }

        public Leak getItem(int position) {
            return (Leak) DisplayLeakActivity.this.leaks.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            String index;
            String title;
            if (convertView == null) {
                convertView = LayoutInflater.from(DisplayLeakActivity.this).inflate(R.layout
                        .__leak_canary_leak_row, parent, false);
            }
            TextView titleView = (TextView) convertView.findViewById(R.id.__leak_canary_row_text);
            TextView timeView = (TextView) convertView.findViewById(R.id.__leak_canary_row_time);
            Leak leak = getItem(position);
            if (position == 0 && DisplayLeakActivity.this.leaks.size() == DisplayLeakActivity
                    .this.maxStoredLeaks) {
                index = "MAX. ";
            } else {
                index = (DisplayLeakActivity.this.leaks.size() - position) + ". ";
            }
            if (leak.result.failure == null) {
                title = index + DisplayLeakActivity.this.getString(R.string
                        .__leak_canary_class_has_leaked, new Object[]{DisplayLeakActivity
                        .classSimpleName(leak.result.className)});
            } else {
                title = index + leak.result.failure.getClass().getSimpleName() + " " + leak
                        .result.failure.getMessage();
            }
            titleView.setText(title);
            timeView.setText(DateUtils.formatDateTime(DisplayLeakActivity.this, leak.heapDump
                    .heapDumpFile.lastModified(), 17));
            return convertView;
        }
    }

    static class LoadLeaks implements Runnable {
        static final Executor        backgroundExecutor = Executors.newSingleThreadExecutor();
        static final List<LoadLeaks> inFlight           = new ArrayList();
        private DisplayLeakActivity activityOrNull;
        private final File    leakDirectory = LeakCanaryInternals.detectedLeakDirectory();
        private final Handler mainHandler   = new Handler(Looper.getMainLooper());

        static void load(DisplayLeakActivity activity) {
            LoadLeaks loadLeaks = new LoadLeaks(activity);
            inFlight.add(loadLeaks);
            backgroundExecutor.execute(loadLeaks);
        }

        static void forgetActivity() {
            for (LoadLeaks loadLeaks : inFlight) {
                loadLeaks.activityOrNull = null;
            }
            inFlight.clear();
        }

        LoadLeaks(DisplayLeakActivity activity) {
            this.activityOrNull = activity;
        }

        public void run() {
            FileInputStream fileInputStream;
            Exception e;
            Exception e2;
            Throwable th;
            final List<Leak> leaks = new ArrayList();
            File[] files = this.leakDirectory.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".hprof");
                }
            });
            if (files != null) {
                for (File heapDumpFile : files) {
                    File resultFile = LeakCanaryInternals.leakResultFile(heapDumpFile);
                    fileInputStream = null;
                    try {
                        FileInputStream fis = new FileInputStream(resultFile);
                        try {
                            ObjectInputStream ois = new ObjectInputStream(fis);
                            leaks.add(new Leak((HeapDump) ois.readObject(), (AnalysisResult) ois
                                    .readObject()));
                            if (fis != null) {
                                try {
                                    fis.close();
                                    fileInputStream = fis;
                                } catch (IOException e3) {
                                    fileInputStream = fis;
                                }
                            }
                        } catch (IOException e4) {
                            e = e4;
                            fileInputStream = fis;
                            e2 = e;
                            try {
                                heapDumpFile.delete();
                                resultFile.delete();
                                Log.e(DisplayLeakActivity.TAG, "Could not read result file, " +
                                        "deleted result and heap dump:" + heapDumpFile, e2);
                                if (fileInputStream == null) {
                                    try {
                                        fileInputStream.close();
                                    } catch (IOException e5) {
                                    }
                                }
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        } catch (ClassNotFoundException e6) {
                            e = e6;
                            fileInputStream = fis;
                            e2 = e;
                            heapDumpFile.delete();
                            resultFile.delete();
                            Log.e(DisplayLeakActivity.TAG, "Could not read result file, deleted " +
                                    "result and heap dump:" + heapDumpFile, e2);
                            if (fileInputStream == null) {
                                fileInputStream.close();
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            fileInputStream = fis;
                        }
                    } catch (IOException e7) {
                        e = e7;
                        e2 = e;
                        heapDumpFile.delete();
                        resultFile.delete();
                        Log.e(DisplayLeakActivity.TAG, "Could not read result file, deleted " +
                                "result and heap dump:" + heapDumpFile, e2);
                        if (fileInputStream == null) {
                            fileInputStream.close();
                        }
                    } catch (ClassNotFoundException e8) {
                        e = e8;
                        e2 = e;
                        heapDumpFile.delete();
                        resultFile.delete();
                        Log.e(DisplayLeakActivity.TAG, "Could not read result file, deleted " +
                                "result and heap dump:" + heapDumpFile, e2);
                        if (fileInputStream == null) {
                            fileInputStream.close();
                        }
                    }
                }
                Collections.sort(leaks, new Comparator<Leak>() {
                    public int compare(Leak lhs, Leak rhs) {
                        return Long.valueOf(rhs.heapDump.heapDumpFile.lastModified()).compareTo
                                (Long.valueOf(lhs.heapDump.heapDumpFile.lastModified()));
                    }
                });
            }
            this.mainHandler.post(new Runnable() {
                public void run() {
                    LoadLeaks.inFlight.remove(LoadLeaks.this);
                    if (LoadLeaks.this.activityOrNull != null) {
                        LoadLeaks.this.activityOrNull.leaks = leaks;
                        LoadLeaks.this.activityOrNull.updateUi();
                    }
                }
            });
            return;
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e9) {
                }
            }
            throw th;
            throw th;
        }
    }

    public static PendingIntent createPendingIntent(Context context, String referenceKey) {
        Intent intent = new Intent(context, DisplayLeakActivity.class);
        intent.putExtra(SHOW_LEAK_EXTRA, referenceKey);
        intent.setFlags(335544320);
        return PendingIntent.getActivity(context, 1, intent, 134217728);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.visibleLeakRefKey = savedInstanceState.getString("visibleLeakRefKey");
        } else {
            Intent intent = getIntent();
            if (intent.hasExtra(SHOW_LEAK_EXTRA)) {
                this.visibleLeakRefKey = intent.getStringExtra(SHOW_LEAK_EXTRA);
            }
        }
        this.leaks = (List) getLastNonConfigurationInstance();
        setContentView(R.layout.__leak_canary_display_leak);
        this.listView = (ListView) findViewById(R.id.__leak_canary_display_leak_list);
        this.failureView = (TextView) findViewById(R.id.__leak_canary_display_leak_failure);
        this.actionButton = (Button) findViewById(R.id.__leak_canary_action);
        this.maxStoredLeaks = getResources().getInteger(R.integer.__leak_canary_max_stored_leaks);
        updateUi();
    }

    public Object onRetainNonConfigurationInstance() {
        return this.leaks;
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("visibleLeakRefKey", this.visibleLeakRefKey);
    }

    protected void onResume() {
        super.onResume();
        LoadLeaks.load(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        LoadLeaks.forgetActivity();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (getVisibleLeak() == null) {
            return false;
        }
        menu.add(R.string.__leak_canary_share_leak).setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                DisplayLeakActivity.this.shareLeak();
                return true;
            }
        });
        menu.add(R.string.__leak_canary_share_heap_dump).setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                DisplayLeakActivity.this.shareHeapDump();
                return true;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            this.visibleLeakRefKey = null;
            updateUi();
        }
        return true;
    }

    public void onBackPressed() {
        if (this.visibleLeakRefKey != null) {
            this.visibleLeakRefKey = null;
            updateUi();
            return;
        }
        super.onBackPressed();
    }

    private void shareLeak() {
        Leak visibleLeak = getVisibleLeak();
        String leakInfo = LeakCanary.leakInfo(this, visibleLeak.heapDump, visibleLeak.result, true);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", leakInfo);
        startActivity(Intent.createChooser(intent, getString(R.string.__leak_canary_share_with)));
    }

    private void shareHeapDump() {
        File heapDumpFile = getVisibleLeak().heapDump.heapDumpFile;
        heapDumpFile.setReadable(true, false);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("application/octet-stream");
        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(heapDumpFile));
        startActivity(Intent.createChooser(intent, getString(R.string.__leak_canary_share_with)));
    }

    private void updateUi() {
        int i = 8;
        if (this.leaks == null) {
            setTitle("Loading leaks...");
            return;
        }
        if (this.leaks.isEmpty()) {
            this.visibleLeakRefKey = null;
        }
        Leak visibleLeak = getVisibleLeak();
        if (visibleLeak == null) {
            this.visibleLeakRefKey = null;
        }
        ListAdapter listAdapter = this.listView.getAdapter();
        this.listView.setVisibility(0);
        this.failureView.setVisibility(8);
        if (visibleLeak != null) {
            AnalysisResult result = visibleLeak.result;
            if (result.failure != null) {
                this.listView.setVisibility(8);
                this.failureView.setVisibility(0);
                this.failureView.setText(getString(R.string.__leak_canary_failure_report) + Log
                        .getStackTraceString(result.failure));
                setTitle(R.string.__leak_canary_analysis_failed);
                invalidateOptionsMenu();
                getActionBar().setDisplayHomeAsUpEnabled(true);
                this.actionButton.setVisibility(0);
                this.actionButton.setText(R.string.__leak_canary_delete);
                this.listView.setAdapter(null);
                return;
            }
            DisplayLeakAdapter adapter;
            if (listAdapter instanceof DisplayLeakAdapter) {
                adapter = (DisplayLeakAdapter) listAdapter;
            } else {
                adapter = new DisplayLeakAdapter();
                this.listView.setAdapter(adapter);
                this.listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                            long id) {
                        adapter.toggleRow(position);
                    }
                });
                invalidateOptionsMenu();
                getActionBar().setDisplayHomeAsUpEnabled(true);
                this.actionButton.setVisibility(0);
                this.actionButton.setText(R.string.__leak_canary_delete);
                this.actionButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Leak visibleLeak = DisplayLeakActivity.this.getVisibleLeak();
                        LeakCanaryInternals.leakResultFile(visibleLeak.heapDump.heapDumpFile)
                                .delete();
                        visibleLeak.heapDump.heapDumpFile.delete();
                        DisplayLeakActivity.this.visibleLeakRefKey = null;
                        DisplayLeakActivity.this.leaks.remove(visibleLeak);
                        DisplayLeakActivity.this.updateUi();
                    }
                });
            }
            HeapDump heapDump = visibleLeak.heapDump;
            adapter.update(result.leakTrace, heapDump.referenceKey, heapDump.referenceName);
            setTitle(getString(R.string.__leak_canary_class_has_leaked, new
                    Object[]{classSimpleName(result.className)}));
            return;
        }
        if (listAdapter instanceof LeakListAdapter) {
            ((LeakListAdapter) listAdapter).notifyDataSetChanged();
        } else {
            this.listView.setAdapter(new LeakListAdapter());
            this.listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long
                        id) {
                    DisplayLeakActivity.this.visibleLeakRefKey = ((Leak) DisplayLeakActivity.this
                            .leaks.get(position)).heapDump.referenceKey;
                    DisplayLeakActivity.this.updateUi();
                }
            });
            invalidateOptionsMenu();
            setTitle(getString(R.string.__leak_canary_leak_list_title, new
                    Object[]{getPackageName()}));
            getActionBar().setDisplayHomeAsUpEnabled(false);
            this.actionButton.setText(R.string.__leak_canary_delete_all);
            this.actionButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    File[] files = LeakCanaryInternals.detectedLeakDirectory().listFiles();
                    if (files != null) {
                        for (File file : files) {
                            file.delete();
                        }
                    }
                    DisplayLeakActivity.this.leaks = Collections.emptyList();
                    DisplayLeakActivity.this.updateUi();
                }
            });
        }
        Button button = this.actionButton;
        if (this.leaks.size() != 0) {
            i = 0;
        }
        button.setVisibility(i);
    }

    private Leak getVisibleLeak() {
        if (this.leaks == null) {
            return null;
        }
        for (Leak leak : this.leaks) {
            if (leak.heapDump.referenceKey.equals(this.visibleLeakRefKey)) {
                return leak;
            }
        }
        return null;
    }

    static String classSimpleName(String className) {
        int separator = className.lastIndexOf(46);
        return separator == -1 ? className : className.substring(separator + 1);
    }
}
