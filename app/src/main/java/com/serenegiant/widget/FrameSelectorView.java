package com.serenegiant.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import com.serenegiant.common.R;
import java.util.Locale;

/* loaded from: classes2.dex */
public class FrameSelectorView extends LinearLayout {
    private static final int[] COLOR_BTN_IDS;
    private static final String TAG = FrameSelectorView.class.getSimpleName();
    private static final SparseIntArray sBUTTONS;
    private FrameSelectorViewCallback mCallback;
    private final int[] mColors;
    private final ImageButton[] mFrameButtons;
    private TextView mLineWidthTv;
    private final RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener;
    private final View.OnClickListener mOnColorClickListener;
    private final View.OnClickListener mOnFrameClickListener;
    private final SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;
    private RadioGroup mScaleTypeRadioGroup;
    private SeekBar mSeekBar;

    public interface FrameSelectorViewCallback {
        void onColorSelected(FrameSelectorView frameSelectorView, int i, int i2);

        void onFrameSelected(FrameSelectorView frameSelectorView, int i);

        void onLineWidthChanged(FrameSelectorView frameSelectorView, float f);

        void onLineWidthSelected(FrameSelectorView frameSelectorView, float f);

        void onScaleSelected(FrameSelectorView frameSelectorView, int i);
    }

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        sBUTTONS = sparseIntArray;
        sparseIntArray.put(R.id.color1_button, 0);
        sBUTTONS.put(R.id.color2_button, 1);
        sBUTTONS.put(R.id.color3_button, 2);
        sBUTTONS.put(R.id.color4_button, 3);
        sBUTTONS.put(R.id.color5_button, 4);
        sBUTTONS.put(R.id.color6_button, 5);
        sBUTTONS.put(R.id.color7_button, 6);
        sBUTTONS.put(R.id.color8_button, 7);
        sBUTTONS.put(R.id.color_select_button, -1);
        sBUTTONS.put(R.id.frame_frame_button, 1);
        sBUTTONS.put(R.id.frame_cross_button, 2);
        sBUTTONS.put(R.id.frame_cross_quarter_button, 3);
        sBUTTONS.put(R.id.frame_circle_button, 4);
        sBUTTONS.put(R.id.frame_circle2_button, 6);
        sBUTTONS.put(R.id.frame_cross_circle_button, 5);
        sBUTTONS.put(R.id.frame_cross_circle2_button, 7);
        COLOR_BTN_IDS = new int[]{R.id.color1_button, R.id.color2_button, R.id.color3_button, R.id.color4_button, R.id.color5_button, R.id.color6_button, R.id.color7_button, R.id.color8_button};
    }

    public FrameSelectorView(Context context) {
        this(context, null, 0);
    }

    public FrameSelectorView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FrameSelectorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mColors = new int[]{SupportMenu.CATEGORY_MASK, -23296, InputDeviceCompat.SOURCE_ANY, -16744448, -16776961, -1, -5131855, ViewCompat.MEASURED_STATE_MASK};
        this.mFrameButtons = new ImageButton[7];
        this.mOnFrameClickListener = new View.OnClickListener() { // from class: com.serenegiant.widget.FrameSelectorView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (FrameSelectorView.this.mCallback != null) {
                    try {
                        FrameSelectorView.this.mCallback.onFrameSelected(FrameSelectorView.this, FrameSelectorView.sBUTTONS.get(view.getId()));
                    } catch (Exception e) {
                        Log.w(FrameSelectorView.TAG, e);
                    }
                }
            }
        };
        this.mOnColorClickListener = new View.OnClickListener() { // from class: com.serenegiant.widget.FrameSelectorView.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (FrameSelectorView.this.mCallback != null) {
                    try {
                        int i2 = FrameSelectorView.sBUTTONS.get(view.getId());
                        if (i2 < 0 || i2 >= 8) {
                            FrameSelectorView.this.mCallback.onColorSelected(FrameSelectorView.this, -1, 0);
                        } else {
                            FrameSelectorView.this.mCallback.onColorSelected(FrameSelectorView.this, i2, FrameSelectorView.this.mColors[i2]);
                        }
                    } catch (Exception e) {
                        Log.w(FrameSelectorView.TAG, e);
                    }
                }
            }
        };
        this.mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() { // from class: com.serenegiant.widget.FrameSelectorView.4
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i2) {
                int i3;
                if (i2 == R.id.scale_type_inch_radiobutton) {
                    i3 = 1;
                } else {
                    i3 = i2 == R.id.scale_type_mm_radiobutton ? 2 : 0;
                }
                if (FrameSelectorView.this.mCallback != null) {
                    try {
                        FrameSelectorView.this.mCallback.onScaleSelected(FrameSelectorView.this, i3);
                    } catch (Exception e) {
                        Log.w(FrameSelectorView.TAG, e);
                    }
                }
            }
        };
        this.mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.serenegiant.widget.FrameSelectorView.5
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (FrameSelectorView.this.mLineWidthTv != null) {
                    FrameSelectorView.this.mLineWidthTv.setText(String.format(Locale.US, "%4.1fpx", Float.valueOf(i2 / 10.0f)));
                }
                if (!z || FrameSelectorView.this.mCallback == null) {
                    return;
                }
                try {
                    FrameSelectorView.this.mCallback.onLineWidthChanged(FrameSelectorView.this, seekBar.getProgress() / 10.0f);
                } catch (Exception e) {
                    Log.w(FrameSelectorView.TAG, e);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (FrameSelectorView.this.mCallback != null) {
                    try {
                        FrameSelectorView.this.mCallback.onLineWidthSelected(FrameSelectorView.this, seekBar.getProgress() / 10.0f);
                    } catch (Exception e) {
                        Log.w(FrameSelectorView.TAG, e);
                    }
                }
            }
        };
        setOrientation(1);
        try {
            initView(LayoutInflater.from(context).inflate(R.layout.view_frame_selector, (ViewGroup) this, true));
        } catch (Exception unused) {
        }
    }

    public void setCallback(FrameSelectorViewCallback frameSelectorViewCallback) {
        this.mCallback = frameSelectorViewCallback;
    }

    public FrameSelectorViewCallback getCallback() {
        return this.mCallback;
    }

    public void setFrameType(int i) {
        if (i <= 0 || i >= 8) {
            return;
        }
        for (int i2 = 0; i2 < 7; i2++) {
            this.mFrameButtons[i2].setSelected(false);
        }
        this.mFrameButtons[i - 1].setSelected(true);
    }

    public void setScaleType(int i) {
        RadioGroup radioGroup = this.mScaleTypeRadioGroup;
        if (radioGroup != null) {
            if (i == 1) {
                radioGroup.check(R.id.scale_type_inch_radiobutton);
            } else if (i == 2) {
                radioGroup.check(R.id.scale_type_mm_radiobutton);
            } else {
                radioGroup.check(R.id.scale_type_non_radiobutton);
            }
        }
    }

    public void setLineWidth(float f) {
        SeekBar seekBar = this.mSeekBar;
        if (seekBar != null) {
            seekBar.setProgress((int) (f * 10.0f));
        }
    }

    public void setColors(int[] iArr) {
        if (iArr == null || iArr.length < 8) {
            return;
        }
        System.arraycopy(this.mColors, 0, iArr, 0, 8);
        updateColors(iArr);
    }

    public int[] getColors() {
        return this.mColors;
    }

    public void addColor(int i) {
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i3 >= 8) {
                break;
            }
            if (this.mColors[i3] == i) {
                i2 = i3;
                break;
            }
            i3++;
        }
        int[] iArr = this.mColors;
        System.arraycopy(iArr, i2, iArr, i2 + 1, 7);
        int[] iArr2 = this.mColors;
        iArr2[7] = i;
        updateColors(iArr2);
    }

    private void updateColors(final int[] iArr) {
        post(new Runnable() { // from class: com.serenegiant.widget.FrameSelectorView.1
            @Override // java.lang.Runnable
            public void run() {
                int[] iArr2 = iArr;
                if (iArr2 == null || iArr2.length < 8) {
                    return;
                }
                for (int i = 0; i < 8; i++) {
                    ImageButton imageButton = (ImageButton) FrameSelectorView.this.findViewById(FrameSelectorView.COLOR_BTN_IDS[i]);
                    if (imageButton != null) {
                        imageButton.setBackgroundColor(iArr[i]);
                    }
                }
            }
        });
    }

    private void initView(View view) {
        this.mFrameButtons[0] = (ImageButton) view.findViewById(R.id.frame_frame_button);
        this.mFrameButtons[0].setOnClickListener(this.mOnFrameClickListener);
        this.mFrameButtons[1] = (ImageButton) view.findViewById(R.id.frame_cross_button);
        this.mFrameButtons[1].setOnClickListener(this.mOnFrameClickListener);
        this.mFrameButtons[2] = (ImageButton) view.findViewById(R.id.frame_cross_quarter_button);
        this.mFrameButtons[2].setOnClickListener(this.mOnFrameClickListener);
        this.mFrameButtons[3] = (ImageButton) view.findViewById(R.id.frame_circle_button);
        this.mFrameButtons[3].setOnClickListener(this.mOnFrameClickListener);
        this.mFrameButtons[4] = (ImageButton) view.findViewById(R.id.frame_circle2_button);
        this.mFrameButtons[4].setOnClickListener(this.mOnFrameClickListener);
        this.mFrameButtons[5] = (ImageButton) view.findViewById(R.id.frame_cross_circle_button);
        this.mFrameButtons[5].setOnClickListener(this.mOnFrameClickListener);
        this.mFrameButtons[6] = (ImageButton) view.findViewById(R.id.frame_cross_circle2_button);
        this.mFrameButtons[6].setOnClickListener(this.mOnFrameClickListener);
        ((ImageButton) view.findViewById(R.id.color1_button)).setOnClickListener(this.mOnColorClickListener);
        ((ImageButton) view.findViewById(R.id.color2_button)).setOnClickListener(this.mOnColorClickListener);
        ((ImageButton) view.findViewById(R.id.color3_button)).setOnClickListener(this.mOnColorClickListener);
        ((ImageButton) view.findViewById(R.id.color4_button)).setOnClickListener(this.mOnColorClickListener);
        ((ImageButton) view.findViewById(R.id.color5_button)).setOnClickListener(this.mOnColorClickListener);
        ((ImageButton) view.findViewById(R.id.color6_button)).setOnClickListener(this.mOnColorClickListener);
        ((ImageButton) view.findViewById(R.id.color7_button)).setOnClickListener(this.mOnColorClickListener);
        ((ImageButton) view.findViewById(R.id.color8_button)).setOnClickListener(this.mOnColorClickListener);
        ((ImageButton) view.findViewById(R.id.color_select_button)).setOnClickListener(this.mOnColorClickListener);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.scale_type_radiogroup);
        this.mScaleTypeRadioGroup = radioGroup;
        radioGroup.setOnCheckedChangeListener(this.mOnCheckedChangeListener);
        this.mLineWidthTv = (TextView) view.findViewById(R.id.line_width_textview);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.line_width_seekbar);
        this.mSeekBar = seekBar;
        seekBar.setOnSeekBarChangeListener(this.mOnSeekBarChangeListener);
    }
}
