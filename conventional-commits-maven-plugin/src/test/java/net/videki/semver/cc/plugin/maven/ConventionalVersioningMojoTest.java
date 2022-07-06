package net.videki.semver.cc.plugin.maven;

import net.videki.semver.cc.release.common.ConventionalVersioning;
import net.videki.semver.cc.release.common.SemanticVersion;
import net.videki.semver.cc.release.common.SemanticVersionChange;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.WithoutMojo;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ConventionalVersioningMojoTest extends AbstractScmMojoTest
{
    @Rule public MojoRule rule = new MojoRule()
    {
        @Override
        protected void before()
        {
        }

        @Override
        protected void after()
        {
        }
    };

    /**
     * @throws Exception if any
     */
    @Test
    public void testExecute() throws Exception
    {
        final File pom = projectPath.toFile();
        assertNotNull(pom);
        assertTrue(pom.exists());

        final ConventionalVersioningMojo myMojo = (ConventionalVersioningMojo) rule.lookupConfiguredMojo(pom, "version");
        assertNotNull(myMojo);
        myMojo.execute();

        final File outputDirectory = (File) rule.getVariableValueFromObject(myMojo, "outputDirectory");
        assertNotNull(outputDirectory);
        assertTrue(outputDirectory.exists());

        final File touch = new File(outputDirectory, "version.props");
        assertTrue(touch.exists());
    }

    /**
     * @throws Exception if any
     */
    @Test
    public void testVersionStrings() throws Exception
    {
        final File pom = projectPath.toFile();
        final ConventionalVersioningMojo myMojo = (ConventionalVersioningMojo) rule.lookupConfiguredMojo(pom, "version");

        createCommit("ci: release x.y.z");

        myMojo.execute();

        final ConventionalVersioning versioning =  myMojo.getConventionalVersioning();
        final String originalVersionString = myMojo.getOriginalVersion();
        final SemanticVersion originalVersion = SemanticVersion.parse(originalVersionString);
        final SemanticVersion releaseVersion = versioning.getReleaseVersion(SemanticVersion.parse(originalVersionString));
        final SemanticVersion nextDevelopmentVersion = releaseVersion.nextDevelopmentVersion(SemanticVersionChange.PATCH);

        // set properties for release plugin
        assertEquals(originalVersion.getMajor(), releaseVersion.getMajor());
        assertEquals(originalVersion.getMinor(), releaseVersion.getMinor());
        assertEquals(originalVersion.getPatch(), releaseVersion.getPatch());

        assertEquals(originalVersion.getMajor(), nextDevelopmentVersion.getMajor());
        assertEquals(originalVersion.getMinor(), nextDevelopmentVersion.getMinor());
        assertEquals(originalVersion.getPatch() + 1, nextDevelopmentVersion.getPatch());

        assertTrue(nextDevelopmentVersion.getDevelopmentVersionString().endsWith(SemanticVersion.VERSION_SUFFIX_SNAPSHOT));

    }

    /**
     * Do not need the MojoRule.
     */
    @WithoutMojo
    @Test
    public void testSomethingWhichDoesNotNeedTheMojoAndProbablyShouldBeExtractedIntoANewClassOfItsOwn()
    {
        assertTrue(true);
    }

}

