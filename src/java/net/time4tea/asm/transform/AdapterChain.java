package net.time4tea.asm.transform;

import org.objectweb.asm.ClassVisitor;

public interface AdapterChain {
    ClassVisitor insertInto(ClassVisitor visitor);
}
