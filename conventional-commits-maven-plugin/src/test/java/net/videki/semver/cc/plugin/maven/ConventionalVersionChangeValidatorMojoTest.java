package net.videki.semver.cc.plugin.maven;

import net.videki.semver.cc.release.common.SemanticVersion;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConventionalVersionChangeValidatorMojoTest extends AbstractScmMojoTest
{
    @Rule
    public MojoRule rule = new MojoRule()
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

    ConventionalVersionChangeValidatorMojo getMojo() throws Exception
    {
        final File pom = projectPath.toFile();
        assertNotNull(pom);
        assertTrue(pom.exists());

        ConventionalVersionChangeValidatorMojo myMojo = (ConventionalVersionChangeValidatorMojo) rule.lookupConfiguredMojo(pom, "validate");
        assertNotNull(myMojo);

        return myMojo;
    }

    @Test(expected = NoVersionChangeException.class)
    public void executeNoConventionalCommits() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        myMojo.execute();
    }

    @Test(expected = NoVersionChangeException.class)
    public void executeNoConventionalVersionChangeCommits() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("ci: release x.y.z");
        myMojo.execute();
    }

    @Test(expected = NoVersionChangeException.class)
    public void executeChoreCommit() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("chore: add something");
        myMojo.execute();
    }

    @Test
    public void executeFeatCommit() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("feat: add foo");
        myMojo.execute();
    }

    @Test
    public void executeFixCommit() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("fix: add something");
        myMojo.execute();
    }

    @Test
    public void executeBreakingChangeCommit() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("fix: add something");
        myMojo.execute();
    }

    @Test(expected = NoVersionChangeException.class)
    public void executeMultipleChangesNonBumping() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("ci: add something");
        createCommit("chore: add something");
        myMojo.execute();

        final SemanticVersion originalVersion = SemanticVersion.parse(myMojo.getOriginalVersion());

        assertEquals(originalVersion.getMajor(), myMojo.getReleaseVersion().getMajor());
        assertEquals(originalVersion.getMinor(), myMojo.getReleaseVersion().getMinor());
        assertEquals(originalVersion.getPatch(), myMojo.getReleaseVersion().getPatch());
    }

    @Test
    public void executeMultipleChangesOnlyPatch() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("fix: add something");
        createCommit("fix: add something");
        createCommit("docs: shiny docs added");
        myMojo.execute();

        final SemanticVersion originalVersion = SemanticVersion.parse(myMojo.getOriginalVersion());

        assertNotNull(myMojo.getOriginalVersion());
        assertTrue(myMojo.getOriginalVersion().endsWith(SemanticVersion.VERSION_SUFFIX_SNAPSHOT));

        assertEquals(originalVersion.getMajor(), myMojo.getReleaseVersion().getMajor());
        assertEquals(originalVersion.getMinor(), myMojo.getReleaseVersion().getMinor());
        assertEquals(originalVersion.getPatch(), myMojo.getReleaseVersion().getPatch());
    }

    @Test
    public void executeMultipleChangesMinorAndPatch() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("fix: add something");
        createCommit("feat: add something");
        createCommit("docs: shiny docs added");
        myMojo.execute();

        final SemanticVersion originalVersion = SemanticVersion.parse(myMojo.getOriginalVersion());

        assertEquals(originalVersion.getMajor(), myMojo.getReleaseVersion().getMajor());
        assertEquals(originalVersion.getMinor() + 1, myMojo.getReleaseVersion().getMinor());
        assertEquals(originalVersion.getPatch(), myMojo.getReleaseVersion().getPatch());
    }

    @Test
    public void executeMultipleChangesMinorAndPatchWithBreakingChange() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("fix: add something");
        createCommit("chore: add something");
        createCommit("fix!: add some breaking thing");
        createCommit("docs: shiny docs added");
        myMojo.execute();

        final SemanticVersion originalVersion = SemanticVersion.parse(myMojo.getOriginalVersion());

        assertEquals(originalVersion.getMajor() + 1, myMojo.getReleaseVersion().getMajor());
        assertEquals(originalVersion.getMinor() , myMojo.getReleaseVersion().getMinor());
        assertEquals(originalVersion.getPatch(), myMojo.getReleaseVersion().getPatch());
    }

    @Test
    public void executeMultipleChangesMinorAndPatchWithBreakingChangeInFooter() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("fix: add something");
        createCommit("chore: add something");
        createCommit("fix: add some breaking thing." +
            "With a long, long, long description." +
            "BREAKING CHANGE: this rocks.");
        createCommit("docs: shiny docs added");
        myMojo.execute();

        final SemanticVersion originalVersion = SemanticVersion.parse(myMojo.getOriginalVersion());

        assertEquals(originalVersion.getMajor() + 1, myMojo.getReleaseVersion().getMajor());
        assertEquals(0 , myMojo.getReleaseVersion().getMinor());
        assertEquals(0, myMojo.getReleaseVersion().getPatch());
    }

    @Test
    public void executeMultipleChangesMinorAndPatchWithNonCompliantCommit() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("fix: add something");
        createCommit("chore: add something");
        createCommit("feat: add some new thing." +
            "With a long, long, long description.");
        createCommit("choke add something");
        createCommit("docs: shiny docs added");
        myMojo.execute();

        final SemanticVersion originalVersion = SemanticVersion.parse(myMojo.getOriginalVersion());

        assertEquals(originalVersion.getMajor(), myMojo.getReleaseVersion().getMajor());
        assertEquals(originalVersion.getMinor() + 1, myMojo.getReleaseVersion().getMinor());
        assertEquals(originalVersion.getPatch(), myMojo.getReleaseVersion().getPatch());
    }

    @Test(expected = NoVersionChangeException.class)
    public void executeMultipleChangesWithOnlyNonCompliantCommit() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("fixx add something");
        createCommit("chores add something");
        createCommit("fiks add some breaking thing." +
            "With a long, long, long description." +
            "BREAKING CHANGE: this rocks.");
        createCommit("choke add something");
        createCommit("docs shiny docs added");
        myMojo.execute();

        final SemanticVersion originalVersion = SemanticVersion.parse(myMojo.getOriginalVersion());

        assertEquals(originalVersion.getMajor(), myMojo.getReleaseVersion().getMajor());
        assertEquals(originalVersion.getMinor(), myMojo.getReleaseVersion().getMinor());
        assertEquals(originalVersion.getPatch(), myMojo.getReleaseVersion().getPatch());
    }

    @Test(expected = NoVersionChangeException.class)
    public void executeMultipleChangesWithOnlyNonBumpingCommits() throws Exception
    {
        final ConventionalVersionChangeValidatorMojo myMojo = getMojo();
        createCommit("build: add something");
        createCommit("chore: add something");
        createCommit("ci: add something");
        createCommit("style: add something");
        createCommit("test: shiny tests added");
        myMojo.execute();

        final SemanticVersion originalVersion = SemanticVersion.parse(myMojo.getOriginalVersion());

        assertEquals(originalVersion.getMajor(), myMojo.getReleaseVersion().getMajor());
        assertEquals(originalVersion.getMinor(), myMojo.getReleaseVersion().getMinor());
        assertEquals(originalVersion.getPatch(), myMojo.getReleaseVersion().getPatch());
    }
}
