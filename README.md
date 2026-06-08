# Trivia-Maze---Group-7
A trivia-based maze game built in Java

Members:
-----------------------
Angelina Christianson
Makani Malawo
Tifanie Ngo
-----------------------

Link to Repo: https://github.com/mmalawo/Trivia-Maze-Group-7/tree/main

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

**Iteration Three:**
- Makani: Created QuestionDAO.java to retrieve random trivia questions from the database. Updated Door.java to assign a Question object to each door on creation. Updated Player.java with room tracking and game-over logic.
- Tifanie: Implemented InstructionsView screen, added trivia questions to the database, worked on PlayerSetupView and PlayerController.
- Angelina: Restructured layout with switchPanel() method for cleaner code, added character customization, created MazeView, fixed disappearing MenuBar bug.

**Iteration Four:**
- There's a bug found regarding the questions determining if a door stays locked or unlocked (sometimes, you can answer a door that should have been permanently locked; sometimes "unlocked" doors are still marked as "locked" and you have to answer two questions for a permanent door state change)
That problem was discovered while creating visual door rendering.
- More discussion is needed on how we plan to move the character visually.
- Need to make it so any screen size can play the game. Right now the maze doesn't center itself properly (since we're starting in the middle).

**Iteration Six:**
- Makani: Implemented randomized exit door placement on the maze perimeter each new game. Fixed exit door logic so it triggers a trivia question instead of treating the entire room as the exit. Added win condition when the exit door trivia is answered correctly and lose condition when all 3 attempts are failed. Added a "That door leads nowhere!" popup for non-exit perimeter doors. Updated camera to automatically track and center on the player butterfly, removing manual scroll buttons and zoom in/out.
- Angelina: Added pixel art for night mode, updated MazeView with timer display, added butterfly flap animation, fixed trivia question bugs, cleaned up button display and help category in the menu bar, and general code cleanup.
- Tifanie: Implemented game over logic when the player permanently locks all accessible pathways to the exit. Fixed loadGame feature to handle when no existing save files are found. Revised leaderboard functionality.
