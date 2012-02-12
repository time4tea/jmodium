package net.time4tea.asm.transform.predicate;

import com.google.common.base.Predicate;
import net.time4tea.asm.transform.MemberSignature;

import static com.google.common.base.Predicates.equalTo;

public class ClassNamedPredicate implements Predicate<MemberSignature> {
    
    private Predicate<String> name;

    public ClassNamedPredicate(Predicate<String> name) {
        this.name = name;
    }

    @Override
    public boolean apply(MemberSignature memberSignature) {
        return name.apply(memberSignature.className());
    }

    public static Predicate<MemberSignature> classNamed(Predicate<String> name) {
        return new ClassNamedPredicate(name);
    }

    public static Predicate<MemberSignature> classNamed(String name) {
        return new ClassNamedPredicate(equalTo(name));
    }

    public static Predicate<MemberSignature> classIs(Class<?> klass) {
        return new ClassNamedPredicate(equalTo(klass.getName()));
    }
}
