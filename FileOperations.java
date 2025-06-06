import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption; // Import for append option
import java.io.IOException;

// Class to demonstrate file operations: read, write, and modify
public class FileOperations {
    public static void main(String[] args) {
        // Defining the file path (i.e. creates "sample.txt" in the project directory)
        Path filePath = Paths.get("sample.txt");

        try {
            // 1. Writing to the file (creates a new file or overwrites if it exists)
            String contentToWrite = "Hello, this is the initial content!\nWritten on June 04, 2025 at 05:05 PM IST.";
            Files.writeString(filePath, contentToWrite);
            System.out.println("Successfully wrote to the file: " + filePath);

            // 2. Reading from the file
            String contentRead = Files.readString(filePath);
            System.out.println("\nReading file content:\n" + contentRead);

            // 3. Modifying the file (append new text) - Fixed line
            String contentToAppend = "\nThis line was appended to modify the file.";
            Files.writeString(filePath, contentToAppend, StandardOpenOption.APPEND); // Use StandardOpenOption.APPEND
            System.out.println("\nSuccessfully modified the file by appending new content.");

            // 4. Reading the modified file to confirm changes
            contentRead = Files.readString(filePath);
            System.out.println("\nReading modified file content:\n" + contentRead);

        } catch (IOException e) {
            // Handling any file operation errors
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
