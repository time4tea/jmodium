package net.time4tea;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static net.time4tea.MethodTextifierTest.lines;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class StaticMethodRemoverTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File sourceClass = CodeLocation.sourceFileFor(StaticMethodRemoverTest.class);

    public void simpleMethodCallingVoidFunction() {
        String s = new String("s");
        Affirm.affirmSomeCrap(new Object(), new Object());
        System.out.println(s);
    }

    @Test
    public void removesSystemOutPrintlnWhenItIsTheOnlyThingInAMethod() throws Exception {

        File outputFile = folder.newFile();
        StaticMethodRemover remover = new StaticMethodRemover(sourceClass, outputFile);

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

        assertThat(new MethodTextifier(outputFile).codeFor("simpleMethodCallingVoidFunction"), equalTo(lines(
                "NEW java/lang/String",
                "DUP",
                "LDC \"s\"",
                "INVOKESPECIAL java/lang/String.<init> (Ljava/lang/String;)V",
                "ASTORE 1",
                "GETSTATIC java/lang/System.out : Ljava/io/PrintStream;",
                "ALOAD 1",
                "INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V",
                "RETURN"
        )));
    }

    public static void main(String[] args) {
        new StaticMethodRemoverTest().simpleMethodCallingVoidFunction();
    }
}
