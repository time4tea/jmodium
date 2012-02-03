package net.time4tea;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AsmReader {
    private File file;
    private final int flags;

    public AsmReader(File file, int flags) {
        this.file = file;
        this.flags = flags;
    }

    public void readWith(ClassVisitor visitor) throws IOException {
        FileInputStream stream = new FileInputStream(file);

        try {
            ClassReader cr = new ClassReader(stream);
            cr.accept(visitor, flags);
        } finally {
            stream.close();
        }
    }
}
