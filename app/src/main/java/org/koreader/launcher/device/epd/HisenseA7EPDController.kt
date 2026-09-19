package org.koreader.launcher.device.epd

import android.util.Log
import android.view.View
import org.koreader.launcher.device.EPDInterface

private const val HISENSE_PARTIAL = 0
private const val HISENSE_FULL = 1

class HisenseA7EPDController : EPDInterface {
    companion object {
        private const val TAG = "EPD"
        private const val FORCE_CLEAR_PATH =
            "/sys/devices/platform/soc/soc:ap-ahb/20400000.dsi/20400000.dsi.0/display/panel0/epd_force_clear"
    }

    override fun getPlatform() = "hisense-a7-root"
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

        try {
            val command = "echo 1 > $FORCE_CLEAR_PATH"
            val process = ProcessBuilder("su", "-c", command)
                .redirectErrorStream(true)
                .start()
            val output = process.inputStream.bufferedReader().use { it.readText().trim() }
            val exitCode = process.waitFor()

            if (exitCode == 0) {
                Log.i(TAG, "Hisense A7 force clear PASS")
            } else {
                Log.e(TAG, "Hisense A7 force clear failed: exit=$exitCode output=$output")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Hisense A7 force clear failed", e)
        }
    }

    override fun resume() {}
    override fun pause() {}
}
