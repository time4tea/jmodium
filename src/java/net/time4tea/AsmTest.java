package net.time4tea;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class AsmTest {

    public void foo() {
        Affirm.affirmSomeCrap(new Object(), new Object());
    }

    public static void main(String[] args) throws IOException {


        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor cc = new CheckClassAdapter(cw);
        ClassVisitor tracer = new TraceClassVisitor(cc, new PrintWriter(System.out));
        ClassVisitor cv = new TransformingClassAdapter(tracer);

        new AsmReader(new File("out/production/asm/net/time4tea/ClassToStrip.class")).readWith(cv);
    }

    private static class TransformingClassAdapter extends ClassVisitor {
        public TransformingClassAdapter(ClassVisitor visitor) {
            super(Opcodes.ASM4, visitor);
        }


        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            System.out.println("xxxxxxx  " + name);
            return new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature, exceptions)) {

                int lineNumber = 0;
                boolean avoiding = false;

                @Override
                public void visitLineNumber(int line, Label start) {
                    this.lineNumber = line;
                }

                @Override
                public void visitMethodInsn(int opcode, String owner, String name, String desc) {
                    if (opcode == Opcodes.INVOKESTATIC) {
                        Type[] argumentTypes = Type.getArgumentTypes(desc);
                        System.out.println("returnSizes = " + Arrays.toString(argumentTypes));
                    }

                    super.visitMethodInsn(opcode, owner, name, desc);
//                    if ( !name.equals("affirmSomeCrap")) {
//                        super.visitMethodInsn(opcode, owner, name, desc);
//                    }
//                    else {
//                        avoiding = true;
//                    }
                }

                @Override
                public void visitInsn(int opcode) {
                    if (opcode == Opcodes.POP2 && avoiding) {
                        avoiding = false;
                    } else {
                        super.visitInsn(opcode);
                    }
                }
            };
        }
    }

}
