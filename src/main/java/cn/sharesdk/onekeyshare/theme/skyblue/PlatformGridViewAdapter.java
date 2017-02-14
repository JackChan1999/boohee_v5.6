package cn.sharesdk.onekeyshare.theme.skyblue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.CustomerLogo;
import cn.sharesdk.onekeyshare.ShareCore;
import com.mob.tools.utils.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlatformGridViewAdapter extends BaseAdapter implements OnClickListener {
    private List<Integer> checkedPositionList = new ArrayList();
    private final Context context;
    private int directOnlyPosition = -1;
    private List<Object> logos = new ArrayList();

    static class ViewHolder {
        public ImageView checkedImageView;
        public ImageView logoImageView;
        public TextView nameTextView;
        public Integer position;

        ViewHolder() {
        }
    }

    public PlatformGridViewAdapter(Context context) {
        this.context = context;
    }

    public int getCount() {
        return this.logos.size();
    }

    public Object getItem(int i) {
        return this.logos.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        Bitmap logo;
        String label;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.getLayoutRes(this.context, "skyblue_share_platform_list_item"), null);
            viewHolder = new ViewHolder();
            viewHolder.checkedImageView = (ImageView) view.findViewById(R.getIdRes(this.context, "checkedImageView"));
            viewHolder.logoImageView = (ImageView) view.findViewById(R.getIdRes(this.context, "logoImageView"));
            viewHolder.nameTextView = (TextView) view.findViewById(R.getIdRes(this.context, "nameTextView"));
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CustomerLogo item = getItem(position);
        boolean disabled = this.directOnlyPosition == -1 ? !this.checkedPositionList.isEmpty() && (item instanceof Platform ? ShareCore.isDirectShare((Platform) item) : true) : position != this.directOnlyPosition;
        if (item instanceof Platform) {
            logo = getIcon((Platform) item, disabled ? "" : "_checked");
            label = getName((Platform) item);
            view.setOnClickListener(this);
        } else {
            CustomerLogo customerLogo = item;
            logo = disabled ? customerLogo.disableLogo : customerLogo.enableLogo;
            label = customerLogo.label;
            view.setOnClickListener(this);
        }
        String checkedResName = (this.directOnlyPosition == -1 || this.directOnlyPosition == position) ? "skyblue_platform_checked" : "skyblue_platform_checked_disabled";
        viewHolder.position = Integer.valueOf(position);
        viewHolder.checkedImageView.setImageBitmap(BitmapFactory.decodeResource(this.context.getResources(), R.getBitmapRes(this.context, checkedResName)));
        viewHolder.checkedImageView.setVisibility(this.checkedPositionList.contains(viewHolder.position) ? 0 : 8);
        viewHolder.nameTextView.setText(label);
        viewHolder.logoImageView.setImageBitmap(logo);
        return view;
    }

    public void onClick(View view) {
        Integer position = ((ViewHolder) view.getTag()).position;
        if (this.directOnlyPosition == -1 || position.intValue() == this.directOnlyPosition) {
            boolean direct;
            Object item = getItem(position.intValue());
            if (item instanceof Platform) {
                direct = ShareCore.isDirectShare((Platform) item);
            } else {
                direct = true;
            }
            if (!direct || this.directOnlyPosition != -1 || this.checkedPositionList.isEmpty()) {
                if (this.checkedPositionList.contains(position)) {
                    this.checkedPositionList.remove(position);
                    if (direct) {
                        this.directOnlyPosition = -1;
                    }
                } else {
                    this.checkedPositionList.add(position);
                    if (direct) {
                        this.directOnlyPosition = position.intValue();
                    }
                }
                notifyDataSetChanged();
            }
        }
    }

    public void setData(Platform[] platforms, HashMap<String, String> hiddenPlatforms) {
        if (platforms != null) {
            if (hiddenPlatforms == null || hiddenPlatforms.size() <= 0) {
                this.logos.addAll(Arrays.asList(platforms));
            } else {
                ArrayList<Platform> ps = new ArrayList();
                for (Platform p : platforms) {
                    if (!hiddenPlatforms.containsKey(p.getName())) {
                        ps.add(p);
                    }
                }
                this.logos.addAll(ps);
            }
            this.checkedPositionList.clear();
            notifyDataSetChanged();
        }
    }

    public void setCustomerLogos(ArrayList<CustomerLogo> customers) {
        if (customers != null && customers.size() != 0) {
            this.logos.addAll(customers);
        }
    }

    public List<Object> getCheckedItems() {
        ArrayList<Object> list = new ArrayList();
        if (this.directOnlyPosition != -1) {
            list.add(getItem(this.directOnlyPosition));
        } else {
            for (Integer position : this.checkedPositionList) {
                list.add(getItem(position.intValue()));
            }
        }
        return list;
    }

    private Bitmap getIcon(Platform plat, String subfix) {
        return BitmapFactory.decodeResource(this.context.getResources(), R.getBitmapRes(this.context, "skyblue_logo_" + plat.getName() + subfix));
    }

    private String getName(Platform plat) {
        if (plat == null) {
            return "";
        }
        if (plat.getName() == null) {
            return "";
        }
        int resId = R.getStringRes(this.context, plat.getName());
        if (resId > 0) {
            return this.context.getString(resId);
        }
        return null;
    }
}
