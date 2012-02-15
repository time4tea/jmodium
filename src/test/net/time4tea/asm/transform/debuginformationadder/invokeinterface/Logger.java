package net.time4tea.asm.transform.debuginformationadder.invokeinterface;

public interface Logger {
    void debug(String s1, Throwable t);
    void debug(String s1);
    void debug(String s1, Throwable t, String className, String method, int linenumber);
    void debug(String s1, String className, String method, int linenumber);
}
