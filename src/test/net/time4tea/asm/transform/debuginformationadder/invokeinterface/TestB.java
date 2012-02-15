package net.time4tea.asm.transform.debuginformationadder.invokeinterface;

public class TestB {

    Logger logger;

    @SuppressWarnings("UnusedDeclaration")
    public void aBuggyMethod(String foo, Integer bar) {
                logger.debug("got balance");
    }
}
