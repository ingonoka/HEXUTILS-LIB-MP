/*
 * Copyright (c) 2020. Ingo Noka
 * This file belongs to project HEXUTILS-LIB-MP.
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 *
 */

package com.ingonoka.hexutils

import kotlin.test.Test
import kotlin.test.assertEquals


class ByteArrayExtKtTest {

    @Test
    fun toHex() {

        assertEquals("0x01, 0x02, 0x03, 0x04", byteArrayOf(1, 2, 3, 4).toHex())
    }

    @Test
    fun toHexShort() {

        assertEquals("01 02 03 04", byteArrayOf(1, 2, 3, 4).toHexShort())
    }

    @Test
    fun toHexShortShort() {

        assertEquals("01020304", byteArrayOf(1, 2, 3, 4).toHexShortShort())

        assertEquals("", byteArrayOf().toHexShortShort())
    }

    @Test
    fun toHexChunked() {

        val ba1 = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6)
        assertEquals("00   01 02 03 04 05 06 07 08 09 00 01 02 03 04 05 06", ba1.toHexChunked(printable = false))
        assertEquals(
            "00   01 02 03 04 05 06 07 08 09 00 01 02 03 04 05 06    ................",
            ba1.toHexChunked(printable = true)
        )
        assertEquals(
            "01 02 03 04 05 06 07 08 09 00 01 02 03 04 05 06    ................",
            ba1.toHexChunked(lineNums = false, printable = true)
        )
        assertEquals(
            """
            |01 02 03 04 05 06 07 08    ........
            |09 00 01 02 03 04 05 06    ........""".trimMargin(),
            ba1.toHexChunked(columns = 8, lineNums = false, printable = true)
        )

        assertEquals(
            """
            |00   01 02 03 04 05 06 07 08    ........
            |01   09 00 01 02 03 04 05 06    ........""".trimMargin(),
            ba1.toHexChunked(columns = 8, lineNums = true, printable = true)
        )

        assertEquals(
            """
            |00   01 02 03    ...
            |01   04 05 06    ...
            |02   07 08 09    ...
            |03   00 01 02    ...
            |04   03 04 05    ...
            |05   06          .""".trimMargin(),
            ba1.toHexChunked(columns = 3, lineNums = true, printable = true)
        )


        assertEquals("", byteArrayOf().toHexChunked())

        assertEquals("00   01 02       ..", byteArrayOf(1, 2).toHexChunked(columns = 3))

        println(ba1.toHexChunked("\t\t\t    ", columns = 3))
    }
}