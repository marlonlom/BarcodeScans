package com.github.marlonlom.demos.barcodescans;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
 * Camera Barcode scanner fragment class.
 *
 * @author marlonlom
 */
public final class CameraBarcodeScannerFragment extends BarcodeDetectorFragment {
    public static final int RESULT_INTENT_PHOTO_CAPTURE = 100;
    private static final int PERMISSION_REQUEST_PHOTO_CAPTURE = 200;
    private Button mScanButton;
    private ImageView mBarcodeImage;
    private TextView mResultsText;
    private Uri mImageUri;

    public CameraBarcodeScannerFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_tab_camera_scan, container, false);

        mScanButton = (Button) rootView.findViewById(R.id.button_camera_scan);
        mBarcodeImage = (ImageView) rootView.findViewById(R.id.imageview_camera_barcode);
        mResultsText = (TextView) rootView.findViewById(R.id.textview_camera_results);

        setupView();

        return rootView;

    }

    @Override
    protected void setupView() {
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
            }
        });

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_PHOTO_CAPTURE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    mScanButton.setEnabled(false);
                }
                break;
        }
    }

    private void takePicture() {
        mResultsText.setText(R.string.text_initializing);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, RESULT_INTENT_PHOTO_CAPTURE);
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_PHOTO_CAPTURE);
        } else {
            takePicture();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_INTENT_PHOTO_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    processTakenPicture(data);
                }
                break;
        }
    }

    private void processTakenPicture(Intent data) {
        final Bitmap photoBitmap = (Bitmap) data.getExtras().get("data");
        if (photoBitmap != null) {
            mResultsText.setText(R.string.text_camera_scanning);
            mBarcodeImage.setImageBitmap(photoBitmap);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    performBarcodeScanning(photoBitmap);
                }
            }, 2000);

        } else {
            Snackbar.make(mBarcodeImage, "Could not get the picture to scan",
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private void performBarcodeScanning(final Bitmap bitmap) {
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Barcode> barCodes = getDetector().detect(frame);
        if (barCodes.size() > 0) {
            Barcode thisCode = barCodes.valueAt(0);
            mResultsText.setText(thisCode.rawValue);
        } else {
            mResultsText.setText(R.string.text_error_scanning);
        }
    }
}
