package net.time4tea;

public class StaticMethodRemoverTestData {

    public void simpleMethodCallingVoidFunction() {
        Affirm.affirmSomeCrap(new Object(), new Object());
    }

    public void simpleMethodWithSomeOtherCodeInIt() {
        String s = new String("s");
        Affirm.affirmSomeCrap(new Object(), new Object());
        System.out.println(s);
    }
}
