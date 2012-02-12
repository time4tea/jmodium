package net.time4tea.asm.transform.jar;

import net.time4tea.asm.transform.adapter.AdapterChain;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.objectweb.asm.ClassVisitor;

import java.io.File;

public class JarAdapterTest {
    
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();
    
    @Test
    public void foo() throws Exception {
        JarAdapter adapter = new JarAdapter(new File("lib/jmock-2.5.1.jar"), temp.newFile(), new NoChanges());
        adapter.adapt();
    }

    private static class NoChanges implements AdapterChain {
        @Override
        public ClassVisitor insertInto(ClassVisitor visitor) {
            return visitor;
        }
    }
}
