package net.time4tea.asm.transform;

import net.time4tea.MethodSignature;
import org.objectweb.asm.MethodVisitor;

public interface InvocationMangler {
    void changeInvocation(MethodVisitor visitor, MethodSignature invocation, BytecodeLocation location);
}
