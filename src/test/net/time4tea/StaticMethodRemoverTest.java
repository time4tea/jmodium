package net.time4tea;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.io.File;

public class StaticMethodRemoverTest {

    public void foo() {
        String s= new String("s");
        Affirm.affirmSomeCrap(new Object(), new Object());
        System.out.println(s);
    }

    public static void main(String[] args) {
        new StaticMethodRemoverTest().foo();
    }

    @Test
    public void removesSystemOutPrintlnWhenItIsTheOnlyThingInAMethod() throws Exception {

        File file = CodeLocation.sourceFileFor(StaticMethodRemoverTest.class);
        
        StaticMethodRemover remover = new StaticMethodRemover(file);

        remover.remove(new TypeSafeMatcher<MethodSignature>() {
            @Override
            protected boolean matchesSafely(MethodSignature item) {
                return item.owner.equals("net/time4tea/Affirm") &&
                        item.name.equals("affirmSomeCrap");
            }

            @Override
            public void describeTo(Description description) {
                // don't care
            }
        });

        String text = new MethodTextifier(file).codeFor("foo");

        System.out.println("text = " + text);

    }

}
