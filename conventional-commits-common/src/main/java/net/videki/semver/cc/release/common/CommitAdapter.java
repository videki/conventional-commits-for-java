package net.videki.semver.cc.release.common;

/**
 * SCM commit msg abstraction.
 *
 * @param <T> The scm-specific commit.
 */
public interface CommitAdapter<T> {
  /**
   * Returns the commit message.
   *
   * @return the commit message.
   */
  String getShortMessage();

  /**
   * Returns the original commit.
   *
   * @return the commit itself.
   */
  T getCommit();
}
