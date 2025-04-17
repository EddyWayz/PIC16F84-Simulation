package junit_tests;

import main.PIC;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Test4 {
    private PIC pic;
    private static final int WERT1 = 0x0C;
    private static final int WERT2 = 0x0D;

    // Addresses of DECFSZ and INCFSZ in the program (hex)
    private static final int DECFSZ_ADDR    = 0x12;
    private static final int DECFSZ_NEXT    = 0x13;
    private static final int DECFSZ_EXIT    = 0x14;
    private static final int INCFSZ_ADDR    = 0x1A;
    private static final int INCFSZ_NEXT    = 0x1B;
    private static final int INCFSZ_EXIT    = 0x1C;

    @Before
    public void setUp() {
        String path = "";
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            path = "C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files\\TPicSim4.LST";
        } else if (os.contains("mac")) {
            path = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim4.LST";
        }
        pic = new PIC(path);
    }

    @Test
    public void TestFile4() {
        // set up initial registers: movlw/movwf/addlw
        pic.step(); // 0x00 movlw 11h
        pic.step(); // 0x01 movwf wert1  → W=0x11, wert1=0x11
        pic.step(); // 0x02 addlw 11h   → W=0x22, flags DC=0,C=0,Z=0

        // 0x03 rlf wert1
        pic.step();
        Assert.assertEquals(0x22, pic.memory.read(WERT1));
        Assert.assertEquals(0x22, pic.getW());
        checkFlags(0, 0, 0);

        // 0x04 rlf wert1
        pic.step();
        Assert.assertEquals(0x44, pic.memory.read(WERT1));
        Assert.assertEquals(0x22, pic.getW());
        checkFlags(0, 0, 0);

        // 0x05 rlf wert1
        pic.step();
        Assert.assertEquals(0x88, pic.memory.read(WERT1));
        Assert.assertEquals(0x22, pic.getW());
        checkFlags(0, 0, 0);

        // 0x06 rlf wert1,w
        pic.step();
        Assert.assertEquals(0x88, pic.memory.read(WERT1));
        Assert.assertEquals(0x10, pic.getW());
        checkFlags(1, 0, 0);

        // 0x07 rlf wert1
        pic.step();
        Assert.assertEquals(0x11, pic.memory.read(WERT1));
        Assert.assertEquals(0x10, pic.getW());
        checkFlags(1, 0, 0);

        // 0x08 rlf wert1,w
        pic.step();
        Assert.assertEquals(0x11, pic.memory.read(WERT1));
        Assert.assertEquals(0x23, pic.getW());
        checkFlags(0, 0, 0);

        // 0x09 rrf wert1
        pic.step();
        Assert.assertEquals(0x08, pic.memory.read(WERT1));
        Assert.assertEquals(0x23, pic.getW());
        checkFlags(1, 0, 0);

        // 0x0A movwf wert2
        pic.step();
        Assert.assertEquals(0x23, pic.memory.read(WERT2));

        // 0x0B rrf wert2
        pic.step();
        Assert.assertEquals(0x91, pic.memory.read(WERT2));
        Assert.assertEquals(0x23, pic.getW());
        checkFlags(1, 0, 0);

        // 0x0C rrf wert2,w
        pic.step();
        Assert.assertEquals(0x91, pic.memory.read(WERT2));
        Assert.assertEquals(0xC8, pic.getW());
        checkFlags(1, 0, 0);
    }

    @Test
    public void testDECFSZ_normalBehavior() {
        // run up to clrw to set wert1 = 0x09
        for (int i = 0; i < 15; i++) {
            pic.step();
        }
        // now at 0x10 addlw 1, 0x11 addwf wert2, 0x12 DECFSZ wert1
        pic.step(); // 0x10 addlw 1
        pic.step(); // 0x11 addwf wert2
        Assert.assertEquals(DECFSZ_ADDR, pic.memory.getPC());
        pic.step(); // 0x12 decfsz wert1 (9 → 8), not zero → should NOT skip
        Assert.assertEquals(0x08, pic.memory.read(WERT1));
        Assert.assertEquals(DECFSZ_NEXT, pic.memory.getPC());
        // next is goto loop1
        pic.step();
        Assert.assertEquals(0x10, pic.memory.getPC());
    }

    @Test
    public void testDECFSZ_skipWhenZero() {
        // prepare: set wert1 to 1, jump PC to DECFSZ instruction
        pic.memory.write(WERT1, 1);
        pic.memory.setPC(DECFSZ_ADDR);
        pic.step(); // decfsz wert1 → 1 → 0, skip next
        Assert.assertEquals(0x00, pic.memory.read(WERT1));
        Assert.assertEquals(DECFSZ_EXIT, pic.memory.getPC());
    }

    @Test
    public void testINCFSZ_normalBehavior() {
        // prepare: set wert1 to 0x01, jump PC to INCFSZ
        pic.memory.write(WERT1, 1);
        pic.memory.setPC(INCFSZ_ADDR);
        pic.step(); // incfsz wert1 → 2, not zero → should NOT skip
        Assert.assertEquals(0x02, pic.memory.read(WERT1));
        Assert.assertEquals(INCFSZ_NEXT, pic.memory.getPC());
    }

    @Test
    public void testINCFSZ_skipWhenZero() {
        // prepare: set wert1 to 0xFF, jump PC to INCFSZ
        pic.memory.write(WERT1, 0xFF);
        pic.memory.setPC(INCFSZ_ADDR);
        pic.step(); // incfsz → 0x00, skip next
        Assert.assertEquals(0x00, pic.memory.read(WERT1));
        Assert.assertEquals(INCFSZ_EXIT, pic.memory.getPC());
    }

    private void checkFlags(int carry, int digitCarry, int zero) {
        Assert.assertEquals(carry, pic.memory.get_C());
        Assert.assertEquals(digitCarry, pic.memory.get_DC());
        Assert.assertEquals(zero, pic.memory.get_Z());
    }
}
