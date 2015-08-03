# Running Tumbler from Maven #

It's possible to run Tumbler from a Maven build. Since Tumbler's based on JUnit, the only configuration we need is to let Maven's surefire plugin know, that `*`Scenarios.java classes are really JUnit tests. It's as simple as this:
```
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.8.1</version>
            <configuration>
                <includes>
                    <include>**/*Scenarios.java</include>
                </includes>
            </configuration>
        </plugin>
    </plugins>
</build>
```

# Generating reports #
If you want to generate reports while running Tumbler from Maven, the `<configuation>`section of the surefire plugin should contain additionally:
```
<useFile>false</useFile>
<systemProperties>
	<property>
		<name>generateReport</name>
		<value>html</value>
	</property>
	<property>
		<name>outputFolder</name>
		<value>target/Report-html</value>
	</property>
</systemProperties>
```

The `outputFolder` is optional - if you ommit it, the report will be generated in `target/Tumbler-reports` folder.

# Examplary POM #
Here is a pom.xml file for two profiles. The `default` profile only runs Tumbler tests, but without reporting, and `reports` profile runs Tumbler twice, generating HTML report in the first run, and scenarios text files in the second:
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>pl.pragmatists</groupId>
	<artifactId>test</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<profiles>
		<profile>
			<id>default</id>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
			<build>
				<plugins>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-surefire-plugin</artifactId>
						<version>2.8.1</version>
						<configuration>
							<includes>
								<include>**/*Scenarios.java</include>
							</includes>
						</configuration>
					</plugin>
				</plugins>
			</build>
		</profile>
		<profile>
			<id>reports</id>
			<build>
				<plugins>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-compiler-plugin</artifactId>
						<configuration>
							<source>1.6</source>
							<target>1.6</target>
							<encoding>UTF-8</encoding>
						</configuration>
					</plugin>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-surefire-plugin</artifactId>
						<version>2.8.1</version>
						<executions>
							<execution>
								<id>html</id>
								<goals>
									<goal>test</goal>
								</goals>
								<configuration>
									<useFile>false</useFile>
									<systemProperties>
										<property>
											<name>generateReport</name>
											<value>html</value>
										</property>
										<property>
											<name>outputFolder</name>
											<value>target/Report-html</value>
										</property>
									</systemProperties>
									<includes>
										<include>**/*Scenarios.java</include>
									</includes>
								</configuration>
							</execution>
							<execution>
								<id>scenarios</id>
								<goals>
									<goal>test</goal>
								</goals>
								<configuration>
									<useFile>false</useFile>
									<systemProperties>
										<property>
											<name>generateReport</name>
											<value>scenarios</value>
										</property>
										<property>
											<name>outputFolder</name>
											<value>target/Report-scenarios</value>
										</property>
									</systemProperties>
									<includes>
										<include>**/*Scenarios.java</include>
									</includes>
								</configuration>
							</execution>
						</executions>
					</plugin>
				</plugins>
			</build>
		</profile>
	</profiles>
	<dependencies>
		<dependency>
			<groupId>pl.pragmatists.tumbler</groupId>
			<artifactId>tumbler</artifactId>
			<version>0.3.0</version>
		</dependency>
	</dependencies>
</project>
```