package net.time4tea.asm.transform.testdata;

import net.time4tea.Annotation;
import net.time4tea.asm.transform.Affirm;

public class TestF {
    @SuppressWarnings("UnusedDeclaration")
    public void localVariableAnnotated() {
        @Annotation String var = new String();
        Affirm.affirmSomeCrapReturnString(var, var);
    }
}
