package com.boohee.utility;

import android.view.View;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.status.CommentListActivity;

public class ShowMoreSpan extends MyURLSpan {
    public ShowMoreSpan(String url) {
        super(url);
    }

    public void onClick(View widget) {
        if ((widget instanceof TextView) && (widget.getTag(R.id.timeline_post_id) instanceof
                Integer)) {
            CommentListActivity.startActivity(widget.getContext(), ((Integer) widget.getTag(R.id
                    .timeline_post_id)).intValue(), null, false);
        }
    }
}
