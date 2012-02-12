package net.time4tea.asm.transform;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AsmReader {
    private final int flags;
    private final InputStream stream;

    public AsmReader(InputStream input, int flags) {
        this.stream = input;
        this.flags = flags;
    }
    
    public AsmReader(File file, int flags) throws FileNotFoundException {
        this.flags = flags;
        this.stream = new FileInputStream(file);
    }

    public void readWith(ClassVisitor visitor) throws IOException {
        try {
            ClassReader cr = new ClassReader(stream);
            cr.accept(visitor, flags);
        } finally {
            stream.close();
        }
    }
}
