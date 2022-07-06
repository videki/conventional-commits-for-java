package net.videki.semver.cc.release.common;

import net.videki.semver.cc.release.common.scm.ScmApiException;

import java.io.IOException;

public interface ConventionalVersioning
{
    LogHandler logHandler();

    SemanticVersionChange getNextVersionChangeType() throws ScmApiException, IOException;

    SemanticVersion getReleaseVersion(SemanticVersion currentVersion) throws IOException, ScmApiException;
}
