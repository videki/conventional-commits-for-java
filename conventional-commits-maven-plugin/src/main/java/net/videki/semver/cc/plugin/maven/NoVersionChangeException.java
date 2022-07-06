package net.videki.semver.cc.plugin.maven;

import org.apache.maven.plugin.MojoFailureException;

public class NoVersionChangeException extends MojoFailureException
{
    public NoVersionChangeException(final String message)
    {
        super(message);
    }
}
