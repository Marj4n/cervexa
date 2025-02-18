package com.serenegiant.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.serenegiant.common.R;
import java.util.Locale;

/* loaded from: classes2.dex */
public final class ItemPicker extends LinearLayout {
    private static final char[] DIGIT_CHARACTERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private int mCurrentValue;
    private boolean mDecrement;
    private ItemPickerButton mDecrementButton;
    private String[] mDisplayedValues;
    private Formatter mFormatter;
    private final Handler mHandler;
    private boolean mIncrement;
    private ItemPickerButton mIncrementButton;
    private OnChangedListener mListener;
    private int mMaxValue;
    private int mMinValue;
    private final InputFilter mNumberInputFilter;
    private int mPrevValue;
    private final Runnable mRunnable;
    private long mSpeed;
    private final EditText mText;

    public interface Formatter {
        String toString(int i);
    }

    public interface OnChangedListener {
        void onChanged(ItemPicker itemPicker, int i, int i2);
    }

    public ItemPicker(Context context) {
        this(context, null);
    }

    public ItemPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ItemPicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.mHandler = new Handler();
        this.mRunnable = new Runnable() { // from class: com.serenegiant.widget.ItemPicker.1
            @Override // java.lang.Runnable
            public void run() {
                if (!ItemPicker.this.mIncrement) {
                    if (ItemPicker.this.mDecrement) {
                        ItemPicker.this.changeCurrent(r0.mCurrentValue - 1);
                        ItemPicker.this.mHandler.postDelayed(this, ItemPicker.this.mSpeed);
                        return;
                    }
                    return;
                }
                ItemPicker itemPicker = ItemPicker.this;
                itemPicker.changeCurrent(itemPicker.mCurrentValue + 1);
                ItemPicker.this.mHandler.postDelayed(this, ItemPicker.this.mSpeed);
            }
        };
        this.mSpeed = 300L;
        setOrientation(0);
        setGravity(16);
        LayoutInflater.from(context).inflate(R.layout.item_picker, (ViewGroup) this, true);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ItemPicker, i, 0);
        int i2 = obtainStyledAttributes.getInt(R.styleable.ItemPicker_ItemPickerMinItemValue, -1);
        int i3 = obtainStyledAttributes.getInt(R.styleable.ItemPicker_ItemPickerMaxItemValue, -1);
        int resourceId = obtainStyledAttributes.getResourceId(R.styleable.ItemPicker_ItemPickerDisplayedValue, -1);
        String[] stringArray = resourceId > -1 ? getResources().getStringArray(resourceId) : null;
        int resourceId2 = obtainStyledAttributes.getResourceId(R.styleable.ItemPicker_ItemPickerIncrementBackground, -1);
        int resourceId3 = obtainStyledAttributes.getResourceId(R.styleable.ItemPicker_ItemPickerDecrementBackground, -1);
        int resourceId4 = obtainStyledAttributes.getResourceId(R.styleable.ItemPicker_ItemPickerIncrementSrc, -1);
        int resourceId5 = obtainStyledAttributes.getResourceId(R.styleable.ItemPicker_ItemPickerDecrementSrc, -1);
        int resourceId6 = obtainStyledAttributes.getResourceId(R.styleable.ItemPicker_ItemPickerEditTextBackground, -1);
        int i4 = obtainStyledAttributes.getInt(R.styleable.ItemPicker_ItemPickerCurrentItemValue, -1);
        int i5 = obtainStyledAttributes.getInt(R.styleable.ItemPicker_ItemPickerSpeed, -1);
        obtainStyledAttributes.recycle();
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.serenegiant.widget.ItemPicker.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ItemPicker itemPicker = ItemPicker.this;
                itemPicker.validateInput(itemPicker.mText);
                if (!ItemPicker.this.mText.hasFocus()) {
                    ItemPicker.this.mText.requestFocus();
                }
                if (R.id.increment == view.getId()) {
                    ItemPicker itemPicker2 = ItemPicker.this;
                    itemPicker2.changeCurrent(itemPicker2.mCurrentValue + 1);
                } else if (R.id.decrement == view.getId()) {
                    ItemPicker.this.changeCurrent(r3.mCurrentValue - 1);
                }
            }
        };
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.serenegiant.widget.ItemPicker.3
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    return;
                }
                ItemPicker.this.validateInput(view);
            }
        };
        View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() { // from class: com.serenegiant.widget.ItemPicker.4
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                ItemPicker.this.mText.clearFocus();
                if (R.id.increment == view.getId()) {
                    ItemPicker.this.mIncrement = true;
                    ItemPicker.this.mHandler.post(ItemPicker.this.mRunnable);
                } else if (R.id.decrement == view.getId()) {
                    ItemPicker.this.mDecrement = true;
                    ItemPicker.this.mHandler.post(ItemPicker.this.mRunnable);
                }
                return true;
            }
        };
        NumberPickerInputFilter numberPickerInputFilter = new NumberPickerInputFilter();
        this.mNumberInputFilter = new NumberRangeKeyListener();
        ItemPickerButton itemPickerButton = (ItemPickerButton) findViewById(R.id.increment);
        this.mIncrementButton = itemPickerButton;
        itemPickerButton.setOnClickListener(onClickListener);
        this.mIncrementButton.setOnLongClickListener(onLongClickListener);
        this.mIncrementButton.setNumberPicker(this);
        if (resourceId2 != -1) {
            this.mIncrementButton.setBackgroundResource(resourceId2);
        }
        if (resourceId4 != -1) {
            this.mIncrementButton.setImageResource(resourceId4);
        }
        ItemPickerButton itemPickerButton2 = (ItemPickerButton) findViewById(R.id.decrement);
        this.mDecrementButton = itemPickerButton2;
        itemPickerButton2.setOnClickListener(onClickListener);
        this.mDecrementButton.setOnLongClickListener(onLongClickListener);
        this.mDecrementButton.setNumberPicker(this);
        if (resourceId3 != -1) {
            this.mDecrementButton.setBackgroundResource(resourceId3);
        }
        if (resourceId5 != -1) {
            this.mDecrementButton.setImageResource(resourceId5);
        }
        EditText editText = (EditText) findViewById(R.id.input);
        this.mText = editText;
        editText.setOnFocusChangeListener(onFocusChangeListener);
        this.mText.setFilters(new InputFilter[]{numberPickerInputFilter});
        this.mText.setRawInputType(2);
        if (resourceId6 != -1) {
            this.mText.setBackgroundResource(resourceId6);
        }
        if (!isEnabled()) {
            setEnabled(false);
        }
        if (i2 > -1 && i3 > -1) {
            if (stringArray != null) {
                setRange(i2, i3, stringArray);
            } else {
                setRange(i2, i3);
            }
        }
        if (i4 > -1) {
            setValue(i4);
        }
        if (i5 > -1) {
            setSpeed(i5);
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mIncrementButton.setEnabled(z);
        this.mDecrementButton.setEnabled(z);
        this.mText.setEnabled(z);
    }

    @Override // android.view.View
    public void setOnKeyListener(View.OnKeyListener onKeyListener) {
        super.setOnKeyListener(onKeyListener);
        this.mIncrementButton.setOnKeyListener(onKeyListener);
        this.mDecrementButton.setOnKeyListener(onKeyListener);
        this.mText.setOnKeyListener(onKeyListener);
    }

    public void setOnChangeListener(OnChangedListener onChangedListener) {
        this.mListener = onChangedListener;
    }

    public void setFormatter(Formatter formatter) {
        this.mFormatter = formatter;
    }

    public void setRange(int i, int i2) {
        setRange(i, i2, null);
    }

    public void setRange(int i, int i2, String[] strArr) {
        this.mDisplayedValues = strArr;
        this.mMinValue = i;
        this.mMaxValue = i2;
        int i3 = this.mCurrentValue;
        if (i3 < i || i3 > i2) {
            this.mCurrentValue = i;
        }
        updateView();
        if (strArr != null) {
            this.mText.setRawInputType(524289);
        }
    }

    public void setValue(int i) {
        if (i < this.mMinValue || i > this.mMaxValue) {
            Log.w("ItemPicker", String.format("current(%d) should be between min(%d) to max(%d) changed to min", Integer.valueOf(i), Integer.valueOf(this.mMinValue), Integer.valueOf(this.mMaxValue)));
            i = this.mMinValue;
        }
        this.mCurrentValue = i;
        updateView();
    }

    public void setSpeed(long j) {
        this.mSpeed = j;
    }

    private String formatNumber(int i) {
        Formatter formatter = this.mFormatter;
        if (formatter != null) {
            return formatter.toString(i);
        }
        return String.valueOf(i);
    }

    protected void changeCurrent(int i) {
        int i2 = this.mMaxValue;
        if (i > i2) {
            i = this.mMinValue;
        } else if (i < this.mMinValue) {
            i = i2;
        }
        this.mPrevValue = this.mCurrentValue;
        this.mCurrentValue = i;
        notifyChange();
        updateView();
    }

    private void notifyChange() {
        OnChangedListener onChangedListener = this.mListener;
        if (onChangedListener != null) {
            onChangedListener.onChanged(this, this.mPrevValue, this.mCurrentValue);
        }
    }

    private void updateView() {
        String[] strArr = this.mDisplayedValues;
        if (strArr == null) {
            this.mText.setText(formatNumber(this.mCurrentValue));
        } else {
            this.mText.setText(strArr[this.mCurrentValue - this.mMinValue]);
        }
        EditText editText = this.mText;
        editText.setSelection(editText.getText().length());
    }

    private void validateCurrentView(CharSequence charSequence) {
        int i;
        int selectedPos = getSelectedPos(charSequence.toString());
        if (selectedPos >= this.mMinValue && selectedPos <= this.mMaxValue && (i = this.mCurrentValue) != selectedPos) {
            this.mPrevValue = i;
            this.mCurrentValue = selectedPos;
            notifyChange();
        }
        updateView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void validateInput(View view) {
        String valueOf = String.valueOf(((TextView) view).getText());
        if (TextUtils.isEmpty(valueOf)) {
            updateView();
        } else {
            validateCurrentView(valueOf);
        }
    }

    public void cancelIncrement() {
        this.mIncrement = false;
    }

    public void cancelDecrement() {
        this.mDecrement = false;
    }

    private class NumberPickerInputFilter implements InputFilter {
        private NumberPickerInputFilter() {
        }

        @Override // android.text.InputFilter
        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            if (ItemPicker.this.mDisplayedValues == null) {
                return ItemPicker.this.mNumberInputFilter.filter(charSequence, i, i2, spanned, i3, i4);
            }
            String valueOf = String.valueOf(charSequence.subSequence(i, i2));
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(spanned.subSequence(0, i3)));
            sb.append((Object) valueOf);
            sb.append((Object) spanned.subSequence(i4, spanned.length()));
            String lowerCase = String.valueOf(sb.toString()).toLowerCase(Locale.US);
            for (String str : ItemPicker.this.mDisplayedValues) {
                if (str.toLowerCase(Locale.US).startsWith(lowerCase)) {
                    return valueOf;
                }
            }
            return "";
        }
    }

    private class NumberRangeKeyListener extends NumberKeyListener {
        @Override // android.text.method.KeyListener
        public int getInputType() {
            return 2;
        }

        private NumberRangeKeyListener() {
        }

        @Override // android.text.method.NumberKeyListener
        protected char[] getAcceptedChars() {
            return ItemPicker.DIGIT_CHARACTERS;
        }

        @Override // android.text.method.NumberKeyListener, android.text.InputFilter
        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            CharSequence filter = super.filter(charSequence, i, i2, spanned, i3, i4);
            if (filter == null) {
                filter = charSequence.subSequence(i, i2);
            }
            String str = String.valueOf(spanned.subSequence(0, i3)) + ((Object) filter) + ((Object) spanned.subSequence(i4, spanned.length()));
            return "".equals(str) ? str : ItemPicker.this.getSelectedPos(str) > ItemPicker.this.mMaxValue ? "" : filter;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSelectedPos(String str) {
        try {
            if (this.mDisplayedValues == null) {
                return Integer.parseInt(str);
            }
            for (int i = 0; i < this.mDisplayedValues.length; i++) {
                str = str.toLowerCase(Locale.US);
                if (this.mDisplayedValues[i].toLowerCase(Locale.US).startsWith(str)) {
                    return this.mMinValue + i;
                }
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return this.mMinValue;
        }
    }

    public int getValue() {
        return this.mCurrentValue;
    }

    protected int getEndRange() {
        return this.mMaxValue;
    }

    protected int getBeginRange() {
        return this.mMinValue;
    }
}
