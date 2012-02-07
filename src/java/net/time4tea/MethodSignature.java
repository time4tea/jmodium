package net.time4tea;

import org.objectweb.asm.Type;

public class MethodSignature {

    public final String owner;
    public final String name;
    public final String desc;

    public MethodSignature(String owner, String name, String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "MethodSignature{" +
                "owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public Type returnType() {
        return Type.getReturnType(desc);
    }
    
    public String className() {
        return owner.replaceAll("/", ".");
    }
}
