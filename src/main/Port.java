package main;

public class Port {
    public IO_PIN[] ioPins = new IO_PIN[8];
    public Port() {
        for (int i=0; i<ioPins.length;i++) {
            ioPins[i] = new IO_PIN();
        }
    }
}