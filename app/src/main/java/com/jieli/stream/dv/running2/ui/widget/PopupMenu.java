package com.jieli.stream.dv.running2.ui.widget;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import androidx.core.view.GravityCompat;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.Dbug;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class PopupMenu {
    private OnPopItemClickListener listener;
    private View mContentView;
    private Context mContext;
    private int mGravity;
    private View parentView;
    private PopupWindow popupWindow;
    private Map<Integer, Integer> resIds;
    private String tag = getClass().getSimpleName();
    private final ViewTreeObserver.OnGlobalLayoutListener mLocationLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.jieli.stream.dv.running2.ui.widget.PopupMenu.2
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            PopupWindow popupWindow = PopupMenu.this.popupWindow;
            if (popupWindow == null) {
                return;
            }
            PopupMenu.this.mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            PopupMenu popupMenu = PopupMenu.this;
            PointF calculatePopupLocation = popupMenu.calculatePopupLocation(popupMenu.mGravity, PopupMenu.this.popupWindow, PopupMenu.this.parentView);
            popupWindow.setClippingEnabled(true);
            popupWindow.update((int) calculatePopupLocation.x, (int) calculatePopupLocation.y, popupWindow.getWidth(), popupWindow.getHeight());
            popupWindow.getContentView().requestLayout();
            Dbug.e(PopupMenu.this.tag, "x " + calculatePopupLocation.x + ", y " + calculatePopupLocation.y + ", w " + popupWindow.getWidth() + ", h " + popupWindow.getHeight());
        }
    };

    public interface OnPopItemClickListener {
        void onItemClick(int i, Integer num, int i2);
    }

    public PopupMenu(Context context, Map<Integer, Integer> map) {
        this.resIds = new HashMap();
        if (context == null) {
            Dbug.e(this.tag, "PopupMenu context is null!");
            return;
        }
        this.mContext = context;
        this.resIds = map;
        View inflate = LayoutInflater.from(context).inflate(R.layout.popup_menu_layout, (ViewGroup) null);
        this.mContentView = inflate;
        ListView listView = (ListView) inflate.findViewById(R.id.pop_list_view);
        listView.setAdapter((ListAdapter) new PopupAdapter(this.mContext));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.jieli.stream.dv.running2.ui.widget.PopupMenu.1
            /* JADX WARN: Type inference failed for: r1v2, types: [android.widget.Adapter] */
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Integer num;
                if (adapterView != null && adapterView.getAdapter() != null && (num = (Integer) adapterView.getAdapter().getItem(i)) != null && PopupMenu.this.resIds != null) {
                    Integer num2 = (Integer) PopupMenu.this.resIds.get(num);
                    if (PopupMenu.this.listener != null) {
                        PopupMenu.this.listener.onItemClick(num.intValue(), num2, i);
                    }
                }
                PopupMenu.this.dismiss();
            }
        });
        this.popupWindow = new PopupWindow(this.mContentView, 100, -2);
        this.mContentView.getViewTreeObserver().addOnGlobalLayoutListener(this.mLocationLayoutListener);
        this.popupWindow.setBackgroundDrawable(new ColorDrawable(0));
    }

    public void setOnPopItemClickListener(OnPopItemClickListener onPopItemClickListener) {
        this.listener = onPopItemClickListener;
    }

    public void showAtLocation(View view, int i, int i2, int i3) {
        PopupWindow popupWindow = this.popupWindow;
        if (popupWindow == null) {
            Dbug.e(this.tag, "PopupMenu popupWindow is null!");
            return;
        }
        this.parentView = view;
        popupWindow.showAtLocation(view, i, i2, i3);
        this.popupWindow.setFocusable(true);
        this.popupWindow.setOutsideTouchable(true);
        this.popupWindow.update();
    }

    private static Rect locateView(View view) {
        int[] iArr = new int[2];
        if (view == null) {
            return null;
        }
        try {
            view.getLocationOnScreen(iArr);
            Rect rect = new Rect();
            rect.left = iArr[0] + view.getWidth();
            rect.top = 0;
            rect.right = rect.left + view.getWidth();
            rect.bottom = rect.top + view.getHeight();
            return rect;
        } catch (NullPointerException unused) {
            return null;
        }
    }

    public void showAsDropDown(View view) {
        PopupWindow popupWindow = this.popupWindow;
        if (popupWindow == null) {
            Dbug.e(this.tag, "PopupMenu popupWindow is null!");
            return;
        }
        this.parentView = view;
        popupWindow.showAsDropDown(view);
        this.popupWindow.setOutsideTouchable(true);
        this.popupWindow.update();
    }

    public void showAsUp(View view) {
        if (this.popupWindow == null) {
            Dbug.e(this.tag, "PopupMenu popupWindow is null!");
            return;
        }
        this.mGravity = 48;
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        this.parentView = view;
        this.popupWindow.showAtLocation(view, 0, iArr[0], iArr[1]);
        this.popupWindow.setOutsideTouchable(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PointF calculatePopupLocation(int i, PopupWindow popupWindow, View view) {
        PointF pointF = new PointF();
        RectF calculateRectInWindow = calculateRectInWindow(view);
        PointF pointF2 = new PointF(calculateRectInWindow.centerX(), calculateRectInWindow.centerY());
        if (i == 17) {
            pointF.x = pointF2.x - (popupWindow.getContentView().getWidth() / 2.0f);
            pointF.y = pointF2.y - (popupWindow.getContentView().getHeight() / 2.0f);
        } else if (i == 48) {
            pointF.x = pointF2.x - (popupWindow.getContentView().getWidth() / 2.0f);
            pointF.y = (calculateRectInWindow.top - popupWindow.getContentView().getHeight()) - 0;
        } else if (i == 80) {
            pointF.x = pointF2.x - (popupWindow.getContentView().getWidth() / 2.0f);
            pointF.y = calculateRectInWindow.bottom + 0;
        } else if (i == 8388611) {
            pointF.x = (calculateRectInWindow.left - popupWindow.getContentView().getWidth()) - 0;
            pointF.y = pointF2.y - (popupWindow.getContentView().getHeight() / 2.0f);
        } else if (i == 8388613) {
            pointF.x = calculateRectInWindow.right + 0;
            pointF.y = pointF2.y - (popupWindow.getContentView().getHeight() / 2.0f);
        } else {
            throw new IllegalArgumentException("Gravity must have be CENTER, START, END, TOP or BOTTOM.");
        }
        return pointF;
    }

    private static RectF calculateRectInWindow(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new RectF(location[0], location[1], location[0] + view.getMeasuredWidth(), location[1] + view.getMeasuredHeight());
    }

    public void showAsRight(View view) {
        if (this.popupWindow == null) {
            Dbug.e(this.tag, "PopupMenu popupWindow is null!");
            return;
        }
        this.mGravity = GravityCompat.END;
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        this.parentView = view;
        this.popupWindow.showAtLocation(view, 0, iArr[0] + AppUtils.dp2px(this.mContext, 8), iArr[1]);
        this.popupWindow.setOutsideTouchable(true);
    }

    public void dismiss() {
        PopupWindow popupWindow = this.popupWindow;
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    public boolean isShowing() {
        PopupWindow popupWindow = this.popupWindow;
        return popupWindow != null && popupWindow.isShowing();
    }

    public View getParentView() {
        return this.parentView;
    }

    private class PopupAdapter extends BaseAdapter {
        private Context mContext;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public PopupAdapter(Context context) {
            this.mContext = context;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (PopupMenu.this.resIds == null) {
                return 0;
            }
            return PopupMenu.this.resIds.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            if (PopupMenu.this.resIds != null) {
                Integer[] numArr = (Integer[]) PopupMenu.this.resIds.keySet().toArray(new Integer[PopupMenu.this.resIds.size()]);
                if (i < numArr.length) {
                    return numArr[i];
                }
            }
            return null;
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            Integer num;
            int i2 = 0;
            if (view == null) {
                view = LayoutInflater.from(this.mContext).inflate(R.layout.popup_menu_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) view.findViewById(R.id.item_image);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            Integer num2 = (Integer) getItem(i);
            if (num2 != null) {
                if (PopupMenu.this.resIds != null && (num = (Integer) PopupMenu.this.resIds.get(num2)) != null) {
                    i2 = num.intValue();
                }
                if (i2 != 0) {
                    viewHolder.imageView.setImageResource(i2);
                }
            }
            return view;
        }

        private final class ViewHolder {
            ImageView imageView;

            private ViewHolder() {
            }
        }
    }
}
