package net.time4tea.asm.transform.testdata;

import net.time4tea.asm.transform.Affirm;

public class TestA {
    @SuppressWarnings("UnusedDeclaration")
    public void simpleMethodCallingVoidFunction() {
        Affirm.affirmSomeCrap(new Object(), new Object());
    }
}