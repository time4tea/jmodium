package net.time4tea;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.security.CodeSource;

import static org.hamcrest.Matchers.equalTo;

public class MethodTextifier {


    public static void foo() {
        System.out.println("xx");
    }


    @Test
    public void convertsOnlySelectedMethodsToText() throws Exception {

        AsmReader reader = new AsmReader(sourceFileFor(getClass()));

        reader.readWith(new ClassVisitor(Opcodes.ASM4) {

            Textifier textifier = new Textifier();

            Matcher<String> matcher = equalTo("foo");

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if (matcher.matches(name)) {
                    return new TraceMethodVisitor(textifier);
                }
                return super.visitMethod(access, name, desc, signature, exceptions);
            }

            @Override
            public void visitEnd() {
                PrintWriter printWriter = new PrintWriter(System.out);
                textifier.print(printWriter);
                printWriter.flush();
            }
        });


    }

    private File sourceFileFor(Class<?> aClass) {
        CodeSource codeSource = aClass.getProtectionDomain().getCodeSource();
        URL location = codeSource.getLocation();
        System.out.println("location = " + location);
        return new File(location.getFile(), aClass.getName().replaceAll("\\.", "/") + ".class");
    }
}
