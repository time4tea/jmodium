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
    
    public void callingTheMethodUsingLocalVariables() {
        String s = "foo";
        Affirm.affirmSomeCrap(s, s);
    }
    
    public void differentMethodsSomeWeWantSomeWeDont() {
        Affirm.affirmSomeCrap(System.out, System.in);
        Affirm.someCrapWeWant("foo", "bar");
    }
}
