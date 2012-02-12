package net.time4tea.asm.transform;

import java.lang.reflect.Field;

public class ClassFieldSelector implements ReplacementSelector {

    private Field field;

    public ClassFieldSelector(Field field) {
        this.field = field;
    }

    @Override
    public MemberSignature replacementFor(MemberSignature existingSignature) {
        return new MemberSignature(
                field.getDeclaringClass(),
                field.getName(),
                existingSignature.descriptor()
        );
    }
}
