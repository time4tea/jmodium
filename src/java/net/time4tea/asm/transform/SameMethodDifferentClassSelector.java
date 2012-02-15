package net.time4tea.asm.transform;

import org.objectweb.asm.Type;

public class SameMethodDifferentClassSelector implements ReplacementSelector {
    private final Class<?> classToInvoke;

    public SameMethodDifferentClassSelector(Class<?> classToInvoke) {
        this.classToInvoke = classToInvoke;
    }

    @Override
    public MemberSignature replacementFor(MemberSignature existingSignature) {

        Type[] types = existingSignature.argumentTypes();

//        Method existingMethod = classToInvoke.getMethod(existingSignature.name())
        
        return new MemberSignature(
                classToInvoke,
                existingSignature.name(),
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V"
        );
    }
}
