package com.github.marlonlom.demos.barcodescans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Dummy Barcode scanner fragment class.
 *
 * @author marlonlom
 */
public final class DummyBarcodeScannerFragment extends BarcodeDetectorFragment {

    private Button mScanButton;
    private ImageView mBarcodeImage;
    private TextView mResultsText;
    private Bitmap mBitmap;

    public DummyBarcodeScannerFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_tab_dummy_scan, container, false);

        mScanButton = (Button) rootView.findViewById(R.id.button_dummy_scan);
        mBarcodeImage = (ImageView) rootView.findViewById(R.id.imageview_dummy_barcode);
        mResultsText = (TextView) rootView.findViewById(R.id.textview_dummy_results);

        setupView();

        return rootView;

    }

    @Override
    protected void setupView() {
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Frame frame = new Frame.Builder().setBitmap(mBitmap).build();
                SparseArray<Barcode> barCodes = getDetector().detect(frame);
                Barcode thisCode = barCodes.valueAt(0);
                mResultsText.setText(thisCode.rawValue);
            }
        });

        mBitmap = BitmapFactory.decodeResource(
                getActivity().getApplicationContext().getResources(),
                R.drawable.barcodummy);
        mBarcodeImage.setImageBitmap(mBitmap);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final int testResourceId =
                        !isDetectorOperational() ? R.string.text_error_no_operational
                                : R.string.textView_waiting;
                mResultsText.setText(testResourceId);
            }
        }, 2000);
    }

}
