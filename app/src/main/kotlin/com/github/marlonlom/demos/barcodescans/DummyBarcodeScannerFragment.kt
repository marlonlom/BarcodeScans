package com.github.marlonlom.demos.barcodescans

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.github.marlonlom.demos.barcodescans.R

import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode

/**
 * Dummy Barcode scanner fragment class.

 * @author marlonlom
 */
class DummyBarcodeScannerFragment : BarcodeDetectorFragment() {

    private var mScanButton: Button? = null
    private var mBarcodeImage: ImageView? = null
    private var mResultsText: TextView? = null
    private var mBitmap: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater!!.inflate(R.layout.fragment_tab_dummy_scan, container, false)

        mScanButton = rootView.findViewById(R.id.button_dummy_scan) as Button
        mBarcodeImage = rootView.findViewById(R.id.imageview_dummy_barcode) as ImageView
        mResultsText = rootView.findViewById(R.id.textview_dummy_results) as TextView

        setupView()

        return rootView

    }

    override fun setupView() {
        mScanButton!!.setOnClickListener {
            val frame = Frame.Builder().setBitmap(mBitmap!!).build()
            val barCodes = detector!!.detect(frame)
            val thisCode = barCodes.valueAt(0)
            mResultsText!!.text = thisCode.rawValue
        }

        mBitmap = BitmapFactory.decodeResource(
                activity.applicationContext.resources,
                R.drawable.barcodummy)
        mBarcodeImage!!.setImageBitmap(mBitmap)

        val handler = Handler()
        handler.postDelayed({
            val testResourceId = if (!isDetectorOperational)
                R.string.text_error_no_operational
            else
                R.string.textView_waiting
            mResultsText!!.setText(testResourceId)
        }, 2000)
    }

}
