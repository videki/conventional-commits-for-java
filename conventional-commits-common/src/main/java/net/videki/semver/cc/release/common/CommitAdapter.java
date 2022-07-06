package net.videki.semver.cc.release.common;

/**
 * SCM commit msg abstraction.
 *
 * @param <T> The scm-specific commit.
 */
public interface CommitAdapter<T>
{
    String getShortMessage();

    T getCommit();
}
