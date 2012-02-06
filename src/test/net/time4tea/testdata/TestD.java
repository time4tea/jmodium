package net.time4tea.testdata;

import net.time4tea.Affirm;

public class TestD {
    @SuppressWarnings("UnusedDeclaration")

    public void differentMethodsSomeWeWantSomeWeDont() {
        Affirm.affirmSomeCrap(System.out, System.in);
        Affirm.someCrapWeWant("foo", "bar");
    }
}
