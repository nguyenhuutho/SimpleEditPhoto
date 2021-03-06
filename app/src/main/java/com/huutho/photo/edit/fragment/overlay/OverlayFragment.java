package com.huutho.photo.edit.fragment.overlay;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.huutho.photo.R;
import com.huutho.photo.base.BaseToolFragment;
import com.huutho.photo.models.Overlay;

import org.wysaid.nativePort.CGENativeLibrary;

import java.util.List;

import butterknife.BindView;

/**
 * Created by ThoNh on 11/1/2017.
 */

public class OverlayFragment extends BaseToolFragment implements OverlayView, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = OverlayFragment.class.getSimpleName();

    @InjectPresenter
    OverlayPresenter mPresenter;

    @BindView(R.id.rv_overlay)
    RecyclerView mOverlayView;

    private Overlay mOverlay;

    public static OverlayFragment newInstance() {
        Bundle args = new Bundle();
        OverlayFragment fragment = new OverlayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overlay, container, false);
    }

    @Override
    public void onSave() {
        Bitmap bitmap = Bitmap.createBitmap(getBitmapSurfaceView());
        bitmap = CGENativeLibrary.filterImage_MultipleEffects(bitmap, mOverlay.getConfig(), 1.0f);
        setBitmapSurfaceView(bitmap);
    }

    @Override
    public void onCancel() {
        Bitmap bitmap = Bitmap.createBitmap(getBitmapSurfaceView());
        bitmap = CGENativeLibrary.filterImage_MultipleEffects(bitmap, "", 1.0f);
        setBitmapSurfaceView(bitmap);
    }

    @Override
    public void setupOverlayView(List<Overlay> overlayList) {
        OverlayAdapter mAdapter = new OverlayAdapter();
        mOverlayView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mOverlayView.setAdapter(mAdapter);
        mAdapter.setData(overlayList);
        mAdapter.setListener(new OverlayAdapter.OverlayEventListener() {
            @Override
            public void onClick(View view, int position, Overlay overlay) {
                mPresenter.onOverlayOnClick(overlay);
            }
        });
    }

    @Override
    public void onOverlayOnClick(Overlay overlay) {
        if (mContainerSeekbar.getVisibility() == View.INVISIBLE) {
            mContainerSeekbar.setVisibility(View.VISIBLE);
        }

        mOverlay = overlay;
        final String config = mOverlay.originConfig();
        mImageGLSurfaceView.post(new Runnable() {
            @Override
            public void run() {
                mImageGLSurfaceView.setFilterWithConfig(config);
                mSeekBar.setProgress(50);
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
        mOverlay.setIntensity((float) progress / 100.0f);
        mImageGLSurfaceView.setFilterIntensity((float) progress / 100.0f);
    }
}
