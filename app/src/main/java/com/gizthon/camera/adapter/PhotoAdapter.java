package com.gizthon.camera.adapter;

import android.text.TextUtils;
import android.view.View;
import com.gizthon.camera.activity.PreviewPhotoActivity;
import com.gizthon.camera.activity.PreviewVideoActivity;
import com.gizthon.camera.model.PhotoBean;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PhotoAdapter extends BindAdapter<PhotoBean> {
    private boolean isSelectedStatus;
    private OnChangeListener onChangeListener;

    public interface OnChangeListener {
        void onCheck(boolean z);
    }

    public PhotoAdapter(List<PhotoBean> list, int i) {
        super(list, i);
        this.isSelectedStatus = false;
    }

    public void onClickPicture(View view, PhotoBean photoBean) {
        if (this.isSelectedStatus) {
            photoBean.setSelected(!photoBean.isSelected());
            notifySelectedStatus();
            return;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        for (int i2 = 0; i2 < this.dataModelList.size(); i2++) {
            PhotoBean photoBean2 = (PhotoBean) this.dataModelList.get(i2);
            arrayList.add(photoBean2.getPath());
            if (TextUtils.equals(photoBean.getPath(), photoBean2.getPath())) {
                i = i2;
            }
        }
        if (photoBean.getVideoVisible() == 0) {
            PreviewVideoActivity.start(view.getContext(), photoBean.getPath());
        } else {
            PreviewPhotoActivity.start(view.getContext(), arrayList, i);
        }
    }

    public boolean onLongClickPicture(View view, PhotoBean photoBean) {
        photoBean.setSelected(!photoBean.isSelected());
        notifySelectedStatus();
        return true;
    }

    public void notifySelectedStatus() {
        boolean z = false;
        for (int i = 0; i < this.dataModelList.size(); i++) {
            if (((PhotoBean) this.dataModelList.get(i)).isSelected()) {
                z = true;
            }
        }
        for (int i2 = 0; i2 < this.dataModelList.size(); i2++) {
            ((PhotoBean) this.dataModelList.get(i2)).setSelectedCoverVisible(z ? 0 : 8);
        }
        this.isSelectedStatus = z;
        this.onChangeListener.onCheck(z);
    }

    public void changeSelectedStatus() {
        for (int i = 0; i < this.dataModelList.size(); i++) {
            PhotoBean photoBean = (PhotoBean) this.dataModelList.get(i);
            if (photoBean.isSelected()) {
                photoBean.setSelected(false);
            }
            photoBean.setSelectedCoverVisible(8);
        }
        notifyDataSetChanged();
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }
}
