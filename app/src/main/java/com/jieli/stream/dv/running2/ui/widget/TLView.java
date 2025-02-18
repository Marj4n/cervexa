package com.jieli.stream.dv.running2.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IConstant;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class TLView extends ViewGroup implements IConstant {
    private static final long DAY_MILLIS = 86400000;
    private static final long HOUR_MILLIS = 3600000;
    private static final long MIN_MILLIS = 60000;
    private static final int MSG_GET_THUMBNAIL = 256;
    private static final int MSG_SET_TIME = 258;
    private static final long SEC_MILLIS = 1000;
    private static final String TAG_LEFT_IMAGEVIEW = "tag_left_image_view";
    private static final String TAG_LEFT_PROG = "tag_left_progress";
    private static final String TAG_LEFT_THUMBNAIL = "tag_left_thumbnail";
    private static final String TAG_LEFT_TIME = "tag_left_time";
    private static final String TAG_RIGHT_IMAGEVIEW = "tag_right_image_view";
    private static final String TAG_RIGHT_PROG = "tag_right_progress";
    private static final String TAG_RIGHT_THUMBNAIL = "tag_right_thumbnail";
    private static final String TAG_RIGHT_TIME = "tag_right_time";
    private Calendar acalendar;
    private Paint blackline;
    private TextPaint blacktext30;
    private TextPaint dateAndTimePaint;
    float day_pixels;
    private Paint eventPaint;
    int finger1id;
    volatile float finger1x;
    volatile float finger1y;
    int finger2id;
    volatile float finger2x;
    volatile float finger2y;
    int fingers;
    private final Handler handler;
    float hour_pixels;
    private boolean isBuffering;
    private volatile boolean isMove;
    private volatile boolean isRequestNext;
    private volatile boolean isSelectionMode;
    private long left;
    private Paint mBackgroundPaint;
    private final List<FileInfo> mCalendarStuff;
    private Paint mCapturePaint;
    private Paint mDelayPaint;
    private float mDensity;
    private int mDeviceWidth;
    private volatile long mFinger1Time;
    volatile float mFinger1x;
    volatile float mFinger1y;
    private volatile long mFinger2Time;
    volatile float mFinger2x;
    volatile float mFinger2y;
    private Paint mGrayLine;
    private int mHeight;
    private OnValueChangeListener mOnValueChangeListener;
    private Paint mSelectedPaint;
    private Paint mSosPaint;
    private volatile int mWhichFinger;
    private Paint middlePaint;
    float min_pixels;
    float pixels_per_milli;
    private long right;
    float sec_pixels;
    float span;
    private final String tag;
    private TextPaint textPaint;
    private float textWidth;
    private int width;
    static final SimpleDateFormat yyyy_MMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    public interface OnValueChangeListener {
        void onValueChangeEnd(Calendar calendar);
    }

    public void setData(List<FileInfo> list) {
        List<FileInfo> list2 = this.mCalendarStuff;
        if (list2 != null) {
            list2.clear();
            this.mCalendarStuff.addAll(list);
            return;
        }
        throw new NullPointerException("mCalendarStuff is null");
    }

    public void setData(FileInfo fileInfo) {
        List<FileInfo> list = this.mCalendarStuff;
        if (list != null) {
            list.clear();
            if (fileInfo == null) {
                throw new NullPointerException("Param is null");
            }
            this.left = fileInfo.getStartTime().getTimeInMillis() - MIN_MILLIS;
            this.right = fileInfo.getEndTime().getTimeInMillis() + MIN_MILLIS;
            this.span = r0 - this.left;
            this.mCalendarStuff.add(fileInfo);
            invalidate();
            return;
        }
        throw new NullPointerException("mCalendarStuff is null");
    }

    public TLView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.tag = getClass().getSimpleName();
        this.mCalendarStuff = new ArrayList();
        this.finger2id = -1;
        this.isMove = false;
        this.isSelectionMode = false;
        this.mWhichFinger = 0;
        this.mFinger1Time = 0L;
        this.mFinger2Time = 0L;
        this.isRequestNext = false;
        this.handler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.widget.TLView.1
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                if (message.what != 256 || TLView.this.mOnValueChangeListener == null) {
                    return false;
                }
                long longValue = ((Long) message.obj).longValue();
                TLView.this.acalendar.setTimeInMillis(longValue);
                Dbug.i(TLView.this.tag, "MSG_GET_THUMBNAIL Format time=" + TLView.yyyy_MMddHHmmss.format(Long.valueOf(longValue)));
                TLView.this.mOnValueChangeListener.onValueChangeEnd(TLView.this.acalendar);
                return false;
            }
        });
        this.textWidth = 0.0f;
        this.isBuffering = false;
        init();
    }

    public TLView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TLView(Context context) {
        this(context, null);
    }

    private void init() {
        this.mDensity = getResources().getDisplayMetrics().density;
        Display defaultDisplay = ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        this.mDeviceWidth = point.x;
        Paint paint = new Paint();
        this.mBackgroundPaint = paint;
        paint.setStrokeWidth(1.0f);
        this.mBackgroundPaint.setAntiAlias(true);
        this.mBackgroundPaint.setColor(getResources().getColor(R.color.half_transparent));
        TextPaint textPaint = new TextPaint(1);
        this.textPaint = textPaint;
        textPaint.setColor(-1);
        this.textPaint.setTextSize(this.mDensity * 15.0f);
        TextPaint textPaint2 = new TextPaint(1);
        this.dateAndTimePaint = textPaint2;
        textPaint2.setColor(-1);
        this.dateAndTimePaint.setTextSize(this.mDensity * 15.0f);
        Paint paint2 = new Paint();
        this.mSelectedPaint = paint2;
        paint2.setStrokeWidth(1.0f);
        this.mSelectedPaint.setAntiAlias(true);
        this.mSelectedPaint.setColor(getResources().getColor(R.color.half_transparent));
        Paint paint3 = new Paint();
        this.eventPaint = paint3;
        paint3.setStrokeWidth(1.0f);
        this.eventPaint.setAntiAlias(true);
        this.eventPaint.setColor(-16711936);
        Paint paint4 = new Paint();
        this.middlePaint = paint4;
        paint4.setStrokeWidth(1.0f);
        this.middlePaint.setAntiAlias(true);
        this.middlePaint.setColor(-1);
        Paint paint5 = new Paint();
        this.blackline = paint5;
        paint5.setColor(-1);
        this.blackline.setStrokeWidth(1.0f);
        this.blackline.setAntiAlias(true);
        this.blackline.setStyle(Paint.Style.STROKE);
        TextPaint textPaint3 = new TextPaint();
        this.blacktext30 = textPaint3;
        textPaint3.setColor(-1);
        this.blacktext30.setStrokeWidth(1.0f);
        this.blacktext30.setAntiAlias(true);
        this.blacktext30.setTextSize(this.mDensity * 10.0f);
        Paint paint6 = new Paint();
        this.mGrayLine = paint6;
        paint6.setColor(-7829368);
        this.mGrayLine.setStrokeWidth(1.0f);
        this.mGrayLine.setAntiAlias(true);
        this.mGrayLine.setStyle(Paint.Style.STROKE);
        Paint paint7 = new Paint();
        this.mSosPaint = paint7;
        paint7.setStrokeWidth(1.0f);
        this.mSosPaint.setAntiAlias(true);
        this.mSosPaint.setColor(SupportMenu.CATEGORY_MASK);
        Paint paint8 = new Paint();
        this.mCapturePaint = paint8;
        paint8.setStrokeWidth(1.0f);
        this.mCapturePaint.setAntiAlias(true);
        this.mCapturePaint.setColor(-16776961);
        Paint paint9 = new Paint();
        this.mDelayPaint = paint9;
        paint9.setStrokeWidth(1.0f);
        this.mDelayPaint.setAntiAlias(true);
        this.mDelayPaint.setColor(InputDeviceCompat.SOURCE_ANY);
        this.acalendar = new GregorianCalendar();
        this.left = System.currentTimeMillis() - HOUR_MILLIS;
        this.right = System.currentTimeMillis();
        this.span = r0 - this.left;
        this.mFinger1x = (this.mDeviceWidth / 2) - 200.0f;
        this.mFinger2x = (this.mDeviceWidth / 2) + 200.0f;
        setSelectionMode(this.isSelectionMode);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.width = getWidth();
        this.mHeight = getHeight();
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int measuredWidth = getMeasuredWidth() - getPaddingRight();
        int i5 = measuredWidth - paddingLeft;
        int measuredHeight = (getMeasuredHeight() - getPaddingBottom()) - paddingTop;
        int i6 = paddingLeft;
        int i7 = 0;
        int i8 = 0;
        while (i7 < childCount) {
            View childAt = getChildAt(i7);
            if (childAt.getVisibility() == 8) {
                return;
            }
            childAt.measure(View.MeasureSpec.makeMeasureSpec(i5, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(measuredHeight, Integer.MIN_VALUE));
            int measuredWidth2 = childAt.getMeasuredWidth();
            int measuredHeight2 = childAt.getMeasuredHeight();
            if (i6 + measuredWidth2 >= measuredWidth) {
                paddingTop += i8;
                i6 = paddingLeft;
                i8 = 0;
            }
            int i9 = measuredWidth2 + i6;
            childAt.layout(i6, paddingTop, i9, paddingTop + measuredHeight2);
            if (i8 < measuredHeight2) {
                i8 = measuredHeight2;
            }
            i7++;
            i6 = i9;
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int childCount = getChildCount();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            if (childAt.getVisibility() != 8) {
                measureChild(childAt, i, i2);
                i4 += Math.max(i4, childAt.getMeasuredWidth());
                i6 += childAt.getMeasuredWidth();
                if (i6 / this.mDeviceWidth > i7) {
                    i3 += childAt.getMeasuredHeight();
                    i7++;
                } else {
                    i3 = Math.max(i3, childAt.getMeasuredHeight());
                }
                i5 = combineMeasuredStates(i5, childAt.getMeasuredState());
            }
        }
        setMeasuredDimension(resolveSizeAndState(Math.max(i4, getSuggestedMinimumWidth()), i, i5), resolveSizeAndState(Math.max(i3, getSuggestedMinimumHeight()), i2, i5 << 16));
    }

    public long getCurrentMiddleTime() {
        long j = this.left;
        long j2 = j + ((this.right - j) / 2);
        if (j2 > 0) {
            return j2;
        }
        return -1L;
    }

    public void setTimeOffset(long j) {
        if (this.isMove) {
            Dbug.e(this.tag, "Moving..............");
            return;
        }
        if (j == 0) {
            Dbug.e(this.tag, "offsetMilliseconds = 0");
            return;
        }
        long j2 = this.left;
        long j3 = j - (j2 + ((this.right - j2) / 2));
        if (j3 >= 800 || j3 <= -1200) {
            this.left += j3;
            this.right += j3;
            postInvalidate();
        }
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        this.mOnValueChangeListener = onValueChangeListener;
    }

    private void drawMiddleLine(Canvas canvas) {
        canvas.save();
        int i = this.width;
        int i2 = this.mHeight;
        canvas.drawLine(i / 2, i2 - (this.mDensity * 100.0f), i / 2, i2, this.middlePaint);
        canvas.restore();
    }

    private void drawMiddleTimeText(Canvas canvas) {
        Calendar calendar = this.acalendar;
        long j = this.left;
        calendar.setTimeInMillis(j + ((this.right - j) / 2));
        SimpleDateFormat simpleDateFormat = timeFormat;
        long j2 = this.left;
        String format = simpleDateFormat.format(Long.valueOf(j2 + ((this.right - j2) / 2)));
        float f = this.width / 2;
        float f2 = this.mDensity;
        canvas.drawText(format, f + (5.0f * f2), this.mHeight - (f2 * 85.0f), this.dateAndTimePaint);
        drawDateText(canvas);
    }

    private void drawDateText(Canvas canvas) {
        String date2DateStr = date2DateStr(this.acalendar.getTime());
        float desiredWidth = Layout.getDesiredWidth(date2DateStr, this.textPaint);
        this.textWidth = desiredWidth;
        float f = this.width / 2;
        float f2 = this.mDensity;
        canvas.drawText(date2DateStr, f - (desiredWidth + (5.0f * f2)), this.mHeight - (f2 * 85.0f), this.dateAndTimePaint);
    }

    public String date2DateStr(Date date) {
        return dateFormat.format(date);
    }

    public void setSelectionMode(boolean z) {
        this.isSelectionMode = z;
        if (z) {
            setLeftThumbnailVisibility(0);
            setRightThumbnailVisibility(0);
            this.mFinger1x = (this.mDeviceWidth / 2) - 200.0f;
            this.mFinger2x = (this.mDeviceWidth / 2) + 200.0f;
            long j = this.left + ((long) ((this.mFinger1x * this.span) / this.width));
            this.mFinger1Time = j;
            this.mWhichFinger = 1;
            this.acalendar.setTimeInMillis(j);
            this.isRequestNext = true;
            TextView textView = (TextView) findViewWithTag(TAG_LEFT_TIME);
            if (textView != null) {
                textView.setText(timeFormat.format(Long.valueOf(this.left + ((long) ((this.mFinger1x * this.span) / this.width)))));
            }
            TextView textView2 = (TextView) findViewWithTag(TAG_RIGHT_TIME);
            if (textView2 != null) {
                textView2.setText(timeFormat.format(Long.valueOf(this.left + ((long) ((this.mFinger2x * this.span) / this.width)))));
            }
        } else {
            setLeftThumbnailVisibility(8);
            setRightThumbnailVisibility(8);
            setLeftThumbnail(null);
            setRightThumbnail(null);
            this.isRequestNext = false;
        }
        postInvalidate();
    }

    public boolean isSelectionMode() {
        return this.isSelectionMode;
    }

    public void setThumbnail(byte[] bArr) {
        Dbug.w(this.tag, "setThumbnail: mWhichFinger=" + this.mWhichFinger + ", isRequestNext=" + this.isRequestNext);
        int i = this.mWhichFinger;
        if (i != 1) {
            if (i != 2) {
                return;
            }
            setRightThumbnail(bArr);
            return;
        }
        setLeftThumbnail(bArr);
        if (this.isRequestNext) {
            this.isRequestNext = false;
            long j = this.left + ((long) ((this.mFinger2x * this.span) / this.width));
            this.mFinger2Time = j;
            this.mWhichFinger = 2;
            Calendar.getInstance().setTimeInMillis(j);
        }
    }

    public long getInterceptionStartTime() {
        return this.mFinger1Time;
    }

    public long getInterceptionEndTime() {
        return this.mFinger2Time;
    }

    private void setLeftThumbnail(byte[] bArr) {
        final ImageView imageView;
        View findViewWithTag = findViewWithTag(TAG_LEFT_THUMBNAIL);
        if (findViewWithTag == null || (imageView = (ImageView) findViewWithTag.findViewWithTag(TAG_LEFT_IMAGEVIEW)) == null) {
            return;
        }
        if (bArr != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inPurgeable = true;
            final Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            if (decodeByteArray != null) {
                this.handler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.widget.TLView.2
                    @Override // java.lang.Runnable
                    public void run() {
                        TLView.this.setLeftProgressVisibility(8);
                        imageView.setImageBitmap(decodeByteArray);
                    }
                });
                return;
            }
            return;
        }
        this.handler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.widget.TLView.3
            @Override // java.lang.Runnable
            public void run() {
                imageView.setImageBitmap(null);
                TLView.this.setLeftProgressVisibility(0);
            }
        });
    }

    private void setRightThumbnail(byte[] bArr) {
        final ImageView imageView;
        View findViewWithTag = findViewWithTag(TAG_RIGHT_THUMBNAIL);
        if (findViewWithTag == null || (imageView = (ImageView) findViewWithTag.findViewWithTag(TAG_RIGHT_IMAGEVIEW)) == null) {
            return;
        }
        if (bArr != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inPurgeable = true;
            final Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            if (decodeByteArray != null) {
                this.handler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.widget.TLView.4
                    @Override // java.lang.Runnable
                    public void run() {
                        TLView.this.setRightProgressVisibility(8);
                        imageView.setImageBitmap(decodeByteArray);
                    }
                });
                return;
            }
            return;
        }
        this.handler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.widget.TLView.5
            @Override // java.lang.Runnable
            public void run() {
                imageView.setImageBitmap(null);
                TLView.this.setRightProgressVisibility(0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLeftProgressVisibility(int i) {
        View findViewWithTag;
        View findViewWithTag2 = findViewWithTag(TAG_LEFT_THUMBNAIL);
        if (findViewWithTag2 == null || (findViewWithTag = findViewWithTag2.findViewWithTag(TAG_LEFT_PROG)) == null) {
            return;
        }
        findViewWithTag.setVisibility(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRightProgressVisibility(int i) {
        View findViewWithTag;
        View findViewWithTag2 = findViewWithTag(TAG_RIGHT_THUMBNAIL);
        if (findViewWithTag2 == null || (findViewWithTag = findViewWithTag2.findViewWithTag(TAG_RIGHT_PROG)) == null) {
            return;
        }
        findViewWithTag.setVisibility(i);
    }

    private void setLeftThumbnailVisibility(int i) {
        View findViewWithTag = findViewWithTag(TAG_LEFT_THUMBNAIL);
        if (findViewWithTag != null) {
            findViewWithTag.setVisibility(i);
        }
    }

    private void setRightThumbnailVisibility(int i) {
        View findViewWithTag = findViewWithTag(TAG_RIGHT_THUMBNAIL);
        if (findViewWithTag != null) {
            findViewWithTag.setVisibility(i);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:174:0x0658  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x0739 A[LOOP:3: B:163:0x052d->B:192:0x0739, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0729 A[SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onDraw(android.graphics.Canvas r29) {
        /*
            Method dump skipped, instructions count: 1853
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.widget.TLView.onDraw(android.graphics.Canvas):void");
    }

    private void drawEvents(Canvas canvas) {
        for (FileInfo fileInfo : this.mCalendarStuff) {
            if (fileInfo != null) {
                long timeInMillis = fileInfo.getStartTime().getTimeInMillis();
                if (fileInfo.getEndTime().getTimeInMillis() >= this.left && timeInMillis <= this.right) {
                    float f = this.span;
                    int i = this.width;
                    float f2 = ((timeInMillis - r7) / f) * i;
                    float f3 = ((r5 - r7) / f) * i;
                    float f4 = this.mHeight;
                    float f5 = this.mDensity;
                    int i2 = (int) (f4 - (80.0f * f5));
                    float f6 = f5 * 28.0f;
                    int type = fileInfo.getType();
                    float f7 = f3 - f2;
                    float f8 = this.mDensity;
                    if (f7 > f8 * 2.0f) {
                        if (type == 2) {
                            float f9 = i2;
                            canvas.drawRect(f2, f9, f3, f9 + f6, this.mSosPaint);
                        } else if (type == 1) {
                            float f10 = i2;
                            canvas.drawRect(f2, f10, f3, f10 + f6, this.eventPaint);
                        } else if (type == 3) {
                            float f11 = i2;
                            canvas.drawRect(f2, f11, f3, f11 + f6, this.mDelayPaint);
                        }
                    } else if (type == 2) {
                        float f12 = i2;
                        canvas.drawRect(f2, f12, f2 + (f8 * 2.0f), f12 + f6, this.mSosPaint);
                    } else if (type == 1) {
                        float f13 = i2;
                        canvas.drawRect(f2, f13, f2 + f8, f13 + f6, this.eventPaint);
                    } else if (type == 3) {
                        float f14 = i2;
                        canvas.drawRect(f2, f14, f2 + f8, f14 + f6, this.mDelayPaint);
                    }
                }
            }
        }
    }

    private void drawHourText(Canvas canvas, float f, float f2, int i, int i2) {
        if (i < 12) {
            canvas.drawText(Integer.toString(i2), f, f2, this.blacktext30);
        } else {
            canvas.drawText(Integer.toString(i), f, f2, this.blacktext30);
        }
    }

    public void setMovingLock(boolean z) {
        this.isBuffering = z;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float y;
        float f;
        float y2;
        float f2;
        if (this.isBuffering) {
            return super.onTouchEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.fingers = 1;
            this.finger1id = motionEvent.getPointerId(motionEvent.getActionIndex());
            this.finger1x = motionEvent.getX();
            this.finger1y = motionEvent.getY();
            invalidate();
            return true;
        }
        if (actionMasked == 1) {
            Dbug.i(this.tag, "ACTION_UP=");
            this.fingers = 0;
            invalidate();
            this.acalendar.setTimeInMillis(this.left + ((long) ((this.finger1x * this.span) / this.width)));
            OnValueChangeListener onValueChangeListener = this.mOnValueChangeListener;
            if (onValueChangeListener != null) {
                onValueChangeListener.onValueChangeEnd(this.acalendar);
            }
            return true;
        }
        if (actionMasked == 2) {
            this.isMove = true;
            if (this.fingers == 0) {
                return false;
            }
            int findPointerIndex = motionEvent.findPointerIndex(this.finger1id);
            if (findPointerIndex == -1) {
                f = this.finger1x;
                y = this.finger1y;
            } else {
                float x = motionEvent.getX(findPointerIndex);
                y = motionEvent.getY(findPointerIndex);
                f = x;
            }
            int findPointerIndex2 = motionEvent.findPointerIndex(this.finger2id);
            if (findPointerIndex2 == -1) {
                f2 = this.finger2x;
                y2 = this.finger2y;
            } else {
                float x2 = motionEvent.getX(findPointerIndex2);
                y2 = motionEvent.getY(findPointerIndex2);
                f2 = x2;
            }
            this.finger1x = f;
            this.finger1y = y;
            this.finger2x = f2;
            this.finger2y = y2;
            invalidate();
            return true;
        }
        if (actionMasked == 5) {
            Dbug.d(this.tag, "ACTION_POINTER_DOWN=");
            if (this.fingers != 2) {
                this.fingers = 2;
                int pointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
                this.finger2id = pointerId;
                this.finger2x = motionEvent.getX(pointerId);
                this.finger2y = motionEvent.getY(this.finger2id);
                invalidate();
                return true;
            }
        } else if (actionMasked == 6) {
            int pointerId2 = motionEvent.getPointerId(motionEvent.getActionIndex());
            if (pointerId2 == this.finger1id) {
                this.finger1id = this.finger2id;
                this.finger1x = this.finger2x;
                this.finger1y = this.finger2y;
                this.fingers = 1;
            } else {
                if (pointerId2 != this.finger2id) {
                    return false;
                }
                this.fingers = 1;
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }
}
