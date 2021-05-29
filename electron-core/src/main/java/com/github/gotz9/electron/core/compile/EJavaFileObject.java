package com.github.gotz9.electron.core.compile;

import javax.tools.SimpleJavaFileObject;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EJavaFileObject extends SimpleJavaFileObject {

    private final File file;

    /**
     * Construct a SimpleJavaFileObject of the given kind and with the
     * given URI.
     *
     * @param file the URI for this file object
     */
    protected EJavaFileObject(File file) {
        super(file.toURI(), Kind.SOURCE);
        this.file = file;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        char[] cache = new char[1024];

        try (FileReader inputStream = new FileReader(file)) {
            int l = 0;
            while ((l = inputStream.read(cache)) > -1) {
                stringBuilder.append(cache, 0, l);
            }
        }
        return stringBuilder;
    }
}
