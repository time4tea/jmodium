package net.time4tea.asm.transform;

import net.time4tea.CodeLocation;
import net.time4tea.asm.transform.adapter.AdapterChain;
import net.time4tea.asm.transform.adapter.ClassAdapter;
import net.time4tea.asm.transform.staticaccesschanger.TestA;
import net.time4tea.asm.transform.trace.MethodTextifier;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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
    public void swapsSystemOutForADifferentFieldInDifferentClass() throws Exception {

        File input = CodeLocation.sourceFileFor(TestA.class);

        MethodTextifier result = adapt(
                input, new SystemOutPrintlnAdapter(TestA.Bob.class.getField("stream"))
        );

        assertThat(result.codeFor("printer"),
                equalTo(new MethodTextifier(input).codeFor("printerExpectedResult"))
        );
    }

    private MethodTextifier adapt(File input, AdapterChain adapterChain) throws Exception {
        ClassAdapter adapter = new ClassAdapter(input, outputFile);
        adapter.adaptWith(adapterChain);
        return new MethodTextifier(outputFile);
    }
}
