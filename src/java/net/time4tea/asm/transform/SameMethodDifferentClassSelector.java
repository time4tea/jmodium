package net.time4tea.asm.transform;

import net.time4tea.asm.transform.adapter.AdapterRuntimeException;
import org.objectweb.asm.Type;

public class SameMethodDifferentClassSelector implements ReplacementSelector {
    private final Class<?> classToInvoke;

    public SameMethodDifferentClassSelector(Class<?> classToInvoke) {
        this.classToInvoke = classToInvoke;
    }

    @Override
    public MemberSignature replacementFor(MemberSignature existingSignature) {
        try {
            return new MemberSignature(existingSignature.asMethodOnClass(
                        classToInvoke,
                        String.class,
                        String.class,
                        int.class
                ));
        } catch (ClassNotFoundException e) {
            throw new AdapterRuntimeException("Unable to find replacement class", e);
        } catch (NoSuchMethodException e) {
            throw new AdapterRuntimeException("Unable to find replacement method", e);
        }
    }
}
