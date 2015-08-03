# News #
  * **May 16, 2012:** There's a nice new theme for Tumbler. Check it out!: https://github.com/piotrpietrzak/tumbler-glass-theme
  * **Jan 9, 2012:** Tumbler 0.4.1 released, upgraded to JUnitParams-0.4.0 and with fix of one templates-related issue
  * **Jan 2, 2012:** Tumbler 0.4.0 released, now officially with Parameterised scenarios and upgraded to junit4.10
  * **Jun 14, 2011:**   Parameters should be working fine now. Some issues have also been solved (see the issues section) - you can check it in the Mercurial, or with maven use version 0.3.1-SNAPSHOT from sonatype snapshots repository. Some more little improvements on the way, and I'll make a release.
  * **Apr 29, 2011:**   Moved to Mercurial from SVN. Parameterised scenarios are implemented. Check it out and build from the source if you want to try them. Though v0.4 with them will be released soon.
  * **Aug 13, 2010:**   Tumbler 0.3.0 released, see ReleaseNotes
  * **May 24, 2010:**   Tumbler 0.2.2 released, see ReleaseNotes
  * **May 18, 2010:**   Tumbler 0.2.1 is now in Maven central repository. See IntroductionForDevelopers for details.

<table width='600'><tr><td width='100'><img src='http://tumbler-glass.googlecode.com/hg/apidocs/tumbler/doc-files/tumbler.jpg' /></td><td align='left'><font size='60'>Tumbler</font></td><td align='right' valign='center'><a href='http://pragmatists.pl'><img src='http://pragmatists.pl/misc/logo-kwadrat.gif' align='middle' /></a></td></tr></table>

# What is it? #
What is the first thing you do when you want to make your favourite drink? Prepare a proper glass. It will lead the process of creating a tasty and good-looking drink by defining amounts of its ingredients and how they should be placed for the drink to look nice.

Tumbler is a coding-by-example library which will help you do the same with your code. It will lead the way you think about and build your code, as well as help you remember what should be implemented. It supports you in the 'think' phase if you do TDD (or rather BDD), can help your discussions with business people (they like examples) and
additionally you'll be able to impress your customer with always up-to-date reports of what has already been implemented (and whether it works or not), and what's still pending.

# How to use it? #
Depending who you are, your usage style will be different. If you're on the business/requirements side, see IntroductionForBusinessPeople. If you're a developer, check IntroductionForDevelopers and be sure to see Tumbler documentation!

# Documentation #
Whole developers documentation is within  [Tumbler Javadoc](http://tumbler-glass.googlecode.com/hg/apidocs/tumbler/Tumbler.html). It guides you through everything that Tumbler can do for you.

# Thanks #
The whole idea of Tumber is rooted in coding-by-example idea. Szczepan Faber of [Mockito](http://mockito.org) has been an evangelist of this way of writing tests (with given, when, and then as obligatory comments in all tests) and Tumbler is just an extension of that idea. Also the way documentation is organised (as Javadoc of main library class) is 'stolen' from Mockito. Thanks man!

Actually it's a great idea to fill Tumbler with Mockito - it tastes just great!

Tumbler is a BDD tool. And BDD is Dan North's idea - see [his introduction](http://blog.dannorth.net/introducing-bdd) to BDD. You can also have a look at [Wikipedia's BDD article](http://en.wikipedia.org/wiki/Behavior_Driven_Development) for more details