package net.time4tea.asm.transform.adapter;

import net.time4tea.asm.transform.AsmReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ClassAdapter {

    private final InputStream input;
    private final ClassWriter parent;
    private final OutputStream output;

    public ClassAdapter(File input, File output) throws FileNotFoundException {
        this(new FileInputStream(input), new FileOutputStream(output));
    }

    public ClassAdapter(InputStream input, OutputStream output) {
        this.input = input;
        this.output = output;
        this.parent = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    }

    public void adaptWith(AdapterChain adapterChain) throws IOException, AdapterException {
        CheckClassAdapter adapter = new CheckClassAdapter(parent);
        try {
            new AsmReader(input, 0).readWith(
                    adapterChain.insertInto(adapter)
            );
            try {
                output.write(parent.toByteArray());
            } finally {
                output.close();
            }
        } catch (AdapterRuntimeException e) {
            throw e.cause();
        } finally {
            input.close();
        }
    }

}
