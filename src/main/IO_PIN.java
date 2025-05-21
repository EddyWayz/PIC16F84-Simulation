package main;

public class IO_PIN {
    private boolean value;

    public void toggleValue() {
        try {
            value = !value;
        } catch (Exception e) {
            System.err.println("Fehler beim Umschalten des PIN-Werts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setValue(boolean value) {
        try {
            this.value = value;
        } catch (Exception e) {
            System.err.println("Fehler beim Setzen des PIN-Werts: " + e.getMessage());
        }
    }

    public boolean getValue() {
        return value;
    }

}
