package etu2011.framework.utils.javaObject;

import java.io.File;

import etu2011.framework.exceptions.JavaFileException;

/**
 * The {@code JavaFile} class is used to represent a Java file.
 */
public class JavaFile {
    /* FIELD SECTION */
    private File javaFile;

    /* CONSTRUCTORS SECTION */

    /**
     * Default constructor.
     */
    public JavaFile() {
    }

    /**
     * Constructor with the file parameter.
     * 
     * @param file the file to be set.
     * @throws Exception if the file is not a Java file.
     */
    public JavaFile(File file) throws Exception {
        this.setJavaFile(file);
    }

    /* SETTER SECTION */

    /**
     * This method sets the Java file.
     * 
     * @param file the file to be set.
     * @throws JavaFileException if the file is not a Java file.
     */
    public void setJavaFile(File file) throws JavaFileException {
        if (!isJavaFile(file))
            throw new JavaFileException();
        this.javaFile = file;
    }

    /* GETTER SECTION */

    /**
     * Returns the java File object
     * 
     * @return the Java file.
     */
    public File getJavaFile() {
        return this.javaFile;
    }

    /* METHODS SECTION */

    /**
     * Check if the target file is a java file
     * 
     * @param file the file to check.
     * @return true if the file is a Java file, false otherwise.
     */
    public boolean isJavaFile(File file) {
        return file.getName().toLowerCase().endsWith(".java") || file.getName().toLowerCase().endsWith(".class");
    }

    /**
     * Get the Class object from the java file of this instance
     * 
     * @param pathToRemove the path to remove from the file path.
     * @return the class object.
     */
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
