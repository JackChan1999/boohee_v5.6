package com.boohee.one.ui;

import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.boohee.main.GestureActivity;
import com.boohee.model.Contact;
import com.boohee.one.R;
import com.boohee.utils.Helper;
import com.umeng.socialize.common.SocializeConstants;

import java.util.Arrays;
import java.util.List;

public class ContactUsActivity extends GestureActivity {
    private ContactAdapter mAdapter;
    private List<Contact> mContacts = Arrays.asList(new Contact[]{new Contact("商务合作", "Adela",
            "021-6109 1580", "adela@boohee.com"), new Contact("产品销售合作", "张小姐", "021-6109 3795",
            "zhangshengdan@boohee.com"), new Contact("伙伴招聘", "杨小姐", "021-6109 1573",
            "zhaopin@boohee.com")});
    private ListView mListView;

    private class ContactAdapter extends BaseAdapter {
        public int getCount() {
            return ContactUsActivity.this.mContacts.size();
        }

        public Contact getItem(int position) {
            return (Contact) ContactUsActivity.this.mContacts.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final Contact contact = getItem(position);
            if (convertView == null) {
                convertView = View.inflate(ContactUsActivity.this, R.layout.k3, null);
            }
            ((TextView) convertView.findViewById(R.id.tv_section)).setText(contact.section);
            ((TextView) convertView.findViewById(R.id.tv_username)).setText(contact.user_name);
            TextView cellphone = (TextView) convertView.findViewById(R.id.tv_cellphone);
            cellphone.setText(contact.cellphone);
            cellphone.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ContactUsActivity.this.cellPhoneCall(contact.cellphone);
                }
            });
            TextView email = (TextView) convertView.findViewById(R.id.tv_email);
            email.setText(contact.email);
            email.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ContactUsActivity.this.sendEmail(contact.email);
                }
            });
            return convertView;
        }
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.ek);
        setTitle(R.string.gt);
        this.mListView = (ListView) findViewById(R.id.listview);
        this.mAdapter = new ContactAdapter();
        this.mListView.setAdapter(this.mAdapter);
    }

    private void cellPhoneCall(final String phone) {
        new Builder(this).setMessage("要打电话吗?").setCancelable(false).setPositiveButton(R.string
                .y8, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ContactUsActivity.this.startActivity(new Intent("android.intent.action.CALL", Uri
                        .parse("tel:" + phone.replace(SocializeConstants.OP_DIVIDER_MINUS, ""))));
            }
        }).setNegativeButton(R.string.eq, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        }).show();
    }

    private void sendEmail(final String email) {
        new Builder(this).setMessage("要发邮件吗?").setCancelable(false).setPositiveButton(R.string
                .y8, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent("android.intent.action.SEND");
                i.setType("message/rfc822");
                i.putExtra("android.intent.extra.EMAIL", new String[]{email});
                try {
                    ContactUsActivity.this.startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (ActivityNotFoundException e) {
                    Helper.showToast(ContactUsActivity.this.activity, (CharSequence)
                            "没有检测到邮件客户端哦~");
                }
            }
        }).setNegativeButton(R.string.eq, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        }).show();
    }
}
