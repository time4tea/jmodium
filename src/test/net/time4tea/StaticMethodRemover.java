package net.time4tea;

import org.hamcrest.Matcher;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StaticMethodRemover {

    private File file;
    private File outputFile;
    private ClassWriter parent;

    public StaticMethodRemover(File inputFile) {
        this.file = inputFile;
        this.parent = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    }

    public void remove(Matcher<MethodSignature> matcher) throws IOException {
        new AsmReader(file, 0).readWith(
                new StaticMethodRemoverClassVisitor(
                        new CheckClassAdapter(parent)
                        , matcher)
        );
        byte[] bytes = parent.toByteArray();
        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(bytes);
        } finally {
            stream.close();
        }
    }
}
