package org.koreader.launcher.device.epd

import android.util.Log
import android.view.View
import org.koreader.launcher.device.EPDInterface

private const val HUAWEI_PARTIAL = 0
private const val HUAWEI_FULL = 32

internal fun huaweiDefaultModeForRefresh(
    mode: Int,
    epdMode: String?,
): Int? {
    return when {
        epdMode == "EPD_FULL" -> HUAWEI_FULL
        epdMode == "EPD_AUTO" -> HUAWEI_PARTIAL
        epdMode != null -> null
        mode == HUAWEI_FULL -> HUAWEI_FULL
        mode == HUAWEI_PARTIAL -> HUAWEI_PARTIAL
        else -> null
    }
}

class HuaweiMatePadPaperEPDController : EPDInterface {
    companion object {
        private const val TAG = "EPD"
    }

    private val setDefaultModeMethod by lazy {
        Class.forName("android.eink.EPDCDevice")
            .getMethod("setEpdcDefaultMode", Integer.TYPE)
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
        val defaultMode =
            huaweiDefaultModeForRefresh(mode, epdMode) ?: return

        try {
            setDefaultModeMethod.invoke(null, defaultMode)

            Log.v(
                TAG,
                "Huawei MatePad Paper setEpdcDefaultMode($defaultMode)"
            )
        } catch (e: Exception) {
            Log.e(
                TAG,
                "Huawei MatePad Paper setEpdcDefaultMode($defaultMode) failed",
                e
            )
        }
    }

    override fun resume() {}

    override fun pause() {
        try {
            setDefaultModeMethod.invoke(null, HUAWEI_PARTIAL)

            Log.v(
                TAG,
                "Huawei MatePad Paper pause: setEpdcDefaultMode(0)"
            )
        } catch (e: Exception) {
            Log.e(
                TAG,
                "Huawei MatePad Paper pause reset failed",
                e
            )
        }
    }
}
