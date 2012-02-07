package net.time4tea.asm.transform;

import net.time4tea.MethodSignature;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DiagnosticsAddingInvocationMangler implements InvocationMangler {

    private final String classToInvokeInternalName;
    private final Class<?> classToInvoke;

    public DiagnosticsAddingInvocationMangler(Class<?> classToInvoke) {
        this.classToInvoke = classToInvoke;
        this.classToInvokeInternalName = classToInvoke.getName().replaceAll("\\.", "/");
    }

    @Override
    public void changeInvocation(MethodVisitor visitor, MethodSignature invocation, BytecodeLocation location) {
        visitor.visitLdcInsn(location.className());
        visitor.visitLdcInsn(location.methodName());
        visitor.visitLdcInsn(location.lineNumber());
        visitor.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                classToInvokeInternalName,
                invocation.methodName(), "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)"
        );
    }
}
