import com.ingonoka.hexutils.hexToByte
import com.ingonoka.hexutils.hexToBytes
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

        assertTrue { assertArrayEquals(byteArrayOf(1, 2), "0102".hexToBytes()) }

        assertTrue(assertThrown(IllegalArgumentException::class) { "1".hexToBytes() })

        assertTrue { assertArrayEquals(byteArrayOf(), "".hexToBytes()) }

    }

    @Test
    fun hexToByte() {
        assertEquals(1.toByte(), "01".hexToByte())
        assertEquals(1.toByte(), "1".hexToByte())
        assertEquals(1.toByte(), "0103".hexToByte())
        assertTrue(assertThrown(NumberFormatException::class) { "".hexToByte() })
    }
}

