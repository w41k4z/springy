package etu2011.framework.utils;

import java.io.IOException;

import jakarta.servlet.http.Part;

/**
 * The {@code UploadedFile} class is used to represent an uploaded file.
 */
public class UploadedFile {
    /* FIELDS SECTION */
    private String fileName;
    private String path;
    private byte[] fileBytes;

    /* CONSTRUCTOR SECTION */

    /**
     * The default constructor
     * 
     * @param part The part object from a request
     * @throws IOException
     */
    public UploadedFile(Part part) throws IOException {
        this.setFileName(extractFileName(part));
        this.setPath("");
        this.setFileBytes(part.getInputStream().readAllBytes());
    }

    /* SETTERS SECTION */
    /**
     * Set the file name
     * 
     * @param name The file name
     */
    private void setFileName(String name) {
        this.fileName = name;
    }

    /**
     * Set the file path
     * 
     * @param path The path
     */
    private void setPath(String path) {
        this.path = path;
    }

    /**
     * Set the file bytes
     * 
     * @param bytes The Part bytes
     */
    private void setFileBytes(byte[] bytes) {
        this.fileBytes = bytes;
    }

    /* GETTERS SECTION */

    /**
     * Returns the file name
     * 
     * @return The file name
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Returns the file path
     * 
     * @return The file path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Returns the file bytes
     * 
     * @return The file bytes
     */
    public byte[] getFileBytes() {
        return this.fileBytes;
    }

    /* METHODS SECTION */

    /**
     * Extracts the file name from the {@code Part} object.
     * 
     * @param part The {@code Part} object
     * @return The file name
     */
    public static String extractFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
