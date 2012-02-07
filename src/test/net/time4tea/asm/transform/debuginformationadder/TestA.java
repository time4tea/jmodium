package net.time4tea.asm.transform.debuginformationadder;

public class TestA {
    
    boolean fingerscrossed;
    
    public static class OriginalLogger {
        public static void debug(String s1, Throwable t) {
            System.out.println("arg!");
        }
        
        public static void debug(String s1) {
            System.out.println("mum!");
        }
    }
    
    public static class DiagnosticLogger {
        public static void debug(String s1, Throwable t, String className, String method, int linenumber) {
            System.out.println("gra!");
        }

        public static void debug(String s1, String className, String method, int linenumber) {
            System.out.println("yo mama!");
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public double aBuggyMethod(String foo, Integer bar) {
        double bankbalance = 0;
        try {
            if (foo != null && bar != null && fingerscrossed) {
                bankbalance = bar + Math.E;
                OriginalLogger.debug("got balance of " + bankbalance);
            }
        } finally {
            bankbalance--;
        }
        return bankbalance;
    }

    @SuppressWarnings("UnusedDeclaration")
    public double aBuggyMethodExpectedResult(String foo, Integer bar) {
        double bankbalance = 0;
        try {
            if (foo != null && bar != null && fingerscrossed) {
                bankbalance = bar + Math.E;
                DiagnosticLogger.debug("got balance of " + bankbalance, "TestA","aBuggyMethod",33);
            }
        } finally {
            bankbalance--;
        }
        return bankbalance;
    }
}
