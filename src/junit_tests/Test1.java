package junit_tests;

import main.PIC;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Test1 {
    PIC pic;

    @Before
    public void setUp() {
        String path = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            path = "C:\\Users\\Noah\\Desktop\\HSO\\Prakt Rechnerarchitekturen\\PIC Sim\\test_files\\TPicSim1.LST";
            System.out.println("Running on Windows");
        } else if (osName.contains("mac")) {
            path = "/Users/eddywayz/Desktop/Studium/Rechnerarchitektur/test_files/TPicSim1.LST";
            System.out.println("Running on Mac");
        } else {
            System.out.println("Running on another OS: " + osName);
        }
        pic = new PIC(path);
    }

    @Test
    public void TestFile1() {
        //0000 movlw 11h
        pic.step();
        Assert.assertEquals( 0x11, pic.getW());

        //0001 andlw 30h
        pic.step();
        Assert.assertEquals(0x10, pic.getW());
        Assert.assertEquals(0, pic.memory.get_Z());

        //0002 iorlw 0Dh
        pic.step();
        Assert.assertEquals(0x1D, pic.getW());
        Assert.assertEquals(0, pic.memory.get_Z());

        //0003 sublw 3Dh
        pic.step();
        Assert.assertEquals(0x20, pic.getW());
        checkFlags(1,1,0);

        //0004 xorlw 20h
        pic.step();
        Assert.assertEquals(0x00, pic.getW());
        checkFlags(1,1,1);

        //0005 addlw 25h
        pic.step();
        Assert.assertEquals(0x25, pic.getW());
        checkFlags(0,0,0);
    }

    private void checkFlags(int carry, int digitcarry, int zeroflag) {
        Assert.assertEquals(carry, pic.memory.get_C());
        Assert.assertEquals(digitcarry, pic.memory.get_DC());
        Assert.assertEquals(zeroflag, pic.memory.get_Z());
    }


}
