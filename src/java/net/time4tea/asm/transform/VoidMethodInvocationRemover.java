package net.time4tea.asm.transform;

import com.google.common.base.Predicate;
import net.time4tea.AccessibleSignature;
import net.time4tea.asm.transform.adapter.AdapterRuntimeException;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VoidMethodInvocationRemover extends ClassVisitor {

    private final Predicate<AccessibleSignature> predicate;

    public VoidMethodInvocationRemover(ClassVisitor parent, Predicate<AccessibleSignature> predicate) {
        super(Opcodes.ASM4, parent);
        this.predicate = predicate;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new MyMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
    }

    public static interface Delayed {
        void donow();
    }

    public static abstract class DescribableDelayed implements Delayed {
        private final String desc;

        protected DescribableDelayed(Object... descripton) {
            this.desc = Arrays.toString(descripton);
        }

        @Override
        public String toString() {
            return desc;
        }
    }

    private class MyMethodVisitor extends MethodVisitor {
        private final List<Delayed> delayed = new ArrayList<Delayed>();

        public MyMethodVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM4, methodVisitor);
        }

        @Override
        public void visitAttribute(final Attribute attr) {
            delayed.add(new DescribableDelayed("visit attribute",attr) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitAttribute(attr);
                }
            });
        }

        @Override
        public void visitCode() {
            delayed.add(new DescribableDelayed("code") {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitCode();
                }
            });
            replay();
        }

        @Override
        public void visitLabel(final Label label) {
            delayed.add(new DescribableDelayed("label",label) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitLabel(label);
                }
            });
            replay();
        }

        @Override
        public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
            delayed.add(new DescribableDelayed("frame", type, nLocal, local, nLocal, nStack, stack) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitFrame(type, nLocal, local, nStack, stack);
                }
            });
        }

        @Override
        public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
            delayed.add(new DescribableDelayed("trycatch", start,end, handler,type) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitTryCatchBlock(start, end, handler, type);
                }
            });
            replay();
        }

        @Override
        public void visitLineNumber(final int line, final Label start) {
            delayed.add(new DescribableDelayed("linenumber", line, start) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitLineNumber(line, start);
                }
            });
        }

        @Override
        public void visitMaxs(final int maxStack, final int maxLocals) {
            delayed.add(new DescribableDelayed("maxs", maxStack,maxLocals) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitMaxs(maxStack, maxLocals);
                }
            });
        }

        @Override
        public void visitInsn(final int opcode) {
            delayed.add(new DescribableDelayed("insn", opcode) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitInsn(opcode);
                }
            });
        }

        @Override
        public void visitIntInsn(final int opcode, final int operand) {
            delayed.add(new DescribableDelayed("intinsn", opcode, operand) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitIntInsn(opcode, operand);
                }
            });
        }

        @Override
        public void visitVarInsn(final int opcode, final int var) {
            delayed.add(new DescribableDelayed("varissn", opcode, var) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitVarInsn(opcode, var);
                }
            });
        }

        @Override
        public void visitTypeInsn(final int opcode, final String type) {
            delayed.add(new DescribableDelayed("type insn" ,opcode , type) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitTypeInsn(opcode, type);
                }
            });
        }

        @Override
        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
            delayed.add(new DescribableDelayed("fields insn" , opcode , owner , name , desc) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitFieldInsn(opcode, owner, name, desc);
                }
            });
        }

        @Override
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
            AccessibleSignature signature = new AccessibleSignature(owner, name, desc);
            if (predicate.apply(signature)) {
                    assertAbleToRemove(signature);

                    delayed.clear();
            } else {
                delayed.add(new DescribableDelayed("method ", owner, name, desc) {
                    @Override
                    public void donow() {
                        MyMethodVisitor.super.visitMethodInsn(opcode, owner, name, desc);
                    }
                });
            }
        }

        @Override
        public void visitInvokeDynamicInsn(final String name, final String desc, final Handle bsm, final Object... bsmArgs) {
            delayed.add(new DescribableDelayed("invoke dynamic ", name, desc, bsm, Arrays.toString(bsmArgs)) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
                }
            });
            replay();
        }

        @Override
        public void visitJumpInsn(final int opcode, final Label label) {
            delayed.add(new DescribableDelayed("jump ", label) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitJumpInsn(opcode, label);
                }
            });
        }

        @Override
        public void visitLdcInsn(final Object cst) {
            delayed.add(new DescribableDelayed("ldc ", cst) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitLdcInsn(cst);
                }
            });
        }

        @Override
        public void visitIincInsn(final int var, final int increment) {
            delayed.add(new DescribableDelayed("iinc ", var, increment) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitIincInsn(var, increment);
                }
            });
        }

        @Override
        public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
            delayed.add(new DescribableDelayed("tableswitch", min, max, dflt,labels) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitTableSwitchInsn(min, max, dflt, labels);
                }
            });
        }

        @Override
        public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
            delayed.add(new DescribableDelayed("lookupswitch", dflt, keys, labels) {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitLookupSwitchInsn(dflt, keys, labels);
                }
            });
        }

        @Override
        public void visitMultiANewArrayInsn(final String desc, final int dims) {
            delayed.add(new DescribableDelayed("multianewarray") {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitMultiANewArrayInsn(desc, dims);
                }
            });
        }

        @Override
        public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
            delayed.add(new DescribableDelayed("localvariable") {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitLocalVariable(name, desc, signature, start, end, index);
                }
            });
        }

        @Override
        public void visitEnd() {
            replay();
            super.visitEnd();
        }

        private void replay() {
            for (Delayed d : delayed) {
                d.donow();
            }
            delayed.clear();
        }
    }

    private void assertAbleToRemove(AccessibleSignature signature) {
        if (!signature.returnType().equals(Type.VOID_TYPE)) {
            throw new AdapterRuntimeException(
                    "Unable to remove a method with non void signature " + signature
            );
        }
    }

}
