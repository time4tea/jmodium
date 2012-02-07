package net.time4tea.asm.transform.voidmethodremover;

import net.time4tea.asm.transform.Affirm;

public class TestB {

    @SuppressWarnings("UnusedDeclaration")
    public void simpleMethodWithSomeOtherCodeInIt() {
        String s = new String("s");
        Affirm.affirmSomeCrap(new Object(), new Object());
        System.out.println(s);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void simpleMethodWithSomeOtherCodeInItExpectedResult() {
        String s = new String("s");
        System.out.println(s);
    }
}
