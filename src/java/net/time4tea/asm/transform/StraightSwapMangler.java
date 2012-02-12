package net.time4tea.asm.transform;

import net.time4tea.asm.transform.adapter.BytecodeLocation;
import org.objectweb.asm.MethodVisitor;

public class StraightSwapMangler implements Mangler {

    private final ReplacementSelector selector;

    public StraightSwapMangler(ReplacementSelector replacement) {
        this.selector = replacement;
    }

    @Override
    public void changeInvocation(int opcode, MethodVisitor visitor, MemberSignature invocation, BytecodeLocation location) {
        MemberSignature replacement = selector.replacementFor(invocation);

        visitor.visitFieldInsn(
                opcode,
                replacement.internalClassName(),
                replacement.name(),
                replacement.descriptor()
        );
    }
}
