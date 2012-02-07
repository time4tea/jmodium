package net.time4tea.asm.transform;

public class Affirm {
    
    
    public static void affirmSomeCrap(Object arg, Object arg2) {
        System.out.println("Affirm.affirmSomeCrap");
    }

    public static String affirmSomeCrapReturnString(Object arg, Object arg2) {
        System.out.println("Affirm.returnString");
        return "";
    }

    public static void someCrapWeWant(Object foo, Object bar) {
        System.out.println(foo);
        System.out.println(bar);
    }
}
