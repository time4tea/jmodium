package net.time4tea.asm.transform.adapter;

import org.objectweb.asm.ClassVisitor;

public interface AdapterChain {
    ClassVisitor insertInto(ClassVisitor visitor);
}
