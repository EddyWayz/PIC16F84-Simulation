package main;

public class Port {
    public IO_PIN[] pins = new IO_PIN[8];
    private final String name;

    public Port(String name) {
        this.name = name;
        try {
            for (int i = 0; i < pins.length; i++) {
                pins[i] = new IO_PIN();
            }
        } catch (Exception e) {
            System.err.println("⚠ Fehler beim Initialisieren der IO_PINs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }
}