package main;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Port {
    private static final Logger LOGGER = Logger.getLogger(Port.class.getName());
    public IO_PIN[] pins = new IO_PIN[8];
    private final String name;

    public Port(String name) {
        this.name = name;
        try {
            for (int i = 0; i < pins.length; i++) {
                pins[i] = new IO_PIN();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Initialisieren und Füllen der IO_PINs", e);
        }
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(IO_PIN pin : pins){
            stringBuilder.append(pin.getValue() ? 1 : 0);
        }
        return stringBuilder.toString();
    }
}
