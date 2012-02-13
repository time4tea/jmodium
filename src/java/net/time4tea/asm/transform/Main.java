package net.time4tea.asm.transform;

import net.time4tea.asm.transform.adapter.AdapterChain;
import net.time4tea.asm.transform.adapter.AdapterException;
import net.time4tea.asm.transform.jar.JarAdapter;
import org.objectweb.asm.ClassVisitor;

import java.io.File;
import java.io.IOException;

public class Main {

    private static class NoChanges implements AdapterChain {
        @Override
        public ClassVisitor insertInto(ClassVisitor visitor) {
            return visitor;
        }
    }

    public static void main(String[] args) throws AdapterException, IOException {

        JarAdapter adapter =
                new JarAdapter(
                        new File("testdata/applet.jar"),
                        new File("testdata/applet-after.jar"),
                        new NoChanges()
                );
        adapter.adapt();
    }
}
