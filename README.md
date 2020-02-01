SOEN 6441 - RISK GAME GROUP PROJECT
---

<!-- TOC -->

- [1. Continue Development](#1-continue-development)
    - [1.1. Pack and run from console](#11-pack-and-run-from-console)
    - [1.2. Generate the javadoc](#12-generate-the-javadoc)
- [2. Members](#2-members)
- [3. General description](#3-general-description)
- [4. Game Rule](#4-game-rule)
- [5. Implementations](#5-implementations)
    - [5.1. Design Pattern](#51-design-pattern)
    - [5.2. JDK version and tools](#52-jdk-version-and-tools)

<!-- /TOC -->

| Build Status | Test Coverage | Coverage Tree |
|--------------|---------------|---------------|
| ![](https://github.com/huntertran/soen6441-riskgame/workflows/Java%20CI/badge.svg) | [![codecov](https://codecov.io/gh/huntertran/soen6441-riskgame/branch/master/graph/badge.svg?token=crTbuvO5Gq)](https://codecov.io/gh/huntertran/soen6441-riskgame) | [![codecov](https://codecov.io/gh/huntertran/soen6441-riskgame/branch/master/graph/tree.svg?token=crTbuvO5Gq)](https://codecov.io/gh/huntertran/soen6441-riskgame) |

Code Quality

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f8884dd413fb450c8ff6d4c7a4ef041f)](https://www.codacy.com/manual/huntertran/soen6441-riskgame?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=huntertran/soen6441-riskgame&amp;utm_campaign=Badge_Grade)

> Official Site: [https://huntertran.github.io/soen6441-riskgame/](https://huntertran.github.io/soen6441-riskgame/)

# 1. Continue Development
<a id="markdown-continue-development" name="continue-development"></a>

## 1.1. Pack and run from console
<a id="markdown-pack-and-run-from-console" name="pack-and-run-from-console"></a>

To pack the project, simply run:

```s
mvn package
```

a file called `soen6441riskgame-[version]-SNAPSHOT.jar` will be created in `\target` folder

To run the project (with or without commands and args)

```s
# change directory to target folder, or the folder contain the compiled .jar file

cd target

# run the .jar file with java

java -jar .\soen6441riskgame-[version]-SNAPSHOT.jar [your_command] [your_args]
```

## 1.2. Generate the javadoc
<a id="markdown-generate-the-javadoc" name="generate-the-javadoc"></a>

The javadoc command require git submodules to be cloned

If you've already cloned the repo:

```s
git submodule init
git submodule update
```

otherwise

```s
git clone --recursive https://github.com/huntertran/soen6441-riskgame.git
```

then

```s
# go to the root directory of the project
mvn -f plugins/uml-java-doclet/pom.xml clean install
mvn javadoc:javadoc
```

The javadoc files will be generated at `\docs\javadocs`. When you commit the newly created files to github, they will be deployed to the project home page

# 2. Members
<a id="markdown-members" name="members"></a>
- Van Tuan Tran
- Bharti Saini

# 3. General description
<a id="markdown-general-description" name="general-description"></a>
The project is to be undertaken teams of 5 members and consists of the building of a challengingly large Java program.

The completion of the project is divided into three separate components:

1. the First and Second Intermediate Project Delivery are intermediate operational build of the software, effectively demonstrating the full implementation of some important software features;

2. the Final Project Delivery is the demonstration of the finalized version of your software. During the final project delivery, you also have to demonstrate that your code includes many of the Java features presented in the lectures, and that you effectively use the tools presented in the lectures in your project.

All project deliveries are to be undertaken in the laboratory where the team presents the implemented features to the instructor following a pre-circulated grading sheet. The individual assignments will also be related to the project, but graded individually and separately from the project.

It is important to realize that the project description is purposely incomplete, and that it is one of your duties in this project to:

1. elicit and formulate all the missing details before you start the implementation,
2. limit the scope of the project according to the time that is available,
3. determine what design decisions will be made, as well as
4. what tools will be used for the implementation. These activities require some investigations and discussions that are important aspects of software development and this project.

# 4. Game Rule
<a id="markdown-game-rule" name="game-rule"></a>

We using the game rule from [hasbro](https://www.hasbro.com/common/instruct/risk.pdf)

> An offline version of the file is in `grades/GameRule.pdf`

# 5. Implementations
<a id="markdown-implementations" name="implementations"></a>

This part of the document briefly introduce the implementations of the project. For more detailed implementation, check out `wiki` section.

## 5.1. Design Pattern
<a id="markdown-design-pattern" name="design-pattern"></a>

The project using MVC Pattern, which is stand for Model-View-Controller

More about applying MVC in Java applications can be found here: [MVC article on Codeproject](https://www.codeproject.com/Articles/879896/Programming-in-Java-using-the-MVC-architecture)

For a more details MVC pattern, please look in [this part](https://github.com/huntertran/soen6441-riskgame/wiki/System-Architectural#2-model-view-controller) of wiki.

## 5.2. JDK version and tools
<a id="markdown-jdk-version-and-tools" name="jdk-version-and-tools"></a>

> The original project use features of Java 8

The maintaining project using Java 13 with AdoptOpenJDK

Work best with Visual Studio Code / Eclipse
