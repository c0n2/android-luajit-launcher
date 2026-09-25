package org.koreader.launcher.device.epd

import android.util.Log
import android.view.View
import org.koreader.launcher.device.EPDInterface

private const val HUAWEI_PARTIAL = 0
private const val HUAWEI_FULL = 32

internal fun huaweiForceRefreshModeForRefresh(
    mode: Int,
    epdMode: String?,
): Int? {
    return when {
        epdMode == "EPD_FULL" -> HUAWEI_FULL
        epdMode != null -> null
        mode == HUAWEI_FULL -> HUAWEI_FULL
        else -> null
    }
}

class HuaweiMatePadPaperEPDController : EPDInterface {
    companion object {
        private const val TAG = "EPD"
    }

    private val epdcDeviceClass by lazy {
        Class.forName("android.eink.EPDCDevice")
    }

    private val setDefaultModeMethod by lazy {
        epdcDeviceClass.getMethod(
            "setEpdcDefaultMode",
            Integer.TYPE
        )
    }

    private val forceRefreshMethod by lazy {
        epdcDeviceClass.getMethod(
            "forceRefresh",
            Integer.TYPE
        )
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
        val forceRefreshMode =
            huaweiForceRefreshModeForRefresh(mode, epdMode)

        if (forceRefreshMode != null) {
            try {
                forceRefreshMethod.invoke(null, forceRefreshMode)

                Log.v(
                    TAG,
                    "Huawei MatePad Paper forceRefresh($forceRefreshMode)"
                )
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "Huawei MatePad Paper forceRefresh($forceRefreshMode) failed",
                    e
                )
            }
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
