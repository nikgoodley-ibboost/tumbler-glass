# Tumbler introduction for business people #
If you are a Business Analyst, Product Owner, or simply the business side of any software project, this document is for you. It will briefly introduce you to the Tumbler and give you reasons to ask your developers to include it in the project.

## What is Tumbler? ##
Tumbler is a tool, which will help you cooperate with your development team. How often is it, that developers don't really get what you meant in some requirement description? How often do you get something different than you asked for? And the worst - how often do you realise that it's not exactly what you meant, when the software is already deployed? Tumbler will ease the communication between you and the team and will help you prevent such situations.

Tumbler is so called Behaviour-Driven Development tool. It means, that YOU drive the code development by specifying how you'd like the application to behave. You probably think you already do that - in the end it's you who defines requirements. Yep, to some extent you're right. But you don't _drive_ the development, you just give it one big kick. Now what I mean by driving is defining the requirements in such a way, that you can be sure their implementation reflects your ideas. How can that be achieved?

## How to use it? ##
You start with defining _stories_ - minimal features, that you can sell or you just care for.<br>
Let me give you an example: say you're in the internet shopping business. What are minimal features you might want to have in your application? It can be adding things to the shop (as an admin), having stuff divided into categories, comparing items from the same category (as a user), etc.<br>
For each of these stories you can define a set of <i>scenarios</i> (I'm sure you do it anyway, just in a different form). A scenario is an example of use of a story, or in other words a behaviour of the thing you're dealing with.<br>
Again an example: let's take the story 'As a user I want to compare things from the same category to be able to choose the better one'. Now you can specify scenarios like: given that there are two tea types in a 'teas' category, when a user compares them, then they see teas' photos, names, contents and prices. Or another one: given there's only one coffee in 'coffees' category, when user wants to compare coffees, then they see a message that it's impossible.<br>
<br>
Great - that's basically it. You only need to write these stories and their scenarios in normal text file and give them to your developers. Hm, actually it would be much better if you could define them together with a developer, to make sure you did not miss anything obvious to developers but not for mere humans.<br>
<br>
So, say, you have a file like this:<br>
<pre>
Story: Comparing items from the same category<br>
<br>
Scenario: should compare if there are at least two items in a category<br>
Given there are two teas in 'teas' category<br>
When user compares them<br>
Then they see teas' photos, names, contents, and prices<br>
<br>
Scenario: should not compare if there's less then two items in a category<br>
Given there's one coffee in 'coffees' category<br>
When user wants to compare it<br>
Then they get a message 'not enough items in this category'<br>
</pre>

This form is required - the keywords are <code>Story:, Scenario:, Given, When,</code> and <code>Then</code>. They need to be in different lines, but otherwise you can format it as you want (with any number of spaces, tabs and newlines) to make it readable for you. You can have one file per story, but equally well can define many stories in one file - just place them one below another.<br>
<br>
Now that the stories are ready, give them to your development and ask for an always up-to-date report accessible somewhere in your intranet. They will generate Java tests from your stories and will fill them up with calls to the application, to prove that a scenario is working.<br>
<br>
You can use your native language if you need to. This is how it looks in Polish:<br>
<pre>
Historia: Porównywanie produktów w tej samej kategorii<br>
<br>
Scenariusz: powinien porównać gdy są przynajmniej dwa produkty w jednej kategorii<br>
Zakładając  że są dwie herbaty w jednej kategorii<br>
Gdy użytkownik je porówna<br>
Wtedy zobaczy zdjęcia, nazwy i ceny herbat<br>
<br>
Scenariusz: nie powinien porównać jeśli jest mniej niż dwa produkty w jednej kategorii<br>
Zakładając że jest jedna kawa w kategorii 'kawy'<br>
Gdy użytkownik chce porównać<br>
Wtedy dostaje wiadomość 'nie wystarczająca liczba produktów w tej kategorii'<br>
</pre>

<h2>Reports</h2>
Tumbler can currently generate two kinds of reports - plain text and HTML.<br>
<br>
The first one is in exactly the same form as you gave to the development - it's just plain text, but next to each scenario you have an information about its status. This can be one of three:<br>
<ul><li>PASSED - means that the scenario is implemented and its test is passing,<br>
</li><li>FAILED - means that the scenario has been implemented, but for some reason is not working properly - most probably as a sideeffect of some other change to the application (so you have also free regression testing)<br>
</li><li>PENDING - means that the scenario has not been implemented yet<br>
So your story's report would look like this:<br>
<pre>
Story: Comparing items from the same category<br>
<br>
Scenario: should compare if there are at least two items in a category    [PASSED]<br>
Given there are two teas in 'teas' category<br>
When user compares them<br>
Then they see teas' photos, names, contents, and prices<br>
<br>
Scenario: should not compare if there's less then two items in a category    [PENDING]<br>
Given there's one coffee in 'coffees' category<br>
When user wants to compare it<br>
Then they get a message 'not enough items in this category'<br>
</pre></li></ul>


The HTML report can have basically any form. There's one default template integrated with Tumbler, but you can ask your developers to create your own. Any template must have 2 kinds of pages: Table of Contents which lists all your stories, and Story page, which shows all scenarios of a story.<br>
<br>
Here is ToC with all Tumbler stories (yep, Tumbler is itself driven by Tumbler stories):<br>
<p align='center'>
<img src='http://tumbler-glass.googlecode.com/svn/trunk/apidocs/tumbler/doc-files/html-toc.gif' />
</p>
As you can see, the ToC contains overall run statistics - how many stories passed (a passing story is one in which all scenarios pass), how many failed (at least one scenario fails) and how many are pending (at least one scenario is still waiting for implementation). Below it there's a table with links to consecutive stories with their status info.<br>
<br>
And here is a report for one of the stories:<br>
<p align='center'>
<img src='http://tumbler-glass.googlecode.com/svn/trunk/apidocs/tumbler/doc-files/html-example.gif' />
</p>
This one contains all the scenarios of the story together with your description of the behaviour and status information.<br>
<br>
<br>
As you can see the reports can also serve as a project documentation. So to summarise, Tumbler:<br>
<ul><li>helps you think about the requirements,<br>
</li><li>helps you communicate your thoughts to the development team<br>
</li><li>helps the team to implement exactly what you need<br>
</li><li>helps you keep track of the implementation progress<br>
</li><li>helps new project members to learn what the project is about<br>
Isn't that really a lot?