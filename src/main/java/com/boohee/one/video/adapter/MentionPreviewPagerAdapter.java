package com.boohee.one.video.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.boohee.one.video.entity.Mention;
import com.boohee.one.video.fragment.MentionPreviewFragment;

import java.util.List;

public class MentionPreviewPagerAdapter extends FragmentPagerAdapter {
    private List<Mention> mentionList;

    public MentionPreviewPagerAdapter(FragmentManager fm, List<Mention> mentionList) {
        super(fm);
        this.mentionList = mentionList;
    }

    public Fragment getItem(int position) {
        return MentionPreviewFragment.newInstance((Mention) this.mentionList.get(position));
    }

    public int getCount() {
        return this.mentionList == null ? 0 : this.mentionList.size();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        MentionPreviewFragment f = (MentionPreviewFragment) super.instantiateItem(container,
                position);
        f.setMention((Mention) this.mentionList.get(position));
        return f;
    }

    public int getItemPosition(Object object) {
        return -2;
    }
}
