package net.time4tea.asm.transform;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.time4tea.CodeLocation;
import net.time4tea.MemberSignature;
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

import static net.time4tea.asm.transform.predicate.ClassMemberNamedPredicate.fieldNamed;
import static net.time4tea.asm.transform.predicate.ClassNamedPredicate.classIs;
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

        MethodTextifier result = adaptStaticAccess(
                input,
                Predicates.and(classIs(System.class), fieldNamed("out")),
                new StraightSwapMangler(new ClassFieldSelector(TestA.Bob.class.getField("stream"))));

        assertThat(result.codeFor("printer"),
                equalTo(new MethodTextifier(input).codeFor("printerExpectedResult"))
        );
    }

    private MethodTextifier adaptStaticAccess(File input,
                                              final Predicate<? super MemberSignature> predicate,
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
