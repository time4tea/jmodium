package net.time4tea.asm.transform.testdata;

import net.time4tea.asm.transform.Affirm;

public class TestC {
    @SuppressWarnings("UnusedDeclaration")

    public void callingTheMethodUsingLocalVariables() {
        String s = "foo";
        Affirm.affirmSomeCrap(s, s);
    }

}