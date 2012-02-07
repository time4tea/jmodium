package net.time4tea.asm.transform;

public interface BytecodeLocation {
    String className();
    String methodName();
    int lineNumber();
}
