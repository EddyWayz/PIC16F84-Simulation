package junit_tests;

import main.PIC;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestDrei {
    private PIC pic;
    // Die Adressen, wie im Listing: wert1 = 0x0C, wert2 = 0x0D
    private static final int WERT1 = 0x0C;
    private static final int WERT2 = 0x0D;

    @Before
    public void setUp() {
        String path = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            path = "C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files\\TPicSim3.LST";
            System.out.println("Running on Windows");
        } else if (osName.contains("mac")) {
            path = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim3.LST";
            System.out.println("Running on Mac");
        } else {
            System.out.println("Running on another OS: " + osName);
        }
        pic = new PIC(path);
    }

    @Test
    public void TestFile3() {
        // 0000 movlw 11h
        pic.step();
        Assert.assertEquals(0x11, pic.getW());

        // 0001 movwf wert1
        pic.step();
        Assert.assertEquals(0x11, pic.getW());
        // statt getFile() jetzt read()
        Assert.assertEquals(0x11, pic.memory.read(WERT1));

        // 0002 movlw 14h
        pic.step();
        Assert.assertEquals(0x14, pic.getW());

        // 0003 addwf wert1,w (d=0 → nur W verändert)
        pic.step();
        Assert.assertEquals(0x25, pic.getW());
        checkFlags(0, 0, 0);
        // File‑Register bleibt unverändert
        Assert.assertEquals(0x11, pic.memory.read(WERT1));

        // 0004 addwf wert1    (d=1 → ins File)
        pic.step();
        Assert.assertEquals(0x25, pic.getW());
        Assert.assertEquals(0x36, pic.memory.read(WERT1));
        checkFlags(0, 0, 0);

        // 0005 andwf wert1,w
        pic.step();
        Assert.assertEquals(0x24, pic.getW());
        Assert.assertEquals(0x36, pic.memory.read(WERT1));
        checkFlags(0, 0, 0);

        // 0006 movwf wert2
        pic.step();
        Assert.assertEquals(0x24, pic.getW());
        Assert.assertEquals(0x24, pic.memory.read(WERT2));

        // 0007 clrf wert1
        pic.step();
        Assert.assertEquals(0x24, pic.getW());
        Assert.assertEquals(0x00, pic.memory.read(WERT1));
        checkFlags(0, 0, 1);

        // 0008 comf wert2,w
        pic.step();
        Assert.assertEquals(0xDB, pic.getW());
        Assert.assertEquals(0x24, pic.memory.read(WERT2));
        checkFlags(0, 0, 0);

        // 0009 decf wert1,w
        pic.step();
        Assert.assertEquals(0xFF, pic.getW());
        Assert.assertEquals(0x00, pic.memory.read(WERT1));
        checkFlags(0, 0, 0);

        // 000A incf wert2      (d=1 → ins File)
        pic.step();
        Assert.assertEquals(0xFF, pic.getW());
        Assert.assertEquals(0x25, pic.memory.read(WERT2));
        checkFlags(0, 0, 0);

        // 000B movf wert1     (d=1 → ins File)
        pic.step();
        Assert.assertEquals(0xFF, pic.getW());
        Assert.assertEquals(0x00, pic.memory.read(WERT1));
        checkFlags(0, 0, 1);

        // 000C iorwf wert1    (d=1 → ins File)
        pic.step();
        Assert.assertEquals(0xFF, pic.getW());
        Assert.assertEquals(0xFF, pic.memory.read(WERT1));
        checkFlags(0, 0, 0);

        // 000D subwf wert2,w
        pic.step();
        Assert.assertEquals(0x26, pic.getW());
        Assert.assertEquals(0xFF, pic.memory.read(WERT1));
        Assert.assertEquals(0x25, pic.memory.read(WERT2));
        checkFlags(0, 0, 0);

        // 000E swapf wert2    (d=1 → ins File)
        pic.step();
        Assert.assertEquals(0x26, pic.getW());
        Assert.assertEquals(0x52, pic.memory.read(WERT2));
        checkFlags(0, 0, 0);

        // 000F xorwf wert1    (d=1 → ins File)
        pic.step();
        Assert.assertEquals(0x26, pic.getW());
        Assert.assertEquals(0xD9, pic.memory.read(WERT1));
        checkFlags(0, 0, 0);

        // 0010 clrw
        pic.step();
        Assert.assertEquals(0x00, pic.getW());
        // Dateien bleiben gleich
        Assert.assertEquals(0xD9, pic.memory.read(WERT1));
        Assert.assertEquals(0x52, pic.memory.read(WERT2));
        checkFlags(0, 0, 1);

        // 0011 subwf wert1,w
        pic.step();
        Assert.assertEquals(0xD9, pic.getW());
        checkFlags(1, 1, 0);

        // 0012 subwf wert2,w
        pic.step();
        Assert.assertEquals(0x79, pic.getW());
        checkFlags(0, 0, 0);

        // 0013 subwf wert2    (d=1 → ins File)
        pic.step();
        Assert.assertEquals(0x79, pic.getW());
        Assert.assertEquals(0xD9, pic.memory.read(WERT2));
        checkFlags(0, 0, 0);

        // 0014 subwf wert2    (d=1 → ins File)
        pic.step();
        Assert.assertEquals(0x79, pic.getW());
        Assert.assertEquals(0x60, pic.memory.read(WERT2));
        checkFlags(1, 1, 0);
    }

    private void checkFlags(int carry, int digitCarry, int zero) {
        Assert.assertEquals(carry, pic.memory.get_C());
        Assert.assertEquals(digitCarry, pic.memory.get_DC());
        Assert.assertEquals(zero, pic.memory.get_Z());
    }
}
