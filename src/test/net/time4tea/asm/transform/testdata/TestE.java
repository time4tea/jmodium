package net.time4tea.asm.transform.testdata;

import net.time4tea.Annotation;
import net.time4tea.asm.transform.Affirm;

public class TestE {
    @SuppressWarnings("UnusedDeclaration")

    public void localVariableAnnotated() {
        @Annotation String var = new String();
        Affirm.affirmSomeCrap(var, var);
    }
}
