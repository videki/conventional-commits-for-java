package net.videki.semver.cc.plugin.maven;

import net.videki.semver.cc.release.common.ConventionalVersioning;
import net.videki.semver.cc.release.common.SemanticVersion;
import net.videki.semver.cc.release.common.SemanticVersionChange;
import net.videki.semver.cc.release.common.scm.ScmApiException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

abstract class AbstractVersioningMojo extends AbstractMojo
{
    private final static String MVN_RELEASE_VERSION_PROPERTY = "releaseVersion";
    private final static String MVN_RELEASE_VERSION_SUB_PROJECT_PROPERTY_PREFIX = "project.rel.";
    private final static String MVN_DEVELOPMENT_VERSION_PROPERTY = "developmentVersion";
    private final static String MVN_DEVELOPMENT_VERSION_SUB_PROJECT_PROPERTY_PREFIX = "project.dev.";
    private final static String MVN_SCM_TAG_PROPERTY = "scm.tag";

    @Parameter(defaultValue = "${project.basedir}", required = true)
    private File baseDir;

    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
    File outputDirectory;

    @Parameter(defaultValue = "${project.version}", required = true)
    private String versionString;

    @Parameter(defaultValue = "${reactorProjects}", readonly = true, required = true)
    private List<MavenProject> reactorProjects;

    private SemanticVersion releaseVersion;

    private SemanticVersion nextDevelopmentVersion;

    public String getOriginalVersion()
    {
        return this.versionString;
    }

    public SemanticVersion getReleaseVersion() throws IOException, ScmApiException
    {
        if (this.releaseVersion == null) {
            createReleaseProperties();
        }

        return this.releaseVersion;
    }

    ConventionalVersioning getConventionalVersioning() throws IOException
    {
        final Repository repository = new RepositoryBuilder().setWorkTree(baseDir).build();
        final MavenConventionalVersioning mvnConventionalVersioning = new MavenConventionalVersioning(repository);

        return mvnConventionalVersioning.getConventionalVersioning();
    }

    Properties createReleaseProperties() throws IOException, ScmApiException
    {
        final ConventionalVersioning versioning =  this.getConventionalVersioning();
        final Properties props = new Properties();

        this.releaseVersion = versioning.getReleaseVersion(SemanticVersion.parse(this.versionString));
        this.nextDevelopmentVersion = releaseVersion.nextDevelopmentVersion(SemanticVersionChange.PATCH);

        // set properties for release plugin
        props.setProperty(MVN_RELEASE_VERSION_PROPERTY, this.releaseVersion.toString());
        props.setProperty(MVN_DEVELOPMENT_VERSION_PROPERTY, this.nextDevelopmentVersion.getDevelopmentVersionString());

        for (final MavenProject project : reactorProjects)
        {
            final String projectKey = project.getGroupId() + ":" + project.getArtifactId();
            props.setProperty(MVN_RELEASE_VERSION_SUB_PROJECT_PROPERTY_PREFIX + projectKey, releaseVersion.toString());
            props.setProperty(MVN_DEVELOPMENT_VERSION_SUB_PROJECT_PROPERTY_PREFIX + projectKey, nextDevelopmentVersion.toString());
        }

        return props;
    }
}
