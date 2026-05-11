# Trivia-Maze---Group-7
A trivia-based maze game built in Java

Members:
-----------------------
Angelina Christianson
Makani Malawo
Tifanie Ngo
-----------------------

**Iteration One:**
* One issue we encountered was adding a timer that starts when the Play button gets pressed. It was confusing where to add the logic and information, but we ended up putting most of it in GameController and Player.
* An issue we're still trying to figure out is how to add sound to the game, because we want it to be as immersive as our skill levels can get, with the deadline to keep in mind as well.
* The biggest trouble we've had, however, was trying to push and pull through git. It made us have to merge through GitHub to see the changes, since we were both editing the same version of the file at the same time, and one of us pushed before the other. It all worked out in the end though (thankfully).

**Iteration Two:**
* I wasn't able to finish connecting the trivia questions to the Door objects this iteration. I'll be completing that in iteration 3 by creating QuestionDAO.java and linking questions to doors when the maze generates. The database is fully set up and working, it just needs to be connected to the doors! -**Makani**
* There was an issue where it was creating a new view every time you clicked a button (like play or settings). I added public static variables for the view panels and connected them so that it brings up the same view every time, instead of making a new one. -**Angelina**
* An achievement made during the 2nd iteration was successfully implement a sound system, with randomized automatic track switching and a working volume slider that can change how loud audio is for the program. -**Tifanie**
* I was focused primarily on developing and implementing the audio system for in-game music/SFX that time invested on the player logic portion of the project wasn't as prominent as expected, but I'd like to make it a goal for the upcoming iteration now that the main menu has been mostly implemented by now and the group can start working on the contents of the game for basic gameplay. -**Tifanie**

