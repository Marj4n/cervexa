package com.jieli.stream.dv.running2.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.Dbug;

/* loaded from: classes.dex */
public class PlaybackSeekbar extends View {
    public static final int BROWSE_CONTENT_MODE = 1;
    public static final int BROWSE_COVER_MODE = 0;
    public static final int REAL_TIME_STREAM_MODE = 2;
    private Paint backgroundPaint;
    private float baseline;
    private float browseCursorPos;
    private int browseMaxPostion;
    private Paint browseRightPaint;
    private int browseinterval;
    private int currentSelected;
    private Paint cursorPaint;
    private Paint cursorThumbPaint;
    private float density;
    private float endPos;
    private Paint grayPaint;
    private int halfHeight;
    private long left;
    private String leftTip;
    private int mHeight;
    private OnStatechangeListener mOnStatechangeListener;
    private int mWidth;
    private int mode;
    private int offset;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private long right;
    private String rightTip;
    private FileInfo selectFileInfo;
    private String tag;
    private Paint textPaint;
    private Paint textTipPaint;
    private int thumbRadio;
    private long time;
    private float timeCursorPos;
    private Paint timeLeftPaint;
    private Paint timeSosPaint;
    private Paint tipBackgroundPaint;
    private float tipHeight;
    private String tipText;
    private float tipWidth;

    public interface OnStatechangeListener {
        void onBrowseContentChange(long j, int i);

        void onBrowseCoverChange(int i);

        void onModeChange(int i);
    }

    public void setOnStatechangeListener(OnStatechangeListener onStatechangeListener) {
        this.mOnStatechangeListener = onStatechangeListener;
    }

    public void setMode(int i) {
        OnStatechangeListener onStatechangeListener;
        setModeNotCallback(i);
        if (this.browseMaxPostion <= -1 || (onStatechangeListener = this.mOnStatechangeListener) == null) {
            return;
        }
        onStatechangeListener.onModeChange(i);
    }

    public int getMode() {
        return this.mode;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.selectFileInfo = fileInfo;
        this.left = fileInfo.getStartTime().getTimeInMillis();
        long timeInMillis = fileInfo.getEndTime().getTimeInMillis();
        this.right = timeInMillis;
        this.time = timeInMillis - this.left;
        this.timeCursorPos = this.paddingLeft;
        this.offset = 0;
        invalidate();
    }

    public void setModeNotCallback(int i) {
        this.mode = i;
        if (i == 0) {
            this.tipText = this.rightTip;
        } else if (i == 2) {
            this.tipText = this.leftTip;
            this.browseCursorPos = this.endPos;
        }
        Dbug.i(this.tag, modeToString(i));
        invalidate();
    }

    public void setFileInfoCount(int i) {
        if (i < 0) {
            return;
        }
        this.browseMaxPostion = i - 1;
        setBrowseinterval(i);
        int i2 = this.browseMaxPostion;
        if (i2 > 0) {
            float f = this.endPos;
            this.browseCursorPos = f - ((this.currentSelected * f) / i2);
        } else {
            this.currentSelected = 0;
        }
        if (this.mode == 2) {
            float f2 = this.browseCursorPos;
            float f3 = this.endPos;
            if (f2 < f3) {
                this.browseCursorPos = f3 + this.thumbRadio;
            }
        }
        invalidate();
    }

    public void setBrowseinterval(int i) {
        if (i < 60 && i > 0) {
            this.browseinterval = (int) ((this.endPos - this.paddingLeft) / i);
        } else if (i >= 60) {
            this.browseinterval = (int) ((this.endPos - this.paddingLeft) / 60.0f);
        } else {
            this.browseinterval = 0;
        }
    }

    public PlaybackSeekbar(Context context) {
        this(context, null);
    }

    public PlaybackSeekbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PlaybackSeekbar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.tag = getClass().getSimpleName();
        this.thumbRadio = 3;
        this.paddingRight = 20;
        this.paddingLeft = 20;
        this.paddingTop = 20;
        this.density = 1.0f;
        this.browseinterval = -1;
        this.tipText = "";
        this.timeCursorPos = 20;
        this.offset = 0;
        init();
    }

    private void init() {
        this.density = getResources().getDisplayMetrics().density;
        Dbug.i(this.tag, "density->" + this.density);
        float f = (float) this.thumbRadio;
        float f2 = this.density;
        this.thumbRadio = (int) (f * f2);
        int i = (int) (f2 * 6.0f);
        this.paddingTop = i;
        this.paddingLeft = i;
        this.paddingRight = i;
        Paint paint = new Paint();
        this.backgroundPaint = paint;
        paint.setColor(getResources().getColor(R.color.half_transparent));
        this.backgroundPaint.setStrokeWidth(1.0f);
        this.backgroundPaint.setAntiAlias(true);
        this.backgroundPaint.setStyle(Paint.Style.FILL);
        TextPaint textPaint = new TextPaint(1);
        this.textPaint = textPaint;
        textPaint.setColor(-1);
        this.textPaint.setTextSize(getResources().getDisplayMetrics().density * 15.0f);
        Paint paint2 = new Paint();
        this.grayPaint = paint2;
        paint2.setColor(-7829368);
        this.grayPaint.setStrokeWidth(1.0f);
        this.grayPaint.setAntiAlias(true);
        this.grayPaint.setStyle(Paint.Style.STROKE);
        Paint paint3 = new Paint();
        this.cursorPaint = paint3;
        paint3.setColor(getResources().getColor(R.color.bg_playback_seek_thumb));
        this.cursorPaint.setStrokeWidth(this.thumbRadio / 2);
        this.cursorPaint.setAntiAlias(true);
        this.cursorPaint.setStyle(Paint.Style.FILL);
        Paint paint4 = new Paint();
        this.cursorThumbPaint = paint4;
        paint4.setColor(-1);
        this.cursorThumbPaint.setStrokeWidth(2.0f);
        this.cursorThumbPaint.setAntiAlias(true);
        this.cursorThumbPaint.setStyle(Paint.Style.FILL);
        Paint paint5 = new Paint();
        this.browseRightPaint = paint5;
        paint5.setColor(getResources().getColor(R.color.bg_playback_seek_bar));
        this.browseRightPaint.setStrokeWidth(2.0f);
        this.browseRightPaint.setAntiAlias(true);
        this.browseRightPaint.setStyle(Paint.Style.FILL);
        Paint paint6 = new Paint();
        this.timeLeftPaint = paint6;
        paint6.setColor(getResources().getColor(R.color.bg_playback_seek_bar));
        this.timeLeftPaint.setStrokeWidth(2.0f);
        this.timeLeftPaint.setAntiAlias(true);
        this.timeLeftPaint.setStyle(Paint.Style.FILL);
        Paint paint7 = new Paint();
        this.timeSosPaint = paint7;
        paint7.setColor(getResources().getColor(R.color.bg_playback_seek_bar));
        this.timeSosPaint.setStrokeWidth(2.0f);
        this.timeSosPaint.setAntiAlias(true);
        this.timeSosPaint.setStyle(Paint.Style.FILL);
        Paint paint8 = new Paint();
        this.textTipPaint = paint8;
        paint8.setColor(getResources().getColor(R.color.text_white));
        this.textTipPaint.setAntiAlias(true);
        this.textTipPaint.setTextSize(this.density * 14.0f);
        this.textTipPaint.setTextAlign(Paint.Align.CENTER);
        this.leftTip = getResources().getString(R.string.playback_seekbar_tip_left);
        this.rightTip = getResources().getString(R.string.playback_seekbar_tip_right);
        String str = this.leftTip;
        this.tipText = str;
        float measureText = this.textTipPaint.measureText(str);
        float measureText2 = this.textTipPaint.measureText(this.rightTip);
        if (measureText <= measureText2) {
            measureText = measureText2;
        }
        this.tipWidth = measureText;
        this.tipWidth = measureText + 20.0f;
        this.tipHeight = (this.textTipPaint.descent() - this.textTipPaint.ascent()) * 1.5f;
        Paint.FontMetricsInt fontMetricsInt = this.textTipPaint.getFontMetricsInt();
        this.baseline = ((((this.tipHeight - fontMetricsInt.bottom) + fontMetricsInt.top) / 2.0f) - fontMetricsInt.top) + this.paddingTop;
        Paint paint9 = new Paint();
        this.tipBackgroundPaint = paint9;
        paint9.setColor(getResources().getColor(R.color.bg_playback_tip));
        this.tipBackgroundPaint.setAntiAlias(true);
        this.tipBackgroundPaint.setStrokeWidth(2.0f);
        this.tipBackgroundPaint.setStyle(Paint.Style.FILL);
        int dp2px = AppUtils.dp2px(getContext(), 10);
        this.paddingRight = dp2px;
        this.paddingLeft = dp2px;
    }

    private void drawTimeRule(Canvas canvas) {
        RectF rectF = new RectF(0.0f, this.halfHeight, this.mWidth, this.mHeight);
        canvas.drawRect(rectF, this.backgroundPaint);
        rectF.set(0.0f, this.halfHeight, this.timeCursorPos, this.mHeight);
        canvas.drawRect(rectF, this.timeLeftPaint);
        this.textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("0", this.paddingLeft, this.mHeight * 0.85f, this.textPaint);
        this.textPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(formatTime((int) this.time), this.mWidth - this.paddingRight, this.mHeight * 0.85f, this.textPaint);
        float f = this.timeCursorPos;
        canvas.drawLine(f, this.halfHeight, f, this.mHeight, this.cursorPaint);
        float f2 = this.timeCursorPos;
        int i = this.thumbRadio;
        int i2 = this.mHeight;
        int i3 = this.halfHeight;
        canvas.drawRect(f2 - i, (((i2 - i3) / 2) + i3) - i, i + f2, ((i2 - i3) / 2) + i3 + i, this.cursorThumbPaint);
    }

    private String formatTime(int i) {
        int i2 = i / 1000;
        return String.format("%02d", i2 / 60) + ":" + String.format("%02d", i2 % 60);
    }

    private void drawBrowseRule(Canvas canvas) {
        canvas.drawRect(new RectF(this.browseCursorPos, this.halfHeight, this.mWidth, this.mHeight), this.browseRightPaint);
        if (this.browseinterval > 0) {
            int i = this.paddingLeft;
            int i2 = 0;
            while (i < this.mWidth) {
                if (i2 == 5) {
                    float f = i;
                    canvas.drawLine(f, this.halfHeight, f, this.mHeight, this.grayPaint);
                    i2 = 0;
                } else {
                    float f2 = i;
                    canvas.drawLine(f2, this.halfHeight + 20, f2, this.mHeight - 20, this.grayPaint);
                    i2++;
                }
                i += this.browseinterval;
            }
        }
        float f3 = this.browseCursorPos;
        canvas.drawLine(f3, this.halfHeight, f3, this.mHeight, this.cursorPaint);
        float f4 = this.browseCursorPos;
        int i3 = this.thumbRadio;
        int i4 = this.mHeight;
        int i5 = this.halfHeight;
        canvas.drawRect(f4 - i3, (((i4 - i5) / 2) + i5) - i3, f4 + i3, ((i4 - i5) / 2) + i5 + i3, this.cursorThumbPaint);
    }

    private void drawTip(Canvas canvas) {
        float f = this.browseCursorPos;
        float f2 = this.tipWidth;
        float f3 = (f2 / 2.0f) + f;
        int i = this.mWidth;
        if (f3 >= i) {
            f = i - (f2 / 2.0f);
        } else if (f - (f2 / 2.0f) <= 0.0f) {
            f = f2 / 2.0f;
        }
        float f4 = this.tipWidth;
        int i2 = this.paddingTop;
        RectF rectF = new RectF(f - (f4 / 2.0f), i2, (f4 / 2.0f) + f, this.tipHeight + i2);
        canvas.drawRoundRect(rectF, 10.0f, 10.0f, this.cursorPaint);
        canvas.drawText(this.tipText, f, this.baseline, this.textTipPaint);
        canvas.drawLine(this.browseCursorPos, rectF.bottom, this.browseCursorPos, this.halfHeight - (this.thumbRadio * 2), this.tipBackgroundPaint);
        float f5 = this.browseCursorPos;
        int i3 = this.halfHeight;
        canvas.drawCircle(f5, i3 - (r2 * 2), this.thumbRadio, this.tipBackgroundPaint);
    }

    private void handleBrowseCoverTouchEvent(MotionEvent motionEvent) {
        float f;
        float x = motionEvent.getX();
        int i = this.thumbRadio;
        if (x < i) {
            f = i;
        } else {
            f = x < ((float) (this.mWidth - i)) ? x : r2 - i;
        }
        this.browseCursorPos = f;
        float f2 = this.endPos;
        if (x > f2) {
            this.mode = 2;
            this.tipText = this.leftTip;
            OnStatechangeListener onStatechangeListener = this.mOnStatechangeListener;
            if (onStatechangeListener != null) {
                onStatechangeListener.onModeChange(getMode());
                return;
            }
            return;
        }
        int round = Math.round(((f2 - f) / (f2 - this.paddingLeft)) * this.browseMaxPostion);
        if (round < 0 || this.browseMaxPostion < 0) {
            return;
        }
        if (round != this.currentSelected || motionEvent.getAction() == 1) {
            int i2 = this.browseMaxPostion;
            if (round > i2) {
                round = i2;
            }
            this.currentSelected = round;
            OnStatechangeListener onStatechangeListener2 = this.mOnStatechangeListener;
            if (onStatechangeListener2 != null) {
                onStatechangeListener2.onBrowseCoverChange(round);
            } else {
                Dbug.d(this.tag, "mOnStatechangeListener is null");
            }
        }
    }

    private void handleBrowseContentTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        int i = this.thumbRadio;
        if (x < i) {
            x = i;
        } else {
            int i2 = this.mWidth;
            if (x >= i2 - i) {
                x = i2 - i;
            }
        }
        this.timeCursorPos = x;
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            float f = this.timeCursorPos;
            int i3 = this.paddingLeft;
            int i4 = (int) (((f - i3) / (this.endPos - i3)) * this.time);
            this.offset = i4;
            long j = i4 + this.left;
            OnStatechangeListener onStatechangeListener = this.mOnStatechangeListener;
            if (onStatechangeListener != null) {
                onStatechangeListener.onBrowseContentChange(j, i4);
            }
        }
    }

    private void handleRealTimeModeTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getX() > this.endPos) {
            float x = motionEvent.getX();
            int i = this.mWidth;
            int i2 = this.thumbRadio;
            if (x < i - i2) {
                this.browseCursorPos = motionEvent.getX();
                return;
            } else {
                this.browseCursorPos = i - i2;
                return;
            }
        }
        this.mode = 0;
        this.tipText = this.rightTip;
        OnStatechangeListener onStatechangeListener = this.mOnStatechangeListener;
        if (onStatechangeListener != null) {
            onStatechangeListener.onModeChange(getMode());
        }
        handleBrowseCoverTouchEvent(motionEvent);
    }

    private void dispatchEvent(MotionEvent motionEvent) {
        int mode = getMode();
        if (mode == 0) {
            handleBrowseCoverTouchEvent(motionEvent);
        } else if (mode == 1) {
            handleBrowseContentTouchEvent(motionEvent);
        } else {
            if (mode != 2) {
                return;
            }
            handleRealTimeModeTouchEvent(motionEvent);
        }
    }

    public void setBrowsePostion(int i) {
        if (i == this.currentSelected || this.mode == 2) {
            return;
        }
        this.currentSelected = i;
        int i2 = this.browseMaxPostion;
        if (i2 > 0) {
            this.browseCursorPos = ((i2 - i) * this.endPos) / i2;
        }
        invalidate();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            return motionEvent.getY() >= ((float) this.halfHeight);
        }
        if (actionMasked == 1 || actionMasked == 2 || actionMasked == 3) {
            dispatchEvent(motionEvent);
            invalidate();
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(new RectF(0.0f, this.halfHeight, this.mWidth, this.mHeight), this.backgroundPaint);
        if (getMode() == 1) {
            drawTimeRule(canvas);
        } else if (getMode() == 0 || getMode() == 2) {
            drawBrowseRule(canvas);
            drawTip(canvas);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mWidth = measureWidth(i);
        int measureHeight = measureHeight(i2);
        this.mHeight = measureHeight;
        this.halfHeight = (int) (measureHeight / 1.8f);
        float strokeWidth = (this.mWidth - this.cursorPaint.getStrokeWidth()) - this.paddingRight;
        this.endPos = strokeWidth;
        if (this.mode == 2 && this.browseCursorPos <= strokeWidth) {
            this.browseCursorPos = strokeWidth + this.thumbRadio;
        }
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    private int measureWidth(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == Integer.MIN_VALUE || mode == 1073741824) {
            return size;
        }
        return 500;
    }

    private int measureHeight(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == Integer.MIN_VALUE || mode == 1073741824) {
            return size;
        }
        return 100;
    }

    public static String modeToString(int i) {
        if (i == 0) {
            return "PlaybackSeekBar mode: 封面浏览";
        }
        if (i == 1) {
            return "PlaybackSeekBar mode: 内容浏览";
        }
        if (i != 2) {
            return "PlaybackSeekBar mode: ";
        }
        return "PlaybackSeekBar mode: 实时流浏览";
    }

    public int getOffset() {
        return this.offset;
    }
}
