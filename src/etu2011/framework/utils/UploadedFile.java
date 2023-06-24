package etu2011.framework.utils;

import java.io.IOException;

import jakarta.servlet.http.Part;

public class UploadedFile {
    /* FIELDS SECTION */
    private String fileName;
    private String path;
    private byte[] fileBytes;

    /* CONSTRUCTOR SECTION */
    public UploadedFile(Part part) throws IOException {
        this.setFileName(extractFileName(part));
        this.setPath("");
        this.setFileBytes(part.getInputStream().readAllBytes());
    }

    /* SETTERS SECTION */
    private void setFileName(String name) {
        this.fileName = name;
    }

    private void setPath(String path) {
        this.path = path;
    }

    private void setFileBytes(byte[] bytes) {
        this.fileBytes = bytes;
    }

    /* GETTERS SECTION */
    public String getFileName() {
        return this.fileName;
    }

    public String getPath() {
        return this.path;
    }

    public byte[] getFileBytes() {
        return this.fileBytes;
    }

    /* METHODS SECTION */
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
