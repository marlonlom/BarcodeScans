package com.github.marlonlom.demos.barcodescans

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.vision.Frame

/**
 * Camera Barcode scanner fragment class.

 * @author marlonlom
 */
class CameraBarcodeScannerFragment : BarcodeDetectorFragment() {
    private var mScanButton: Button? = null
    private var mBarcodeImage: ImageView? = null
    private var mResultsText: TextView? = null
    private val mImageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater!!.inflate(R.layout.fragment_tab_camera_scan, container, false)

        mScanButton = rootView.findViewById(R.id.button_camera_scan) as Button
        mBarcodeImage = rootView.findViewById(R.id.imageview_camera_barcode) as ImageView
        mResultsText = rootView.findViewById(R.id.textview_camera_results) as TextView

        setupView()

        return rootView

    }

    override fun setupView() {
        mScanButton!!.setOnClickListener { checkCameraPermission() }

        val handler = Handler()
        handler.postDelayed({
            val testResourceId = if (!isDetectorOperational)
                R.string.text_error_no_operational
            else
                R.string.textView_waiting
            mResultsText!!.setText(testResourceId)
        }, 2000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_PHOTO_CAPTURE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            } else {
                mScanButton!!.isEnabled = false
            }
        }
    }

    private fun takePicture() {
        mResultsText!!.setText(R.string.text_camera_scanning)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            startActivityForResult(takePictureIntent, RESULT_INTENT_PHOTO_CAPTURE)
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_PHOTO_CAPTURE)
        } else {
            takePicture()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_INTENT_PHOTO_CAPTURE -> if (resultCode == Activity.RESULT_OK) {
                processTakenPicture(data)
            }
        }
    }

    private fun processTakenPicture(data: Intent) {
        val photoBitmap = data.extras.get("data") as Bitmap
        mResultsText!!.setText(R.string.text_camera_scanning)
        mBarcodeImage!!.setImageBitmap(photoBitmap)
        Handler().postDelayed({ performBarcodeScanning(photoBitmap) }, 2000)
    }

    private fun performBarcodeScanning(bitmap: Bitmap) {
        val frame = Frame.Builder().setBitmap(bitmap).build()
        val barCodes = detector!!.detect(frame)
        if (barCodes.size() > 0) {
            val thisCode = barCodes.valueAt(0)
            mResultsText!!.text = thisCode.rawValue
        } else {
            mResultsText!!.setText(R.string.text_error_scanning)
        }
    }

    companion object {
        val RESULT_INTENT_PHOTO_CAPTURE = 100
        private val PERMISSION_REQUEST_PHOTO_CAPTURE = 200
    }
}
