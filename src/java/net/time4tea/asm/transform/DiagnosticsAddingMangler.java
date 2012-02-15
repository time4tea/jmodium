package net.time4tea.asm.transform;

import net.time4tea.asm.transform.adapter.BytecodeLocation;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DiagnosticsAddingMangler implements Mangler {

    private final ReplacementSelector selector;

    public DiagnosticsAddingMangler(ReplacementSelector replacement) {
        this.selector = replacement;
    }

    @Override
    public void changeInvocation(int opcode, MethodVisitor visitor, MemberSignature invocation, BytecodeLocation location) {
        visitor.visitLdcInsn(location.className());
        visitor.visitLdcInsn(location.methodName());
        pushLineNumber(visitor, location);

        MemberSignature replacement = selector.replacementFor(invocation);

        visitor.visitMethodInsn(
                opcode,
                replacement.internalClassName(),
                replacement.name(),
                replacement.descriptor()
        );
    }

    private void pushLineNumber(MethodVisitor visitor, BytecodeLocation location) {
        int lineNumber = location.lineNumber();
        if (lineNumber < Byte.MAX_VALUE) {
            visitor.visitIntInsn(Opcodes.BIPUSH, lineNumber);
        } else {
            visitor.visitIntInsn(Opcodes.SIPUSH, lineNumber);
        }
    }


}
