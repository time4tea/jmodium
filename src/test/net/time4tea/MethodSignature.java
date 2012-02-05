package net.time4tea;

public class MethodSignature {

    public final String owner;
    public final String name;
    public final String desc;

    public MethodSignature(String owner, String name, String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }
}
