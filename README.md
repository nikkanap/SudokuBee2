# SudokuBee2
This is the repository for Group 4's SudokuBee2 code.

## How to run the game:
Just run it like normal:
> javac SudokuBee.java
> java SudokuBee.java
Or click ctrl + alt + N

## Notes about the game:
1. Create Mode - it's for manually creating your own puzzle.
2. Game Mode (Play) - allows you to create a new game.
UPDATES:
- New Game leads to a Generation popup where you select the number of given grids and whether to start from an empty grid or not.
- Clicking EMPTY GRID allows the sudoku to generate new grids.
- Leaving it unclicked allows the user to manually enter a number of given grids. 
3. Opening the numbers popup for numbers selection - right click on your mouse on an empty sudoku grid (applies to both create and game mode)

## The program files:
- UI<name>.java - contains the UI of the program. Action listeners are not present inside here but inside SudokuBee.java (yeah, it's weird).
1. UIBoard - handles the graphics for the sudoku boards in the game.
2. UIExit - handles the various exit confirmations in the game (the "Are you sure you want to leave the game?" type of exit confirmations)
3. UIGame - handles the buttons present during the sudoku game (like "New Game", "Solve", "Help", "Options" and "Exit")
4. UIGenerate - (NOT PART OF ORIGINAL SudokuBee) handles the generation popup that shows when you click "Play" or "New Game".
5. UIHelp - just handles the help UI (v bazic).
6. UILoad - handles the UI for loading an existing sudoku .sav file 
7. UIOptions - just handles the UI for the options (also bazic).
8. UIPop - handles the numbers selection popup used during create mode or game mode.
9. UISolve - handles the UI for the solve settings selection.
10. UIStatus - just handles the status UI or buttons used during either create mode or game mode (its the buttons on top of the sudoku board).
- SudokuBee.java - the main program. This is where almost everything is present. 
- ABC.java - Artificial Bee Colony algorithm. (untouched)
- Animation.java - where the animation sequences during the solve and generate sudoku occurs. (untouched)
- Bee.java - a single sudoku solution.
- customSet.java - literally just a class extending the hashset lol. (untouched)
- EndsWithFilter.java - just searches for files with a specific suffix like ".sav". (untouched)
- Fitness.java - just a fitness function. (changed from receiving an int to a double)
- generalPanel.java - a parent class used by the UI<name>.java files for adding panels and other UI elements.
- GenerateSudoku.java - the program that generates the sudoku board for answering (removing numbers and setting specific ones as given numbers).
- GreedySelection.java - Greedy Selection algorithm. (untouched)
- LoadSudoku.java - handles the logic for loading an existing sudoku puzzle into the game. (untouched)
- PrintResult.java - (untouched)
- SaveSudoku.java - handles the logic for saving your current sudoku game into the save folder as a ".sav" file. (untouched)
- Subgrid.java - handles the subgrids within the puzzle. (untouched)
- SudokuSolver.java - an almost empty file lol. Prolly not even needed and can be deleted.
- Tunog.java - handles the sounds in the game (modified to remove deprecated APIs).

## Things to do:
### Task 1: SudokuBee Code Analysis
- [x] Write report (use own words, no plagiarism)
- [x] Create table: filenames/directories & their purposes
- [ ] Explain sub-grid constraint implementation with code snippet and line-by-line explanation
- [ ] Create diagram showing Sudoku puzzle representation in SudokuBee code
- [ ] Create table: general swarm intelligence procedures vs ABC algorithm code 

### Task 2: Development of SudokuBee2 Program
- [x] Modify SudokuBee source code and rename as SudokuBee2
- [x] Implement puzzle board sizes: 9x9 (default), 16x16, and 25x25
- [x] Save 16 Sudoku test instances in save directory
- [x] Add option for user to choose penalty function during fitness evaluation:
  - [x] (default) Missing numbers with intrinsic sub-grid constraint
  - [x] Sum-Product with intrinsic sub-grid constraint
  - [x] Sum-Product without intrinsic sub-grid constraint
- [x] Implement random puzzle generation options:
  - [x] Percentage of given cells (0%-95%, default 10%, increments of 5%)
  - [x] Option to start from empty board or user-provided partially filled puzzle
  - [x] Verify puzzles for validity against Sudoku rules
  - SIDE QUESTS:
    - [x] Make cancel when creating new game within sudoku just go back to the game not main menu
- [ ] Modify experiment mode to:
  - [ ] Record time in seconds until stop (whether solved or not)
  - [ ] Record maximum cycles until stop (whether solved or not)
  - [ ] Save given puzzle and corresponding solution (if found)
- [ ] Package all modified files into SudokuBee2.zip

### Task 3: Testing SudokuBee2 Program
- [ ] Run experiment mode on 16 Sudoku test instances, 5 trials each
- [ ] Test each penalty function 5 times per instance
- [ ] Record for each run:
  - [ ] Name of Sudoku test instance
  - [ ] Board size
  - [ ] Solved or not solved
  - [ ] Time in seconds before stopping
  - [ ] Maximum cycle before stopping
  - [ ] Given Sudoku puzzle (text file)
  - [ ] Solution to puzzle (text file), if solved
- [ ] Write report answering questions using own words:
  - [ ] Screenshot of modified SudokuBee2 UI
  - [ ] Code snippets for penalty function choices and calling class/method
  - [ ] Explain with illustration how to determine a "given cell" with code snippet
  - [ ] Summarize results in a table (exclude unsolved runs from averages)
  - [ ] Compute statistics (mean, median, min, max, std dev) on time and cycles excluding unsolved runs
- [ ] Use proper filename format: GroupX_A22.docx

### Submission Guidelines
- [ ] Submit GroupX_A21.docx, GroupX_A22.docx, SudokuBee2.zip via email
- [ ] Use proper email subject and include group members' names
- [ ] Submit by deadline (October 27, 2025, midnight)