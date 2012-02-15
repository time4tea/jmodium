package net.time4tea.asm.transform.debuginformationadder.invokeinterface;

public class OriginalLogger implements Logger {

    public void debug(String s1, Throwable t) {
        System.out.println("OriginalLogger.debug");
    }
    public void debug(String s1) {
        System.out.println("OriginalLogger.debug");
    }

    @Override
    public void debug(String s1, Throwable t, String className, String method, int linenumber) {
        System.out.println("OriginalLogger.debug");
    }

    @Override
    public void debug(String s1, String className, String method, int linenumber) {
        System.out.println("OriginalLogger.debug");
    }
}
