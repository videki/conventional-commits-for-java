package net.videki.semver.cc.release.common;

import java.util.Arrays;
import java.util.List;

/**
 * Conventional commits accepted commit types.
 */
public enum ConventionalCommitType implements Comparable<ConventionalCommitType> {
  /**
   * A breaking change
   */
  BREAKING_CHANGE(SemanticVersionChange.MAJOR, "", "!"),
  /**
   * Build related changes
   */
  BUILD(SemanticVersionChange.NONE, "build"),
  /**
   * Changes to the build process or auxiliary tools and libraries such as documentation generation
   */
  CHORE(SemanticVersionChange.NONE, "chore"),
  /**
   * CI related changes
   */
  CI(SemanticVersionChange.NONE, "ci"),
  /**
   * Documentation related changes
   */
  DOCS(SemanticVersionChange.PATCH, "docs"),
  /**
   * Issue fix
   */
  FIX(SemanticVersionChange.PATCH, "fix"),
  /**
   * New feature added
   */
  FEAT(SemanticVersionChange.MINOR, "feat"),
  /**
   * Refactoring
   */
  REFACTOR(SemanticVersionChange.MINOR, "refactor"),
  /**
   * Styling changes - where applicable
   */
  STYLE(SemanticVersionChange.NONE, "style"),
  /**
   * Test related changes
   */
  TEST(SemanticVersionChange.NONE, "test");

  /**
   * The defined commit types
   */
  private final List<String> commitTypes;

  /**
   * The change type (major, minor, patch, none) based on the commit messages
   */
  private final SemanticVersionChange changeType;

  /**
   * Constructor to setup the particular commit type.
   *
   * @param change      the change type.
   * @param commitTypes the affecting commit types.
   */
  ConventionalCommitType(final SemanticVersionChange change, final String... commitTypes) {
    this.commitTypes = Arrays.asList(commitTypes);
    this.changeType = change;
  }

  /**
   * Returns the affected change type for the change (which version part to increment).
   *
   * @return the change type.
   */
  public SemanticVersionChange getChangeType() {
    return changeType;
  }

  /**
   * Returns the affecting commit types.
   *
   * @return the commit types.
   */
  public List<String> getCommitTypes() {
    return commitTypes;
  }
}
