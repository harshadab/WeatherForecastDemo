package com.assignment.core.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalTime

class UtilsTest {

    @Test
    fun `toHour formats epoch seconds correctly`() {
        val epochSeconds = 1696896000
        val formatted = epochSeconds.toHour()
        assert(formatted.isNotEmpty())
        
        val nullInt: Int? = null
        assertEquals("", nullInt.toHour())
    }

    @Test
    fun `toDayOfWeek formats epoch seconds correctly`() {
        val epochSeconds = 1696896000
        val formatted = epochSeconds.toDayOfWeek()
        assert(formatted.isNotEmpty())

        val nullInt: Int? = null
        assertEquals("", nullInt.toDayOfWeek())
    }

    @Test
    fun `getFullUrl appends https correctly`() {
        assertEquals("https://example.com/icon.png", "//example.com/icon.png".getFullUrl())
        assertEquals("https://example.com/icon.png", "https://example.com/icon.png".getFullUrl())
        
        val nullStr: String? = null
        assertEquals("", nullStr.getFullUrl())
    }

    @Test
    fun `roundToString rounds decimal correctly`() {
        assertEquals("15", 15.2.roundToString())
        assertEquals("16", 15.6.roundToString())
        
        val nullDouble: Double? = null
        assertEquals("", nullDouble.roundToString())
    }

    @Test
    fun `orDefault returns default for null or empty`() {
        val nullStr: String? = null
        assertEquals("", nullStr.orDefault())
        assertEquals("default", nullStr.orDefault("default"))
        
        assertEquals("", "".orDefault())
        assertEquals("default", "".orDefault("default"))
        
        assertEquals("default", "   ".orDefault("default"))
        
        assertEquals("valid", "valid".orDefault("default"))
    }

    @Test
    fun `formatHourLabel formats AM PM correctly`() {
        assertEquals("12AM", formatHourLabel("00:00"))
        assertEquals("5AM", formatHourLabel("05:00"))
        assertEquals("12PM", formatHourLabel("12:00"))
        assertEquals("3PM", formatHourLabel("15:00"))
    }

    @Test
    fun `isDayTime returns true for day result based on system time`() {
        val now = LocalTime.now()
        val expected = now.isAfter(LocalTime.of(6, 0)) &&
                now.isBefore(LocalTime.of(18, 0))
        val result = isDayTime()
        assertEquals(expected, result)
    }
}
