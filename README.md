# Conventional Commits for Java

[![Build](https://github.com/videki/conventional-commits-for-java/actions/workflows/build.yml/badge.svg)](https://github.com/videki/conventional-commits-for-java/actions/workflows/build.yml)
[![Codecov branch](https://img.shields.io/codecov/c/github/videki/conventional-commits-for-java/master?label=Coverage)](https://codecov.io/gh/videki/conventional-commits-for-java)

Provides a Java implementation of [Conventional Commits] for projects built
with Java 11+ using Git for version control.

## Maven Plugin

### Usage

This plugin works together with the [Maven Release Plugin] to create
conventional commit compliant releases for your Maven projects

#### Install the Plugin

In your main `pom.xml` file add the plugin:

```xml
    <plugins>
        <plugin>
            <groupId>com.smartling.cc4j</groupId>
            <artifactId>conventional-commits-maven-plugin</artifactId>
            <version>${version}</version>
        </plugin>

        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-release-plugin</artifactId>
            <version>3.0.0-M5</version>
            <configuration>
                <scmCommentPrefix>build(release): </scmCommentPrefix>
                <scmDevelopmentCommitComment>ci(release): open next snapshot version</scmDevelopmentCommitComment>
                <scmReleaseCommitComment>build(release): publish</scmReleaseCommitComment>
            </configuration>
        </plugin>
    </plugins>

```

In your CI code:

- Execute only for master|main branch
- skip builds for build(release) and ci(release) commits

GitHub workflow:

```yaml
jobs:
  main:
    ...
    if: |
      "!contains(github.event.head_commit.message, 'build(release)')" &&
      "!contains(github.event.head_commit.message, 'ci(release)')"
```

Gitlab:

```yaml
  only:
    variables:
      - '$CI_COMMIT_MESSAGE !~ /^(build|ci)\(release\):.+/'
      - '$CI_COMMIT_BRANCH =~ /^(master|main)/'
```

#### Release a Version

    mvn conventional-commits:version release:prepare
    mvn release:perform

## Gradle Plugin

A [Gradle] plugin is planned for future release.











[Conventional Commits]: https://www.conventionalcommits.org/en/v1.0.0/
[Maven Release Plugin]: https://maven.apache.org/maven-release/maven-release-plugin/
