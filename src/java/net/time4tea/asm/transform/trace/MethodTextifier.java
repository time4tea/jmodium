package net.time4tea.asm.transform.trace;

import net.time4tea.asm.transform.AsmReader;
import org.hamcrest.Matcher;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Textifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.Matchers.equalTo;

public class MethodTextifier {

    private final AsmReader reader;
    private final File file;

    public MethodTextifier(File f) {
        file = f;
        try {
            reader = new AsmReader(file, ClassReader.SKIP_DEBUG);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("crappy hack",e);
        }
    }

    public String codeFor(String method) throws IOException {
        MT visitor = new MT(equalTo(method));
        reader.readWith(visitor);
        if ( ! visitor.found ) {
            throw new IOException("File " + file + " didn't contain method " + method);
        }
        return visitor.result();
    }

    private static class MT extends ClassVisitor {

        Textifier textifier = new Textifier();

        private String result;

        private Matcher<String> nameMatcher;

        private boolean found = false;

        public MT(Matcher<String> name) {
            super(Opcodes.ASM4);
            this.nameMatcher = name;
        }

        public String result() {
            return result;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (nameMatcher.matches(name)) {
                found = true;
                return new TraceMethodVisitor(textifier){
                    @Override
                    public void visitMaxs(int maxStack, int maxLocals) {

                    }
                };
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

        @Override
        public void visitEnd() {
            StringWriter writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            textifier.print(pw);
            pw.flush();

            String output = writer.toString();

            result = output.replaceAll("(?m)^\\s+","");


        }
    }
}
