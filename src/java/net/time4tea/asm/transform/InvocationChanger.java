package net.time4tea.asm.transform;

import com.google.common.base.Predicate;
import net.time4tea.AccessibleSignature;
import net.time4tea.asm.transform.adapter.BytecodeLocation;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class InvocationChanger extends ClassVisitor {
    private Predicate<AccessibleSignature> methodsToChange;
    private Mangler mangler;
    private MutableBytecodeLocation location;

    public InvocationChanger(ClassVisitor visitor,
                             Predicate<AccessibleSignature> methodsToChange,
                             Mangler mangler) {
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
                AccessibleSignature accessibleSignature = new AccessibleSignature(owner, name, desc);
                if ( methodsToChange.apply(accessibleSignature)) {
                    mangler.changeInvocation(opcode, this, accessibleSignature, location);
                }
                else {
                    super.visitMethodInsn(opcode, owner, name, desc);
                }
            }
        };
    }
}
