package net.time4tea.asm.transform.adapter;

public interface BytecodeLocation {
    String className();
    String methodName();
    int lineNumber();
}
