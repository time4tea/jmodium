package net.time4tea;

import org.hamcrest.Matcher;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

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
        MT visitor = new MT();
        reader.readWith(visitor);
        return visitor.result();
    }

    private static class MT extends ClassVisitor {

        Textifier textifier = new Textifier();

        private String result;

        Matcher<String> matcher = equalTo("foo");

        public MT() {
            super(Opcodes.ASM4);
        }

        public String result() {
            return result;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (matcher.matches(name)) {
                return new TraceMethodVisitor(textifier);
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

        @Override
        public void visitEnd() {
            StringWriter writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            textifier.print(pw);
            pw.flush();

            result = writer.toString();
        }
    }
}
