package net.videki.semver.cc.release.common;

import net.videki.semver.cc.release.common.scm.ScmApiException;

import java.io.IOException;

/**
 * Versioning adapter.
 */
public interface ConventionalVersioning {
  /**
   * Internal log handler adapter for the calculation burndown.
   *
   * @return the confgiured log handler.
   */
  LogHandler logHandler();

  /**
   * Main entry point to determine the next (post-release) development version.
   *
   * @return The semantic version to be used after the next release.
   * @throws ScmApiException thrown in case of SCM-related errors.
   * @throws IOException     thrown in case of any IO-related issues.
   */
  SemanticVersionChange getNextVersionChangeType() throws ScmApiException, IOException;

  /**
   * Main entry point to calculate the next release version based on the current version, the last SCM tag and commits since then.
   *
   * @param currentVersion The current (semantic) version.
   * @return The next (semantic) version based on the changes.
   * @throws IOException     thrown in case of any IO issues.
   * @throws ScmApiException thrown in case os SCM-related errors.
   */
  SemanticVersion getReleaseVersion(SemanticVersion currentVersion) throws IOException, ScmApiException;
}
