package net.time4tea.asm.transform.predicate;

import com.google.common.base.Predicate;
import net.time4tea.asm.transform.MemberSignature;

import static com.google.common.base.Predicates.equalTo;

public class ClassMemberNamedPredicate implements Predicate<MemberSignature> {

    private Predicate<String> name;

    public ClassMemberNamedPredicate(Predicate<String> name) {
        this.name = name;
    }

    @Override
    public boolean apply(MemberSignature memberSignature) {
        return name.apply(memberSignature.name());
    }

    public static Predicate<MemberSignature> methodNamed(Predicate<String> name) {
        return new ClassMemberNamedPredicate(name);
    }

    public static Predicate<MemberSignature> methodNamed(String name) {
        return new ClassMemberNamedPredicate(equalTo(name));
    }

    public static Predicate<MemberSignature> methodIs(String name) {
        return new ClassMemberNamedPredicate(equalTo(name));
    }

    public static Predicate<MemberSignature> fieldNamed(Predicate<String> name) {
        return new ClassMemberNamedPredicate(name);
    }

    public static Predicate<MemberSignature> fieldNamed(String name) {
        return new ClassMemberNamedPredicate(equalTo(name));
    }
}
