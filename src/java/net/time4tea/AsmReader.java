package net.time4tea;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AsmReader {
    private File file;

    public AsmReader(File file) {

        this.file = file;
    }

    public void readWith(ClassVisitor visitor) throws IOException {
        FileInputStream stream = new FileInputStream(file);

        try {
            ClassReader cr = new ClassReader(stream);
            cr.accept(visitor, ClassReader.SKIP_DEBUG);
        } finally {
            stream.close();
        }
    }
}
