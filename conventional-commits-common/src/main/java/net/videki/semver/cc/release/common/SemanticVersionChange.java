package net.videki.semver.cc.release.common;

/**
 * Semantic version change types.
 * The top most of these is returned when parsing the commits since the commits after the last scm tag.
 */
public enum SemanticVersionChange {
  /**
   * Major version bump needed.
   */
  MAJOR,
  /**
   * Minor version bump is needed.
   */
  MINOR,
  /**
   * Patch version bump is needed.
   */
  PATCH,
  /**
   * No version bump is needed.
   */
  NONE
}
