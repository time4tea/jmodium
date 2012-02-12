package net.time4tea.asm.transform;

import net.time4tea.AccessibleSignature;

import java.lang.reflect.Field;

public class ClassFieldSelector implements ReplacementSelector {

    private Field field;

    public ClassFieldSelector(Field field) {
        this.field = field;
    }

    @Override
    public AccessibleSignature replacementFor(AccessibleSignature existingSignature) {
        return new AccessibleSignature(
                field.getDeclaringClass(),
                field.getName(),
                existingSignature.descriptor()
        );
    }
}
