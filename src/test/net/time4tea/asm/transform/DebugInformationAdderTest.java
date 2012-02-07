package net.time4tea.asm.transform;

import com.google.common.base.Predicate;
import net.time4tea.CodeLocation;
import net.time4tea.MethodSignature;
import net.time4tea.MethodTextifier;
import net.time4tea.asm.transform.debuginformationadder.TestA;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.objectweb.asm.ClassVisitor;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DebugInformationAdderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File outputFile;

    @Before
    public void setup() throws Exception {
        outputFile = folder.newFile();
    }

    @Ignore
    @Test
    public void addsLineNumberInformationToSpecifiedMethod() throws Exception {

        File input = CodeLocation.sourceFileFor(TestA.class);

        MethodTextifier result = adaptMethodCalls(input, new Predicate<MethodSignature>() {
            @Override
            public boolean apply(MethodSignature methodSignature) {
                return methodSignature.className().equals(TestA.OriginalLogger.class.getName());
            }
        });

        assertThat(result.codeFor("aBuggyMethod"),
                equalTo(new MethodTextifier(input).codeFor("aBuggyMethodExpectedResult"))
        );
    }

    private MethodTextifier adaptMethodCalls(File input,
                                             final Predicate<MethodSignature> predicate) throws Exception {
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
