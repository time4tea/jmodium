package net.time4tea.asm.transform.voidmethodremover;

import net.time4tea.asm.transform.Affirm;

public class TestC {
    @SuppressWarnings("UnusedDeclaration")
    public void callingTheMethodUsingLocalVariables() {
        String s = "foo";
        Affirm.affirmSomeCrap(s, s);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void callingTheMethodUsingLocalVariablesExpectedResult() {
        String s = "foo";
    }

}
