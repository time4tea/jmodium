package net.time4tea.asm.transform.testdata;

import net.time4tea.asm.transform.Affirm;

public class TestD {
    @SuppressWarnings("UnusedDeclaration")

    public void differentMethodsSomeWeWantSomeWeDont() {
        Affirm.affirmSomeCrap(System.out, System.in);
        Affirm.someCrapWeWant("foo", "bar");
    }
}
