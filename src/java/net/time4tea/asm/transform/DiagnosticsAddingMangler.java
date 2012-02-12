package net.time4tea.asm.transform;

import net.time4tea.AccessibleSignature;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DiagnosticsAddingMangler implements Mangler {

    private final ReplacementSelector selector;

    public DiagnosticsAddingMangler(ReplacementSelector replacement) {
        this.selector = replacement;
    }

    @Override
    public void changeInvocation(int opcode, MethodVisitor visitor, AccessibleSignature invocation, BytecodeLocation location) {
        visitor.visitLdcInsn(location.className());
        visitor.visitLdcInsn(location.methodName());
        visitor.visitIntInsn(Opcodes.SIPUSH, location.lineNumber());

        AccessibleSignature replacement = selector.replacementFor(invocation);

        visitor.visitMethodInsn(
                opcode,
                replacement.internalClassName(),
                replacement.name(),
                replacement.descriptor()
        );
    }
}
