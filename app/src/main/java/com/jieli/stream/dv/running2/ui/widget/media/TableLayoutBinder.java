package com.jieli.stream.dv.running2.ui.widget.media;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import com.jieli.stream.dv.running2.R;

/* loaded from: classes.dex */
public class TableLayoutBinder {
    private Context mContext;
    public TableLayout mTableLayout;
    public ViewGroup mTableView;

    public TableLayoutBinder(Context context) {
        this(context, R.layout.table_media_info);
    }

    public TableLayoutBinder(Context context, int i) {
        this.mContext = context;
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(i, (ViewGroup) null);
        this.mTableView = viewGroup;
        this.mTableLayout = (TableLayout) viewGroup.findViewById(R.id.table);
    }

    public TableLayoutBinder(Context context, TableLayout tableLayout) {
        this.mContext = context;
        this.mTableView = tableLayout;
        this.mTableLayout = tableLayout;
    }

    public View appendRow1(String str, String str2) {
        return appendRow(R.layout.table_media_info_row1, str, str2);
    }

    public View appendRow1(int i, String str) {
        return appendRow1(this.mContext.getString(i), str);
    }

    public View appendRow2(String str, String str2) {
        return appendRow(R.layout.table_media_info_row2, str, str2);
    }

    public View appendRow2(int i, String str) {
        return appendRow2(this.mContext.getString(i), str);
    }

    public View appendSection(String str) {
        return appendRow(R.layout.table_media_info_section, str, null);
    }

    public View appendSection(int i) {
        return appendSection(this.mContext.getString(i));
    }

    public View appendRow(int i, String str, String str2) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.mContext).inflate(i, (ViewGroup) this.mTableLayout, false);
        setNameValueText(viewGroup, str, str2);
        this.mTableLayout.addView(viewGroup);
        return viewGroup;
    }

    public ViewHolder obtainViewHolder(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder != null) {
            return viewHolder;
        }
        ViewHolder viewHolder2 = new ViewHolder();
        viewHolder2.mNameTextView = (TextView) view.findViewById(R.id.name);
        viewHolder2.mValueTextView = (TextView) view.findViewById(R.id.value);
        view.setTag(viewHolder2);
        return viewHolder2;
    }

    public void setNameValueText(View view, String str, String str2) {
        ViewHolder obtainViewHolder = obtainViewHolder(view);
        obtainViewHolder.setName(str);
        obtainViewHolder.setValue(str2);
    }

    public void setValueText(View view, String str) {
        obtainViewHolder(view).setValue(str);
    }

    public ViewGroup buildLayout() {
        return this.mTableView;
    }

    public AlertDialog.Builder buildAlertDialogBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setView(buildLayout());
        return builder;
    }

    private static class ViewHolder {
        public TextView mNameTextView;
        public TextView mValueTextView;

        private ViewHolder() {
        }

        public void setName(String str) {
            TextView textView = this.mNameTextView;
            if (textView != null) {
                textView.setText(str);
            }
        }

        public void setValue(String str) {
            TextView textView = this.mValueTextView;
            if (textView != null) {
                textView.setText(str);
            }
        }
    }
}
