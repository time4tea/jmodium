package net.time4tea;

import org.objectweb.asm.Type;

public class MethodSignature {

    private final String owner;
    private final String name;
    private final String desc;

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
    
    public String descriptor() {
        return desc;
    }

    public String className() {
        return owner.replaceAll("/", ".");
    }

    public String methodName() {
        return name;
    }
}
