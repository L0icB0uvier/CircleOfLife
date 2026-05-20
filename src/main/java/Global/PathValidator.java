package Global;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathValidator {

    /**
     * Validates an output path and creates parent directories if needed.
     * @param rawPath The raw path string provided by the user (e.g., "outputs/report.pdf")
     * @return A valid Path object ready for writing.
     * @throws IllegalArgumentException If the path is invalid or unwritable.
     */
    public static Path validateAndPrepareOutputPath(String rawPath) {
        // 1. Basic check (not empty)
        if (rawPath == null || rawPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: Output path cannot be empty.");
        }

        Path targetPath;
        try {
            // 2. Syntactic validation (checks for OS-forbidden characters)
            targetPath = Paths.get(rawPath);
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("Error: The path contains invalid characters. (" + e.getMessage() + ")");
        }

        // 3. Retrieve the parent directory
        Path parentDir = targetPath.getParent();

        // If parentDir is null, it means writing to the root directory (e.g., "report.pdf")
        if (parentDir != null) {
            try {
                // 4. Automatically create parent directories if they don't exist
                // (Safe operation: does nothing if directories already exist)
                Files.createDirectories(parentDir);
            } catch (IOException e) {
                throw new IllegalArgumentException("Error: Failed to create directory structure: " + parentDir);
            }

            // 5. Check write permissions on parent directory
            if (!Files.isWritable(parentDir)) {
                throw new IllegalArgumentException("Error: You do not have permission to write to directory: " + parentDir);
            }
        }

        // 6. If the file already exists, check if it can be overwritten
        if (Files.exists(targetPath) && !Files.isWritable(targetPath)) {
            throw new IllegalArgumentException("Error: Target file already exists and is write-protected.");
        }

        return targetPath;
    }
}