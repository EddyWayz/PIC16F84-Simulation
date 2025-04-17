package main;

public class IO_PIN {
    private boolean value;
    private boolean input;
    public void toggleValue() {
        value  = !value;
    }

    public void setValue(boolean value) {
        this.value = value;
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
