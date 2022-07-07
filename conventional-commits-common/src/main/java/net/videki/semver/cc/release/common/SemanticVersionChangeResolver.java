package net.videki.semver.cc.release.common;

import org.eclipse.jgit.revwalk.RevCommit;

/**
 * Generic resolver for version change adapters.
 */
public interface SemanticVersionChangeResolver {
  /**
   * Determine a semantic version change for a commit list.
   *
   * @param commits The commit list.
   * @return The calculated version change need.
   */
  SemanticVersionChange resolveChange(Iterable<RevCommit> commits);
}
