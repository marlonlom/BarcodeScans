package com.github.marlonlom.demos.barcodescans;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

/**
 * Barcode detector fragment definition class.
 *
 * @author marlonlom
 */
public abstract class BarcodeDetectorFragment extends Fragment {

    private BarcodeDetector mDetector;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupBarcodeDetector(getContext());
    }

    private void setupBarcodeDetector(Context appContext) {
        mDetector = new BarcodeDetector.Builder(appContext)
                .build();
    }

    protected abstract void setupView();

    protected BarcodeDetector getDetector() {
        return mDetector;
    }

    protected boolean isDetectorOperational() {
        return mDetector != null && mDetector.isOperational();
    }
}
