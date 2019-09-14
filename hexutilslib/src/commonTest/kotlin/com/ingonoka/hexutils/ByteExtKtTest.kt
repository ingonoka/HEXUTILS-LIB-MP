import com.ingonoka.hexutils.toHex
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