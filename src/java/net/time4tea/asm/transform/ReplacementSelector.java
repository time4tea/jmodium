package net.time4tea.asm.transform;

public interface ReplacementSelector {
    MemberSignature replacementFor(MemberSignature existingSignature);
}
