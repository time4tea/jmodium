package net.time4tea.asm.transform.testdata;

import net.time4tea.asm.transform.Affirm;

public class TestG {
    @SuppressWarnings("UnusedDeclaration")
    public String aTryCatchBlock() {
        String var = new String();
        try {
            Affirm.affirmSomeCrap(var, var);
            maybeThrowException();
            return "B";
        } finally {
            Affirm.affirmSomeCrapReturnString(var, var);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public String aTryCatchBlockExpectedResult() {
        String var = new String();
        try {
            maybeThrowException();
            return "B";
        } finally {
            Affirm.affirmSomeCrapReturnString(var, var);
        }
    }



    private void maybeThrowException() {
        if ( Math.random() > 0.5 ) {
            throw new RuntimeException("foo");
        }
    }

    public static void main(String[] args) {
        new TestG().aTryCatchBlock();
    }
}
