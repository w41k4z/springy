package etu2011.framework.reader;

import java.io.File;

import fileActivity.Reader;

public class JavaFileReader extends Reader {

    // constructor
    public JavaFileReader(File file) throws Exception {
        super(file);
        if (this.isJavaFile())
            throw new Exception("ERROR: This is not a java file");
    }

    // methods
    private boolean isJavaFile() {
        return this.getFile().getName().toLowerCase().endsWith(".java");
    }

    public Class getClassObject() {
        String className = this.getFile().getName();
        return null;
    }

    public void scanProject() {

    }
}
