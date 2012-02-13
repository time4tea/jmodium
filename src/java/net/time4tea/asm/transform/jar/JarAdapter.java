package net.time4tea.asm.transform.jar;

import com.google.common.io.ByteStreams;
import net.time4tea.asm.transform.adapter.AdapterChain;
import net.time4tea.asm.transform.adapter.AdapterException;
import net.time4tea.asm.transform.adapter.ClassAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

public class JarAdapter {

    private File input;
    private File output;
    private AdapterChain adapter;

    public JarAdapter(File input, File output, AdapterChain adapter) {
        this.input = input;
        this.output = output;
        this.adapter = adapter;
    }

    public void adapt() throws IOException, AdapterException {
        JarInputStream inJar = new JarInputStream(new FileInputStream(input));
        JarOutputStream outJar = new JarOutputStream(new FileOutputStream(output), inJar.getManifest());

        try {
            JarEntry entry;
            while ((entry = inJar.getNextJarEntry()) != null) {

                if (entry.isDirectory()) {
                    outJar.putNextEntry(entry);
                } else if (isCode(entry)) {
                    ByteArrayOutputStream adapted = new ByteArrayOutputStream();
                    new ClassAdapter(new ByteArrayInputStream(ByteStreams.toByteArray(inJar)), adapted).adaptWith(adapter);
                    outJar.putNextEntry(new JarEntry(entry.getName()));
                    outJar.write(adapted.toByteArray());
                } else {
                    outJar.putNextEntry(entry);
                    ByteStreams.copy(inJar, outJar);
                }
            }
        } finally {
            inJar.close();
            outJar.close();
        }
    }

    private boolean isCode(JarEntry entry) {
        return !entry.isDirectory() && entry.getName().endsWith(".class");
    }
}
