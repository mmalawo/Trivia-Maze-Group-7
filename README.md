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
Requirements

Java JDK 17 or higher
IntelliJ IDEA (recommended)
SQLite JDBC driver (sqlite-jdbc-3.34.0.jar) — included in the lib/ folder

-----------------------
How to Run

Note: The trivia.db file will be created automatically the first time you run the game. Do not include an existing trivia.db when running fresh — delete it if one already exists.

Open the project in IntelliJ IDEA
Go to File → Project Structure → Libraries
Make sure sqlite-jdbc-3.34.0.jar is added as a library (it should already be in lib/)
Go to Run → Edit Configurations
Set the Main class to view.MainGUI
Click Run

-----------------------
How to Play

Use the N/S/W/E buttons on the right side of the screen to move through the maze
Click a door to attempt answering a trivia question
You have 2 attempts per door — fail both and the door is permanently locked
Use the Hint button to get a directional hint toward the exit
Find and answer the exit door correctly to win!
If all accessible doors are permanently locked, the game is over

-----------------------
Menu Options (File menu)

Save Game — saves your current progress (Ctrl+S)
Load Game — loads your last saved game (Ctrl+L)
Main Menu — returns to the main menu (Ctrl+R)
Exit — exits the game (Ctrl+E)
Settings — adjust volume and display settings
Leaderboard — view the top scores

Resume
If you save and exit to the main menu, a Resume button will appear on the main menu to continue your saved game.

-----------------------
How to Run Unit Tests

Open the project in IntelliJ IDEA
Navigate to src/test/model/ in the project panel
Right-click the test.model package → Run 'All Tests'
All tests should pass with exit code 0

Test Coverage

DoorTest — door locking, permanent closure, reset behavior
PlayerTest — score tracking, timer, room tracking, reset
MazeTest — maze dimensions, room access, entrance/exit setup
SaveManagerTest — save/load/delete game state
QuestionFactoryTest — correct question subclass creation

-----------------------
Project Structure
src/
├── controller/       # Controllers (AppController, GameController, etc.)
├── model/            # Model classes (Maze, Room, Door, Player, etc.)
├── view/             # View classes (MazeView, GameMenuView, etc.)
├── test/model/       # Unit tests
├── images/           # Game images and pixel art
└── sounds/           # Background music files

-----------------------
Extra Credit Features

Pixel art graphics — custom day/night mode butterfly characters and maze backgrounds
Animated butterfly character — flapping animation as the player moves
Camera tracking — camera automatically follows and centers on the player
Leaderboard — tracks and displays top scores using SQLite
Background music — randomized playlist with volume control
Resume button — dynamically appears on the main menu when a save file exists
Night mode — dark mode toggle in settings changes the visual theme
Hint system — directional hints guide the player toward the exit
Timer restoration — save/load correctly restores the game timer from where it left off

-----------------------
Iteration History

Iteration One:

Set up Trivia Maze GitHub repository
Implemented Room, Door, and Maze classes
Connected Room and Door classes
Implemented GameController, MainGUI, GameMenuView, PlayerSetupView, SettingsView, Player, MenuController, SettingsController files
Added timer logic

Iteration Two:

Implemented SoundManager with randomized playlist and volume slider
Created SQLite trivia question database
Implemented Java database connection
Connected trivia questions to Door objects
Implemented Question model class
Fixed JPanel/View recreation bug
Pixel Art work begun

Iteration Three:

Implemented DatabaseManager class
Implemented QuestionDAO for random question retrieval
Updated Player model with room tracking and game-over logic
Implemented InstructionsView, PlayerController
Restructured layout with cleaner view management
Added character customization
Fixed disappearing MenuBar bug
Structure cleanup

Iteration Four:

Implemented Save Game State feature
Implemented trivia question popup for door interactions
Implemented player movement
Implemented butterfly player tracker
Implemented zoom feature
Displayed current room coordinates and door status
Implemented GenerateMaze and MazeView files
Dynamic scaling begun

Iteration Five:

Implemented LeaderboardEntry, LeaderboardDAO, LeaderboardView
Added leaderboard database table support
Fixed pre-unlocked door bug
Implemented 3 attempts system with permanent lock
Fixed question randomization
Fixed answer checking for multiple choice questions
Added 10 new trivia questions to the database
Implemented new question on wrong answer
Fixed correctly answered questions from repeating

Iteration Six:

Randomized exit door location on maze perimeter each new game
Exit door requires trivia question to unlock
Win condition when exit door trivia is answered correctly
Lose condition when exit door trivia fails all attempts
Dead end popup for non-exit perimeter doors
Camera auto-tracks and centers on player butterfly
Fixed old save file bug
Pixel art for night mode added
Timer display added to MazeView
Butterfly flap animation added
Fixed trivia question display bugs
Cleaned up button display and help menu
Game over logic when player permanently locks all accessible pathways
Fixed loadGame feature when no save files exist
Revised leaderboard functionality
Added more trivia questions to database
