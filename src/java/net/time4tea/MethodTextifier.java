package net.time4tea;

import org.hamcrest.Matcher;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Textifier;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.Matchers.equalTo;

public class MethodTextifier {

    private final AsmReader reader;

    public MethodTextifier(File f) {
        reader = new AsmReader(f, ClassReader.SKIP_DEBUG);
    }

    public String codeFor(String method) throws IOException {
        MT visitor = new MT(equalTo(method));
        reader.readWith(visitor);
        return visitor.result();
    }

    private static class MT extends ClassVisitor {

        Textifier textifier = new Textifier();

        private String result;

        private Matcher<String> nameMatcher;

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
