package net.time4tea.asm.transform.predicate;

import com.google.common.base.Predicate;
import net.time4tea.asm.transform.MemberSignature;

public class ParameterCountPredicate implements Predicate<MemberSignature> {

    private Predicate<Integer> name;

    public ParameterCountPredicate(Predicate<Integer> name) {
        this.name = name;
    }

    @Override
    public boolean apply(MemberSignature memberSignature) {
        return name.apply(memberSignature.argumentTypes().length);
    }

    public static Predicate<MemberSignature> parameterCount(Predicate<Integer> name) {
        return new ParameterCountPredicate(name);
    }
}
