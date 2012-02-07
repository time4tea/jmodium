package net.time4tea.asm.transform;

import com.google.common.base.Predicate;
import net.time4tea.MethodSignature;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DebugInformationAdder extends ClassVisitor {
    private Predicate<MethodSignature> methodsToChange;
    private InvocationMangler mangler;
    private MutableBytecodeLocation location;


    public DebugInformationAdder(ClassVisitor visitor, 
                                 Predicate<MethodSignature> methodsToChange, 
                                 InvocationMangler mangler) {
        super(Opcodes.ASM4, visitor);
        this.methodsToChange = methodsToChange;
        this.mangler = mangler;
        this.location = new MutableBytecodeLocation();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        location.className = name.replaceAll("/", ".");
        super.visit(version, access, name, signature, superName, interfaces);
    }

    private static class MutableBytecodeLocation implements BytecodeLocation {
        
        private String className;
        private String methodName;
        private int lineNumber;
        
        @Override
        public String className() {
            return className;
        }

        @Override
        public String methodName() {
            return methodName;
        }

        @Override
        public int lineNumber() {
            return lineNumber;
        }
    }
    
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        location.methodName = name;

        return new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature, exceptions)) {
            @Override
            public void visitLineNumber(int line, Label start) {
                location.lineNumber = line;
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc) {
                MethodSignature methodSignature = new MethodSignature(owner, name, desc);
                if ( methodsToChange.apply(methodSignature)) {
                    mangler.changeInvocation(this, methodSignature, location );
                }
                else {
                    super.visitMethodInsn(opcode, owner, name, desc);
                }
            }
        };
    }
}
