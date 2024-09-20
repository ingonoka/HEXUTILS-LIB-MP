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
import kotlin.test.assertTrue

class CharExtTest {

    val hexChars: List<Char> =
        listOf(
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            '0',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f'
        )

    @Test
    fun testisHex() {
        assertTrue(hexChars.all {
            it.isHex()
        })
    }
}