package com.boohee.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.boohee.main.GestureActivity;
import com.boohee.model.Notice;
import com.boohee.modeldao.NoticeDao;
import com.boohee.one.R;
import com.boohee.utility.Const;
import com.boohee.utility.Event;
import com.boohee.utils.Helper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class NoticeListActivity extends GestureActivity implements OnItemClickListener {
    static final String TAG = NoticeListActivity.class.getName();
    private NoticeListAdapter adapter;
    private TextView          noMsgText;
    private ArrayList<Notice> notices;
    private ListView          tipList;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.n3);
        setTitle(R.string.wa);
        MobclickAgent.onEvent(this, Event.OTHER_CLICKALERTPAGE);
        findView();
    }

    private void findView() {
        this.noMsgText = (TextView) findViewById(R.id.no_msg_tip);
        this.tipList = (ListView) findViewById(R.id.tip_list);
        this.tipList.setOnItemClickListener(this);
        registerForContextMenu(this.tipList);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("消息提醒");
        menu.add(0, 2, 2, "一键删除").setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 2) {
            NoticeDao dao = new NoticeDao(this.ctx);
            dao.clear();
            dao.closeDB();
            onResume();
            Helper.showToast(this.ctx, (CharSequence) "清除成功");
        }
        return super.onOptionsItemSelected(item);
    }

    private void initList() {
        NoticeDao noticeDao = new NoticeDao(this.ctx);
        this.notices = noticeDao.getNotices();
        if (this.notices != null && this.notices.size() > 0) {
            this.adapter = new NoticeListAdapter(this.ctx, this.notices);
            this.tipList.setAdapter(this.adapter);
        }
        noticeDao.closeDB();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Notice notice = this.adapter.getItem(position);
        if (!notice.isOpened()) {
            NoticeDao noticeDao = new NoticeDao(this.ctx);
            noticeDao.updateIsOpened(notice);
            noticeDao.closeDB();
        }
        Intent intent = new Intent(this.ctx, ViewTipActivity.class);
        intent.putExtra(Const.NOTICE_MESSAGE, notice.alarm_tip_message);
        intent.putExtra(Const.NOTICE_ID, notice.alarm_tip_id);
        startActivity(intent);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.wa);
        menu.add(0, 1, 0, "删除");
    }

    public boolean onContextItemSelected(MenuItem item) {
        deleteNotice(((AdapterContextMenuInfo) item.getMenuInfo()).position);
        return super.onContextItemSelected(item);
    }

    private void deleteNotice(int position) {
        NoticeDao noticeDao = new NoticeDao(this.ctx);
        if (noticeDao.delete(this.adapter.getItem(position))) {
            this.notices.remove(position);
            this.adapter.notifyDataSetChanged();
        }
        noticeDao.closeDB();
    }

    public void onResume() {
        super.onResume();
        initList();
        if (this.notices.size() > 0) {
            this.noMsgText.setVisibility(4);
            this.tipList.setVisibility(0);
            return;
        }
        this.noMsgText.setVisibility(0);
        this.tipList.setVisibility(4);
    }
}
