package net.time4tea.asm.transform.debuginformationadder.invokeinterface;

public class TestB {

    boolean fingerscrossed;

    Logger logger;

    @SuppressWarnings("UnusedDeclaration")
    public double aBuggyMethod(String foo, Integer bar) {
        double bankbalance = 0;
        try {
            if (foo != null && bar != null && fingerscrossed) {
                bankbalance = bar + Math.E;
                logger.debug("got balance of " + bankbalance);
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
                logger.debug("got balance of " + bankbalance, "net.time4tea.asm.transform.debuginformationadder.invokeinterface.TestB", "aBuggyMethod", 15);
            }
        } finally {
            bankbalance--;
        }
        return bankbalance;
    }

}
