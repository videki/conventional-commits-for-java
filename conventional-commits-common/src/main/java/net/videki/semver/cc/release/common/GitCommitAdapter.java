package net.videki.semver.cc.release.common;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Objects;

/**
 * Git commit adapter.
 */
public class GitCommitAdapter implements CommitAdapter<RevCommit> {
  /**
   * The underlying commit.
   */
  private final RevCommit commit;

  /**
   * Constructor based on the underlying commit.
   *
   * @param commit the specific commit.
   */
  GitCommitAdapter(final RevCommit commit) {
    Objects.requireNonNull(commit, "commit cannot be null");
    this.commit = commit;
  }

  /**
   * Returns the commit message.
   *
   * @return The commit message.
   */
  @Override
  public String getShortMessage() {
    return commit.getShortMessage();
  }

  /**
   * Returns the commit itself.
   *
   * @return The SCM commit itself.
   */
  @Override
  public RevCommit getCommit() {
    return commit;
  }
}
