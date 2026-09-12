package org.koreader.launcher.device

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koreader.launcher.device.epd.HuaweiMatePadPaperEPDController
import org.koreader.launcher.device.epd.huaweiDefaultModeForRefresh

class HuaweiMatePadPaperTest {
    @Test
    fun detectsOnlyHuaweiHmwW09() {
        assertTrue(isHuaweiMatePadPaper("huawei", "huawei", "hmw-w09"))
        assertTrue(isHuaweiMatePadPaper("huawei", "other", "hmw-w09"))
        assertTrue(isHuaweiMatePadPaper("other", "huawei", "hmw-w09"))
        assertFalse(isHuaweiMatePadPaper("other", "other", "hmw-w09"))
        assertFalse(isHuaweiMatePadPaper("huawei", "huawei", "hmw-w19"))
    }

    @Test
    fun mapsEveryHuaweiFrameToItsDefaultRefreshMode() {
        assertEquals(32, huaweiDefaultModeForRefresh(32, null))
        assertEquals(0, huaweiDefaultModeForRefresh(0, null))

        assertEquals(32, huaweiDefaultModeForRefresh(0, "EPD_FULL"))
        assertEquals(0, huaweiDefaultModeForRefresh(0, "EPD_AUTO"))

        assertEquals(null, huaweiDefaultModeForRefresh(2, null))
        assertEquals(null, huaweiDefaultModeForRefresh(3, null))
        assertEquals(null, huaweiDefaultModeForRefresh(0, "EPD_PART"))
    }

    @Test
    fun exposesKoreaderEinkContract() {
        val epd = HuaweiMatePadPaperEPDController()

        assertEquals("huawei", epd.getPlatform())
        assertEquals("all", epd.getMode())

        assertEquals(32, epd.getWaveformFull())
        assertEquals(0, epd.getWaveformPartial())

        assertEquals(32, epd.getWaveformFullUi())
        assertEquals(0, epd.getWaveformPartialUi())
        assertEquals(0, epd.getWaveformFast())

        assertFalse(epd.needsView())
    }
}
