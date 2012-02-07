package net.time4tea.asm.transform;

import com.google.common.base.Predicate;
import net.time4tea.MethodSignature;
import net.time4tea.MethodTextifier;
import net.time4tea.asm.transform.voidmethodremover.TestA;
import net.time4tea.asm.transform.voidmethodremover.TestB;
import net.time4tea.asm.transform.voidmethodremover.TestC;
import net.time4tea.asm.transform.voidmethodremover.TestD;
import net.time4tea.asm.transform.voidmethodremover.TestE;
import net.time4tea.asm.transform.voidmethodremover.TestF;
import net.time4tea.asm.transform.voidmethodremover.TestG;
import net.time4tea.asm.transform.voidmethodremover.TestH;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.objectweb.asm.ClassVisitor;

import java.io.File;

import static net.time4tea.CodeLocation.sourceFileFor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class VoidMethodInvocationRemoverTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File outputFile;

    @Before
    public void setup() throws Exception {
        outputFile = folder.newFile();
    }

    @Test
    public void removesMethodWhenItIsTheOnlyThingInAMethod() throws Exception {

        File input = sourceFileFor(TestA.class);
        MethodTextifier processed = removeMethodCalls(input, affirmMethod("affirmSomeCrap"));

        assertThat(processed.codeFor("simpleMethodCallingVoidFunction"),
                equalTo(new MethodTextifier(input).codeFor("simpleMethodCallingVoidFunctionExpectedResult"))
        );
    }

    @Test
    public void removesMethodWithCodeSurroundingIt() throws Exception {

        File input = sourceFileFor(TestB.class);
        MethodTextifier processed = removeMethodCalls(input, affirmMethod("affirmSomeCrap"));

        assertThat(processed.codeFor("simpleMethodWithSomeOtherCodeInIt"),
                equalTo(new MethodTextifier(input).codeFor("simpleMethodWithSomeOtherCodeInItExpectedResult"))
        );
    }

    @Test
    public void removesMethodUsingLocalVariables() throws Exception {

        File input = sourceFileFor(TestC.class);
        MethodTextifier processed = removeMethodCalls(input, affirmMethod("affirmSomeCrap"));

        assertThat(processed.codeFor("callingTheMethodUsingLocalVariables"),
                equalTo(new MethodTextifier(input).codeFor("callingTheMethodUsingLocalVariablesExpectedResult"))
        );
    }

    @Test
    public void leavesMethodsWeWant() throws Exception {

        File input = sourceFileFor(TestD.class);
        MethodTextifier processed = removeMethodCalls(input, affirmMethod("affirmSomeCrap"));

        assertThat(processed.codeFor("differentMethodsSomeWeWantSomeWeDont"),
                equalTo(new MethodTextifier(input).codeFor("differentMethodsSomeWeWantSomeWeDontExpectedResult"))
        );
    }

    @Test
    public void leavesLocalVariableAnnotations() throws Exception {

        File input = sourceFileFor(TestE.class);
        MethodTextifier processed = removeMethodCalls(input, affirmMethod("affirmSomeCrap"));

        assertThat(processed.codeFor("localVariableAnnotated"),
                equalTo(new MethodTextifier(input).codeFor("localVariableAnnotatedExpectedResult"))
        );
    }

    @Test(expected = AdapterException.class)
    public void refusesToRemoveAMethodThatIsNotVoid() throws Exception {
        File input = sourceFileFor(TestF.class);
        removeMethodCalls(input, affirmMethod("affirmSomeCrapReturnString"));
    }

    @Test
    public void removesMethodInTryCatchBlock() throws Exception {

        File input = sourceFileFor(TestG.class);

        MethodTextifier processed = removeMethodCalls(input, affirmMethod("affirmSomeCrap"));

        assertThat(processed.codeFor("aTryCatchBlock"),
                equalTo(new MethodTextifier(input).codeFor("aTryCatchBlockExpectedResult"))
        );
    }

    @Test
    public void removesVoidNonStaticInvocations() throws Exception {

        File input = sourceFileFor(TestH.class);

        MethodTextifier processed = removeMethodCalls(input, new Predicate<MethodSignature>() {
            @Override
            public boolean apply(MethodSignature methodSignature) {
                return methodSignature.className().equals(TestH.Logger.class.getName())
                        && methodSignature.methodName().equals("error");
            }
        });

        assertThat(processed.codeFor("nonStaticInvocation"),
                equalTo(new MethodTextifier(input).codeFor("nonStaticInvocationExpectedResult"))
        );
    }

    private Predicate<MethodSignature> affirmMethod(final String method) {
        return new Predicate<MethodSignature>() {
            @Override
            public boolean apply(MethodSignature item) {
                return item.className().equals(Affirm.class.getName()) &&
                        item.methodName().equals(method);
            }
        };
    }

    private MethodTextifier removeMethodCalls(File input, final Predicate<MethodSignature> predicate) throws Exception {
        ClassAdapter adapter = new ClassAdapter(input, outputFile);

        adapter.adaptWith(new AdapterChain() {
            @Override
            public ClassVisitor insertInto(ClassVisitor visitor) {
                return new VoidMethodInvocationRemover(visitor, predicate);
            }
        });

        return new MethodTextifier(outputFile);
    }
}
