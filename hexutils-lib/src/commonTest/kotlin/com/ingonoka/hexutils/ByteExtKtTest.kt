/*
 * Copyright (c) 2024. Ingo Noka
 * This file belongs to project hexutils-mp.
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 *
 */

package com.ingonoka.hexutils

import kotlin.test.Test
import kotlin.test.assertEquals

class ByteExtKtTestJvm {

    @Test
    fun toHex() {

        assertEquals("01", 1.toByte().toHex())
        assertEquals("FF", 0xFF.toByte().toHex())
        assertEquals("FF", (-1).toByte().toHex())
        assertEquals("80", (-128).toByte().toHex())
        assertEquals("00", 0.toByte().toHex(prefix = ""))
        assertEquals("0x00", 0.toByte().toHex(prefix = "0x"))

    }
}