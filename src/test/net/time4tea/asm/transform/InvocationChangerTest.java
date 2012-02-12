package net.time4tea.asm.transform;

import com.google.common.base.Predicate;
import net.time4tea.CodeLocation;
import net.time4tea.asm.transform.trace.MethodTextifier;
import net.time4tea.asm.transform.adapter.AdapterChain;
import net.time4tea.asm.transform.adapter.ClassAdapter;
import net.time4tea.asm.transform.debuginformationadder.TestA;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.objectweb.asm.ClassVisitor;

import java.io.File;

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

        MethodTextifier result = adaptMethodCalls(input, new Predicate<MemberSignature>() {
            @Override
            public boolean apply(MemberSignature methodSignature) {
                return methodSignature.className().equals(TestA.OriginalLogger.class.getName());
            }
        }, new DiagnosticsAddingMangler(new SameMethodDifferentClassSelector(TestA.DiagnosticLogger.class)));

        assertThat(result.codeFor("aBuggyMethod"),
                equalTo(new MethodTextifier(input).codeFor("aBuggyMethodExpectedResult"))
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
