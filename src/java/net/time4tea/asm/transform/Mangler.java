package net.time4tea.asm.transform;

import net.time4tea.AccessibleSignature;
import net.time4tea.asm.transform.adapter.BytecodeLocation;
import org.objectweb.asm.MethodVisitor;

public interface Mangler {
    void changeInvocation(int opcode, MethodVisitor visitor, AccessibleSignature invocation, BytecodeLocation location);
}
