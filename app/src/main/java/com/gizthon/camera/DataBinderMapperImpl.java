package com.gizthon.camera;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.generalplus.GoPlusDrone.Fragment.BaseFragment;
import com.gizthon.camera.databinding.DialogShareBindingImpl;
import com.gizthon.camera.databinding.GalleryActivityBindingImpl;
import com.gizthon.camera.databinding.GalleryPhotoFragmentBindingImpl;
import com.gizthon.camera.databinding.GalleryPhotoItemBindingImpl;
import com.gizthon.camera.databinding.HelpActivityBindingImpl;
import com.gizthon.camera.databinding.PreviewPhotoActivityBindingImpl;
import com.gizthon.camera.databinding.PreviewPhotoItemBindingImpl;
import com.gizthon.camera.databinding.PreviewVideoActivityBindingImpl;
import com.gizthon.camera.databinding.SplashActivityBindingImpl;
import com.gizthon.camera.databinding.UsbPreviewActivityBindingImpl;
import com.jieli.lib.dv.control.utils.TopicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class DataBinderMapperImpl extends DataBinderMapper {
    private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP;
    private static final int LAYOUT_DIALOGSHARE = 1;
    private static final int LAYOUT_GALLERYACTIVITY = 2;
    private static final int LAYOUT_GALLERYPHOTOFRAGMENT = 3;
    private static final int LAYOUT_GALLERYPHOTOITEM = 4;
    private static final int LAYOUT_HELPACTIVITY = 5;
    private static final int LAYOUT_PREVIEWPHOTOACTIVITY = 6;
    private static final int LAYOUT_PREVIEWPHOTOITEM = 7;
    private static final int LAYOUT_PREVIEWVIDEOACTIVITY = 8;
    private static final int LAYOUT_SPLASHACTIVITY = 9;
    private static final int LAYOUT_USBPREVIEWACTIVITY = 10;

    static {
        SparseIntArray sparseIntArray = new SparseIntArray(10);
        INTERNAL_LAYOUT_ID_LOOKUP = sparseIntArray;
        sparseIntArray.put(com.weioa.GoPlusDrone.R.layout.dialog_share, 1);
        INTERNAL_LAYOUT_ID_LOOKUP.put(com.weioa.GoPlusDrone.R.layout.gallery_activity, 2);
        INTERNAL_LAYOUT_ID_LOOKUP.put(com.weioa.GoPlusDrone.R.layout.gallery_photo_fragment, 3);
        INTERNAL_LAYOUT_ID_LOOKUP.put(com.weioa.GoPlusDrone.R.layout.gallery_photo_item, 4);
        INTERNAL_LAYOUT_ID_LOOKUP.put(com.weioa.GoPlusDrone.R.layout.help_activity, 5);
        INTERNAL_LAYOUT_ID_LOOKUP.put(com.weioa.GoPlusDrone.R.layout.preview_photo_activity, 6);
        INTERNAL_LAYOUT_ID_LOOKUP.put(com.weioa.GoPlusDrone.R.layout.preview_photo_item, 7);
        INTERNAL_LAYOUT_ID_LOOKUP.put(com.weioa.GoPlusDrone.R.layout.preview_video_activity, 8);
        INTERNAL_LAYOUT_ID_LOOKUP.put(com.weioa.GoPlusDrone.R.layout.splash_activity, 9);
        INTERNAL_LAYOUT_ID_LOOKUP.put(com.weioa.GoPlusDrone.R.layout.usb_preview_activity, 10);
    }

    @Override // androidx.databinding.DataBinderMapper
    public ViewDataBinding getDataBinder(DataBindingComponent dataBindingComponent, View view, int i) {
        int i2 = INTERNAL_LAYOUT_ID_LOOKUP.get(i);
        if (i2 <= 0) {
            return null;
        }
        Object tag = view.getTag();
        if (tag == null) {
            throw new RuntimeException("view must have a tag");
        }
        switch (i2) {
            case 1:
                if ("layout/dialog_share_0".equals(tag)) {
                    return new DialogShareBindingImpl(dataBindingComponent, view);
                }
                throw new IllegalArgumentException("The tag for dialog_share is invalid. Received: " + tag);
            case 2:
                if ("layout/gallery_activity_0".equals(tag)) {
                    return new GalleryActivityBindingImpl(dataBindingComponent, view);
                }
                throw new IllegalArgumentException("The tag for gallery_activity is invalid. Received: " + tag);
            case 3:
                if ("layout/gallery_photo_fragment_0".equals(tag)) {
                    return new GalleryPhotoFragmentBindingImpl(dataBindingComponent, view);
                }
                throw new IllegalArgumentException("The tag for gallery_photo_fragment is invalid. Received: " + tag);
            case 4:
                if ("layout/gallery_photo_item_0".equals(tag)) {
                    return new GalleryPhotoItemBindingImpl(dataBindingComponent, view);
                }
                throw new IllegalArgumentException("The tag for gallery_photo_item is invalid. Received: " + tag);
            case 5:
                if ("layout/help_activity_0".equals(tag)) {
                    return new HelpActivityBindingImpl(dataBindingComponent, view);
                }
                throw new IllegalArgumentException("The tag for help_activity is invalid. Received: " + tag);
            case 6:
                if ("layout/preview_photo_activity_0".equals(tag)) {
                    return new PreviewPhotoActivityBindingImpl(dataBindingComponent, view);
                }
                throw new IllegalArgumentException("The tag for preview_photo_activity is invalid. Received: " + tag);
            case 7:
                if ("layout/preview_photo_item_0".equals(tag)) {
                    return new PreviewPhotoItemBindingImpl(dataBindingComponent, view);
                }
                throw new IllegalArgumentException("The tag for preview_photo_item is invalid. Received: " + tag);
            case 8:
                if ("layout/preview_video_activity_0".equals(tag)) {
                    return new PreviewVideoActivityBindingImpl(dataBindingComponent, view);
                }
                throw new IllegalArgumentException("The tag for preview_video_activity is invalid. Received: " + tag);
            case 9:
                if ("layout/splash_activity_0".equals(tag)) {
                    return new SplashActivityBindingImpl(dataBindingComponent, view);
                }
                throw new IllegalArgumentException("The tag for splash_activity is invalid. Received: " + tag);
            case 10:
                if ("layout/usb_preview_activity_0".equals(tag)) {
                    return new UsbPreviewActivityBindingImpl(dataBindingComponent, view);
                }
                throw new IllegalArgumentException("The tag for usb_preview_activity is invalid. Received: " + tag);
            default:
                return null;
        }
    }

    @Override // androidx.databinding.DataBinderMapper
    public ViewDataBinding getDataBinder(DataBindingComponent dataBindingComponent, View[] viewArr, int i) {
        if (viewArr == null || viewArr.length == 0 || INTERNAL_LAYOUT_ID_LOOKUP.get(i) <= 0 || viewArr[0].getTag() != null) {
            return null;
        }
        throw new RuntimeException("view must have a tag");
    }

    @Override // androidx.databinding.DataBinderMapper
    public int getLayoutId(String str) {
        Integer num;
        if (str == null || (num = InnerLayoutIdLookup.sKeys.get(str)) == null) {
            return 0;
        }
        return num.intValue();
    }

    @Override // androidx.databinding.DataBinderMapper
    public String convertBrIdToString(int i) {
        return InnerBrLookup.sKeys.get(i);
    }

    @Override // androidx.databinding.DataBinderMapper
    public List<DataBinderMapper> collectDependencies() {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
        return arrayList;
    }

    private static class InnerBrLookup {
        static final SparseArray<String> sKeys;

        private InnerBrLookup() {
        }

        static {
            SparseArray<String> sparseArray = new SparseArray<>(11);
            sKeys = sparseArray;
            sparseArray.put(0, "_all");
            sKeys.put(1, "bitmap");
            sKeys.put(2, "editVisible");
            sKeys.put(3, "eventHandler");
            sKeys.put(4, "index");
            sKeys.put(5, BaseFragment.DATA_NAME);
            sKeys.put(6, TopicKey.PATH);
            sKeys.put(7, "selected");
            sKeys.put(8, "selectedCoverVisible");
            sKeys.put(9, "videoVisible");
            sKeys.put(10, "viewModel");
        }
    }

    private static class InnerLayoutIdLookup {
        static final HashMap<String, Integer> sKeys;

        private InnerLayoutIdLookup() {
        }

        static {
            HashMap<String, Integer> hashMap = new HashMap<>(10);
            sKeys = hashMap;
            hashMap.put("layout/dialog_share_0", Integer.valueOf(com.weioa.GoPlusDrone.R.layout.dialog_share));
            sKeys.put("layout/gallery_activity_0", Integer.valueOf(com.weioa.GoPlusDrone.R.layout.gallery_activity));
            sKeys.put("layout/gallery_photo_fragment_0", Integer.valueOf(com.weioa.GoPlusDrone.R.layout.gallery_photo_fragment));
            sKeys.put("layout/gallery_photo_item_0", Integer.valueOf(com.weioa.GoPlusDrone.R.layout.gallery_photo_item));
            sKeys.put("layout/help_activity_0", Integer.valueOf(com.weioa.GoPlusDrone.R.layout.help_activity));
            sKeys.put("layout/preview_photo_activity_0", Integer.valueOf(com.weioa.GoPlusDrone.R.layout.preview_photo_activity));
            sKeys.put("layout/preview_photo_item_0", Integer.valueOf(com.weioa.GoPlusDrone.R.layout.preview_photo_item));
            sKeys.put("layout/preview_video_activity_0", Integer.valueOf(com.weioa.GoPlusDrone.R.layout.preview_video_activity));
            sKeys.put("layout/splash_activity_0", Integer.valueOf(com.weioa.GoPlusDrone.R.layout.splash_activity));
            sKeys.put("layout/usb_preview_activity_0", Integer.valueOf(com.weioa.GoPlusDrone.R.layout.usb_preview_activity));
        }
    }
}
