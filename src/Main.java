
import cli.CommandLineInterface;

public class Main {
    public static void main(String[] args) {
        try {
            CommandLineInterface cli = new CommandLineInterface();
            cli.start();
        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}