/*
 * Copyright (c) 2021. Ingo Noka
 * This file belongs to project hexutils-mp.
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 *
 */

package com.ingonoka.hexutils

import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class StringExtKtTestJvm {

    private fun assertThrown(expected: KClass<out Any>, test: () -> Any?): Boolean {

        return try {
            test()
            false
        } catch (e: Exception) {
            when (e::class) {
                expected -> true
                else -> {
                    println("Expected: $expected\nActual: $e")
                    false
                }
            }
        }

    }

    private fun assertArrayEquals(expected: ByteArray, actual: ByteArray) = expected.contentEquals(actual)

    @Test
    fun hexToBytes() {

        assertTrue { assertArrayEquals(byteArrayOf(0, 1, 127), "00017F".hexToBytes()) }
        assertTrue { assertArrayEquals(byteArrayOf(0, 1, 127), "00017f".hexToBytes()) }
        assertTrue { assertArrayEquals(byteArrayOf(-128, -1), "80FF".hexToBytes()) }
        assertTrue { assertArrayEquals(byteArrayOf(-128, -1), "80ff".hexToBytes()) }
        assertTrue { assertArrayEquals(byteArrayOf(-128, -1), "80Ff".hexToBytes()) }
        assertTrue { assertArrayEquals(byteArrayOf(-128, -1), "80fF".hexToBytes()) }

        assertTrue(assertThrown(IllegalArgumentException::class) { "1".hexToBytes() })

        assertTrue { assertArrayEquals(byteArrayOf(), "".hexToBytes()) }

        assertArrayEquals(
            byteArrayOf(0x5F, 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
            "0x5F, 0xFF, 0xFF, 0xFF".hexToBytes(true)
        )

    }

    @Test
    fun hexToByte() {
        assertEquals(1.toByte(), "01".hexToByte())
        assertEquals(1.toByte(), "1".hexToByte())

        val hexChars = listOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'a',
            'b',
            'c',
            'e',
            'f'
        )

        for (c1 in hexChars) {
            assertEquals("$c1".toInt(16).toByte(), "$c1".hexToByte())
            for (c2 in hexChars) {
                assertEquals("$c1$c2".toInt(16).toByte(), "$c1$c2".hexToByte())
            }
        }
    }
}

