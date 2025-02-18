package com.shizhefei.view.indicator.slidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import com.shizhefei.view.indicator.slidebar.ScrollBar;

/* loaded from: classes2.dex */
public class SpringBar extends View implements ScrollBar {
    private float acceleration;
    private Point foot;
    private float footMoveOffset;
    private Point head;
    private float headMoveOffset;
    private float maxRadiusPercent;
    private float minRadiusPercent;
    private Paint paint;
    private Path path;
    private float positionOffset;
    private float radiusMax;
    private float radiusMin;
    private float radiusOffset;
    private int tabWidth;

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public View getSlideView() {
        return this;
    }

    public SpringBar(Context context, int i) {
        this(context, i, 0.9f, 0.35f);
    }

    public SpringBar(Context context, int i, float f, float f2) {
        super(context);
        this.acceleration = 0.5f;
        this.headMoveOffset = 0.6f;
        this.footMoveOffset = 1.0f - 0.6f;
        this.maxRadiusPercent = f;
        this.minRadiusPercent = f2;
        this.foot = new Point();
        this.head = new Point();
        this.path = new Path();
        Paint paint = new Paint();
        this.paint = paint;
        paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paint.setStrokeWidth(1.0f);
        this.paint.setColor(i);
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public int getHeight(int i) {
        float f = i / 2;
        this.foot.setY(f);
        this.head.setY(f);
        float f2 = this.maxRadiusPercent * f;
        this.radiusMax = f2;
        float f3 = f * this.minRadiusPercent;
        this.radiusMin = f3;
        this.radiusOffset = f2 - f3;
        return i;
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public int getWidth(int i) {
        this.tabWidth = i;
        float f = this.positionOffset;
        if (f < 0.02f || f > 0.98f) {
            onPageScrolled(0, 0.0f, 0);
        }
        return i * 2;
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public ScrollBar.Gravity getGravity() {
        return ScrollBar.Gravity.CENTENT_BACKGROUND;
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public void onPageScrolled(int i, float f, int i2) {
        this.positionOffset = f;
        float f2 = 0.0f;
        if (f < 0.02f || f > 0.98f) {
            this.head.setX(getOffsetX(0.0f));
            this.foot.setX(getOffsetX(0.0f));
            this.head.setRadius(this.radiusMax);
            this.foot.setRadius(this.radiusMax);
            return;
        }
        if (f < 0.5f) {
            this.head.setRadius(this.radiusMin);
        } else {
            this.head.setRadius((((f - 0.5f) / 0.5f) * this.radiusOffset) + this.radiusMin);
        }
        float f3 = 1.0f;
        if (f < 0.5f) {
            this.foot.setRadius(((1.0f - (f / 0.5f)) * this.radiusOffset) + this.radiusMin);
        } else {
            this.foot.setRadius(this.radiusMin);
        }
        float f4 = this.headMoveOffset;
        if (f > f4) {
            float f5 = (f - f4) / (1.0f - f4);
            float f6 = this.acceleration;
            f2 = (float) ((Math.atan(((f5 * f6) * 2.0f) - f6) + Math.atan(this.acceleration)) / (Math.atan(this.acceleration) * 2.0d));
        }
        this.head.setX(getOffsetX(f) - (f2 * getPositionDistance(i)));
        if (f < this.footMoveOffset) {
            float f7 = this.acceleration;
            f3 = (float) ((Math.atan((((f / r12) * f7) * 2.0f) - f7) + Math.atan(this.acceleration)) / (Math.atan(this.acceleration) * 2.0d));
        }
        this.foot.setX(getOffsetX(f) - (f3 * getPositionDistance(i)));
    }

    private float getOffsetX(float f) {
        int i = this.tabWidth;
        return (((i * 2) - (i / 4)) - (i * (1.0f - f))) + (i / 4.0f);
    }

    private float getPositionDistance(int i) {
        return this.tabWidth;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        makePath();
        canvas.drawColor(0);
        canvas.drawPath(this.path, this.paint);
        canvas.drawCircle(this.head.getX(), this.head.getY(), this.head.getRadius(), this.paint);
        canvas.drawCircle(this.foot.getX(), this.foot.getY(), this.foot.getRadius(), this.paint);
        super.onDraw(canvas);
    }

    private void makePath() {
        float radius = (float) (this.foot.getRadius() * Math.sin(Math.atan((this.head.getY() - this.foot.getY()) / (this.head.getX() - this.foot.getX()))));
        float radius2 = (float) (this.foot.getRadius() * Math.cos(Math.atan((this.head.getY() - this.foot.getY()) / (this.head.getX() - this.foot.getX()))));
        float radius3 = (float) (this.head.getRadius() * Math.sin(Math.atan((this.head.getY() - this.foot.getY()) / (this.head.getX() - this.foot.getX()))));
        float radius4 = (float) (this.head.getRadius() * Math.cos(Math.atan((this.head.getY() - this.foot.getY()) / (this.head.getX() - this.foot.getX()))));
        float x = this.foot.getX() - radius;
        float y = this.foot.getY() + radius2;
        float x2 = this.foot.getX() + radius;
        float y2 = this.foot.getY() - radius2;
        float x3 = this.head.getX() - radius3;
        float y3 = this.head.getY() + radius4;
        float x4 = this.head.getX() + radius3;
        float y4 = this.head.getY() - radius4;
        float x5 = (this.head.getX() + this.foot.getX()) / 2.0f;
        float y5 = (this.head.getY() + this.foot.getY()) / 2.0f;
        this.path.reset();
        this.path.moveTo(x, y);
        this.path.quadTo(x5, y5, x3, y3);
        this.path.lineTo(x4, y4);
        this.path.quadTo(x5, y5, x2, y2);
        this.path.lineTo(x, y);
    }

    private class Point {
        private float radius;
        private float x;
        private float y;

        private Point() {
        }

        public float getX() {
            return this.x;
        }

        public void setX(float f) {
            this.x = f;
        }

        public float getY() {
            return this.y;
        }

        public void setY(float f) {
            this.y = f;
        }

        public float getRadius() {
            return this.radius;
        }

        public void setRadius(float f) {
            this.radius = f;
        }
    }
}
