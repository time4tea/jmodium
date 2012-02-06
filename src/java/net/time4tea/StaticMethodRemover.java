package net.time4tea;

import com.google.common.base.Predicate;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StaticMethodRemover {

    private final File input;
    private final File output;
    private final ClassWriter parent;

    public StaticMethodRemover(File input, File output) {
        this.input = input;
        this.output = output;
        this.parent = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    }

    public void remove(Predicate<MethodSignature> matcher) throws IOException {
        new AsmReader(input, 0).readWith(
                new StaticMethodRemoverClassVisitor(
                        new CheckClassAdapter(parent)
                        , matcher)
        );
        byte[] bytes = parent.toByteArray();
        FileOutputStream stream = new FileOutputStream(output);
        try {
            stream.write(bytes);
        } finally {
            stream.close();
        }
    }
}
