package net.time4tea.asm.transform;

import com.google.common.base.Predicate;
import net.time4tea.AccessibleSignature;
import net.time4tea.CodeLocation;
import net.time4tea.MethodTextifier;
import net.time4tea.asm.transform.adapter.AdapterChain;
import net.time4tea.asm.transform.adapter.ClassAdapter;
import net.time4tea.asm.transform.staticaccesschanger.TestA;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.objectweb.asm.ClassVisitor;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class StaticAccessChangerTest {

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

        MethodTextifier result = adaptStaticAccess(input, new Predicate<AccessibleSignature>() {
            @Override
            public boolean apply(AccessibleSignature methodSignature) {
                return methodSignature.className().equals("java.lang.System") &&
                        methodSignature.name().equals("out");
            }
        }, new StraightSwapMangler(new ClassFieldSelector(TestA.Bob.class.getField("stream"))));

        assertThat(result.codeFor("printer"),
                equalTo(new MethodTextifier(input).codeFor("printerExpectedResult"))
        );
    }

    private MethodTextifier adaptStaticAccess(File input,
                                              final Predicate<AccessibleSignature> predicate,
                                              final Mangler mangler) throws Exception {
        ClassAdapter adapter = new ClassAdapter(input, outputFile);

        adapter.adaptWith(new AdapterChain() {
            @Override
            public ClassVisitor insertInto(ClassVisitor visitor) {
                return new StaticAccessChanger(visitor, predicate, mangler);
            }
        });

        return new MethodTextifier(outputFile);
    }
}
