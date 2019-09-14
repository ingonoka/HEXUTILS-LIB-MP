package com.ingonoka.hexutils

/**
 * Create a [ByteArray] from a string of Hex characters
 *
 * The following characters and character sequences will be removed before the string is converted:
 * space,comma, square brackets and "0x"
 * ```
 * "010203".hexToBytes().contentEquals(byteArrayOf(1,2,3)
 *```
 * **Removing spaces is done with regular expressions and is almost 30% slower and should be avoided**
 *
 * @return The [ByteArray]
 *
 * @Throws NumberFormatException if any character is not a hex character or the number of hex characters is less than 2 or the number of characters is not even
 */
fun String.hexToBytes(removeSpace: Boolean = false): ByteArray {

    val hexNoSpace = if (removeSpace) replace("\\p{Space}|0x|0X|[,\\[\\]]".toRegex(), "") else this

    val res = ByteArray(hexNoSpace.length / 2)

    require (hexNoSpace.length % 2 == 0) { "Only conversion of strings with even length supported: $this" }

    hexNoSpace.chunked(2).forEachIndexed { i, chunk ->
        if (chunk.length == 2) res[i] = chunk.hexToByte()
    }

    return res
}

/**
 * Create a [Byte] from a string of two Hex characters
 *
 * @throws NumberFormatException  if the number of characters is less than 2 or one of the characters is not
 * a hex character
 *
 */
fun String.hexToByte(): Byte = this.take(2).toInt(16).toByte()