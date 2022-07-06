package net.videki.semver.cc.plugin.maven;

import net.videki.semver.cc.release.common.ConventionalVersioning;
import net.videki.semver.cc.release.common.GitConventionalVersioning;
import org.eclipse.jgit.lib.Repository;

import java.util.Objects;

public class MavenConventionalVersioning
{
    private final Repository repository;

    public MavenConventionalVersioning(final Repository repository)
    {
        Objects.requireNonNull(repository, "repository required");
        this.repository = repository;
    }

    public ConventionalVersioning getConventionalVersioning()
    {
        return new GitConventionalVersioning(repository);
    }
}
