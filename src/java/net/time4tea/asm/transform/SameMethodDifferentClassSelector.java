package net.time4tea.asm.transform;

import net.time4tea.AccessibleSignature;

public class SameMethodDifferentClassSelector implements ReplacementSelector {
    private final Class<?> classToInvoke;

    public SameMethodDifferentClassSelector(Class<?> classToInvoke) {
        this.classToInvoke = classToInvoke;
    }

    @Override
    public AccessibleSignature replacementFor(AccessibleSignature existingSignature) {
        return new AccessibleSignature(
                classToInvoke,
                existingSignature.methodName(),
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V"
        );
    }
}
