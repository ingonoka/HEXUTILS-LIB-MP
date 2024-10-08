/*
 * Copyright (c) 2024. Ingo Noka
 * This file belongs to project hexutils-mp.
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 *
 */


@file:Suppress("unused")

package com.ingonoka.hexutils

/**
 * Convert list of integers into string of hex characters.  Each integer is separated by ", " and prefixed with "0x"
 *
 * ```
 * brackets = true: [ 0x01, 0x02 ]
 * brackets = false: 0x01, 0x02
 * ```
 * @param [brackets] Surround hex string with square brackets
 *
 */
fun List<Int>.toHex(brackets: Boolean = false): String =
    this.joinToString(
        prefix = when (brackets) {
            true -> "["; else -> ""
        }, postfix = when (brackets) {
            true -> "]"; else -> ""
        }
    ) { b -> b.toHex("0x") }

/**
 * Convert [ByteArray] into string of hex characters.  Each Byte is separated by " ": `01 02 03`
 */
fun List<Int>.toHexShort(): String = this.joinToString(separator = " ") { b ->
    b.toHex()
}

/**
 * Convert list of integers into string of hex characters.  Bytes are not separated: `010203`
 */
fun List<Int>.toHexShortShort(): String = this.joinToString(separator = "") { b ->
    b.toHex()
}

/**
 * Convert list of integers into lines of [columns] bytes
 *
 * Assuming `val ba = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6)`
 *
 * ```
 * printable = false : "00   01 02 03 04 05 06 07 08 09 00 01 02 03 04 05 06"
 * printable = true  : "00   01 02 03 04 05 06 07 08 09 00 01 02 03 04 05 06    ................"
 * lineNums = false  : "01 02 03 04 05 06 07 08 09 00 01 02 03 04 05 06    ................",
 * columns = 8       : "00   01 02 03 04 05 06 07 08    ........
 *                      01   09 00 01 02 03 04 05 06    ........"
 * ```
 * @param columns Number of bytes per line
 * @param lineNums Line numbers will be printed at the start tof each line
 * @param printable Print printable characters after each chunk
 *
 *
 */
fun List<Int>.toHexChunked(indent: String = "", columns: Int = 16, lineNums: Boolean = true, printable: Boolean = true): String {

    return buildString {
        this@toHexChunked.asSequence()
            .chunked(columns)
            .forEachIndexed { index, list ->

                append(indent)
                if (lineNums) append("${index.toString().padStart(2, '0')}   ")
                append(list.toHexShort())

                if (printable && list.size < columns) repeat(columns - list.size) {
                    append("   ")
                }

                if (printable) {
                    append("    ")

                    list.map { if (it in 0..31 || it in 128.toByte()..255.toByte()) 46 else it }
                        .forEach { byte -> append(byte.toChar()) }
                }
                append('\n')
            }
    }.trimEnd('\n')
}
