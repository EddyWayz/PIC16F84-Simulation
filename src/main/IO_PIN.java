package main;

public class IO_PIN {
    private boolean value;
    private boolean input;
    public void toggleValue() {
        value  = !value;
    }

    public void setValue(boolean value) {
        try {
            this.value = value;
        } catch (Exception e) {
            System.err.println("⚠ Fehler beim Setzen des PIN-Werts: " + e.getMessage());
        }
    }

    public boolean getValue() {
        return value;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

    public boolean getInput() {
        return input;
    }
}
