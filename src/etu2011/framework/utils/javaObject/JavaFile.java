package etu2011.framework.utils.javaObject;

import java.io.File;

import etu2011.framework.exceptions.JavaFileException;

public class JavaFile {
    /* FIELD SECTION */
    private File javaFile;

    /* CONSTRUCTORS SECTION */
    public JavaFile() {
    }

    public JavaFile(File file) throws Exception {
        this.setJavaFile(file);
    }

    /* SETTER SECTION */
    public void setJavaFile(File file) throws JavaFileException {
        if (!isJavaFile(file))
            throw new JavaFileException();
        this.javaFile = file;
    }

    /* GETTER SECTION */
    public File getJavaFile() {
        return this.javaFile;
    }

    /* METHODS SECTION */
    public boolean isJavaFile(File file) {
        return file.getName().toLowerCase().endsWith(".java") || file.getName().toLowerCase().endsWith(".class");
    }

    public Class<?> getClassObject(String pathToRemove) {
        String path = this.getJavaFile().getPath().replace(pathToRemove, "").replace("/", ".");
        try {
            return Class.forName(path.substring(0, path.length() - 6));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
