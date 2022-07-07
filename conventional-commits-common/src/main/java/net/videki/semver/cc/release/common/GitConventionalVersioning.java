package net.videki.semver.cc.release.common;

import net.videki.semver.cc.release.common.scm.ScmApiException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.Objects;

/**
 * Conventional commits version handler facade for a specific git repository.
 * Use this for version resolution and handling of git repositories.
 */
public class GitConventionalVersioning implements ConventionalVersioning {
  /**
   * The SCM repository to work on.
   */
  private final Repository repository;

  /**
   * Constructor to be used with a specific repo.
   *
   * @param repository The repository to be parsed.
   */
  public GitConventionalVersioning(final Repository repository) {
    Objects.requireNonNull(repository, "repository required");
    this.repository = repository;
  }

  /**
   * Returns the configured log handler for behaviourial logging.
   *
   * @return The log handler.
   */
  @Override
  public LogHandler logHandler() {
    return new LogHandler(repository);
  }

  /**
   * Constructs the versioning resolver.
   *
   * @return The versioning resolver.
   */
  private SemanticVersionChangeResolver semanticVersionChangeResolver() {
    return new SimpleSemanticVersionChangeResolver();
  }

  /**
   * Calculates the change type ro calculate the next (release) version based on the commits since the last tag.
   *
   * @return The top-most change type according to the commits.
   * @throws ScmApiException thrown in case os SCM-related errors.
   * @throws IOException     thrown in case of any IO issues.
   */
  @Override
  public SemanticVersionChange getNextVersionChangeType() throws ScmApiException, IOException {
    try {
      final Iterable<RevCommit> commits = logHandler().getCommitsSinceLastTag();
      final SemanticVersionChangeResolver resolver = semanticVersionChangeResolver();

      return resolver.resolveChange(commits);
    } catch (final GitAPIException e) {
      throw new ScmApiException("Git operation failed", e);
    }
  }

  /**
   * Returns the actual release version based on the commits.
   *
   * @param currentVersion The current (semantic) version.
   * @return The release version (semantic version) to be passed over to the maven release plugin.
   * @throws IOException     thrown in case of any IO issues.
   * @throws ScmApiException thrown in case os SCM-related errors.
   */
  @Override
  public SemanticVersion getReleaseVersion(final SemanticVersion currentVersion) throws IOException, ScmApiException {
    final SemanticVersionChange change = this.getNextVersionChangeType();
    return currentVersion.releaseVersion(change);
  }
}
