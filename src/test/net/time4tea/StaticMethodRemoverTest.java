package net.time4tea;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
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

    private File sourceClass = CodeLocation.sourceFileFor(StaticMethodRemoverTestData.class);

    private StaticMethodRemover remover;
    private File outputFile;

    @Before
    public void setup() throws Exception {
        outputFile = folder.newFile();
        remover = new StaticMethodRemover(sourceClass, outputFile);
    }

    @Test
    public void removesMethodWhenItIsTheOnlyThingInAMethod() throws Exception {

        remover.remove(affirmMethod());

        assertThat(new MethodTextifier(outputFile).codeFor("simpleMethodCallingVoidFunction"), equalTo(lines(
                "RETURN"
        )));
    }

    @Test
    public void removesMethodWithCodeSurroundingIt() throws Exception {
        remover.remove(affirmMethod());

        assertThat(new MethodTextifier(outputFile).codeFor("simpleMethodWithSomeOtherCodeInIt"), equalTo(lines(
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
    
    
    @Test
    public void removesMethodUsingLocalVariables() throws Exception {
        remover.remove(affirmMethod());

        assertThat(new MethodTextifier(outputFile).codeFor("callingTheMethodUsingLocalVariables"), equalTo(lines(
                "LDC \"foo\"",
                "ASTORE 1",
                "ALOAD 1",
                "ALOAD 1",
                "RETURN"
        )));
    }

    @Test
    public void leavesMethodsWeWant() throws Exception {
        remover.remove(affirmMethod());

        assertThat(new MethodTextifier(outputFile).codeFor("differentMethodsSomeWeWantSomeWeDont"), equalTo(lines(
                "LDC \"foo\"",
                "LDC \"bar\"",
                "INVOKESTATIC net/time4tea/Affirm.someCrapWeWant (Ljava/lang/Object;Ljava/lang/Object;)V",
                "RETURN"
        )));
    }

    private TypeSafeMatcher<MethodSignature> affirmMethod() {
        return new TypeSafeMatcher<MethodSignature>() {
            @Override
            protected boolean matchesSafely(MethodSignature item) {
                return item.owner.equals("net/time4tea/Affirm") &&
                        item.name.equals("affirmSomeCrap");
            }

            @Override
            public void describeTo(Description description) {
                // don't care
            }
        };
    }
}
