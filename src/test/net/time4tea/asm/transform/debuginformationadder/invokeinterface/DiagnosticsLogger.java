package net.time4tea.asm.transform.debuginformationadder.invokeinterface;

public interface DiagnosticsLogger {
    void debug(String s1, Throwable t, String className, String method, int linenumber);
    void debug(String s1, String className, String method, int linenumber);
}
