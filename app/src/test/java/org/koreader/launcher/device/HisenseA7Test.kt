package org.koreader.launcher.device

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koreader.launcher.device.epd.HisenseA7EPDController

class HisenseA7Test {
    @Test
    fun detectsOnlyHisenseHnr320t() {
        assertTrue(isHisenseA7("hisense", "hisense", "hnr320t"))
        assertTrue(isHisenseA7("hisense", "other", "hnr320t"))
        assertTrue(isHisenseA7("other", "hisense", "hnr320t"))
        assertFalse(isHisenseA7("other", "other", "hnr320t"))
        assertFalse(isHisenseA7("hisense", "hisense", "hitv205n"))
    }

    @Test
    fun exposesKoreaderEinkContract() {
        val epd = HisenseA7EPDController()

        assertEquals("hisense-a7-root", epd.getPlatform())
        assertEquals("all", epd.getMode())

        assertEquals(1, epd.getWaveformFull())
        assertEquals(0, epd.getWaveformPartial())
        assertEquals(1, epd.getWaveformFullUi())
        assertEquals(0, epd.getWaveformPartialUi())
        assertEquals(0, epd.getWaveformFast())

        assertFalse(epd.needsView())
    }
}
