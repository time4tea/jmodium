package net.time4tea.asm.transform;

import com.google.common.base.Predicate;
import net.time4tea.AccessibleSignature;
import net.time4tea.asm.transform.adapter.BytecodeLocation;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class StaticAccessChanger extends ClassVisitor {
    private Predicate<AccessibleSignature> fieldsToChange;
    private Mangler mangler;

    public StaticAccessChanger(ClassVisitor visitor,
                               Predicate<AccessibleSignature> fieldsToChange,
                               Mangler mangler) {
        super(Opcodes.ASM4, visitor);
        this.fieldsToChange = fieldsToChange;
        this.mangler = mangler;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        return new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature, exceptions)) {

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String desc) {

                AccessibleSignature accessibleSignature = new AccessibleSignature(owner, name, desc);

                if ( fieldsToChange.apply(accessibleSignature)) {
                    mangler.changeInvocation(opcode, this, accessibleSignature, new BytecodeLocation() {
                        @Override
                        public String className() {
                            throw new UnsupportedOperationException("james didn't write");
                        }

                        @Override
                        public String methodName() {
                            throw new UnsupportedOperationException("james didn't write");
                        }

                        @Override
                        public int lineNumber() {
                            throw new UnsupportedOperationException("james didn't write");
                        }
                    });
                }
                else {
                    super.visitFieldInsn(opcode, owner, name, desc);
                }
            }
        };
    }
}
