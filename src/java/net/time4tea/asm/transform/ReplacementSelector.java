package net.time4tea.asm.transform;

import net.time4tea.MemberSignature;

public interface ReplacementSelector {
    MemberSignature replacementFor(MemberSignature existingSignature);
}
