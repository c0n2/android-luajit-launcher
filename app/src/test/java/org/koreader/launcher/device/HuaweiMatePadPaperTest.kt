package org.koreader.launcher.device

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koreader.launcher.device.epd.HuaweiMatePadPaperEPDController
import org.koreader.launcher.device.epd.huaweiForceRefreshMode

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
    fun requestsForceRefreshOnlyForFullModes() {
        assertEquals(32, huaweiForceRefreshMode(32, null))
        assertEquals(32, huaweiForceRefreshMode(0, "EPD_FULL"))
        assertEquals(null, huaweiForceRefreshMode(0, null))
        assertEquals(null, huaweiForceRefreshMode(2, "EPD_PART"))
        assertEquals(null, huaweiForceRefreshMode(3, "EPD_A2"))
    }

    @Test
    fun exposesKoreaderEinkContract() {
        val epd = HuaweiMatePadPaperEPDController()
        assertEquals("huawei", epd.getPlatform())
        assertEquals("all", epd.getMode())
        assertEquals(32, epd.getWaveformFull())
        assertEquals(0, epd.getWaveformPartial())
        assertFalse(epd.needsView())
    }
}
