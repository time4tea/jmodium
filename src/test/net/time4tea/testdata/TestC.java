package net.time4tea.testdata;

import net.time4tea.Affirm;

public class TestC {
    @SuppressWarnings("UnusedDeclaration")

    public void callingTheMethodUsingLocalVariables() {
        String s = "foo";
        Affirm.affirmSomeCrap(s, s);
    }

}
