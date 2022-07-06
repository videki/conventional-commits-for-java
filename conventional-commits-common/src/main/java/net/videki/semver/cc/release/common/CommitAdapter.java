package net.videki.semver.cc.release.common;

public interface CommitAdapter<T>
{
    String getShortMessage();

    T getCommit();
}
