package net.time4tea.testdata;

import net.time4tea.Affirm;
import net.time4tea.Annotation;

public class TestE {
    public void localVariableAnnotated() {
        @Annotation String var = new String();
        Affirm.affirmSomeCrap(var, var);
    }
}
