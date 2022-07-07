package net.videki.semver.cc.release.common.scm;

/**
 * Exception type for handling SCM-related exceptions.
 */
public class ScmApiException extends Exception {
  /**
   * Constructor to initialize the exception.
   *
   * @param message error-specific message.
   * @param cause   error cause.
   */
  public ScmApiException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
