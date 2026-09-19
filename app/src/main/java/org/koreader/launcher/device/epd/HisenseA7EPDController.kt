package org.koreader.launcher.device.epd

import android.util.Log
import android.view.View
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import org.koreader.launcher.device.EPDInterface

private const val HISENSE_PARTIAL = 0
private const val HISENSE_FULL = 1

class HisenseA7EPDController : EPDInterface {
    companion object {
        private const val TAG = "EPD"
        private const val FORCE_CLEAR_PATH =
            "/sys/devices/platform/soc/soc:ap-ahb/20400000.dsi/20400000.dsi.0/display/panel0/epd_force_clear"
    }

    private var rootProcess: Process? = null
    private var rootStdin: BufferedWriter? = null

    @Synchronized
    private fun closeRootShell() {
        try {
            rootStdin?.close()
        } catch (_: Exception) {
        }
        rootStdin = null
        try {
            rootProcess?.destroy()
        } catch (_: Exception) {
        }
        rootProcess = null
    }

    @Synchronized
    private fun ensureRootShell(): Boolean {
        val process = rootProcess
        if (process != null) {
            try {
                process.exitValue()
                closeRootShell()
            } catch (_: IllegalThreadStateException) {
                return rootStdin != null
            }
        }

        return try {
            val newProcess = ProcessBuilder("su")
                .redirectErrorStream(true)
                .start()
            rootProcess = newProcess
            rootStdin = BufferedWriter(OutputStreamWriter(newProcess.outputStream))
            Log.i(TAG, "Hisense A7 persistent root shell started")
            true
        } catch (e: Exception) {
            closeRootShell()
            Log.e(TAG, "Hisense A7 persistent root shell failed", e)
            false
        }
    }

    @Synchronized
    private fun forceClearWithRoot(): Boolean {
        if (!ensureRootShell()) return false

        return try {
            rootStdin?.apply {
                write("echo 1 > $FORCE_CLEAR_PATH\n")
                flush()
            } ?: return false
            Log.i(TAG, "Hisense A7 root forceClear queued")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Hisense A7 root forceClear failed", e)
            closeRootShell()
            false
        }
    }

    override fun getPlatform() = "hisense-a7-native-present-probe"
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

        if (!forceClearWithRoot()) {
            Log.e(TAG, "Hisense A7 root forceClear unavailable")
        }
    }

    override fun resume() {}

    override fun pause() {
        closeRootShell()
    }
}
