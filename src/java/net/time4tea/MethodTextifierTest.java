package net.time4tea;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MethodTextifierTest {


    public static void foo() {
        System.out.println("xx");
    }


    @Test
    public void convertsOnlySelectedMethodsToText() throws Exception {

        File file = CodeLocation.sourceFileFor(getClass());

        assertThat(new MethodTextifier(file).codeFor("foo"), equalTo(""));

    }


}
