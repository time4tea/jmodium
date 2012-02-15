package net.time4tea.asm.transform;

import com.google.common.base.Predicate;
import com.google.common.collect.Ranges;
import net.time4tea.CodeLocation;
import net.time4tea.asm.transform.adapter.AdapterChain;
import net.time4tea.asm.transform.adapter.ClassAdapter;
import net.time4tea.asm.transform.debuginformationadder.TestA;
import net.time4tea.asm.transform.debuginformationadder.invokeinterface.Logger;
import net.time4tea.asm.transform.debuginformationadder.invokeinterface.TestB;
import net.time4tea.asm.transform.trace.MethodTextifier;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.objectweb.asm.ClassVisitor;

import java.io.File;

import static com.google.common.base.Predicates.and;
import static net.time4tea.asm.transform.predicate.ClassMemberNamedPredicate.methodIs;
import static net.time4tea.asm.transform.predicate.ClassNamedPredicate.classIs;
import static net.time4tea.asm.transform.predicate.ParameterCountPredicate.parameterCount;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class InvocationChangerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File outputFile;

    @Before
    public void setup() throws Exception {
        outputFile = folder.newFile();
    }

    @Test
    public void addsLineNumberInformationToSpecifiedMethod() throws Exception {

        File input = CodeLocation.sourceFileFor(TestA.class);

        MethodTextifier result = adaptMethodCalls(input,
                classIs(TestA.OriginalLogger.class),
                new DiagnosticsAddingMangler(new SameMethodDifferentClassSelector(TestA.DiagnosticLogger.class)));

        assertThat(result.codeFor("aBuggyMethod"),
                equalTo(new MethodTextifier(input).codeFor("aBuggyMethodExpectedResult"))
        );
    }

    @Test
    public void canChangeInterfaceInvoked() throws Exception {
        File input = CodeLocation.sourceFileFor(TestB.class);
        File expected = CodeLocation.sourceFileFor(TestB.class);

        Predicate<MemberSignature> debug = and(classIs(Logger.class), methodIs("debug"), parameterCount(Ranges.lessThan(3)));
        MethodTextifier result = adaptMethodCalls(input,
                debug,
                        new DiagnosticsAddingMangler(new SameMethodDifferentClassSelector(Logger.class)));

        assertThat(result.codeFor("aBuggyMethod"),
                equalTo(new MethodTextifier(expected).codeFor("aBuggyMethodExpectedResult"))
        );
    }

    private MethodTextifier adaptMethodCalls(File input,
                                             final Predicate<MemberSignature> predicate,
                                             final Mangler mangler) throws Exception {
        ClassAdapter adapter = new ClassAdapter(input, outputFile);

        adapter.adaptWith(new AdapterChain() {
            @Override
            public ClassVisitor insertInto(ClassVisitor visitor) {
                return new InvocationChanger(visitor, predicate, mangler);
            }
        });

        return new MethodTextifier(outputFile);
    }
}
