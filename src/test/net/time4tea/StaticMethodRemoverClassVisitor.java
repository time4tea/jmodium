package net.time4tea;

import org.hamcrest.Matcher;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class StaticMethodRemoverClassVisitor extends ClassVisitor {

    private final List<Delayed> delayed = new ArrayList<Delayed>();
    private final Matcher<MethodSignature> matcher;

    public StaticMethodRemoverClassVisitor(ClassVisitor parent, Matcher<MethodSignature> matcher) {
        super(Opcodes.ASM4, parent);
        this.matcher = matcher;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new MyMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
    }

    private class MyMethodVisitor extends MethodVisitor {
        public MyMethodVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM4, methodVisitor);
        }

        @Override
        public void visitAttribute(final Attribute attr) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitAttribute(attr);
                }
            });
        }

        @Override
        public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitFrame(type, nLocal, local, nStack, stack);
                }
            });
        }

        @Override
        public void visitLabel(final Label label) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitLabel(label);
                }
            });
        }

        @Override
        public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitTryCatchBlock(start, end, handler, type);
                }
            });
        }

        @Override
        public void visitLineNumber(final int line, final Label start) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitLineNumber(line, start);
                }
            });
            replay();
        }

        @Override
        public void visitMaxs(final int maxStack, final int maxLocals) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitMaxs(maxStack, maxLocals);
                }
            });
        }

        @Override
        public void visitCode() {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitCode();
                }
            });
        }

        @Override
        public void visitInsn(final int opcode) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitInsn(opcode);
                }
            });
        }

        @Override
        public void visitIntInsn(final int opcode, final int operand) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitIntInsn(opcode, operand);
                }
            });
        }

        @Override
        public void visitVarInsn(final int opcode, final int var) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitVarInsn(opcode, var);
                }
            });
            replay();
        }

        @Override
        public void visitTypeInsn(final int opcode, final String type) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitTypeInsn(opcode, type);
                }
            });

        }

        @Override
        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitFieldInsn(opcode, owner, name, desc);
                }
            });
        }

        @Override
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
            if (matcher.matches(new MethodSignature(owner, name, desc))) {
                System.out.println("StaticMethodRemoverClassVisitor$MyMethodVisitor.visitMethodInsn");
                delayed.clear();
            } else {
                delayed.add(new Delayed() {
                    @Override
                    public void donow() {
                        MyMethodVisitor.super.visitMethodInsn(opcode, owner, name, desc);
                    }
                });
            }
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

        @Override
        public void visitInvokeDynamicInsn(final String name, final String desc, final Handle bsm, final Object... bsmArgs) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
                }
            });
        }

        @Override
        public void visitJumpInsn(final int opcode, final Label label) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitJumpInsn(opcode, label);
                }
            });
        }

        @Override
        public void visitLdcInsn(final Object cst) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitLdcInsn(cst);
                }
            });
        }

        @Override
        public void visitIincInsn(final int var, final int increment) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitIincInsn(var, increment);
                }
            });
        }

        @Override
        public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitTableSwitchInsn(min, max, dflt, labels);
                }
            });
        }

        @Override
        public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitLookupSwitchInsn(dflt, keys, labels);
                }
            });
        }

        @Override
        public void visitMultiANewArrayInsn(final String desc, final int dims) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitMultiANewArrayInsn(desc, dims);
                }
            });
        }

        @Override
        public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
            delayed.add(new Delayed() {
                @Override
                public void donow() {
                    MyMethodVisitor.super.visitLocalVariable(name, desc, signature, start, end, index);
                }
            });
        }
    }
}
