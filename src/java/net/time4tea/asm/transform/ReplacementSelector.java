package net.time4tea.asm.transform;

import net.time4tea.AccessibleSignature;

public interface ReplacementSelector {
    AccessibleSignature replacementFor(AccessibleSignature existingSignature);
}
