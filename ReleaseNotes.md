# 0.4.0 (January 2, 2012) #
  * Parameterised scenarios
  * Spring integration (SpringTumberRunner)

---


# 0.3.0 (August 13, 2010) #
  * **BIG CHANGE** _Given_, _When_ and _Then_ methods are now starting with a capital letter - so it's ok to statically import both tumbler and mockito
  * i18n of scenario files English, German, Danish and Polish are already there - looking for translators :)
  * parser for .scenario files is rewritten, now much more flexible
  * Given, When and Then can have And, Then can also have But (Given something And something else...)

---


# 0.2.2 (May 24, 2010) #
  * works on JUnit 4.4 and 4.5
  * java files are not generated in Tumbler-reports anymore
  * in .scenarios file can use 'Feature:' instead of 'Story:' - beginning to introduce Cucumber syntax

---


# 0.2.1 (May 17, 2010) #
  * informative exception if JUnit older than 4.6 is used
  * informative exception if @Test is used in a story
  * NPE fixed if @Test was used in a story
  * going for maven central repo with this release

---


# 0.2.0 #
  * @Subject changed to @Story, @Example changed to @Scenario  to match standard BDD naming
  * .example changed to .scenarios
  * .scenarios file can have comments ( -- here's my comment)
  * supports many stories per one scenario file
  * @CamelCase changed to -DcamelCase=true, after Lasse Koskela's comment that it's bad to force whole team to do that
  * html reports added
  * reports can be generated using templates

---


# 0.1.1 #
  * JUnit runner (TumblerRunner)
  * given, when, then methods
  * test class annotated as @Subject, test method as @Example
  * `*`.example files (plain text) generation from java
  * java generation from `*`.example files
  * full-text test names in IDEs (using JUnit's Description class)