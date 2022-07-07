package net.videki.semver.cc.release.common;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * SCM commit representation.
 *
 * @author rossillo
 */
public class Commit {
  /**
   * Message pattern to check commit messages for a breaking change
   */
  private static final Pattern BREAKING_REGEX = Pattern.compile(
    "^((build|chore|ci|docs|fix|feat|refactor|style|test)[a-z0-9\\(\\)]*)((\\!([\\s]*:(.|\\n)*))|" +
      "([\\s]*:(.|\\n)*(BREAKING(\\s|-)CHANGE)(.|\\n)*))", Pattern.CASE_INSENSITIVE);

  /**
   * Message pattern to check commit messages for being conventional commits compliant
   */
  private static final Pattern CONVENTIONAL_COMMIT_REGEX = Pattern.compile(
    "^((build|chore|ci|docs|fix|feat|refactor|style|test)[a-z0-9\\(\\)]*)((\\!([\\s]*:" +
      "(.|\\n)*))|([\\s]*:(.|\\n)*))", Pattern.CASE_INSENSITIVE);

  /**
   * The SCM-specific commit
   */
  private final CommitAdapter commit;

  /**
   * Constructor to initialize the commit.
   * @param commit the SCM-specific commit.
   */
  public Commit(final CommitAdapter commit) {
    Objects.requireNonNull(commit, "commit may not be null");
    this.commit = commit;
  }

  /**
   * Returns true if this commit is written in conventional commit style;
   * false otherwise.
   * @return true, if the commit is conventional commits compliant (matches the pattern).
   */
  public boolean isConventional() {
    return this.getCommitType().isPresent();
  }

  /**
   * Returns the commit type.
   * @return the commit type.
   */
  public Optional<ConventionalCommitType> getCommitType() {
    String msg = this.getMessageForComparison();
    ConventionalCommitType type = null;

    if (msg.startsWith("!")) {
      return Optional.empty();
    }

    if (BREAKING_REGEX.matcher(msg).matches()) {
      return Optional.of(ConventionalCommitType.BREAKING_CHANGE);
    }

    if (CONVENTIONAL_COMMIT_REGEX.matcher(msg).matches()) {
      for (final ConventionalCommitType cc : ConventionalCommitType.values()) {
        if (ConventionalCommitType.BREAKING_CHANGE.equals(cc)) {
          continue;
        }

        for (final String t : cc.getCommitTypes()) {
          if (msg.startsWith(t)) {
            type = cc;
            break;
          }
        }
      }
    }

    return Optional.ofNullable(type);
  }

  /**
   * Returns the commit message.
   * @return the commit message.
   */
  private String getMessageForComparison() {
    String msg = commit.getShortMessage();
    return msg != null ? msg.toLowerCase() : "";
  }
}
