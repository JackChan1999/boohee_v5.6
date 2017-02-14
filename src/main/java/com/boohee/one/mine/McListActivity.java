package com.boohee.one.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boohee.api.RecordApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.mine.McPeriod;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.widgets.LightAlertDialog;

import java.util.ArrayList;

import org.json.JSONObject;

public class McListActivity extends GestureActivity {
    static final String  TAG       = McListActivity.class.getName();
    private      boolean edit      = false;
    private      boolean isChecked = false;
    private ListView     listView;
    private McListAdaper mcListAdapter;
    private ArrayList<McPeriod> mcPeriodList = new ArrayList();

    public class McListAdaper extends BaseAdapter {
        boolean edit = false;
        ArrayList<McPeriod> mcPeriodList;

        public class ViewHolder {
            public TextView  McCircle;
            public TextView  Mcday;
            public ImageView editBtn;
            public TextView  startDate;
        }

        private class removeMcListener implements OnClickListener {
            String date;

            private removeMcListener(String date) {
                this.date = date;
            }

            public void onClick(View v) {
                LightAlertDialog.create(McListActivity.this.ctx, McListActivity.this.ctx
                        .getResources().getString(R.string.o_), McListActivity.this.ctx
                        .getResources().getString(R.string.ow)).setNegativeButton(McListActivity
                        .this.ctx.getResources().getString(R.string.eq), null).setPositiveButton
                        (McListActivity.this.ctx.getResources().getString(R.string.gn), new
                                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        McListAdaper.this.removeMc(removeMcListener.this.date);
                    }
                }).show();
            }
        }

        public McListAdaper(ArrayList<McPeriod> mcPeriodList, boolean edit) {
            this.mcPeriodList = mcPeriodList;
            this.edit = edit;
        }

        public void setEdit(boolean edit) {
            this.edit = edit;
        }

        public int getCount() {
            return this.mcPeriodList.size();
        }

        public McPeriod getItem(int position) {
            return (McPeriod) this.mcPeriodList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(McListActivity.this.ctx).inflate(R.layout.kn,
                        null);
                holder.startDate = (TextView) convertView.findViewById(R.id.start_date);
                holder.Mcday = (TextView) convertView.findViewById(R.id.mc_days);
                holder.McCircle = (TextView) convertView.findViewById(R.id.mc_circle);
                holder.editBtn = (ImageView) convertView.findViewById(R.id.edit);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (this.mcPeriodList != null) {
                if (this.edit) {
                    holder.editBtn.setVisibility(0);
                } else {
                    holder.editBtn.setVisibility(8);
                }
                McPeriod mcPeriod = getItem(position);
                holder.editBtn.setOnClickListener(new removeMcListener(mcPeriod.start_on));
                holder.startDate.setText(mcPeriod.start_on);
                holder.Mcday.setText(mcPeriod.duration + "");
                holder.McCircle.setText(mcPeriod.cycle + "");
            }
            return convertView;
        }

        private void removeMc(final String date) {
            RecordApi.deleteMcRecords(McListActivity.this.activity, date, new JsonCallback
                    (McListActivity.this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    for (int i = 0; i < McListAdaper.this.mcPeriodList.size(); i++) {
                        if (((McPeriod) McListAdaper.this.mcPeriodList.get(i)).start_on.equals
                                (date)) {
                            McListAdaper.this.mcPeriodList.remove(McListAdaper.this.mcPeriodList
                                    .get(i));
                        }
                    }
                    McListAdaper.this.notifyDataSetChanged();
                }
            });
        }
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.km);
        setTitle(R.string.ir);
        findView();
        getList();
    }

    private void findView() {
        this.listView = (ListView) findViewById(R.id.list_view);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "编辑").setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 1) {
            return super.onOptionsItemSelected(item);
        }
        if (this.isChecked) {
            item.setTitle("编辑");
            this.isChecked = false;
        } else {
            item.setTitle("取消");
            this.isChecked = true;
        }
        if (this.mcListAdapter == null) {
            return true;
        }
        this.mcListAdapter.setEdit(this.isChecked);
        this.mcListAdapter.notifyDataSetChanged();
        return true;
    }

    private void getList() {
        RecordApi.getMcPeriods(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                McListActivity.this.mcPeriodList = McPeriod.parseMcList(object);
                McListActivity.this.mcListAdapter = new McListAdaper(McListActivity.this
                        .mcPeriodList, McListActivity.this.edit);
                McListActivity.this.listView.setAdapter(McListActivity.this.mcListAdapter);
            }
        });
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, McListActivity.class));
        }
    }
}
