package net.time4tea.asm.transform;

import net.time4tea.MemberSignature;

public class SameMethodDifferentClassSelector implements ReplacementSelector {
    private final Class<?> classToInvoke;

    public SameMethodDifferentClassSelector(Class<?> classToInvoke) {
        this.classToInvoke = classToInvoke;
    }

    @Override
    public MemberSignature replacementFor(MemberSignature existingSignature) {
        return new MemberSignature(
                classToInvoke,
                existingSignature.name(),
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V"
        );
    }
}
