package net.time4tea.asm.transform;

import net.time4tea.MethodSignature;

public interface ReplacementMethodSelector {
    MethodSignature replacementFor(MethodSignature existingSignature);
}
