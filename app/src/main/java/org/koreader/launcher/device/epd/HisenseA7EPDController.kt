package org.koreader.launcher.device.epd

import android.util.Log
import android.view.View
import org.koreader.launcher.device.EPDInterface

private const val HISENSE_PARTIAL = 0
private const val HISENSE_FULL = 1

class HisenseA7EPDController : EPDInterface {
    companion object {
        private const val TAG = "EPD"
    }

    override fun getPlatform() = "hisense-a7-vendor-test"
    override fun getMode() = "all"

    override fun getWaveformFull() = HISENSE_FULL
    override fun getWaveformPartial() = HISENSE_PARTIAL
    override fun getWaveformFullUi() = HISENSE_FULL
    override fun getWaveformPartialUi() = HISENSE_PARTIAL
    override fun getWaveformFast() = HISENSE_PARTIAL

    override fun getWaveformDelay() = 0
    override fun getWaveformDelayUi() = 0
    override fun getWaveformDelayFast() = 0

    override fun needsView() = false

    override fun setEpdMode(
        targetView: View,
        mode: Int,
        delay: Long,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        epdMode: String?,
    ) {
        val requestFull = epdMode == "EPD_FULL" || (epdMode == null && mode == HISENSE_FULL)
        if (!requestFull) return

        Log.i(TAG, "Hisense A7 vendor forceClear scheduled: next animation frame")

        targetView.postOnAnimation {
            try {
                val epd = targetView.context.getSystemService("epd")
                val method = Class.forName("com.hmct.epd.EpdManager")
                    .getMethod("forceClear")
                method.invoke(epd)
                Log.i(TAG, "Hisense A7 vendor forceClear PASS on next animation frame")
            } catch (e: Exception) {
                Log.e(TAG, "Hisense A7 vendor forceClear failed on next animation frame", e)
            }
        }
    }

    override fun resume() {}
    override fun pause() {}
}
