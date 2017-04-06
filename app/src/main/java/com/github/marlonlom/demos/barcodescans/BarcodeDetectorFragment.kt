package com.github.marlonlom.demos.barcodescans

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector

/**
 * Barcode detector fragment definition class.

 * @author marlonlom
 */
abstract class BarcodeDetectorFragment : Fragment() {

    protected var detector: BarcodeDetector? = null
        private set

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBarcodeDetector(context)
    }

    private fun setupBarcodeDetector(appContext: Context) {
        detector = BarcodeDetector.Builder(appContext)
                .build()
    }

    protected abstract fun setupView()

    protected val isDetectorOperational: Boolean
        get() = detector != null && detector!!.isOperational
}
