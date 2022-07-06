package net.videki.semver.cc.plugin.maven;

import net.videki.semver.cc.release.common.ConventionalVersioning;
import net.videki.semver.cc.release.common.SemanticVersionChange;
import net.videki.semver.cc.release.common.scm.ScmApiException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;

@Mojo(name = "validate", aggregator = true, defaultPhase = LifecyclePhase.VALIDATE)
public class ConventionalVersionChangeValidatorMojo extends AbstractVersioningMojo
{
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            final ConventionalVersioning versioning = this.getConventionalVersioning();
            final SemanticVersionChange change = versioning.getNextVersionChangeType();

            if (SemanticVersionChange.NONE.equals(change))
            {
                throw new NoVersionChangeException("No conventional commit version change to release");
            }
        }
        catch (final IOException | ScmApiException e)
        {
            throw new MojoExecutionException("Unable to access repository", e);
        }
    }
}
