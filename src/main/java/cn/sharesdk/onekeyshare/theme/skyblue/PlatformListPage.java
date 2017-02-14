package cn.sharesdk.onekeyshare.theme.skyblue;

import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.PlatformListFakeActivity;
import com.mob.tools.utils.R;
import java.util.List;

public class PlatformListPage extends PlatformListFakeActivity implements OnClickListener {
    private PlatformGridViewAdapter gridViewAdapter;

    public void onCreate() {
        super.onCreate();
        this.activity.setContentView(R.getLayoutRes(this.activity, "skyblue_share_platform_list"));
        initView();
    }

    private void initView() {
        View backImageView = findViewByResName("backImageView");
        backImageView.setTag(Integer.valueOf(17039360));
        backImageView.setOnClickListener(this);
        View okImageView = findViewByResName("okImageView");
        okImageView.setTag(Integer.valueOf(17039370));
        okImageView.setOnClickListener(this);
        this.gridViewAdapter = new PlatformGridViewAdapter(this.activity);
        this.gridViewAdapter.setCustomerLogos(this.customerLogos);
        ((GridView) findViewByResName("gridView")).setAdapter(this.gridViewAdapter);
        new AsyncTask<Void, Void, Platform[]>() {
            protected Platform[] doInBackground(Void... params) {
                return ShareSDK.getPlatformList();
            }

            protected void onPostExecute(Platform[] platforms) {
                PlatformListPage.this.gridViewAdapter.setData(platforms, PlatformListPage.this.hiddenPlatforms);
            }
        }.execute(new Void[0]);
    }

    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null && (tag instanceof Integer)) {
            switch (((Integer) tag).intValue()) {
                case 17039360:
                    setCanceled(true);
                    finish();
                    return;
                case 17039370:
                    onShareButtonClick(v);
                    return;
                default:
                    return;
            }
        }
    }

    private void onShareButtonClick(View v) {
        if (this.gridViewAdapter != null && !"locked".equals(v.getTag())) {
            List<Object> checkedPlatforms = this.gridViewAdapter.getCheckedItems();
            if (checkedPlatforms.size() == 0) {
                Toast.makeText(this.activity, R.getStringRes(this.activity, "select_one_plat_at_least"), 0).show();
                return;
            }
            v.setTag("locked");
            onShareButtonClick(v, checkedPlatforms);
        }
    }
}
