# Trivia-Maze---Group-7
A trivia-based maze game built in Java

Members:
Angelina Christianson
Makani Malawo
Tifanie Ngo
-----------------------

**Link to Repo: ** https://github.com/mmalawo/Trivia-Maze-Group-7/tree/main

-----------------------
**Requirements**

Java JDK 17 or higher

IntelliJ IDEA (recommended)

SQLite JDBC driver (sqlite-jdbc-3.53.1.0.jar) — included in the lib/ folder

-----------------------
**How to Run**

_Note: The trivia.db file will be created automatically the first time you run the game. Do not include an existing trivia.db when running fresh — delete it if one already exists._

The software should be runnable just by downloading the zip file and running it via the IntelliJ IDEA IDE. If not,
do the following below:

- Open the project in IntelliJ IDEA
- Go to File → Project Structure → Libraries
- Make sure sqlite-jdbc-3.53.1.0.jar is added as a library (it should already be in lib/)
- Go to Run → Edit Configurations
- Set the Main class to view.MainGUI
- Click Run

-----------------------
**Database**

_Note:_

_Modifying (add/remove) trivia questions in `DatabaseManager.java`:_

_delete your local `trivia.db` file before running again and the database will be regenerated with the latest questions on the next run_

_**DO NOT COMMIT `trivia.db` to Git because it is listed in `.gitignore` and should remain local only**_


_Database Files:_

 **`DatabaseManager.java`**: connects to the SQLite database, creates the `questions` and `leaderboard` tables, and populates them with trivia questions on first run

 **`QuestionDAO.java`**: retrieves questions from the database, manages the question pool, and tracks which questions have already been correctly answered so they don't repeat

 **`QuestionFactory.java`**: builds the correct Question subclass (MultipleChoiceQuestion, TrueFalseQuestion, or ShortAnswerQuestion) from a database row

**`LeaderboardDAO.java`**: saves player scores to the leaderboard table and retrieves the top scores for display

**`trivia.db`**: the local SQLite database file (auto-generated, not committed to Git)

-----------------------
**How to Play**

Use the N/S/W/E buttons on the right side of the screen to move through the maze
Click a door to attempt answering a trivia question

You have 2 attempts per door - fail both and the door is permanently locked

Use the Hint button to get a directional hint toward the exit

Find and answer the exit door correctly to win!

If all accessible doors are permanently locked, the game is over

-----------------------
**Menu Options (File menu)**

- Save Game: saves your current progress (Ctrl+S)

- Load Game: loads your last saved game (Ctrl+L)

- Main Menu: returns to the main menu (Ctrl+R)

- Exit: exits the game (Ctrl+E)

- Settings: adjust volume and display settings

- Leaderboard: view the top scores

Resume

If you save and exit to the main menu, a Resume button will appear on the main menu to continue your saved game.

-----------------------
**How to Run Unit Tests**

Open the project in IntelliJ IDEA

Navigate to src/test/model/ in the project panel

Right-click the test.model package → Run 'All Tests'

All tests should pass with exit code 0

_Test Coverage:_

- DoorTest: door locking, permanent closure, reset behavior

- PlayerTest: score tracking, timer, room tracking, reset

- MazeTest:  maze dimensions, room access, entrance/exit setup

- SaveManagerTest: save/load/delete game state

- QuestionFactoryTest: correct question subclass creation

-----------------------
**Project Structure**

src/
  
  ├── controller/       # Controllers (AppController, GameController, etc.)

  ├── model/            # Model classes (Maze, Room, Door, Player, etc.)

  ├── view/             # View classes (MazeView, GameMenuView, etc.)

  ├── test/model/       # Unit tests

  ├── images/           # Game images and pixel art

  └── sounds/           # Background music files

-----------------------
**Extra Credit Features**

- Pixel art graphics - custom day/night mode butterfly characters and maze backgrounds

- Animated butterfly character - flapping animation as the player moves

- Camera tracking - camera automatically follows and centers on the player

- Leaderboard - tracks and displays top scores using SQLite

- Background music - randomized playlist with volume control

- Resume button - dynamically appears on the main menu when a save file exists

- Night mode - dark mode toggle in settings changes the visual theme

- Hint system - directional hints guide the player toward the exit

- Timer restoration - save/load correctly restores the game timer from where it left off

-----------------------
**Iteration History**

_Iteration One:_

- Set up Trivia Maze GitHub repository

- Implemented Room, Door, and Maze classes

- Connected Room and Door classes

- Implemented GameController, MainGUI, GameMenuView, PlayerSetupView, SettingsView, Player, MenuController, SettingsController files

- Added timer logic

_Iteration Two:_

- Implemented SoundManager with randomized playlist and volume slider

- Created SQLite trivia question database

- Implemented Java database connection

- Connected trivia questions to Door objects

- Implemented Question model class

- Fixed JPanel/View recreation bug

- Pixel Art work begun

_Iteration Three:_

- Implemented DatabaseManager class

- Implemented QuestionDAO for random question retrieval

- Updated Player model with room tracking and game-over logic

- Implemented InstructionsView, PlayerController

- Restructured layout with cleaner view management

- Added character customization

- Fixed disappearing MenuBar bug

- Structure cleanup

_Iteration Four:_

- Implemented Save Game State feature

- Implemented trivia question popup for door interactions

- Implemented player movement

- Implemented butterfly player tracker

- Implemented zoom feature

- Displayed current room coordinates and door status

- Debugging GenerateMaze and MazeView files

- Dynamic scaling 

_Iteration Five:_

- Implemented LeaderboardEntry, LeaderboardDAO, LeaderboardView

- Added leaderboard database table support

- Fixed pre-unlocked door bug

- Implemented 3 attempts system with permanent lock (initially - now 2 attempts)

- Fixed question randomization

- Fixed answer checking for multiple choice questions

- Added 10 new trivia questions to the database

- Implemented new question on wrong answer

- Fixed correctly answered questions from repeating

_Iteration Six:_

- Randomized exit door location on maze perimeter each new game

- Exit door requires trivia question to unlock

- Win condition when exit door trivia is answered correctly

- Lose condition when exit door trivia fails all attempts

- Dead end popup for non-exit perimeter doors

- Camera auto-tracks and centers on player butterfly

- Fixed old save file bug

- Pixel art for night mode added

- Timer display added to MazeView

- Butterfly flap animation added

- Fixed trivia question display bugs

- Cleaned up button display and help menu

- Game over logic when player permanently locks all accessible pathways

- Fixed loadGame feature when no save files exist

- Revised leaderboard functionality

- Added more trivia questions to database

-----------------------
## Software Architecture

The project follows the Model-View-Controller (MVC) design pattern.

### Model
- Maze
- Room
- Door
- Player
- Question hierarchy
- DatabaseManager
- SaveManager

### View
- MainGUI
- MazeView
- GameMenuView
- PlayerSetupView
- InstructionsView
- SettingsView
- LeaderboardView

### Controller
- AppController
- GameController
- MenuController
- PlayerController
- SettingsController

Controllers handle user input, update model state, and refresh the appropriate views.

-----------------------
### Design Patterns

Factory Pattern:
- QuestionFactory creates the appropriate Question subclass
    - MultipleChoiceQuestion
    - TrueFalseQuestion
    - ShortAnswerQuestion

Memento Pattern:
- SaveManager and Memento support game state serialization and restoration.
