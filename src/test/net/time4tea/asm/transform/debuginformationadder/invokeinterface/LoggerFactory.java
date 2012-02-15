package net.time4tea.asm.transform.debuginformationadder.invokeinterface;

public class LoggerFactory {
    static Logger getLogger() {
        return new OriginalLogger();
    }
}
