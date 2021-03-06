package com.huutho.photo.edit.fragment.adjust;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.huutho.photo.App;
import com.huutho.photo.di.module.AdjustModule;
import com.huutho.photo.models.Adjust;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by ThoNh on 11/1/2017.
 */

@InjectViewState
public class AdjustPresenter extends MvpPresenter<AdjustView> {

    @Inject
    List<Adjust> mAdjusts;

    @Inject
    String mAdjustConfig;

    public AdjustPresenter(){
        App.editorComponent.inject(this);
        getViewState().setupAdjustView(mAdjusts,mAdjustConfig);
    }

}
