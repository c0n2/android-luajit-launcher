package org.koreader.launcher.device

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koreader.launcher.device.epd.HuaweiMatePadPaperEPDController
import org.koreader.launcher.device.epd.huaweiForceRefreshModeForRefresh

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
    fun fullRefreshUsesOneShotForceRefresh() {
        assertEquals(32, huaweiForceRefreshModeForRefresh(32, null))
        assertEquals(32, huaweiForceRefreshModeForRefresh(0, "EPD_FULL"))
    }

    @Test
    fun nonFullRefreshDoesNotForceRefresh() {
        assertEquals(null, huaweiForceRefreshModeForRefresh(0, null))
        assertEquals(null, huaweiForceRefreshModeForRefresh(0, "EPD_AUTO"))
        assertEquals(null, huaweiForceRefreshModeForRefresh(2, null))
        assertEquals(null, huaweiForceRefreshModeForRefresh(3, null))
        assertEquals(null, huaweiForceRefreshModeForRefresh(0, "EPD_PART"))
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
