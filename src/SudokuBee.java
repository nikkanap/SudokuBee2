import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Container;
import java.awt.Desktop;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;

public class SudokuBee extends Thread{
	private generalPanel GP;
	private UIGame game;
	private UIGenerate generation;
	private UIExit exit;
	private UIBoard board;
	private UIStatus status;
	private UIPop pop;
	private UIOptions options;
	private UISave save;
	private UISolve solve;
	private UILoad load;
	private UIHelp help;
	private int btnX, btnY;
	private int numOnlook, numEmp, numCycle, percentOfGivenCells, chosenGrid;

	// isAns = indicates whether the board is in answer sheet mode or puzzle creation state
	private boolean isAns = false, generate = true, startFlag =  false, gameMode = true, isSolved = false;
	private final Tunog snd;
	private final Tunog error;
	private final JFrame frame = new JFrame();
	private final Container container = frame.getContentPane();
	private String saveFileName = "";
	private String loadFileName = "";

	public SudokuBee() {
		frame.setTitle(" Sudoku Bee 2");
		snd = new Tunog("snd/1.mid");
		error = new Tunog("snd/error.wav");
		snd.loop();

		// opens up the menu
		menu();

		// opens up the options
		options();

		// JFrame setup
		frame.setVisible(true);
		frame.setSize(800,625);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// generates the main menu UI
	private void menu() {
		// opening up the general panel for a bunch of stuff
		GP = new generalPanel(container);

		// action listener for the play button
		GP.play.addActionListener(_ -> { 
			generation(); 
		});

		// action listener for the load button
		GP.open.addActionListener(_ -> {
			// hidings the main menu buttons
			GP.setVisibleButton(false);

			isSolved = false;
			int size = (int) (Math.pow(options.sz + 3, 2));

			// generates a sudoku board
			board(new int[size][size][2], true);

			// loads a sudoku game from the saves directory
			loadSudoku(7, 0);
		});

		// action listener for the create button
		GP.create.addActionListener(_ -> {
			// opens up the sudoku game background
			mainGame();

			isAns = false;
			isSolved = false;
			int size = (int) (Math.pow(options.sz + 3, 2));
			
			// generates a sudoku board
			board(new int[size][size][2], true);
			game.setVisible(false);
			status("create");

			// opens the popup for numbers selection
			popUp(size);
		});

		// action listener for the options button
		GP.options.addActionListener(_ -> {
			// hides the main menu buttons
			GP.setVisibleButton(false);

			// makes the options UI visible, specifically to page 1 of options
			options.refreshPage(1);
			options.setVisible(true, 0);
		});

		// action listener for the help button
		GP.help.addActionListener(_ -> {
			help(7); // open up helpUI
		});

		// action listener for the exit button
		GP.exit.addActionListener(_ -> {
			// hides the main menu buttons
			GP.setVisibleButton(false);
			exit(0); // opens up exit UI 0
		});
	}

	// for loading a saved sudoku game
	private void loadSudoku(int num, int type) {
		// keeps a specific element in the GeneralPanel visible
		GP.setVisible(num);

		// loading the UILoad on the GP.solve panel
		load = new UILoad(GP.solve); // don't question this. I did and it still doesn't make sense - Nikka
		load.lists.grabFocus();
		final int number = num;

		try {
			status.setVisible(false);
		} catch (Exception e) {}

		// action listener to cancel button 
		load.cancel.addActionListener(_ -> {
			try {
				// make both the game and status UI visible
				game.setVisible(true);
				status.setVisible(true);
			} catch (Exception e) {}

			// hides the main menu buttons
			GP.setVisibleButton(true);

			// decompose the load UI
			load.decompose();
			load = null;

			// make a specific or set of panels visible
			GP.setVisible(number);
		});

		// action listener to load button
		load.load.addActionListener(_ -> {
			// open up the specific file
			open(load.lists.getSelectedValue() + "");
			
			// decompose the load UI
			load.decompose();
			load = null;

			if (type == 1) 
				statusFunction(11);
		});
	}

	// function used in loadSudoku()
	private void open(String str) {
		LoadSudoku sod = new LoadSudoku("save/"+str+".sav");
		loadFileName = str;

		if (sod.getStatus()) {
			try {
				board.decompose();
				game.decompose();
			} catch (Exception e) {}

			mainGame();
			status("");
			isAns = true;
			board = null;
			board(sod.getArray(), false);
			popUp(sod.getSize());
		}
		else 
			exit(3);

		sod = null;
	}

	// generates the sudoku board
	private void board(int sudokuArray[][][], boolean isNull) {
		// load panel 5 (panel for the board)
		GP.setVisible(5); 

		// create the board UI
		board = new UIBoard(sudokuArray, isNull, GP.panel[5]);
		int size = board.getSize();

		for(btnX = 0; btnX<size; btnX++) {
			for(btnY = 0; btnY<size; btnY++) {
				if (board.getStatus(btnX, btnY) != 0) {
					final int x = btnX;
					final int y = btnY;

					// action listener when user right clicks their mouse on a grid
					board.btn[btnX][btnY].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if (isSolved || e.getButton() != MouseEvent.BUTTON3) {
								return;
							}
							// make the popup visible for a certain grid
							pop.setVisible(true, x, y, board.getValue(x, y));
							
							// make the status and the game invisible
							status.setVisible(false);
							game.setVisible(false);
						}
					});
				}
			}
		}
	}
	
	// generates the popUp for answer (number) selection and erasing
	private void popUp(int size) {
		// just a try catch in case popup is null
		try {
			pop.decompose();
			pop = null;
		} catch (Exception e) {}

		// generates a popup UI for the selection of numbers
		pop = new UIPop(size, GP.panel[3]);
		for(int ctr = 0; ctr<size; ctr++) {
			final int popCounter = ctr+1;

			pop.btn[ctr].addActionListener(_ -> {
				popupFunction(size, popCounter);
			});
		}

		// basically key actions when the user enters something into the field in the popup
		pop.field.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				String str = pop.field.getText();

				// if not valid input
				if (str.length() > 2 || !(e.getKeyCode() > 47 && e.getKeyCode() < 58 || e.getKeyCode() > 95 && e.getKeyCode() < 106 || e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) ) {
					try {
						pop.field.setText(str.substring(0, str.length() - 1));
					} catch (Exception ee) {}
				} // if valid input (numbers)
				else if (e.getKeyCode() > 47 && e.getKeyCode() < 58 || e.getKeyCode() > 95 && e.getKeyCode() < 106 || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					try {
						// if the value of str is greater than the no. of valid numbers in the puzzle, display red
						if (Integer.parseInt(str) > pop.size){ 
							pop.field.setForeground(java.awt.Color.red);
							return;
						}
						pop.field.setForeground(java.awt.Color.black);
					} catch (Exception ee) {}
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				// if ENTER KEY is pressed
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String str = pop.field.getText();
					
					// if nothing was entered
					if (str.length() == 0) {
						// change the number on the grid to a nothing value
						GP.changePicture(board.btn[pop.btnX][pop.btnY],"img/box/"+pop.size+"x"+pop.size+"/normal/0.png");
						
						// updates the sudoku array
						board.setSudokuArray(0, false, pop.btnX, pop.btnY);
						
						// closes the popup and reopens the game and status
						pop.setVisible(false,0,0,0);
						status.setVisible(true);
						game.setVisible(isAns);
						return;
					}

					// if something was entered
					try {
						int size = pop.size, num = Integer.parseInt(str);
						
						// if the value entered was invalid
						if (num > size || num < 1)
							throw new Exception();

						// if value enterd was valid (1 - 9 if size = 9), then we add that answer to the board and sudoku array
						popupFunction(size, num);
					} catch (Exception ee) {
						error.play();
					}
				}
			}
		});

		// for erasing answers
		pop.erase.addActionListener(_ -> {
			GP.changePicture(board.btn[pop.btnX][pop.btnY],"img/box/"+pop.size+"x"+pop.size+"/normal/0.png");
			board.setSudokuArray(0, false,pop.btnX, pop.btnY);
			pop.setVisible(false,0,0,0);
			status.setVisible(true);
			game.setVisible(isAns);
		});

		// closing the popup
		pop.cancel.addActionListener(_ -> {
			pop.setVisible(false,0,0,0);
			status.setVisible(true);
			game.setVisible(isAns);
		});
	}

	private void popupFunction (int size, int num) {
		// change the value of the selected grid on the board 
		GP.changePicture(board.btn[pop.btnX][pop.btnY],"img/box/"+size+"x"+size+"/normal/"+num+".png");
		
		// update the sudoku array
		board.setSudokuArray(num, isAns,pop.btnX, pop.btnY);
		pop.field.setForeground(java.awt.Color.black);

		// closes the popup and reopens the status and game
		pop.setVisible(false,0,0,0);
		status.setVisible(true);
		game.setVisible(isAns);

		// if in answer mode and the number of answers equals the size of the board, we validate the answers
		if (!isAns || board.getAns() != size*size) {
			return;
		}

		int sudoku[][][] = board.getSudokuArray();
		Subgrid subgrid[] = new Subgrid[sudoku.length];
		int subDimY = (int)Math.sqrt(sudoku.length);
		int subDimX = sudoku.length/subDimY;
		
		for(int ctr = 0, xCount = 0; ctr<sudoku.length; ctr++, xCount++) {
			subgrid[ctr] = new Subgrid(xCount*subDimX, ((ctr/subDimY)*subDimY), subDimX, subDimY);
			if ((ctr+1)%subDimY != 0 || ctr>0)
				continue;
			xCount =- 1;
		}
		
		if (!(new Validator(sudoku, subgrid).checkAnswer()))
			return;
		exit(5);
	}

	// generating the game panel
	private void mainGame() {
		// generate the UIGame in panel 6 and make it visible
		GP.setVisible(6);
		game = new UIGame(GP.panel[6]);

		// playing a new game 
		game.newGame.addActionListener(_ -> {
			// hide the game and status buttons
			game.setVisible(false);
			status.setVisible(false);
			isSolved = false;

			// then head to exit type 2 (returning to the game)
			exit(2);
		});

		// exiting the game (not the program)
		game.exit.addActionListener(_ -> {
			// hide the game and status buttons
			game.setVisible(false);
			status.setVisible(false);

			// then head to exit type 1 (returning to main menu)
			exit(1);
		});

		// opening the optionsUI
		game.options.addActionListener(_ -> {
			// hide the game and status buttons
			game.setVisible(false);
			status.setVisible(false);

			// refreshing back to page 1 of options and making options UI visible
			options.refreshPage(1);
			options.setVisible(true,1);
		});

		// solving the game
		game.solve.addActionListener(_ -> {
			// hiding the game and status buttons
			game.setVisible(false);
			status.setVisible(false);

			// then solving the board
			game.solve.setEnabled(false);
			solve();
			game.solve.setEnabled(true);
		});

		// opening helpUI
		game.help.addActionListener(_ -> {
				help(5);
		});
	}

	// generating the helpUI and making it visible
	private void help(int num) {
		// adding helpUI to panel 0 and making it visible
		GP.setVisible(0);
		help = new UIHelp(GP.panel[0], num);

		// going to the next instruction
		help.next.addActionListener(_ -> {
			help.increase();
		});

		// going to the previous instruction
		help.back.addActionListener(_ -> {
			help.decrease();
		});

		// leaving the helpUI
		help.cancel.addActionListener(_ -> {
			help.decompose();
			GP.setVisible(help.panelNum);
			help = null;
		});
	}

	// for solving the sudoku puzzle
	private void solve() {
		// opening the solve UI
		solve = new UISolve(GP.solve);

		// leaving the solveUI
		solve.cancel.addActionListener(_ -> {
			// make the status and game buttons visible
			status.setVisible(true);
			game.setVisible(true);

			// decompose solve
			solve.decompose();
			solve = null;

			// make specific panels visible
			GP.setVisible(5);
		});

		// this changes when the user clicks on "Experiment Mode" in the solveUI
		solve.mode.addActionListener(_ -> {
			solve.changeMode();
		});

		// processes the solving of the sudoku board
		solve.solve.addActionListener(_ -> {
			status.setVisible(false);
			try {
				numEmp = Integer.parseInt(solve.numEmployed.getText());
				numOnlook = Integer.parseInt(solve.numOnlook.getText());
				numCycle = Integer.parseInt(solve.numCycles.getText());
				generate = false;
				status.setVisible(false);

				if (numEmp >= numOnlook || numEmp<2)
					throw new Exception();
				
				gameMode = (solve.modeNum == 0) ? true : false;

				try {
					start();
				} catch (Exception e) {
					startFlag = true;
				}
			} catch (Exception e) {
				solve.decompose();
				solve = null;
				GP.setVisible(5);
				exit(7);
			}
		});
	}

	// THREAD method: runs when start() is called
	@Override
	public void run() {
		while(true) {
			try {
				solve.decompose();
				solve = null;
			} catch (Exception e) {}
			game.setVisible(1);
			
			// if in game mode and not creation mode
			if (gameMode) {
				status.setVisible(false);

				// create a new result file
				PrintResult printer = new PrintResult("results/.xls");
				int sudoku[][][] = board.getSudokuArray();
				ABC abc = new ABC(printer, sudoku, numEmp, numOnlook, numCycle, options.fp);
				
				// run the generation animation at the special panel
				Animation animate = new Animation(sudoku, GP.special);
				board.decompose();
				board = null;

				// Set the result file name in ABC provided that
				// there is actually a load filename
				if (!loadFileName.isBlank()) {
					abc.setResultName(loadFileName);
				}

				// start the artificial bee colony thread
				abc.start(); 

				// delay cause yes
				delay(100);

				// keep changing the pic if abc isn't done every 100ms (methinks its ms)
				while(!abc.isDone()) {
					delay(100);
					animate.changePic(abc.getBestSolution());
				}

				int[][][] finalBoard = abc.getBestSolution();
				
				animate.decompose();
				animate = null;

				// if generating a game rather than loading an existing game
				if (generate) {
					// generate a sudoku board with empty grids and
					// display it in the board

					GenerateSudoku gen = new GenerateSudoku(abc.getBestSolution(), percentOfGivenCells, chosenGrid);
					board(gen.getSudoku(), false);

					gen = null;
					isSolved = false;
					abc = null;
				} 
				// if we're loading an existing game
				else {
					// if existing game is already solved, go to the exitUI and display the finished game
					if (abc.getFitness() == 1) {
						exit(8);
						board = new UIBoard(abc.getBestSolution(), GP.panel[5]);
					}
					// just show the partially completed puzzle
					else {
						board(abc.getBestSolution(), false);
						isSolved = false;
					}

					abc = null;
				}

				printer.close();
				printer.delete();
				printer = null;
				status.setVisible(true);
			} 
			// if we're creating our own puzzle
			else {
				// create a new result file for saving the created puzzle
				String file = "results/result.xls";
				PrintResult printer = new PrintResult(file);

				status.setVisible(false);
				String cycle = "", time = "";

				// start the ABC 
				ABC abc = new ABC(printer, board.getSudokuArray(),numEmp,numOnlook, numCycle, options.fp);
				abc.start();
				double startTime = printer.getTime();

				// wait until ABC thread is done running
				while(!abc.isDone()) {
					delay(100);
				};

				double end = (printer.getTime());
				double seconds = ((end-startTime)/1000);

				// update the time
				printer.print("\nCycles:\t "+abc.getCycles()+"\nTime:\t"+seconds);
				printer.close();
				printer = null;

				game.setVisible(0);
				
				try {
					Desktop.getDesktop().open(new File(file));
				} catch (IOException e) {}
				
				board.decompose();
				board = null;

				// if the board is finished/solved, open the exitUI and display the finished board
				if (abc.getFitness() == 1) {
					exit(8);
					board = new UIBoard(abc.getBestSolution(), GP.panel[5]);
				} 
				// else, just show the partially completed puzzle
				else {
					board(abc.getBestSolution(), false);
					isSolved = false;
				}

				abc.decompose();
				abc = null;
				status.setVisible(true);
			}

			game.setVisible(0);
			startFlag = false;

			// added delay to have something inside of the loop  while waiting for startFlag to become true
			while(!startFlag) {
				delay(100);
			}
		}
	}

	// just a delay method
	protected void delay(int newDelay) {
		try {
			sleep(newDelay);
		} catch (InterruptedException err) {}
	}

	// handles the statusIU during the game (save game, open a game, reset the game, etc.)
	private void status(String str) {
		// open the statusIU on panel 4
		status = new UIStatus(str, GP.panel[4]);
		// yes and no buttons shows up when clicking create game
		// save, reset, and load buttons show up when in game mode

		// yes1 - for pure create mode; generate the manually created puzzle
		status.yes1.addActionListener(_ -> {
			int sudoku[][][] = board.getSudokuArray();
			Subgrid subgrid[] = new Subgrid[sudoku.length];
			int subDimY = (int)Math.sqrt(sudoku.length);
			int subDimX = sudoku.length/subDimY;
			
			for(int ctr = 0, xCount = 0; ctr<sudoku.length; ctr++, xCount++) {
				subgrid[ctr] = new Subgrid(xCount*subDimX, ((ctr/subDimY)*subDimY), subDimX, subDimY);
				if ((ctr+1)%subDimY != 0 && ctr>0)
					continue;
				xCount =- 1;
			}

			Validator val = new Validator(sudoku, subgrid);
			// if the created sudoku puzzle is valid, then solidify the generated puzzle
			if (val.checkValidity()) {
				isAns = true;

				status.decompose();
				pop.decompose();
				board.decompose();
				status = null;
				pop = null;
				board = null;

				// display the empty sudoku board
				board(sudoku,false);
				game.setVisible(true);

				// generate the popUpIU
				popUp(sudoku.length);
				status("");
			}
			else 
				exit(4); // display "Incorrect Puzzle"
			
			val = null;
			isSolved = false;
		});

		// no1 - exit the puzzle creation and return to main menu
		status.no1.addActionListener(_ -> {
			exit(1);
		});

		// yes2 - for generation mode; create the generated puzzle according to specs
		status.yes2.addActionListener(_ -> {
			statusFunction(4);
		});

		// no2 - exit the generation and return to main menu
		status.no2.addActionListener(_ -> {
			exit(1);
		});

		// open the loadUI 
		status.open.addActionListener(_ -> {
			// hide the game main buttons and the status buttons
			game.setVisible(false);
			status.setVisible(false);
			isSolved = false;

			// loading an existing game
			loadSudoku(5, 0);
		});

		// open the saveUI
		status.save.addActionListener(_ -> {
			// hiding the game main buttons and the status buttons
			game.setVisible(false);
			status.setVisible(false);

			// saving the current game
			save();
		});

		// open the resetUI
		status.reset.addActionListener(_ -> {
			// hiding the game main buttons and the status buttons
			game.setVisible(false);
			status.setVisible(false);

			// opening exit type 10 (for resetting the game)
			exit(10);
		});
	}

	private void statusFunction(int exitNum) {
		int sudoku[][][] = board.getSudokuArray();
		Subgrid subgrid[] = new Subgrid[sudoku.length];
		int subDimY = (int)Math.sqrt(sudoku.length);
		int subDimX = sudoku.length/subDimY;
		
		for(int ctr = 0, xCount = 0; ctr<sudoku.length; ctr++, xCount++) {
			subgrid[ctr] = new Subgrid(xCount*subDimX, ((ctr/subDimY)*subDimY), subDimX, subDimY);
			if ((ctr+1) % subDimY != 0 && ctr>0)
				continue;
			xCount =- 1;
		}

		Validator val = new Validator(sudoku, subgrid);
		if (!val.checkValidity()) { // display "Incorrect Puzzle"
			exit(exitNum);
			return;
		}

		int currentGiven = 0;
		int n = sudoku.length;
		int totalCells = n * n;
		int numGiven = (int) Math.round(totalCells * (percentOfGivenCells / 100.0));

		// count existing givens (non-zero values)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (sudoku[i][j][1] == 0) {
                    currentGiven++;
				}
            }
        }

		if (numGiven < currentGiven) {
			exit(exitNum);
			return;
		}

		// if the created sudoku puzzle is valid, then solidify the generated puzzle
		isAns = true;
		status.decompose();
		status = null;
		pop.decompose();
		pop = null;
		
		status("");

		numEmp = 100;
		numOnlook = 200;
		numCycle = 100000000;
		generate = true;
		gameMode = true;
		isSolved = false;

		// starting the animation sequence
		try {
			start();
		} catch (Exception e) {
			startFlag = true;
		}

		game.setVisible(true);
		sop(game != null);
		// generates the popup for the numbers selection
		int size = (int) (Math.pow(options.sz + 3, 2));
		popUp(size);
	}

	// handles saving the current sudoku game
	private void save() {
		// load the saveUI to panel 2
		save = new UISave(GP.panel[2]);

		// focus on save.field element
		save.field.grabFocus();

		// hiding the game buttons and the status buttons
		status.setVisible(false);
		game.setVisible(false);

		// cancelling brings u back to the sudoku game
		save.cancel.addActionListener(_ -> {
			// make game and status buttons visible
			game.setVisible(true);
			status.setVisible(true);

			// decompose save UI
			save.decompose();

			// make certain panels visible
			GP.setVisible(5);
		});

		// save the current game (as .sav file)
		save.save.addActionListener(_ -> {
			saveFunction();
		});

		// key listener for the textfield in save UI
		save.field.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					saveFunction();
				}
			}
		});
	}

	private void saveFunction() {
		saveFileName = save.field.getText();
		SaveSudoku saving = new SaveSudoku();
		int num = saving.save(saveFileName, board.getSudokuArray());
		save.decompose();
		GP.setVisible(5);

		if (saveFileName.length()>0 && !(saveFileName.contains("/") || saveFileName.contains("\\") || saveFileName.contains(":")  || saveFileName.contains("*") || saveFileName.contains("?")  || saveFileName.contains("\"") || saveFileName.contains("<") || saveFileName.contains(">"))&& num == 0) {
			game.setVisible(true);
			status.setVisible(true);
			saveFileName = "";
		}
		else if (saveFileName.length() == 0 && (saveFileName.contains("/") || saveFileName.contains("\\") || saveFileName.contains(":")  || saveFileName.contains("*") || saveFileName.contains("?")  || saveFileName.contains("\"") || saveFileName.contains("<") || saveFileName.contains(">")) ||  num==1) {
			exit(6);
			status.setVisible(false);
			game.setVisible(false);
			saveFileName = "";
		}
		else {
			status.setVisible(false);
			game.setVisible(false);
			exit(9);
		}
	}

	// handles the exitUI
	private void exit(int num) {
		// exit gets turned to null each time it's called, so we have to check if exit is null
		if (exit != null) 
			return;

		// if exit is null, proceed
		exit = new UIExit(GP.panel[0], num);

		// pressing yes on any exit yes button 
		exit.yes.addActionListener(_ -> {
			GP.setVisibleButton(true);
			try {
				game.setVisible(true);
				status.setVisible(true);
			} catch (Exception e) {}

			// decompose the exit UI
			exit.decompose();
			
			// select where to proceed based on exit type
			switch (exit.num) {
				case 0 -> // exiting/ending the program
					System.exit(0);
				case 1 -> { // exiting the ongoing sudoku game
					try {
						board.decompose();
						game.decompose();
						status.decompose();
						pop.decompose();
					} catch (Exception e) {}
					
					board = null;
					game = null;
					status = null;
					pop = null;
					
					GP.setVisible(7);
				}
				case 2 -> generation();
				case 9 -> {
					GP.setVisible(5);
					
					SaveSudoku saving = new SaveSudoku();
					saving.delete(saveFileName);
					saving.save(saveFileName, board.getSudokuArray());
				}
				case 10 -> {
					isSolved = false;
					GP.setVisible(5);
					board.changePic();
					int[][][] sudoku = board.getSudokuArray();
					
					board.decompose();
					board = null;
					board(sudoku, false);
				}
				default -> {}
			}
			exit = null;
		});

		// pressing no on any exit button
		exit.no.addActionListener(_ -> {
			GP.setVisibleButton(true);
			try {
				game.setVisible(true);
				status.setVisible(true);
			} catch (Exception e) {}

			// decompose exit UI
			exit.decompose();
			
			switch (exit.num) {
				case 0 -> // just return back to mainMenu
					GP.setVisible(7);
				case 1, 2, 6, 10 -> // just return back to the sudoku game
					GP.setVisible(5);
				case 9 -> {
					// just return to the saveUI (with the sudoku game in the bg)
					GP.setVisible(5);
					save();
				}
				default -> {}
			}
			exit = null;
		});
		
		exit.okay.addActionListener(_ -> {
			GP.setVisibleButton(true);
			game.setVisible(true);
			status.setVisible(true);
			
			switch (exit.num) {
				case 7 -> // open the solveUI and 
					solve();
				case 5 -> {
					// game is solved so show the finished board and change the cursor
					GP.setVisible(5);
					isSolved = true;
					board.changeCursor();
				}
				case 8 -> {
					// game is solved so show the finished board
					GP.setVisible(5);
					isSolved = true;
				}
				case 1, 2, 3, 9 -> {
					// num is 1, 2, 3, and 9, close the board and return to main menu
					board.decompose();
					board = null;
					GP.setVisible(7);
				}
				case 4 -> {
					sop("ENTERED CASE 4");
					// returns to the game panel (panel 5) without opening the game buttons
					GP.setVisible(5);
					game.setVisible(false);
				}
				case 11 -> {
					sop("ENTERED CASE 11");
					board.decompose();
					board = null;

					GP.setVisible(7);
				}
				default -> // exit.num == 6: returns to the game panel with game buttons
					GP.setVisible(5);
			}

			// decompose exit UI and make exit UI null
			exit.decompose();
			exit = null;
		});
	}

	// handles the optionsUI and makes it visible
	private void options() {
		options = new UIOptions(GP.panel);

		// exiting the optionsUI
		options.exit.addActionListener(_ -> {
			// make game and status buttons visible
			try {
				game.setVisible(true);
				status.setVisible(true);
			} catch (Exception e) {}

			// make main menu buttons visible
			GP.setVisibleButton(true);

			// make a specific panel visible
			GP.setVisible((options.num == 0) ? 7 : 5);
		});

		// action listeners for the left and right buttons
		// for setting the grid size (6x6, 9x9, etc.)
		options.left[0].addActionListener(_ -> {
			options.setUpperOption(false);
		});

		options.right[0].addActionListener(_ -> {
			options.setUpperOption(true);
		});

		// action listeners for the left and right buttons
		// for setting the sound of the program
		options.left[1].addActionListener(_ -> {
			options.setLowerOption(false);
			soundFunction();
		});
		
		options.right[1].addActionListener(_ -> {
			options.setLowerOption(true);
			soundFunction();
		});

		options.rightNav.addActionListener(_ -> {
			options.increase();
		});

		options.leftNav.addActionListener(_ -> {
				options.decrease();
		});
	}

	private void soundFunction() {
		if (options.page != 1) {
			return;
		}

		if (options.snd == 1)
			snd.stop();
		else
			snd.loop();
	}

	// for solving the sudoku puzzle
	private void generation() {
		try {
			// hides the main menu buttons
			GP.setVisibleButton(false);

			// hides the game and status buttons
			game.setVisible(false);
			status.setVisible(false);
		} catch (Exception e) {}
		generation = new UIGenerate(GP.solve);

		generation.okay.addActionListener(_ -> {
			chosenGrid = generation.chosen;
			percentOfGivenCells = generation.sliderValue;
			
			generation.decompose();
			generation = null;
			try {
				board.decompose();
				game.decompose();
				status.decompose();
				pop.decompose();
			} catch (Exception e) {}
			board = null;
			game = null;
			status = null;
			pop = null;

			// if generating from an empty grid
			if (chosenGrid == 1) {
				// opens up the sudoku game background
				mainGame(); 

				// change status to game mode
				status("");
				isAns = true;
				int size = (int) (Math.pow(options.sz + 3, 2));

				// generates the sudoku board
				board(new int[size][size][2], true);

				numEmp = 100;
				numOnlook = 200;
				numCycle = 100000000;
				generate = true;
				gameMode = true;
				isSolved = false;

				// starting the animation sequence
				try {
					start();
				} catch (Exception e) {
					startFlag = true;
				}

				// generates the popup for the numbers selection
				popUp(size);
				return;
			} 
			else if (chosenGrid == 2) {
				// else generating from a manually entered grid
				// opens up the sudoku game background
				mainGame();

				isAns = false;
				int size = (int) (Math.pow(options.sz + 3, 2));
				isSolved = false;
				
				// generates a sudoku board
				board(new int[size][size][2], true);
				game.setVisible(false);

				// change status to fill mode
				status("fill");

				// open up the popup UI for numbers selection
				popUp(size);
			} else {
				loadSudoku((game == null) ? 7 : 5, 1);
			}
		});

		generation.cancel.addActionListener(_ -> {			
			generation.decompose();
			generation = null;

			if (game == null) {
				GP.setVisibleButton(true);
				return;
			} 

			game.setVisible(true);
			status.setVisible(true);
			GP.setVisible(5);
		});

		generation.empty.addActionListener(_ -> {
			generation.changeMode(1);
		});

		generation.manual.addActionListener(_ -> {
			generation.changeMode(2);
		});

		generation.load.addActionListener(_ -> {
			generation.changeMode(3);
		});

		generation.genSlider.addChangeListener(_ -> {
			generation.setSliderValue();
		});
	}

	// shortcut for printing (System.out.println = sop)
	private void sop(Object obj) {
		System.out.println(obj+"");
	}

	public static void main(String args[]) {
		new SudokuBee();
	}
}
