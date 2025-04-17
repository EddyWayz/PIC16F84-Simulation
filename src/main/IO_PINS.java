package main;

public class IO_PINS {
    IO_PIN[][] ioPins = new IO_PIN[7][7];
    public IO_PINS() {
        for (int i=0; i<ioPins.length;i++) {
            for (int j=0;j<ioPins[i].length;j++){
                ioPins[i][j] = new IO_PIN();
            }
        }
    }
}
