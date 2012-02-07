package net.time4tea.asm.transform;

import com.google.common.base.Predicate;
import net.time4tea.CodeLocation;
import net.time4tea.MethodSignature;
import net.time4tea.MethodTextifier;
import net.time4tea.asm.transform.testdata.TestA;
import net.time4tea.asm.transform.testdata.TestB;
import net.time4tea.asm.transform.testdata.TestC;
import net.time4tea.asm.transform.testdata.TestD;
import net.time4tea.asm.transform.testdata.TestE;
import net.time4tea.asm.transform.testdata.TestF;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.objectweb.asm.ClassVisitor;

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

        removeMethodCalls(TestA.class, affirmMethod("affirmSomeCrap"));

        MatcherAssert.assertThat(new MethodTextifier(outputFile).codeFor("simpleMethodCallingVoidFunction"), equalTo(lines(
                "RETURN"
        )));
    }

    @Test
    public void removesMethodWithCodeSurroundingIt() throws Exception {
        
        removeMethodCalls(TestB.class, affirmMethod("affirmSomeCrap"));
        
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

        removeMethodCalls(TestC.class, affirmMethod("affirmSomeCrap"));

        assertThat(new MethodTextifier(outputFile).codeFor("callingTheMethodUsingLocalVariables"), equalTo(lines(
                "LDC \"foo\"",
                "ASTORE 1",
                "RETURN"
        )));
    }

    @Test
    public void leavesMethodsWeWant() throws Exception {

        removeMethodCalls(TestD.class, affirmMethod("affirmSomeCrap"));

        assertThat(new MethodTextifier(outputFile).codeFor("differentMethodsSomeWeWantSomeWeDont"), equalTo(lines(
                "LDC \"foo\"",
                "LDC \"bar\"",
                "INVOKESTATIC net/time4tea/asm/transform/Affirm.someCrapWeWant (Ljava/lang/Object;Ljava/lang/Object;)V",
                "RETURN"
        )));
    }

    @Test
    public void leavesLocalVariableAnnotations() throws Exception {

        removeMethodCalls(TestE.class, affirmMethod("affirmSomeCrap"));

        assertThat(new MethodTextifier(outputFile).codeFor("localVariableAnnotated"), equalTo(lines(
                "NEW java/lang/String",
                "DUP",
                "INVOKESPECIAL java/lang/String.<init> ()V",
                "ASTORE 1",
                "RETURN"
        )));
    }

    @Test(expected = AdapterException.class)
    public void refusesToRemoveAMethodThatIsNotVoid() throws Exception {
        removeMethodCalls(TestF.class, affirmMethod("affirmSomeCrapReturnString"));
    }

    private Predicate<MethodSignature> affirmMethod(final String method) {
        return new Predicate<MethodSignature>() {
            @Override
            public boolean apply(MethodSignature item) {
                String classId = Affirm.class.getName().replaceAll("\\.", "/");
                return item.owner.equals(classId) &&
                        item.name.equals(method);
            }
        };
    }

    private void removeMethodCalls(Class<?> aClass, final Predicate<MethodSignature> predicate) throws Exception {
        ClassAdapter adapter = new ClassAdapter(
                CodeLocation.sourceFileFor(aClass),
                outputFile
        );

        adapter.adaptWith(new AdapterChain() {
            @Override
            public ClassVisitor insertInto(ClassVisitor visitor) {
                return new StaticMethodRemover(visitor, predicate);
            }
        });
    }
}
