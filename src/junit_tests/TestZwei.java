package junit_tests;

import main.PIC;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestZwei {
    private PIC pic;

    // Adressen im Listing
    private static final int ADDR_UP1 = 0x06;
    private static final int ADDR_UP2 = 0x08;
    private static final int ADDR_LOOP = 0x00;
    private static final int ADDR_AFTER_CALL_UP1 = 0x02;
    private static final int ADDR_AFTER_CALL_UP2 = 0x04;

    @Before
    public void setUp() {
        String path = "";
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            path = "C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files\\TPicSim2.LST";
            System.out.println("Running on Windows");
        } else if (os.contains("mac")) {
            path = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim2.LST";
            System.out.println("Running on Mac");
        } else {
            System.out.println("Running on another OS: " + os);
        }
        pic = new PIC(path);
    }

    @Test
    public void TestFile2() {
        // 0000 movlw 11h
        pic.step();
        Assert.assertEquals(0x11, pic.getW());

        // 0001 call up1
        pic.step();
        // Nach CALL: PC muss auf Adresse von up1 springen
        Assert.assertEquals(ADDR_UP1, pic.memory.getPC());
        // W bleibt unverändert
        Assert.assertEquals(0x11, pic.getW());

        // 0006 addlw 25h (up1)
        pic.step();
        Assert.assertEquals(0x36, pic.getW());
        checkFlags(0, 0, 0);

        // 0007 return (up1)
        pic.step();
        // Nach RETURN: PC muss auf die Instruktion nach dem CALL zurückspringen (Adresse 0002)
        Assert.assertEquals(ADDR_AFTER_CALL_UP1, pic.memory.getPC());

        // 0002 nop
        pic.step();
        // nop verändert W nicht
        Assert.assertEquals(0x36, pic.getW());

        // 0003 call up2
        pic.step();
        Assert.assertEquals(ADDR_UP2, pic.memory.getPC());
        Assert.assertEquals(0x36, pic.getW());

        // 0008 retlw 77h (up2)
        pic.step();
        // RETLW setzt W und springt zurück an Instruktion nach CALL (Adresse 0004)
        Assert.assertEquals(0x77, pic.getW());
        Assert.assertEquals(ADDR_AFTER_CALL_UP2, pic.memory.getPC());
        checkFlags(0, 0, 0);

        // 0004 nop
        pic.step();
        Assert.assertEquals(0x77, pic.getW());

        // 0005 goto loop
        pic.step();
        // GOTO loop (org 0) → PC muss wieder 0 werden
        Assert.assertEquals(ADDR_LOOP, pic.memory.getPC());
    }

    private void checkFlags(int carry, int digitCarry, int zero) {
        Assert.assertEquals(carry, pic.memory.get_C());
        Assert.assertEquals(digitCarry, pic.memory.get_DC());
        Assert.assertEquals(zero, pic.memory.get_Z());
    }
}
