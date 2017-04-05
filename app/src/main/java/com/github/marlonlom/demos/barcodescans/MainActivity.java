package com.github.marlonlom.demos.barcodescans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class MainActivity extends AppCompatActivity {

    private Bitmap mBitmap;
    private BarcodeDetector mDetector;
    private TextView mTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
        setupBarcodeDetector();
    }

    private void setupView() {
        final Button btn = (Button) findViewById(R.id.button_load_image);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Frame frame = new Frame.Builder().setBitmap(mBitmap).build();
                SparseArray<Barcode> barCodes = mDetector.detect(frame);
                Barcode thisCode = barCodes.valueAt(0);
                mTxtView.setText(thisCode.rawValue);
            }
        });

        final ImageView myImageView = (ImageView) findViewById(R.id.imageview_barcode_sample);
        mBitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.barcodummy);
        myImageView.setImageBitmap(mBitmap);
    }

    void setupBarcodeDetector() {
        mDetector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
        mTxtView = (TextView) findViewById(R.id.textview_content);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mDetector.isOperational()) {
                    mTxtView.setText(R.string.text_error_nooperational);
                } else {
                    mTxtView.setText(R.string.textview_content);
                }
            }
        }, 2000);
    }

}
