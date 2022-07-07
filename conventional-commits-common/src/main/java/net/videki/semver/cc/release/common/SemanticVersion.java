package net.videki.semver.cc.release.common;

import java.util.Locale;
import java.util.Objects;

/**
 * A semantic version representation.
 */
public final class SemanticVersion {
  /**
   * Version suffix for the development version in case of Maven repo layouts.
   */
  public static final String VERSION_SUFFIX_SNAPSHOT = "-SNAPSHOT";

  /**
   * The major version.
   */
  private final Integer major;

  /**
   * The minor version.
   */
  private final Integer minor;

  /**
   * The patch version.
   */
  private final Integer patch;

  /**
   * Constructor to construct a semantic version.
   *
   * @param major The major version part.
   * @param minor The minor version part.
   * @param patch The patch version part.
   */
  public SemanticVersion(final int major, final int minor, final int patch) {
    if (major < 0 || minor < 0 || patch < 0)
      throw new IllegalArgumentException("versions can only contain positive integers");

    this.major = major;
    this.minor = minor;
    this.patch = patch;
  }

  /**
   * Check another object for equality with this.
   *
   * @param o The other object.
   * @return true if the other object is a semantic version and the version parts are equal, false otherwise.
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SemanticVersion that = (SemanticVersion) o;
    return major.equals(that.major) && minor.equals(that.minor) && patch.equals(that.patch);
  }

  /**
   * Hashcode for the class.
   *
   * @return The hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(major, minor, patch);
  }

  /**
   * Returns the major version part of this semantic version.
   *
   * @return The major version part.
   */
  public int getMajor() {
    return major;
  }

  /**
   * Returns the minor part of this semantic version.
   *
   * @return The minor version part.
   */
  public int getMinor() {
    return minor;
  }

  /**
   * Returns the patch version part of this semantic version.
   *
   * @return The patch version part.
   */
  public int getPatch() {
    return patch;
  }

  /**
   * Calculates and returns the next semantic version based on the given change.
   *
   * @param change The change (major, minor...).
   * @return The adjusted semantic version.
   */
  public SemanticVersion releaseVersion(final SemanticVersionChange change) {
    final SemanticVersion nextVersion;

    switch (change) {
      case MAJOR:
        nextVersion = new SemanticVersion(major + 1, 0, 0);
        break;
      case MINOR:
        nextVersion = new SemanticVersion(major, minor + 1, 0);
        break;
      case PATCH:
      case NONE:
        nextVersion = new SemanticVersion(major, minor, patch);
        break;
      default:
        throw new IllegalStateException("Invalid semantic version change");
    }

    return nextVersion;
  }

  /**
   * Calculates and returns the development version to be setup after the next release by the maven release plugin,
   * based on the given change type.
   *
   * @param change The version change (major, minor, ...)
   * @return The calculated version change.
   */
  public SemanticVersion nextDevelopmentVersion(final SemanticVersionChange change) {
    final SemanticVersion nextVersion;

    switch (change) {
      case MAJOR:
        nextVersion = new SemanticVersion(major + 1, 0, 0);
        break;
      case MINOR:
        nextVersion = new SemanticVersion(major, minor + 1, 0);
        break;
      case PATCH:
        nextVersion = new SemanticVersion(major, minor, patch + 1);
        break;
      case NONE:
        nextVersion = new SemanticVersion(major, minor, patch);
        break;
      default:
        throw new IllegalStateException("Invalid semantic version change");
    }

    return nextVersion;
  }

  /**
   * toString for this version.
   *
   * @return The semantic version as string.
   */
  public String toString() {
    return toString(major, minor, patch);
  }

  /**
   * Factory method to parse a string formatted semantic version and construct a semantic version object.
   *
   * @param version The version string.
   * @return The semantic version object, or IllegalArgumentException is thrown.
   */
  public static SemanticVersion parse(final String version) {
    Objects.requireNonNull(version, "version required");
    final String[] parts = version.replace(VERSION_SUFFIX_SNAPSHOT, "").split("\\.");

    if (parts.length != 3)
      throw new IllegalArgumentException("Invalid semantic version: " + version);

    return new SemanticVersion(
      Integer.parseInt(parts[0]),
      Integer.parseInt(parts[1]),
      Integer.parseInt(parts[2])
    );
  }

  /**
   * Creates a semantic version formatted string from segments.
   *
   * @param major The major version part.
   * @param minor The minor version part.
   * @param patch The patch version part.
   * @return The semantic version as string.
   */
  private static String toString(final int major, final int minor, final int patch) {
    return String.format(Locale.US, "%d.%d.%d", major, minor, patch);
  }

  /**
   * Returns the development version as a maven snapshot version, after releasing the current changes.
   *
   * @return The version string to be used as the next snapshot version in case of maven repo layouts.
   */
  public String getDevelopmentVersionString() {
    return this + VERSION_SUFFIX_SNAPSHOT;
  }
}
