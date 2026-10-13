package com.example.auth.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MaskingUtilTest {

    @Test
    public void testMaskIdentificationNumber() {
        assertNull(MaskingUtil.maskIdentificationNumber(null));
        assertEquals("", MaskingUtil.maskIdentificationNumber(""));
        assertEquals("****", MaskingUtil.maskIdentificationNumber("123"));
        assertEquals("****", MaskingUtil.maskIdentificationNumber("1234"));
        assertEquals("****5678", MaskingUtil.maskIdentificationNumber("12345678"));
        assertEquals("****CDEF", MaskingUtil.maskIdentificationNumber("ABCDEF"));
    }

    @Test
    public void testMaskEmail() {
        assertNull(MaskingUtil.maskEmail(null));
        assertEquals("", MaskingUtil.maskEmail(""));
        assertEquals("j***@example.com", MaskingUtil.maskEmail("johndoe@example.com"));
        assertEquals("a***@b.com", MaskingUtil.maskEmail("abc@b.com"));
        assertEquals("***@b.com", MaskingUtil.maskEmail("a@b.com"));
        assertEquals("***", MaskingUtil.maskEmail("noat-sign"));
        assertEquals("***@domain.com", MaskingUtil.maskEmail("@domain.com")); // empty local part
    }

    @Test
    public void testMaskMobileNumber() {
        assertNull(MaskingUtil.maskMobileNumber(null));
        assertEquals("", MaskingUtil.maskMobileNumber(""));
        assertEquals("****", MaskingUtil.maskMobileNumber("123"));
        assertEquals("****", MaskingUtil.maskMobileNumber("1234"));
        assertEquals("****6789", MaskingUtil.maskMobileNumber("123456789"));
        assertEquals("****4321", MaskingUtil.maskMobileNumber("+1987654321"));
    }
}
