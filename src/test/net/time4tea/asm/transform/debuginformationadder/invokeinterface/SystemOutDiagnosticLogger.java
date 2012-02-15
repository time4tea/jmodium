package net.time4tea.asm.transform.debuginformationadder.invokeinterface;

public class SystemOutDiagnosticLogger extends OriginalLogger implements Logger {
    public void debug(String s1, Throwable t, String className, String method, int linenumber) {
        System.out.println("gra!");
    }

    public void debug(String s1, String className, String method, int linenumber) {
        System.out.println("yo mama!");
    }
}
