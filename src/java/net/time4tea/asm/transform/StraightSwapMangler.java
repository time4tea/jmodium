package net.time4tea.asm.transform;

import net.time4tea.AccessibleSignature;
import org.objectweb.asm.MethodVisitor;

public class StraightSwapMangler implements Mangler {

    private final ReplacementSelector selector;

    public StraightSwapMangler(ReplacementSelector replacement) {
        this.selector = replacement;
    }

    @Override
    public void changeInvocation(int opcode, MethodVisitor visitor, AccessibleSignature invocation, BytecodeLocation location) {
        AccessibleSignature replacement = selector.replacementFor(invocation);

        visitor.visitFieldInsn(
                opcode,
                replacement.internalClassName(),
                replacement.methodName(),
                replacement.descriptor()
        );
    }
}
