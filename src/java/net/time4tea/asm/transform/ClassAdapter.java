package net.time4tea.asm.transform;

import net.time4tea.AsmReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClassAdapter {

    private final File input;
    private final File output;
    private final ClassWriter parent;

    public ClassAdapter(File input, File output) {
        this.input = input;
        this.output = output;
        this.parent = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    }

    public void adaptWith(AdapterChain adapterChain) throws IOException {
        CheckClassAdapter adapter = new CheckClassAdapter(parent);
        new AsmReader(input, 0).readWith(
                adapterChain.insertInto(adapter)
        );
        byte[] bytes = parent.toByteArray();
        FileOutputStream stream = new FileOutputStream(output);
        try {
            stream.write(bytes);
        } finally {
            stream.close();
        }
    }

    public interface AdapterChain {
        ClassVisitor insertInto(ClassVisitor visitor);
    }
}
