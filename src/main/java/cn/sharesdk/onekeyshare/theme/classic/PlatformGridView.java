package cn.sharesdk.onekeyshare.theme.classic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.CustomerLogo;
import com.mob.tools.gui.ViewPagerAdapter;
import com.mob.tools.gui.ViewPagerClassic;
import com.mob.tools.utils.R;
import com.mob.tools.utils.UIHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

public class PlatformGridView extends LinearLayout implements OnClickListener, Callback {
    private static final int MIN_CLICK_INTERVAL = 1000;
    private static final int MSG_PLATFORM_LIST_GOT = 1;
    private int COLUMN_PER_LINE;
    private int LINE_PER_PAGE;
    private int PAGE_SIZE;
    private View bgView;
    private Bitmap bluePoint;
    private ArrayList<CustomerLogo> customers;
    private Bitmap grayPoint;
    private HashMap<String, String> hiddenPlatforms;
    private long lastClickTime;
    private ViewPagerClassic pager;
    private PlatformListPage parent;
    private Platform[] platformList;
    private ImageView[] points;
    private HashMap<String, Object> reqData;
    private boolean silent;

    private static class GridView extends LinearLayout {
        private Object[] beans;
        private OnClickListener callback;
        private int lines;
        private PlatformAdapter platformAdapter;

        public GridView(PlatformAdapter platformAdapter) {
            super(platformAdapter.platformGridView.getContext());
            this.platformAdapter = platformAdapter;
            this.callback = platformAdapter.callback;
        }

        public void setData(int lines, Object[] beans) {
            this.lines = lines;
            this.beans = beans;
            init();
        }

        private void init() {
            int size;
            int dp_5 = R.dipToPx(getContext(), 5);
            setPadding(0, dp_5, 0, dp_5);
            setOrientation(1);
            if (this.beans == null) {
                size = 0;
            } else {
                size = this.beans.length;
            }
            int COLUMN_PER_LINE = this.platformAdapter.platformGridView.COLUMN_PER_LINE;
            int lineSize = size / COLUMN_PER_LINE;
            if (size % COLUMN_PER_LINE > 0) {
                lineSize++;
            }
            LayoutParams lp = new LayoutParams(-1, -1);
            lp.weight = 1.0f;
            for (int i = 0; i < this.lines; i++) {
                LinearLayout llLine = new LinearLayout(getContext());
                llLine.setLayoutParams(lp);
                llLine.setPadding(dp_5, 0, dp_5, 0);
                addView(llLine);
                if (i < lineSize) {
                    for (int j = 0; j < COLUMN_PER_LINE; j++) {
                        int index = (i * COLUMN_PER_LINE) + j;
                        LinearLayout llItem;
                        if (index >= size) {
                            llItem = new LinearLayout(getContext());
                            llItem.setLayoutParams(lp);
                            llLine.addView(llItem);
                        } else {
                            llItem = getView(index, this.callback, getContext());
                            llItem.setTag(this.beans[index]);
                            llItem.setLayoutParams(lp);
                            llLine.addView(llItem);
                        }
                    }
                }
            }
        }

        private LinearLayout getView(int position, OnClickListener ocL, Context context) {
            Bitmap logo;
            String label;
            OnClickListener listener;
            if (this.beans[position] instanceof Platform) {
                logo = getIcon((Platform) this.beans[position]);
                label = getName((Platform) this.beans[position]);
                listener = ocL;
            } else {
                logo = ((CustomerLogo) this.beans[position]).enableLogo;
                label = ((CustomerLogo) this.beans[position]).label;
                listener = ocL;
            }
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(1);
            ImageView iv = new ImageView(context);
            int dp_5 = R.dipToPx(context, 5);
            iv.setPadding(dp_5, dp_5, dp_5, dp_5);
            iv.setScaleType(ScaleType.CENTER_INSIDE);
            LayoutParams lpIv = new LayoutParams(-2, -2);
            lpIv.setMargins(dp_5, dp_5, dp_5, dp_5);
            lpIv.gravity = 1;
            iv.setLayoutParams(lpIv);
            iv.setImageBitmap(logo);
            ll.addView(iv);
            TextView tv = new TextView(context);
            tv.setTextColor(AbstractWheelTextAdapter.DEFAULT_TEXT_COLOR);
            tv.setTextSize(1, 14.0f);
            tv.setSingleLine();
            tv.setIncludeFontPadding(false);
            LayoutParams lpTv = new LayoutParams(-2, -2);
            lpTv.gravity = 1;
            lpTv.weight = 1.0f;
            lpTv.setMargins(dp_5, 0, dp_5, dp_5);
            tv.setLayoutParams(lpTv);
            tv.setText(label);
            ll.addView(tv);
            ll.setOnClickListener(listener);
            return ll;
        }

        private Bitmap getIcon(Platform plat) {
            if (plat == null || plat.getName() == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getResources(), R.getBitmapRes(getContext(), ("logo_" + plat.getName()).toLowerCase()));
        }

        private String getName(Platform plat) {
            if (plat == null) {
                return "";
            }
            if (plat.getName() == null) {
                return "";
            }
            int resId = R.getStringRes(getContext(), plat.getName().toLowerCase());
            if (resId > 0) {
                return getContext().getString(resId);
            }
            return null;
        }
    }

    private static class PlatformAdapter extends ViewPagerAdapter {
        private OnClickListener callback;
        private GridView[] girds;
        private int lines;
        private List<Object> logos = new ArrayList();
        private PlatformGridView platformGridView;

        public PlatformAdapter(PlatformGridView platformGridView) {
            this.platformGridView = platformGridView;
            Platform[] platforms = platformGridView.platformList;
            HashMap<String, String> hiddenPlatforms = platformGridView.hiddenPlatforms;
            if (platforms != null) {
                if (hiddenPlatforms != null && hiddenPlatforms.size() > 0) {
                    ArrayList<Platform> ps = new ArrayList();
                    for (Platform p : platforms) {
                        if (!hiddenPlatforms.containsKey(p.getName())) {
                            ps.add(p);
                        }
                    }
                    platforms = new Platform[ps.size()];
                    for (int i = 0; i < platforms.length; i++) {
                        platforms[i] = (Platform) ps.get(i);
                    }
                }
                this.logos.addAll(Arrays.asList(platforms));
            }
            ArrayList<CustomerLogo> customers = platformGridView.customers;
            if (customers != null) {
                this.logos.addAll(customers);
            }
            this.callback = platformGridView;
            this.girds = null;
            if (this.logos != null) {
                int size = this.logos.size();
                int PAGE_SIZE = platformGridView.PAGE_SIZE;
                int pageCount = size / PAGE_SIZE;
                if (size % PAGE_SIZE > 0) {
                    pageCount++;
                }
                this.girds = new GridView[pageCount];
            }
        }

        public int getCount() {
            return this.girds == null ? 0 : this.girds.length;
        }

        public View getView(int position, ViewGroup parent) {
            if (this.girds[position] == null) {
                int pageSize = this.platformGridView.PAGE_SIZE;
                int curSize = pageSize * position;
                int listSize = this.logos == null ? 0 : this.logos.size();
                if (curSize + pageSize > listSize) {
                    pageSize = listSize - curSize;
                }
                Object[] gridBean = new Object[pageSize];
                for (int i = 0; i < pageSize; i++) {
                    gridBean[i] = this.logos.get(curSize + i);
                }
                if (position == 0) {
                    int COLUMN_PER_LINE = this.platformGridView.COLUMN_PER_LINE;
                    this.lines = gridBean.length / COLUMN_PER_LINE;
                    if (gridBean.length % COLUMN_PER_LINE > 0) {
                        this.lines++;
                    }
                }
                this.girds[position] = new GridView(this);
                this.girds[position].setData(this.lines, gridBean);
            }
            return this.girds[position];
        }

        public void onScreenChange(int currentScreen, int lastScreen) {
            ImageView[] points = this.platformGridView.points;
            for (ImageView imageBitmap : points) {
                imageBitmap.setImageBitmap(this.platformGridView.grayPoint);
            }
            points[currentScreen].setImageBitmap(this.platformGridView.bluePoint);
        }
    }

    public PlatformGridView(Context context) {
        super(context);
        init(context);
    }

    public PlatformGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        calPageSize();
        setOrientation(1);
        this.pager = new ViewPagerClassic(context);
        disableOverScrollMode(this.pager);
        this.pager.setLayoutParams(new LayoutParams(-1, -2));
        addView(this.pager);
        new Thread() {
            public void run() {
                try {
                    PlatformGridView.this.platformList = ShareSDK.getPlatformList();
                    if (PlatformGridView.this.platformList == null) {
                        PlatformGridView.this.platformList = new Platform[0];
                    }
                    UIHandler.sendEmptyMessage(1, PlatformGridView.this);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }.start();
    }

    private void calPageSize() {
        float whR = ((float) R.getScreenWidth(getContext())) / ((float) R.getScreenHeight(getContext()));
        if (((double) whR) < 0.63d) {
            this.COLUMN_PER_LINE = 3;
            this.LINE_PER_PAGE = 3;
        } else if (((double) whR) < 0.75d) {
            this.COLUMN_PER_LINE = 3;
            this.LINE_PER_PAGE = 2;
        } else {
            this.LINE_PER_PAGE = 1;
            if (((double) whR) >= 1.75d) {
                this.COLUMN_PER_LINE = 6;
            } else if (((double) whR) >= 1.5d) {
                this.COLUMN_PER_LINE = 5;
            } else if (((double) whR) >= 1.3d) {
                this.COLUMN_PER_LINE = 4;
            } else {
                this.COLUMN_PER_LINE = 3;
            }
        }
        this.PAGE_SIZE = this.COLUMN_PER_LINE * this.LINE_PER_PAGE;
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                afterPlatformListGot();
                break;
        }
        return false;
    }

    public void afterPlatformListGot() {
        this.pager.setAdapter(new PlatformAdapter(this));
        int pageCount = 0;
        if (this.platformList != null) {
            int hideSize;
            int cusSize = this.customers == null ? 0 : this.customers.size();
            int platSize = this.platformList == null ? 0 : this.platformList.length;
            if (this.hiddenPlatforms == null) {
                hideSize = 0;
            } else {
                hideSize = this.hiddenPlatforms.size();
            }
            int size = (platSize - hideSize) + cusSize;
            pageCount = size / this.PAGE_SIZE;
            if (size % this.PAGE_SIZE > 0) {
                pageCount++;
            }
        }
        this.points = new ImageView[pageCount];
        if (this.points.length > 0) {
            Context context = getContext();
            LinearLayout llPoints = new LinearLayout(context);
            llPoints.setVisibility(pageCount > 1 ? 0 : 8);
            LayoutParams lpLl = new LayoutParams(-2, -2);
            lpLl.gravity = 1;
            llPoints.setLayoutParams(lpLl);
            addView(llPoints);
            int dp_5 = R.dipToPx(context, 5);
            int resId = R.getBitmapRes(getContext(), "light_blue_point");
            if (resId > 0) {
                this.grayPoint = BitmapFactory.decodeResource(getResources(), resId);
            }
            resId = R.getBitmapRes(getContext(), "blue_point");
            if (resId > 0) {
                this.bluePoint = BitmapFactory.decodeResource(getResources(), resId);
            }
            for (int i = 0; i < pageCount; i++) {
                this.points[i] = new ImageView(context);
                this.points[i].setScaleType(ScaleType.CENTER_INSIDE);
                this.points[i].setImageBitmap(this.grayPoint);
                LayoutParams lpIv = new LayoutParams(dp_5, dp_5);
                lpIv.setMargins(dp_5, dp_5, dp_5, 0);
                this.points[i].setLayoutParams(lpIv);
                llPoints.addView(this.points[i]);
            }
            this.points[this.pager.getCurrentScreen()].setImageBitmap(this.bluePoint);
        }
    }

    public void onConfigurationChanged() {
        int curFirst = this.pager.getCurrentScreen() * this.PAGE_SIZE;
        calPageSize();
        int newPage = curFirst / this.PAGE_SIZE;
        removeViewAt(1);
        afterPlatformListGot();
        this.pager.setCurrentScreen(newPage);
    }

    public void setData(HashMap<String, Object> data, boolean silent) {
        this.reqData = data;
        this.silent = silent;
    }

    public void setHiddenPlatforms(HashMap<String, String> hiddenPlatforms) {
        this.hiddenPlatforms = hiddenPlatforms;
    }

    public void setCustomerLogos(ArrayList<CustomerLogo> customers) {
        this.customers = customers;
    }

    public void setEditPageBackground(View bgView) {
        this.bgView = bgView;
    }

    public void setParent(PlatformListPage parent) {
        this.parent = parent;
    }

    public void onClick(View v) {
        long time = System.currentTimeMillis();
        if (time - this.lastClickTime >= 1000) {
            this.lastClickTime = time;
            ArrayList<Object> platforms = new ArrayList(1);
            platforms.add(v.getTag());
            this.parent.onPlatformIconClick(v, platforms);
        }
    }

    private void disableOverScrollMode(View view) {
        if (VERSION.SDK_INT >= 9) {
            try {
                Method m = View.class.getMethod("setOverScrollMode", new Class[]{Integer.TYPE});
                m.setAccessible(true);
                m.invoke(view, new Object[]{Integer.valueOf(2)});
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
