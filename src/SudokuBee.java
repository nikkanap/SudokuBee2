import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Container;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;

public class SudokuBee extends Thread{
	private generalPanel GP;
	private UIGame game;
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
	private int numOnlook, numEmp, numCycle;

	// isAns = indicates whether the board is in answer sheet mode or puzzle creation state
	private boolean isAns = false, generate = true, start = false, gameMode = true, isSolved = false;
	private Tunog snd, error;
	private JFrame frame = new JFrame();
	private Container container = frame.getContentPane();
	private String saveFileName = "";

	public SudokuBee(){
		frame.setTitle(" Sudoku Bee");
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
	private void menu(){
		// opening up a general panel for a bunch of stuff
		GP = new generalPanel(container);

		// action listener for the play button
		GP.play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// opens up the sudoku game background
				mainGame(); 
				status("");
				isAns = true;
				int size = (options.sz+2)*3;

				// generates the sudoku board
				board(new int[size][size][2], true);

				numEmp = 100;
				numOnlook = 200;
				numCycle = 100000000;
				generate = true;
				gameMode = true;
				isSolved = false;
				
				// starting the animation sequence
				try{
					start();
				} catch(Exception ee){
					start = true;
				}

				// generates the popup for the numbers selection
				popUp(size);
				
			}
		});

		// action listener for the load button
		GP.open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// hides the main menu buttons
				GP.setVisibleButton(false);

				int size = (options.sz + 2) * 3;
				isSolved = false;

				// generates a sudoku boartd
				board(new int[size][size][2], true);

				// loads a sudoku game from the saves directory
				loadSudoku(7);
			}
		});

		// action listener for the create button
		GP.create.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// opens up the sudoku game background
				mainGame();

				isAns = false;
				int size = (options.sz+2)*3;
				isSolved = false;
				
				// generates a sudoku board
				board(new int[size][size][2], true);
				game.setVisible(false);
				status("create");

				popUp(size);
			}
		});

		// action listener for the options button
		GP.options.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// hides the main menu buttons
				GP.setVisibleButton(false);

				// makes the options UI visible
				options.setVisible(true, 0);
			}
		});

		// action listener for the help button
		GP.help.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// just opens up the help UI
				help(7);
			}
		});

		// action listener for the exit button
		GP.exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// hides the main menu buttons
				GP.setVisibleButton(false);

				// opens up the exit UI
				exit(0);
			}
		});
	}

	// for loading a saved sudoku game
	// USED IN menu() and status()
	private void loadSudoku(int num){
		// keeps the main menu on
		GP.setVisible(num);

		// loading the UILoad on the GP.solve panel
		load = new UILoad(GP.solve); // don't question this. I did and it still doesn't make sense - Nikka
		load.lists.grabFocus();
		final int number = num;

		try{
			status.setVisible(false);
		} catch(Exception e){}

		// action listener to cancel button 
		load.cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					// make both the game and status UI visible
					game.setVisible(true);
					status.setVisible(true);
				}
				catch(Exception ee){}
				// hides the main menu buttons
				GP.setVisibleButton(true);

				load.decompose();
				load = null;

				// makes a specific or set of panels visible
				GP.setVisible(number);
			}
		});

		// action listener to load button
		load.load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				open(load.lists.getSelectedValue()+"");
				load.decompose();
				load = null;
			}
		});
	}

	// USED IN loadSudoku()
	private void open(String str){
		LoadSudoku sod = new LoadSudoku("save/"+str+".sav");

		if (sod.getStatus()){
			board.decompose();
			try{
				game.decompose();
			} catch(Exception e){}

			mainGame();
			status("");
			isAns = true;
			board = null;
			board(sod.getArray(), false);
			popUp(sod.getSize());
		}
		else {
			exit(3);
		}

		sod = null;
	}

	// generates the sudoku board
	// USED IN menu(), open(), run(), status(), exit()
	private void board(int sudokuArray[][][], boolean isNull){
		// load panel 5 (most likely panel for the board)
		GP.setVisible(5); // originally 5; 

		// create the board UI
		board = new UIBoard(sudokuArray, isNull, GP.panel[5]);
		int size = board.getSize();

		for(btnX = 0; btnX<size; btnX++){
			for(btnY = 0; btnY<size; btnY++){
				if(board.getStatus(btnX, btnY) != 0){
					final int x = btnX;
					final int y = btnY;

					// action listener when user right clicks their mouse on a grid
					board.btn[btnX][btnY].addMouseListener(new MouseAdapter(){
						public void mouseClicked(MouseEvent e){
							// NOTE!!! modify to something not deprecated
							if(!isSolved && e.getButton() == MouseEvent.BUTTON3){
								// make the popup visible
								pop.setVisible(true, x, y, board.getValue(x, y));
								
								// make the status and the game invisible
								status.setVisible(false);
								game.setVisible(false);
							}
						}
					});
				}
			}
		}
	}
	
	// generates the popUp for answer (number) selection and erasing
	// USED IN menu(), open(), status(), exit() 
	private void popUp(int size){
		try{
			pop.decompose();
			pop = null;
		} catch(Exception e){}

		// generates a popup UI for the selection of numbers
		pop = new UIPop(size, GP.panel[3]);
		for(int ctr = 0; ctr<size; ctr++){
			final int popCounter = ctr+1;

			pop.btn[ctr].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					int size = pop.size;

					// set the picture of the number selected to the board
					GP.changePicture(board.btn[pop.btnX][pop.btnY],"img/box/"+size+"x"+size+"/normal/"+popCounter+".png");
					board.setSudokuArray(popCounter, isAns, pop.btnX, pop.btnY);
					pop.field.setForeground(java.awt.Color.black);

					// hide the popup
					pop.setVisible(false,0,0,0);

					// open up the game and the status
					status.setVisible(true);
					game.setVisible(isAns);

					// TO BE CORRECTED: if the number of answered blocks equals the size of the board,
					// then we validate the answers
					if(isAns && board.getAns() == size*size){
						int sudoku[][][] = board.getSudokuArray();
						Subgrid subgrid[] = new Subgrid[sudoku.length];
						int subDimY = (int)Math.sqrt(sudoku.length);
						int subDimX = sudoku.length/subDimY;

						for(int ctr = 0, xCount = 0; ctr<sudoku.length; ctr++, xCount++){
							subgrid[ctr] = new Subgrid(xCount*subDimX, ((ctr/subDimY)*subDimY), subDimX, subDimY);

							if((ctr+1)%subDimY == 0 && ctr>0)
								xCount =- 1;
						}

						if(new Validator(sudoku, subgrid).checkAnswer())
							exit(5);
					}
				}
			});
		}

		// basically key actions when the user enters 
		// something into the field in the popup
		pop.field.addKeyListener(new KeyListener(){
			public void keyReleased(KeyEvent eee){
				String str=pop.field.getText();

				// if not valid input
				if(str.length() > 2 || !(eee.getKeyCode() > 47 && eee.getKeyCode() < 58 || eee.getKeyCode() > 95 && eee.getKeyCode() < 106 || eee.getKeyCode() == KeyEvent.VK_BACK_SPACE || eee.getKeyCode() == KeyEvent.VK_ENTER) ){
					try{
						pop.field.setText(str.substring(0, str.length() - 1));
					} catch(Exception ee){}
				} // if valid input (numbers)
				else if(eee.getKeyCode() > 47 && eee.getKeyCode() < 58 || eee.getKeyCode() > 95 && eee.getKeyCode() < 106 || eee.getKeyCode() == KeyEvent.VK_BACK_SPACE){
					try{
						// if the value of str is greater than the no. of valid numbers in the puzzle
						// display red
						if(Integer.parseInt(str) > pop.size)
							pop.field.setForeground(java.awt.Color.red);
						else // otherwise black (normal)
							pop.field.setForeground(java.awt.Color.black);
					} catch(Exception e){}
				}
			}

			public void keyTyped(KeyEvent eee){}

			public void keyPressed(KeyEvent e){
				// if ENTER KEY is pressed
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
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
					}
					else{ // if something was entered
						try{
							int size = pop.size, num = Integer.parseInt(str);
							
							// if value enterd was valid (1 - 9 if size = 9)
							// then we add that answer to the board and sudoku array
							if(num <= size && num >= 1){
								// change the value of the selected grid on the board 
								GP.changePicture(board.btn[pop.btnX][pop.btnY],"img/box/"+size+"x"+size+"/normal/"+num+".png");
								
								// update the sudoku array
								board.setSudokuArray(num, isAns,pop.btnX, pop.btnY);

								// closes the popup and reopens the status and game
								pop.setVisible(false,0,0,0);
								status.setVisible(true);
								game.setVisible(isAns);

								pop.field.setForeground(java.awt.Color.black);

								// if in answer mode and the number of answers equals 
								// the size of the board, we validate the answers
								if(isAns && board.getAns() == size*size){
									int sudoku[][][] = board.getSudokuArray();
									Subgrid subgrid[] = new Subgrid[sudoku.length];
									int subDimY = (int)Math.sqrt(sudoku.length);
									int subDimX = sudoku.length/subDimY;
									
									for(int ctr = 0, xCount = 0; ctr<sudoku.length; ctr++, xCount++){
										subgrid[ctr] = new Subgrid(xCount*subDimX, ((ctr/subDimY)*subDimY), subDimX, subDimY);
										if((ctr+1)%subDimY == 0 && ctr>0)
											xCount =- 1;
									}
									
									if(new Validator(sudoku, subgrid).checkAnswer())
										exit(5);
								}
							}
							else
								throw new Exception();
						} catch(Exception eee){
							error.play();
						}
					}
				}
			}
		});

		// for erasing answers
		pop.erase.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GP.changePicture(board.btn[pop.btnX][pop.btnY],"img/box/"+pop.size+"x"+pop.size+"/normal/0.png");
				board.setSudokuArray(0, false,pop.btnX, pop.btnY);
				pop.setVisible(false,0,0,0);
				status.setVisible(true);
				game.setVisible(isAns);
			}
		});

		// closing the popup
		pop.cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				pop.setVisible(false,0,0,0);
				status.setVisible(true);
				game.setVisible(isAns);
			}
		});
	}

	// generating the game panel
	// USED IN menu(), open(), and exit()
	private void mainGame(){
		// generate the UIGame in panel 6 and make it visible
		GP.setVisible(6);
		game = new UIGame(GP.panel[6]);

		sop("Entered mainGame");
		// playing a new game (TO BE FIXED)
		game.newGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				game.setVisible(false);
				status.setVisible(false);
				isSolved = false;

				// 
				exit(2);
			}
		});

		// exiting the game (not the program)
		game.exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				game.setVisible(false);
				status.setVisible(false);
				sop("Exiting maingame");
				exit(1);
			}
		});

		// opening the optionsUI
		game.options.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				game.setVisible(false);
				status.setVisible(false);
				options.setVisible(true,1);
			}
		});

		// solving the game (TO BE FIXED)
		game.solve.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				game.setVisible(false);
				status.setVisible(false);
				game.solve.setEnabled(false);
				solve();
				game.solve.setEnabled(true);
			}
		});

		// opening helpUI
		game.help.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				help(5);
			}
		});
	}

	// generating the helpUI and making it visible
	// USED IN menu() and mainGame()
	private void help(int num){
		// adding helpUI to panel 0 and making it visible
		GP.setVisible(0);
		help = new UIHelp(GP.panel[0], num);

		// going to the next instruction
		help.next.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				help.increase();
			}
		});

		// going to the previous instruction
		help.back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				help.decrease();
			}
		});

		// leaving the helpUI
		help.cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				help.decompose();
				GP.setVisible(help.panelNum);
				help = null;
			}
		});
	}

	// for solving the sudoku puzzle
	// USED IN mainGame() and exit()
	private void solve(){
		solve = new UISolve(GP.solve);

		// leaving the solveUI
		solve.cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				status.setVisible(true);
				game.setVisible(true);
				solve.decompose();
				solve = null;
				GP.setVisible(5);
			}
		});

		// this changes when the user clicks on "Experiment Mode"
		// in the solveUI
		solve.mode.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				solve.changeMode();
			}
		});

		// processes the solving of the sudoku board
		// TO BE FIXED
		solve.solve.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				status.setVisible(false);
				try{
					numEmp = Integer.parseInt(solve.numEmployed.getText());
					numOnlook = Integer.parseInt(solve.numOnlook.getText());
					numCycle = Integer.parseInt(solve.numCycles.getText());
					generate = false;
					status.setVisible(false);

					if(numEmp >= numOnlook || numEmp<2)
						throw new Exception();
					
					gameMode = (solve.modeNum == 0) ? true : false;

					try{
						start();
					} catch(Exception ee){
						start = true;
					}
				} catch(Exception ee){
					solve.decompose();
					solve = null;
					GP.setVisible(5);
					exit(7);
				}
			}
		});
	}

	// THREAD method: runs when start() is called
	// USED IN menu(), solve(), and exit()
	public void run(){
		while(true){
			try{
				solve.decompose();
				solve = null;
			} catch(Exception e){}
			game.setVisible(1);
			
			// if in game mode and not creation mode
			if(gameMode){
				status.setVisible(false);

				// create a new result file
				PrintResult printer = new PrintResult("results/.xls");
				int sudoku[][][] = board.getSudokuArray();
				ABC abc = new ABC(printer, sudoku, numEmp, numOnlook, numCycle);
				
				// run the generation animation at the special panel
				Animation animate = new Animation(sudoku, GP.special);
				board.decompose();
				board = null;

				// start the artificial bee colony thread
				abc.start(); 

				// delay cause yes
				delay(100);

				// keep changing the pic if abc isn't done every 100ms (methinks its ms)
				while(!abc.isDone()){
					delay(100);
					animate.changePic(abc.getBestSolution());
				}
				
				animate.decompose();
				animate = null;

				// if generating a game rather than loading an existing game
				if(generate){
					// generate a sudoku board with empty grids and
					// display it in the board
					GenerateSudoku gen = new GenerateSudoku(abc.getBestSolution());
					board(gen.getSudoku(), false);

					gen = null;
					isSolved = false;
					abc = null;
				} // if we're loading an existing game
				else {
					// if existing game is already solved, go to the exitUI 
					// and display the finished game
					if (abc.getFitness() == 1){
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
			} // if we're creating our own puzzle
			else {
				// create a new result file for saving the created puzzle
				String file = "results/result.xls";
				PrintResult printer = new PrintResult(file);

				status.setVisible(false);
				String cycle = "", time = "";

				// start the ABC 
				ABC abc = new ABC(printer, board.getSudokuArray(),numEmp,numOnlook, numCycle);
				abc.start();
				double startTime = printer.getTime();

				// wait until ABC thread is done running
				while(!abc.isDone());

				double end = (printer.getTime());
				double seconds = ((end-startTime)/1000);

				// update the time
				printer.print("\nCycles:\t "+abc.getCycles()+"\nTime:\t"+seconds);
				printer.close();
				printer = null;

				game.setVisible(0);
				Runtime rt = Runtime.getRuntime();

				try{
					rt.exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file);
				} catch(Exception ee){}
				
				board.decompose();
				board = null;

				// if the board is finished/solved
				// open the exitUI and display the finished board
				if(abc.getFitness() == 1){
					exit(8);
					board = new UIBoard(abc.getBestSolution(), GP.panel[5]);
				} // just show the partially completed puzzle
				else{
					board(abc.getBestSolution(), false);
					isSolved = false;
				}

				abc.decompose();
				abc = null;
				rt = null;
				status.setVisible(true);
			}

			game.setVisible(0);
			start = false;

			// wait until the value of start becomes true
			while(!start);
		}
	}

	// just a delay method
	protected void delay(int newDelay){
		try{
			sleep(newDelay);
		} catch(InterruptedException err){}
	}

	// handles the statusIU during the game (save game, open a game, reset the game, etc.)
	// USED IN menu(), open(), status(), and exit()
	private void status(String str){
		// open the statusIU on panel 4
		status = new UIStatus(str, GP.panel[4]);

		// yes and no buttons shows up when clicking create game
		// if yes, solidify the entered values and play the game
		status.yes.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				sop("Entered status yes");
				int sudoku[][][] = board.getSudokuArray();
				Subgrid subgrid[] = new Subgrid[sudoku.length];
				int subDimY = (int)Math.sqrt(sudoku.length);
				int subDimX = sudoku.length/subDimY;
				
				for(int ctr = 0, xCount = 0; ctr<sudoku.length; ctr++, xCount++){
					subgrid[ctr] = new Subgrid(xCount*subDimX, ((ctr/subDimY)*subDimY), subDimX, subDimY);
					if((ctr+1)%subDimY == 0 && ctr>0)
						xCount =- 1;
				}

				Validator val = new Validator(sudoku, subgrid);
				// if the created sudoku puzzle is valid, 
				// then solidify the generated puzzle
				if(val.checkValidity()){
					isAns = true;
					status.decompose();
					status = null;
					pop.decompose();
					pop = null;
					board.decompose();
					board = null;

					// display the empty sudoku board
					board(sudoku,false);
					game.setVisible(true);

					// generate the popUpIU
					popUp(sudoku.length);
					status("");
				}
				else {
					// display "Incorrect Puzzle"
					exit(4);
				}

				val = null;
				isSolved = false;
			}
		});

		// exit the puzzle creation and return to main menu
		status.no.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				sop("Entered status no");
				exit(1);
			}
		});

		// open the loadUI 
		status.open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// hide the game main buttons and the status buttons
				game.setVisible(false);
				status.setVisible(false);
				isSolved = false;
				loadSudoku(5);
			}
		});

		// open the saveUI
		status.save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// hide the game main buttons and the status buttons
				game.setVisible(false);
				status.setVisible(false);
				save();
			}
		});

		// open the resetUI
		status.reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				game.setVisible(false);
				status.setVisible(false);
				exit(10);
			}
		});
	}

	// handles saving the current sudoku game
	private void save(){
		// load the saveUI to panel 2
		save = new UISave(GP.panel[2]);

		// focus on save.field element
		save.field.grabFocus();

		
		// hide the game buttons and the status buttons
		status.setVisible(false);
		game.setVisible(false);

		// cancelling brings u back to the sudoku game
		save.cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				game.setVisible(true);
				status.setVisible(true);
				save.decompose();
				GP.setVisible(5);
			}
		});

		// save the current game (as .sav file)
		save.save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				saveFileName = save.field.getText();
				SaveSudoku saving = new SaveSudoku();
				int num = saving.save(saveFileName, board.getSudokuArray());
				save.decompose();
				GP.setVisible(5);

				if(saveFileName.length() > 0 && !(saveFileName.contains("/") || saveFileName.contains("\\") || saveFileName.contains(":")  || saveFileName.contains("*") || saveFileName.contains("?")  || saveFileName.contains("\"") || saveFileName.contains("<") || saveFileName.contains(">"))&& num == 0){
					saveFileName="";
					game.setVisible(true);
					status.setVisible(true);
				}
				else if(saveFileName.length() == 0 && (saveFileName.contains("/") || saveFileName.contains("\\") || saveFileName.contains(":")  || saveFileName.contains("*") || saveFileName.contains("?")  || saveFileName.contains("\"") || saveFileName.contains("<") || saveFileName.contains(">")) ||  num == 1){
					exit(6);
					status.setVisible(false);
					game.setVisible(false);
					saveFileName = "";
				}
				else{
					status.setVisible(false);
					game.setVisible(false);
					exit(9);
				}
			}
		});

		save.field.addKeyListener(new KeyListener(){
			public void keyReleased(KeyEvent ee){}
			public void keyTyped(KeyEvent eee){}
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					saveFileName = save.field.getText();
					SaveSudoku saving = new SaveSudoku();
					int num = saving.save(saveFileName, board.getSudokuArray());
					save.decompose();
					GP.setVisible(5);
					if(saveFileName.length()>0 && !(saveFileName.contains("/") || saveFileName.contains("\\") || saveFileName.contains(":")  || saveFileName.contains("*") || saveFileName.contains("?")  || saveFileName.contains("\"") || saveFileName.contains("<") || saveFileName.contains(">"))&& num == 0){
						game.setVisible(true);
						status.setVisible(true);
						saveFileName = "";
					}
					else if(saveFileName.length() == 0 && (saveFileName.contains("/") || saveFileName.contains("\\") || saveFileName.contains(":")  || saveFileName.contains("*") || saveFileName.contains("?")  || saveFileName.contains("\"") || saveFileName.contains("<") || saveFileName.contains(">")) ||  num==1){
						exit(6);
						status.setVisible(false);
						game.setVisible(false);
						saveFileName = "";
					}
					else{
						status.setVisible(false);
						game.setVisible(false);
						exit(9);
					}
				}
			}
		});
	}

	// handles the exitUI
	private void exit(int num){
		// exit gets turned to null each time it's called,
		// so we have to check if exit is null
		if(exit == null){
			exit = new UIExit(GP.panel[0], num);

			sop("Entered exit UI generation");

			// pressing yes on any exit yes button 
			exit.yes.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					GP.setVisibleButton(true);
					try{
						game.setVisible(true);
						status.setVisible(true);
					} catch(Exception ee){}

					exit.decompose();
					// exiting/ending the program
					if(exit.num == 0)
						System.exit(0);
					// exiting the ongoing sudoku game
					else if(exit.num == 1){
						board.decompose();
						board = null;
						game.decompose();
						game = null;
						status.decompose();
						status = null;
						pop.decompose();
						pop = null;
						GP.setVisible(7);
					}
					// exiting back to the sudoku game 
					else if(exit.num == 2){
						board.decompose();
						board = null;
						game.decompose();
						game = null;
						status.decompose();
						status = null;
						pop.decompose();
						pop = null;
						
						mainGame();
						status("");
						isAns = true;
						int size = (options.sz+2)*3;
						board(new int[size][size][2], true);
						numEmp = 100;
						numOnlook = 200;
						numCycle = 100000000;
						generate = true;
						gameMode = true;

						try{
							start();
						} catch(Exception ee){
							start = true;
						}
						popUp(size);
					}
					// save the current progress of the file and 
					// return to the sudoku game
					else if(exit.num == 9){
						GP.setVisible(5);
						SaveSudoku saving = new SaveSudoku();
						saving.delete(saveFileName);
						saving.save(saveFileName, board.getSudokuArray());
					}
					// reset the board back to its non-answered version
					else if(exit.num == 10){
						isSolved = false;
						GP.setVisible(5);
						board.changePic();
						int[][][] sudoku = board.getSudokuArray();
						board.decompose();
						board = null;
						board(sudoku, false);
					}
					exit = null;
				}
			});

			// pressing no on any exit button
			exit.no.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					GP.setVisibleButton(true);
					try{
						game.setVisible(true);
						status.setVisible(true);
					} catch(Exception ee){}

					exit.decompose();
					
					// just return back to mainMenu
					if(exit.num == 0)
						GP.setVisible(7);
					// just return back to the sudoku game
					else if(exit.num == 1 || exit.num == 2 || exit.num == 6 || exit.num == 10)
						GP.setVisible(5);
					// just return to the saveUI (with the sudoku game in the bg)
					else if(exit.num == 9){
						GP.setVisible(5);
						save();
					}

					// make exitUI to null
					exit = null;
				}
			});
			
			exit.okay.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					GP.setVisibleButton(true);
					game.setVisible(true);
					status.setVisible(true);
					
					// open the solveUI and 
					if(exit.num == 7)
						solve();
					// game is solved so show the finished board 
					// and change the cursor
					else if(exit.num == 5){
						GP.setVisible(5);
						isSolved = true;
						board.changeCursor();
					}
					// game is solved so show the finished board
					else if(exit.num == 8){
						GP.setVisible(5);
						isSolved = true;
					}
					// num is 1, 2, 3, and 9
					// close the board and return to main menu
					else if(exit.num != 4 && exit.num != 6){
						board.decompose();
						board = null;
						GP.setVisible(7);
					}
					// returns to the game panel (panel 5) without opening the game buttons
					else if(exit.num == 4){
						sop("Entered num 4 exit");
						GP.setVisible(5);
						game.setVisible(false);
					}
					// exit.num == 6: returns to the game panel with game buttons
					else {
						GP.setVisible(5);
					}	

					exit.decompose();
					exit = null;
				}
			});
		}
	}

	// handles the optionsUI and makes it visible
	private void options(){
		options = new UIOptions(GP.panel);

		// exiting the optionsUI
		options.exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					game.setVisible(true);
					status.setVisible(true);
				} catch(Exception ee){}
				GP.setVisibleButton(true);

				if(options.num == 0)
					GP.setVisible(7);
				else
					GP.setVisible(5);
			}
		});

		// action listeners for the left and right buttons
		// for setting the grid size (6x6, 9x9, etc.)
		options.left[0].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				options.setSize(false);
			}
		});

		options.right[0].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				options.setSize(true);
			}
		});

		// action listeners for the left and right buttons
		// for setting the sound of the program
		options.left[1].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				options.setSound(false);
				if(options.snd == 1)
					snd.stop();
				else
					snd.loop();
			}
		});
		
		options.right[1].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				options.setSound(true);
				if(options.snd == 1)
					snd.stop();
				else
					snd.loop();
			}
		});
	}

	// shortcut for printing (System.out.println = sop)
	private void sop(Object obj){
		System.out.println(obj+"");
	}

	public static void main(String args[]){
		new SudokuBee();
	}
}
