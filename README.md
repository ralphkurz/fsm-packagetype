# Maven Package Type For FirstSpirit Modules [![Build Status](https://travis-ci.org/zaplatynski/fsm-packagetype.svg?branch=master)](https://travis-ci.org/zaplatynski/fsm-packagetype)  [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.zaplatynski/fsm-packagetype/badge.svg?style=flat)](http://mvnrepository.com/artifact/com.github.zaplatynski/fsm-packagetype)

This is a simple approach to create a Maven package type for FirstSpirit modules (FSM) with a fully working Maven lifecycle inclung install and deploy.

## How to use

In your `pom.xml` add this:
```
<project>

    <groupId>my-group</groupId>
    <artifactId>my-fsm-artifact</artifactId>
    <version>1.2.3</version>
    
    <!-- NEW: make a FSM file -->
    <packaging>fsm</packaging>

    ...
    <build>
    
        <plugins>
            
            <!-- make new FSM package type available to Maven -->
            <plugin>
                <groupId>com.github.zaplatynski</groupId>
                <artifactId>fsm-packagetype</artifactId>
                <version>1.0</version>
                <!-- this is important when extending core Maven functionality: -->
                <extensions>true</extensions>
            </plugin>
            
            <!-- define how the FSM file look like -->
             <plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>2.6</version>
                <configuration>
                    <attach>false</attach>
                    <appendAssemblyId>false</appendAssemblyId>
                    <descriptors>
                        <descriptor>src/assembly/fsm.xml</descriptor>
                    </descriptors>
                </configuration>
            </plugin>
            
        </plugins>    
        ...
    </build>
</project>
```
Inside the `fsm.xml` you need to specify the [Maven assembly plugin](http://maven.apache.org/plugins/maven-assembly-plugin/) descriptor to create a typical FSM file layout:
```
<assembly xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.3"
          xsi:schemaLocation="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.3 http://maven.apache.org/xsd/assembly-1.1.3.xsd">
    <id>fsm</id>
    <formats>
        <format>zip</format>
    </formats>
    <includeBaseDirectory>false</includeBaseDirectory>
    <files>
        <file>
            <source>src/main/resources/module.xml</source>
            <outputDirectory>META-INF</outputDirectory>
            <filtered>true</filtered>
        </file>
    </files>
    <dependencySets>
        <dependencySet>
            <outputDirectory>lib</outputDirectory>
            <includes>
                <include>my-groupId:my-jar-artifactId</include>
                 ...
            </includes>
            <useTransitiveFiltering>true</useTransitiveFiltering>
        </dependencySet>
    </dependencySets>
</assembly>
```
The FSM Maven package type will take care to rename the zip file to a FSM file. In the dependency set you specify your main dependencies. A minimal `module.xml` could look like this:
```
<module>
    <name>${project.artifactId}</name>
    <displayName>${project.name}</displayName>
    <version>${project.version}</version>

    <resources>
        <resource scope="module">lib/my-jar-artifactId-${project.version}.jar</resource>
        ...
    </resources>
</module>
```
Of cause feel free to combine this with [Monday Consulting's FSM plugin](https://github.com/monday-consulting/fsm-maven-plugin) if you don't want to maintain the module.xml manually.

If you want to have a kind of real world example then have a look at my [example project](https://github.com/zaplatynski/fsm-example-project) here on GitHub.

## Build command

[Maven](http://maven.apache.org/) is used to compile and assemble this project:
```
mvn clean install
```

##  Disclaimer

By using it you agree to the license stated in the file [LICENSE](LICENSE). FirstSpirit is a trade mark by the [e-Spirit AG](http://www.e-spirit.com/).

