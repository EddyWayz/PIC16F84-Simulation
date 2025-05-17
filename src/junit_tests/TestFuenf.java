package junit_tests;

import main.PIC;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestFuenf {
    private PIC pic;
    // Adressen aus dem Listing
    private static final int WERT1 = 0x0C;
    private static final int WERT2 = 0x0D;

    @Before
    public void setUp() {
        String path = "";
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            path = "C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files\\TPicSim5.LST";
            System.out.println("Running on Windows");
        } else if (os.contains("mac")) {
            path = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim5.LST";
            System.out.println("Running on Mac");
        } else {
            System.out.println("Running on another OS: " + os);
        }
        pic = new PIC(path);
    }

    @Test
    public void TestFile5() {
        // 0000 movlw 11h
        pic.step();
        Assert.assertEquals(0x11, pic.getW());

        // 0001 movwf wert1
        pic.step();
        Assert.assertEquals(0x11, pic.getW());
        Assert.assertEquals(0x11, pic.memory.read(WERT1));

        // 0002 clrf wert2: W unverändert, wert2=0, Z=1
        pic.step();
        Assert.assertEquals(0x11, pic.getW());
        Assert.assertEquals(0x00, pic.memory.read(WERT2));
        Assert.assertEquals(1, pic.memory.get_Z());

        // 0003 bsf wert1,7 → wert1=0x91, W unverändert, Z bleibt 1
        pic.step();
        Assert.assertEquals(0x11, pic.getW());
        Assert.assertEquals(0x91, pic.memory.read(WERT1));
        Assert.assertEquals(1, pic.memory.get_Z());

        // 0004 bsf wert1,3 → wert1=0x99
        pic.step();
        Assert.assertEquals(0x11, pic.getW());
        Assert.assertEquals(0x99, pic.memory.read(WERT1));
        Assert.assertEquals(1, pic.memory.get_Z());

        // 0005 bcf wert1,4 → wert1=0x89
        pic.step();
        Assert.assertEquals(0x11, pic.getW());
        Assert.assertEquals(0x89, pic.memory.read(WERT1));
        Assert.assertEquals(1, pic.memory.get_Z());

        // 0006 bcf wert1,0 → wert1=0x88
        pic.step();
        Assert.assertEquals(0x11, pic.getW());
        Assert.assertEquals(0x88, pic.memory.read(WERT1));
        Assert.assertEquals(1, pic.memory.get_Z());

        // 0007 btfsc wert1,0 → bit0=0 → skip next, W und wert2 unverändert, Z bleibt 1
        pic.step();
        Assert.assertEquals(0x11, pic.getW());
        Assert.assertEquals(0x00, pic.memory.read(WERT2));
        Assert.assertEquals(1, pic.memory.get_Z());

        // 0009 incf wert2 → wert2=1, Z=0
        pic.step();
        Assert.assertEquals(0x11, pic.getW());
        Assert.assertEquals(0x01, pic.memory.read(WERT2));
        Assert.assertEquals(0, pic.memory.get_Z());

        // 000A btfsc wert1,3 → bit3=1 → do not skip, W unverändert, Z bleibt 0
        pic.step();
        Assert.assertEquals(0x11, pic.getW());
        Assert.assertEquals(0, pic.memory.get_Z());

        // 000B incf wert2 → wert2=2
        pic.step();
        Assert.assertEquals(0x02, pic.memory.read(WERT2));
        Assert.assertEquals(0, pic.memory.get_Z());

        // 000C incf wert2 → wert2=3
        pic.step();
        Assert.assertEquals(0x03, pic.memory.read(WERT2));
        Assert.assertEquals(0, pic.memory.get_Z());

        // 000D btfss wert1,2 → bit2=0 → do not skip, Z bleibt 0
        pic.step();
        Assert.assertEquals(0x11, pic.getW());
        Assert.assertEquals(0, pic.memory.get_Z());

        // 000E incf wert2 → wert2=4
        pic.step();
        Assert.assertEquals(0x04, pic.memory.read(WERT2));
        Assert.assertEquals(0, pic.memory.get_Z());

        // 000F incf wert2 → wert2=5
        pic.step();
        Assert.assertEquals(0x05, pic.memory.read(WERT2));
        Assert.assertEquals(0, pic.memory.get_Z());

        // 0010 btfss wert1,7 → bit7=1 → skip next, Z bleibt 0
        pic.step();
        Assert.assertEquals(0, pic.memory.get_Z());

        // 0012 decf wert2 → wert2=4
        pic.step();
        Assert.assertEquals(0x04, pic.memory.read(WERT2));
        Assert.assertEquals(0, pic.memory.get_Z());
    }
}
