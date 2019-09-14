@file:Suppress("unused")

package com.ingonoka.hexutils

/**
 * Converts a Byte into a hex string. [prefix] is prepended if provided
 */
fun Byte.toHex(prefix: String = "") =
    "$prefix${(this.toInt() and 0xFF).toString(16).padStart(2, '0').toUpperCase()}"