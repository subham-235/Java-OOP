import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

// Main class
class Main {
    public static void main(String[] args) {
        BufferedWriter writer = null;
        
        // Try-catch block to handle exception
        try {
            writer = new BufferedWriter(new FileWriter("output.txt"));
            
            // Writing in the file 
            writer.write("Hello, world!\n");
            writer.write("This is a sample file.");
            
            // Closing the file after writing
            writer.close();
            System.out.println("File written successfully.");
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        
        // Remaining section of the code 
    }
}