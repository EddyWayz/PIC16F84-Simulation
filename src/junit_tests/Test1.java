package junit_tests;

import main.PIC;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Test1 {
    PIC pic;

    @Before
    public void setUp() throws Exception {
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
    public void test_TestFile1() {
        //0000 movlw 11h
        pic.step();
        Assert.assertEquals(pic.getW(), 0x11);

        //0001 andlw 30h
        pic.step();
        Assert.assertEquals(pic.getW(), 0x10);
        Assert.assertEquals(pic.memory.get_Z(), 0);

        //0002 iorlw 0Dh
        pic.step();
        Assert.assertEquals(pic.getW(), 0x1D);
        Assert.assertEquals(pic.memory.get_Z(), 0);

        //0003 sublw 3Dh
        pic.step();
        Assert.assertEquals(pic.getW(), 0x20);
        checkFlags(1,1,0);

        //0004 xorlw 20h
        pic.step();
        Assert.assertEquals(pic.getW(), 0x00);
        checkFlags(1,1,1);

        //0005 addlw 25h
        pic.step();
        Assert.assertEquals(pic.getW(), 0x25);
        checkFlags(0,0,0);
    }

    private void checkFlags(int carry, int digitcarry, int zeroflag) {
        Assert.assertEquals(pic.memory.get_C(), carry);
        Assert.assertEquals(pic.memory.get_DC(), digitcarry);
        Assert.assertEquals(pic.memory.get_Z(), zeroflag);
    }


}
