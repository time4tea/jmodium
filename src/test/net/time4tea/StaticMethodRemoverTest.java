package net.time4tea;

import com.google.common.base.Predicate;
import net.time4tea.testdata.TestA;
import net.time4tea.testdata.TestB;
import net.time4tea.testdata.TestC;
import net.time4tea.testdata.TestD;
import net.time4tea.testdata.TestE;
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

    private File outputFile;

    @Before
    public void setup() throws Exception {
        outputFile = folder.newFile();
    }

    @Test
    public void removesMethodWhenItIsTheOnlyThingInAMethod() throws Exception {

        StaticMethodRemover remover = new StaticMethodRemover(
                CodeLocation.sourceFileFor(TestA.class),
                outputFile
        );

        remover.remove(affirmMethod());

        assertThat(new MethodTextifier(outputFile).codeFor("simpleMethodCallingVoidFunction"), equalTo(lines(
                "RETURN"
        )));
    }

    @Test
    public void removesMethodWithCodeSurroundingIt() throws Exception {

        StaticMethodRemover remover = new StaticMethodRemover(
                CodeLocation.sourceFileFor(TestB.class),
                outputFile
        );

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
        StaticMethodRemover remover = new StaticMethodRemover(
                CodeLocation.sourceFileFor(TestC.class),
                outputFile
        );

        remover.remove(affirmMethod());

        assertThat(new MethodTextifier(outputFile).codeFor("callingTheMethodUsingLocalVariables"), equalTo(lines(
                "LDC \"foo\"",
                "ASTORE 1",
                "RETURN"
        )));
    }

    @Test
    public void leavesMethodsWeWant() throws Exception {
        StaticMethodRemover remover = new StaticMethodRemover(
                CodeLocation.sourceFileFor(TestD.class),
                outputFile
        );

        remover.remove(affirmMethod());

        assertThat(new MethodTextifier(outputFile).codeFor("differentMethodsSomeWeWantSomeWeDont"), equalTo(lines(
                "LDC \"foo\"",
                "LDC \"bar\"",
                "INVOKESTATIC net/time4tea/Affirm.someCrapWeWant (Ljava/lang/Object;Ljava/lang/Object;)V",
                "RETURN"
        )));
    }

    @Test
    public void leavesLocalVariableAnnotations() throws Exception {
        StaticMethodRemover remover = new StaticMethodRemover(
                CodeLocation.sourceFileFor(TestE.class),
                outputFile
        );

        remover.remove(affirmMethod());

        assertThat(new MethodTextifier(outputFile).codeFor("localVariableAnnotated"), equalTo(lines(
                "NEW java/lang/String",
                "DUP",
                "INVOKESPECIAL java/lang/String.<init> ()V",
                "ASTORE 1",
                "RETURN"
        )));
    }

    private Predicate<MethodSignature> affirmMethod() {
        return new Predicate<MethodSignature>() {
            @Override
            public boolean apply(MethodSignature item) {
                return item.owner.equals("net/time4tea/Affirm") &&
                        item.name.equals("affirmSomeCrap");
            }
        };
    }
}
