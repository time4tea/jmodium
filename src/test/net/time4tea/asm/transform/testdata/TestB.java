package net.time4tea.asm.transform.testdata;

import net.time4tea.asm.transform.Affirm;

public class TestB {

    @SuppressWarnings("UnusedDeclaration")

    public void simpleMethodWithSomeOtherCodeInIt() {
        String s = new String("s");
        Affirm.affirmSomeCrap(new Object(), new Object());
        System.out.println(s);
    }
}
