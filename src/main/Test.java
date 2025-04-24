package main;

public class Test {
    public static void main(String[] args) {
        try {
            System.out.println("This is a test");
        } catch (Exception e) {
            System.err.println("⚠ Fehler in Test.main: " + e.getMessage());
        }
    }
}
