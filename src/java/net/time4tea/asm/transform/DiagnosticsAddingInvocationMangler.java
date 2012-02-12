package net.time4tea.asm.transform;

import net.time4tea.MethodSignature;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DiagnosticsAddingInvocationMangler implements InvocationMangler {

    private final ReplacementMethodSelector selector;

    public DiagnosticsAddingInvocationMangler(final Class<?> classToInvoke) {
        this.selector = new SameMethodDifferentClassSelector(classToInvoke);
    }

    @Override
    public void changeInvocation(MethodVisitor visitor, MethodSignature invocation, BytecodeLocation location) {
        visitor.visitLdcInsn(location.className());
        visitor.visitLdcInsn(location.methodName());
        visitor.visitIntInsn(Opcodes.SIPUSH, location.lineNumber());

        MethodSignature replacement = selector.replacementFor(invocation);

        visitor.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                replacement.internalClassName(),
                replacement.methodName(),
                replacement.descriptor()
        );
    }
}
