package net.time4tea.asm.transform;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.time4tea.asm.transform.adapter.AdapterChain;
import org.objectweb.asm.ClassVisitor;

import java.lang.reflect.Field;

import static net.time4tea.asm.transform.predicate.ClassMemberNamedPredicate.fieldNamed;
import static net.time4tea.asm.transform.predicate.ClassNamedPredicate.classIs;

public class SystemOutPrintlnAdapter implements AdapterChain {

    private final Field field;

    public SystemOutPrintlnAdapter(Field field) {
        this.field = field;
    }

    @Override
    public ClassVisitor insertInto(ClassVisitor visitor) {
        Predicate<? super MemberSignature> predicate = Predicates.and(classIs(System.class), fieldNamed("out"));
        Mangler mangler = new StraightSwapMangler(new ClassFieldSelector(field));
        return new StaticAccessChanger(visitor, predicate, mangler);
    }
}
