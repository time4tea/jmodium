package net.time4tea.asm.transform.voidmethodremover;

import net.time4tea.asm.transform.Affirm;

public class TestD {
    @SuppressWarnings("UnusedDeclaration")
    public void differentMethodsSomeWeWantSomeWeDont() {
        Affirm.affirmSomeCrap(System.out, System.in);
        Affirm.someCrapWeWant("foo", "bar");
    }

    @SuppressWarnings("UnusedDeclaration")
    public void differentMethodsSomeWeWantSomeWeDontExpectedResult() {
        Affirm.someCrapWeWant("foo", "bar");
    }
}
