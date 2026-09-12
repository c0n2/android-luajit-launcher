package org.koreader.launcher.device.epd

import android.util.Log
import android.view.View
import org.koreader.launcher.device.EPDInterface

private const val HUAWEI_PARTIAL = 0
private const val HUAWEI_FULL = 32

internal fun huaweiForceRefreshMode(mode: Int, epdMode: String?): Int? {
    return if (mode == HUAWEI_FULL || epdMode == "EPD_FULL") {
        HUAWEI_FULL
    } else {
        null
    }
}

class HuaweiMatePadPaperEPDController : EPDInterface {
    companion object {
        private const val TAG = "EPD"
    }

    private val forceRefreshMethod by lazy {
        Class.forName("android.eink.EPDCDevice")
            .getMethod("forceRefresh", Integer.TYPE)
    }

    override fun getPlatform() = "huawei"
    override fun getMode() = "all"

    override fun getWaveformFull() = HUAWEI_FULL
    override fun getWaveformPartial() = HUAWEI_PARTIAL
    override fun getWaveformFullUi() = HUAWEI_FULL
    override fun getWaveformPartialUi() = HUAWEI_PARTIAL
    override fun getWaveformFast() = HUAWEI_PARTIAL

    override fun getWaveformDelay() = 0
    override fun getWaveformDelayUi() = 0
    override fun getWaveformDelayFast() = 0

    // forceRefresh is device-level and does not require a dedicated SurfaceView.
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
        val refreshMode = huaweiForceRefreshMode(mode, epdMode) ?: return

        try {
            forceRefreshMethod.invoke(null, refreshMode)

            Log.v(TAG, "Huawei MatePad Paper forceRefresh($refreshMode)")
        } catch (e: Exception) {
            Log.e(
                TAG,
                "Huawei MatePad Paper forceRefresh($refreshMode) failed",
                e
            )
        }
    }

    override fun resume() {}
    override fun pause() {}
}
