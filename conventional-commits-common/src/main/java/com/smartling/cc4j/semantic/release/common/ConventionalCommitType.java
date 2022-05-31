package com.smartling.cc4j.semantic.release.common;

import java.util.Arrays;
import java.util.List;

public enum ConventionalCommitType implements Comparable<ConventionalCommitType>
{
    BREAKING_CHANGE(SemanticVersionChange.MAJOR, "", "!"),
    BUILD(SemanticVersionChange.NONE, "build"),
    CHORE(SemanticVersionChange.MINOR, "chore"),
    CI(SemanticVersionChange.NONE, "ci"),
    DOCS(SemanticVersionChange.PATCH, "docs"),
    FIX(SemanticVersionChange.PATCH, "fix"),
    FEAT(SemanticVersionChange.MINOR, "feat"),
    REFACTOR(SemanticVersionChange.MINOR, "refactor"),
    STYLE(SemanticVersionChange.NONE, "style"),
    TEST(SemanticVersionChange.NONE, "test");

    private final List<String> commitTypes;
    private final SemanticVersionChange changeType;

    ConventionalCommitType(final SemanticVersionChange change, final String... commitTypes)
    {
        this.commitTypes = Arrays.asList(commitTypes);
        this.changeType = change;
    }

    public SemanticVersionChange getChangeType()
    {
        return changeType;
    }

    public List<String> getCommitTypes()
    {
        return commitTypes;
    }
}
