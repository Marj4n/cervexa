package com.generalplus.GoPlusDrone.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.github.mikephil.charting.utils.Utils;

/* loaded from: classes.dex */
public class JoystickView extends View implements Runnable {
    public static final int BOTTOM = 7;
    public static final int BOTTOM_LEFT = 8;
    public static final long DEFAULT_LOOP_INTERVAL = 100;
    public static final int FRONT = 3;
    public static final int FRONT_RIGHT = 4;
    public static final int LEFT = 1;
    public static final int LEFT_FRONT = 2;
    public static final int RIGHT = 5;
    public static final int RIGHT_BOTTOM = 6;
    private final double RAD;
    private Paint button;
    private int buttonRadius;
    private double centerX;
    private double centerY;
    private int joystickRadius;
    private int lastAngle;
    private int lastPower;
    private long loopInterval;
    private boolean m_bLeft;
    private Paint mainCircle;
    private OnJoystickMoveListener onJoystickMoveListener;
    private Thread thread;
    private int xPosition;
    private int yPosition;

    public interface OnJoystickMoveListener {
        void onValueChanged(int i, int i2, int i3);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public JoystickView(Context context) {
        super(context);
        this.RAD = 57.2957795d;
        this.thread = new Thread(this);
        this.loopInterval = 100L;
        this.xPosition = 0;
        this.yPosition = 0;
        this.centerX = Utils.DOUBLE_EPSILON;
        this.centerY = Utils.DOUBLE_EPSILON;
        this.lastAngle = 0;
        this.lastPower = 0;
        this.m_bLeft = true;
    }

    public JoystickView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.RAD = 57.2957795d;
        this.thread = new Thread(this);
        this.loopInterval = 100L;
        this.xPosition = 0;
        this.yPosition = 0;
        this.centerX = Utils.DOUBLE_EPSILON;
        this.centerY = Utils.DOUBLE_EPSILON;
        this.lastAngle = 0;
        this.lastPower = 0;
        this.m_bLeft = true;
        initJoystickView();
    }

    public JoystickView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.RAD = 57.2957795d;
        this.thread = new Thread(this);
        this.loopInterval = 100L;
        this.xPosition = 0;
        this.yPosition = 0;
        this.centerX = Utils.DOUBLE_EPSILON;
        this.centerY = Utils.DOUBLE_EPSILON;
        this.lastAngle = 0;
        this.lastPower = 0;
        this.m_bLeft = true;
        initJoystickView();
    }

    public void setLeft(boolean z) {
        this.m_bLeft = z;
    }

    protected void initJoystickView() {
        Paint paint = new Paint(1);
        this.mainCircle = paint;
        paint.setColor(-1);
        this.mainCircle.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mainCircle.setAlpha(50);
        Paint paint2 = new Paint(1);
        this.button = paint2;
        paint2.setColor(-16744193);
        this.button.setStyle(Paint.Style.FILL);
        this.button.setAlpha(100);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.xPosition = getWidth() / 2;
        this.yPosition = getWidth() / 2;
        double min = Math.min(i, i2) / 2;
        this.buttonRadius = (int) (0.25d * min);
        this.joystickRadius = (int) (min * 0.75d);
        if (this.m_bLeft) {
            this.yPosition = getWidth() - this.buttonRadius;
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int min = Math.min(measure(i), measure(i2));
        setMeasuredDimension(min, min);
    }

    private int measure(int i) {
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        if (mode == 0) {
            return 200;
        }
        return size;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        this.centerX = getWidth() / 2;
        this.centerY = getHeight() / 2;
        Object r0 = null;
        canvas.drawCircle((int) this.centerX, (int) r0, this.joystickRadius, this.mainCircle);
        canvas.drawCircle(this.xPosition, this.yPosition, this.buttonRadius, this.button);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.xPosition = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        this.yPosition = y;
        int i = this.xPosition;
        double d = this.centerX;
        double d2 = this.centerY;
        double sqrt = Math.sqrt(((i - d) * (i - d)) + ((y - d2) * (y - d2)));
        int i2 = this.joystickRadius;
        if (sqrt > i2) {
            double d3 = this.xPosition;
            double d4 = this.centerX;
            this.xPosition = (int) ((((d3 - d4) * i2) / sqrt) + d4);
            double d5 = this.yPosition;
            double d6 = this.centerY;
            this.yPosition = (int) ((((d5 - d6) * i2) / sqrt) + d6);
        }
        invalidate();
        if (motionEvent.getAction() == 1) {
            this.xPosition = (int) this.centerX;
            if (!this.m_bLeft) {
                this.yPosition = (int) this.centerY;
            }
            this.thread.interrupt();
            OnJoystickMoveListener onJoystickMoveListener = this.onJoystickMoveListener;
            if (onJoystickMoveListener != null) {
                onJoystickMoveListener.onValueChanged(getAngle(), getPower(), getDirection());
            }
        }
        if (this.onJoystickMoveListener != null && motionEvent.getAction() == 0) {
            Thread thread = this.thread;
            if (thread != null && thread.isAlive()) {
                this.thread.interrupt();
            }
            Thread thread2 = new Thread(this);
            this.thread = thread2;
            thread2.start();
            OnJoystickMoveListener onJoystickMoveListener2 = this.onJoystickMoveListener;
            if (onJoystickMoveListener2 != null) {
                onJoystickMoveListener2.onValueChanged(getAngle(), getPower(), getDirection());
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getAngle() {
        int i = this.xPosition;
        double d = i;
        double d2 = this.centerX;
        if (d > d2) {
            int i2 = this.yPosition;
            double d3 = i2;
            double d4 = this.centerY;
            if (d3 < d4) {
                int atan = (int) ((Math.atan((i2 - d4) / (i - d2)) * 57.2957795d) + 90.0d);
                this.lastAngle = atan;
                return atan;
            }
            if (i2 > d4) {
                int atan2 = ((int) (Math.atan((i2 - d4) / (i - d2)) * 57.2957795d)) + 90;
                this.lastAngle = atan2;
                return atan2;
            }
            this.lastAngle = 90;
            return 90;
        }
        if (i >= d2) {
            if (this.yPosition <= this.centerY) {
                this.lastAngle = 0;
                return 0;
            }
            if (this.lastAngle < 0) {
                this.lastAngle = -180;
                return -180;
            }
            this.lastAngle = 180;
            return 180;
        }
        int i3 = this.yPosition;
        double d5 = i3;
        double d6 = this.centerY;
        if (d5 < d6) {
            int atan3 = (int) ((Math.atan((i3 - d6) / (i - d2)) * 57.2957795d) - 90.0d);
            this.lastAngle = atan3;
            return atan3;
        }
        if (i3 > d6) {
            int atan4 = ((int) (Math.atan((i3 - d6) / (i - d2)) * 57.2957795d)) - 90;
            this.lastAngle = atan4;
            return atan4;
        }
        this.lastAngle = -90;
        return -90;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPower() {
        if (this.m_bLeft) {
            return getLeftPower();
        }
        int i = this.xPosition;
        double d = this.centerX;
        double d2 = (i - d) * (i - d);
        int i2 = this.yPosition;
        double d3 = this.centerY;
        return (int) ((Math.sqrt(d2 + ((i2 - d3) * (i2 - d3))) * 100.0d) / this.joystickRadius);
    }

    private int getLeftPower() {
        return Math.round((float) ((Math.abs((this.yPosition - this.buttonRadius) - (this.joystickRadius * 2)) * 100) / (this.joystickRadius * 2)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDirection() {
        int i = 0;
        if (this.lastPower == 0 && this.lastAngle == 0) {
            return 0;
        }
        int i2 = this.lastAngle;
        if (i2 <= 0) {
            i = (i2 * (-1)) + 90;
        } else if (i2 > 0) {
            i = i2 <= 90 ? 90 - i2 : 360 - (i2 - 90);
        }
        int i3 = ((i + 22) / 45) + 1;
        if (i3 > 8) {
            return 1;
        }
        return i3;
    }

    public void setOnJoystickMoveListener(OnJoystickMoveListener onJoystickMoveListener, long j) {
        this.onJoystickMoveListener = onJoystickMoveListener;
        this.loopInterval = j;
    }

    @Override // java.lang.Runnable
    public void run() {
        while (!Thread.interrupted()) {
            post(new Runnable() { // from class: com.generalplus.GoPlusDrone.View.JoystickView.1
                @Override // java.lang.Runnable
                public void run() {
                    if (JoystickView.this.onJoystickMoveListener != null) {
                        JoystickView.this.onJoystickMoveListener.onValueChanged(JoystickView.this.getAngle(), JoystickView.this.getPower(), JoystickView.this.getDirection());
                    }
                }
            });
            try {
                Thread.sleep(this.loopInterval);
            } catch (InterruptedException unused) {
                return;
            }
        }
    }
}
