package net.videki.semver.cc.release.common;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Commit log handler to parse the commit messages of a git repo since the last commit tag.
 */
public class LogHandler {
  /**
   * Internal logger.
   */
  private final Logger logger = LoggerFactory.getLogger(LogHandler.class);

  /**
   * The git repository to be parsed.
   */
  private final Repository repository;

  /**
   * Git repo handler.
   */
  private final Git git;

  /**
   * Constructor to setup the handler for a given git repo.
   *
   * @param repository The git repository.
   */
  public LogHandler(final Repository repository) {
    Objects.requireNonNull(repository, "repository cannot be null");
    this.repository = repository;
    this.git = Git.wrap(repository);
  }

  /**
   * Returns the last commit belonging to the last git tag.
   *
   * @return The last git commit for the last tag.
   * @throws IOException     thrown in case of any IO issues.
   * @throws GitAPIException thrown in case os git-related errors.
   */
  RevCommit getLastTaggedCommit() throws IOException, GitAPIException {
    final List<Ref> tags = git.tagList().call();
    final List<ObjectId> peeledTags = tags.stream().map(t -> repository.peel(t).getPeeledObjectId()).collect(Collectors.toList());
    final PlotWalk walk = new PlotWalk(repository);
    final RevCommit start = walk.parseCommit(repository.resolve("HEAD"));

    walk.markStart(start);

    RevCommit revCommit;
    while ((revCommit = walk.next()) != null) {
      if (peeledTags.contains(revCommit.getId())) {
        logger.debug("Found commit matching last tag: {}", revCommit);
        break;
      }
    }

    walk.close();

    return revCommit;
  }

  /**
   * Returns the list of commits since the last tag.
   * These will be parsed and based on their commit types will the next release version be determined.
   *
   * @return The interested commit list.
   * @throws IOException     thrown in case of any IO issues.
   * @throws GitAPIException thrown in case os git-related errors.
   */
  public Iterable<RevCommit> getCommitsSinceLastTag() throws IOException, GitAPIException {
    final ObjectId start = repository.resolve("HEAD");
    final RevCommit lastCommit = this.getLastTaggedCommit();

    if (lastCommit == null) {
      logger.warn("No annotated tags found matching any commits on branch");
      return git.log().call();
    }

    logger.info("Listing commits in range {}...{}", start.getName(), lastCommit.getId().getName());

    return git.log().addRange(lastCommit.getId(), start).call();
  }

}

