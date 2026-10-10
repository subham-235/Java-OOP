import java.util.*;
import java.io.*;


class Logger {
    private String path; // to store the path of file
    
    // Constructor
    Logger(String path) throws IOException {
        File file = new File(path); // Open the file path 
        
        // Create the file if it does not exist
        if (!file.exists()) {
            file.createNewFile();
        }
        this.path = path;
    
    }
    
    // Log the message in the file
    public void log(String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(message); 
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Failed to log this message " + message);
        }
    }
}

class Main {
    public static void main(String[] args) {
        try {
            // Create a Logger instance with a specified log file path
            Logger myLogger = new Logger("application.log");

            // Log some messages
            myLogger.log("Application started...");
            myLogger.log("User logged in.");
            myLogger.log("Error: Unable to connect to the database.");
            myLogger.log("Application closed.");

            System.out.println("Logs have been written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}