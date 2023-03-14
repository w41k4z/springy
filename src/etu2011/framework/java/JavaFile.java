package etu2011.framework.java;

import java.io.File;

import etu2011.framework.exception.JavaFileException;

public class JavaFile {

    private File javaFile;

    // constructor
    public JavaFile() {
    }

    public JavaFile(File file) throws Exception {
        this.setJavaFile(file);
    }

    // setter
    public void setJavaFile(File file) throws JavaFileException {
        if (isJavaFile(file))
            throw new JavaFileException();
        this.javaFile = file;
    }

    // getter
    public File getJavaFile() {
        return this.javaFile;
    }

    // methods
    public boolean isJavaFile(File file) {
        return file.getName().toLowerCase().endsWith(".java");
    }

    public Class<?> getClassObject() {
        String path = this.getJavaFile().getPath().replace("/", ".");
        try {
            return Class.forName(path.substring(0, path.length() - 5));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
