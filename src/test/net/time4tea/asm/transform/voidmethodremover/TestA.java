package net.time4tea.asm.transform.voidmethodremover;

import net.time4tea.asm.transform.Affirm;

public class TestA {
    @SuppressWarnings("UnusedDeclaration")
    public void simpleMethodCallingVoidFunction() {
        Affirm.affirmSomeCrap(new Object(), new Object());
    }

    @SuppressWarnings("UnusedDeclaration")
    public void simpleMethodCallingVoidFunctionExpectedResult() {
    }

}
