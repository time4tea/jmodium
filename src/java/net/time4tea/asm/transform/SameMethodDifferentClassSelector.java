package net.time4tea.asm.transform;

import net.time4tea.MethodSignature;

public class SameMethodDifferentClassSelector implements ReplacementMethodSelector {
    private final Class<?> classToInvoke;

    public SameMethodDifferentClassSelector(Class<?> classToInvoke) {
        this.classToInvoke = classToInvoke;
    }

    @Override
    public MethodSignature replacementFor(MethodSignature existingSignature) {
        return new MethodSignature(
                classToInvoke.getName().replaceAll("\\.", "/"),
                existingSignature.methodName(),
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V"
        );
    }
}
