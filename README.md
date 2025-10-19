# SudokuBee2
This is the repository for Group 4's SudokuBee2 code.

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
    - [ ] Make cancel when creating new game within sudoku just go back to the game not main menu
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