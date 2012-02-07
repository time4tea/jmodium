package net.time4tea.asm.transform.voidmethodremover;

import net.time4tea.asm.transform.Affirm;

public class TestH {

    private static final Logger logger = new Logger();
    
    public static class Logger {
        void error(String crap, String morecrap, Throwable t) {
            System.out.println("argh! my tummy");
        }
    }
    
    @SuppressWarnings("UnusedDeclaration")
    public String nonStaticInvocation() {
        String var = new String();
        try {
            logger.error("must", "log more!", new Exception());
            maybeThrowException();
            return "B";
        } finally {
            Affirm.affirmSomeCrapReturnString(var, var);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public String nonStaticInvocationExpectedResult() {
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
}
