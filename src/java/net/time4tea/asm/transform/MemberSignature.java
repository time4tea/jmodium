package net.time4tea.asm.transform;

import org.objectweb.asm.Type;

import java.lang.reflect.Method;

import static java.lang.System.arraycopy;

public class MemberSignature {

    private final String owner;
    private final String name;
    private final String desc;

    public MemberSignature(Class<?> owner, String name, String desc) {
        this(owner.getName().replaceAll("\\.", "/"), name, desc);
    }
    
    public MemberSignature(String owner, String name, String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    public MemberSignature(Method method) {
        this(method.getDeclaringClass(), method.getName(), Type.getMethodDescriptor(method));
    }

    @Override
    public String toString() {
        return "MemberSignature{" +
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

    public String internalClassName() {
        return owner;
    }

    public String name() {
        return name;
    }

    boolean hasReturnType(Type type) {
        return returnType().equals(type);
    }

    public Type[] argumentTypes() {
        return Type.getArgumentTypes(descriptor());
    }

    public Method asMethod() throws ClassNotFoundException, NoSuchMethodException {
        String className = className();

        return asMethodOnClass(Class.forName(className));
    }

    public Method asMethodOnClass(Class<?> klass, Class<?>... additionalArgs) throws ClassNotFoundException, NoSuchMethodException {
        Type[] types = argumentTypes();

        Class<?>[] classes = new Class<?>[types.length + additionalArgs.length];

        for (int i = 0; i < types.length; i++) {
            classes[i] = Class.forName(types[i].getClassName());
        }

        arraycopy(additionalArgs, 0, classes, types.length, additionalArgs.length);

        return klass.getMethod(name(), classes);
    }

}
