import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Cursor;

// UI for the sudoku board
public class UIBoard{
	private JPanel pane;
	private JLabel board;
	private int sudokuArray[][][];
	private int size, startX, startY, inc, btnX, btnY, ans;
	private int increment[] = {56, 31, 20};
	protected JButton btn[][];
	private generalPanel gp = new generalPanel();
	
	UIBoard(){}

	UIBoard(int sudokuArray[][][], JPanel pane){
		sop("UIBOARD: constructor 1");
		this.pane = pane;
		this.sudokuArray = sudokuArray;
		setConstants(false);
	}

	UIBoard(int sudokuArray[][][], boolean isNull, JPanel pane){
		sop("UIBOARD: constructor 2");
		this.pane = pane;
		this.sudokuArray = sudokuArray;
		ans = 0;

		if(isNull)
			fill();
		setConstants(true);
	}

	private void fill(){
		sop("UIBOARD: fill()");
		size = sudokuArray.length;
		sop("size = " + size);
		for(int ctr = 0; ctr<size; ctr++){
			for(int count = 0; count<size; count++){
				sudokuArray[ctr][count][0] = 0;
				sudokuArray[ctr][count][1] = 1;
			}
		}
	}

	private void setConstants(boolean setCursor){
		sop("UIBOARD: setConstants()");
		size = sudokuArray.length;
		sop("size = " + size);
		startX = size/6+6;
		sop("startX = " + startX);
		startY = 86;
		sop("startY = " + startY);
		inc = increment[(int) (size/Math.sqrt(size)-3)];
		sop("inc = " + inc);
		btn = new JButton[size][size];
		
		for(int ctr = 0, X = startX, Y = startY; ctr<size; ctr++, Y += inc, X = startX){
			for(int count = 0; count<size; count++, X += inc){
				String img = "normal";
				if(sudokuArray[ctr][count][1] == 0)
					img = "given";
				btn[ctr][count] = gp.gameButton(pane, "img/box/"+size+"x"+size+"/"+img+"/"+sudokuArray[ctr][count][0]+".png", X, Y);
				
				if(setCursor && img.equals("normal"))
					btn[ctr][count].setCursor(new Cursor(12));
				else
					btn[ctr][count].setCursor(new Cursor(0));
				
				if(sudokuArray[ctr][count][0] != 0)
					ans++;
			}
		}
		board = gp.addLabel(pane,"img/board/"+size+"x"+size+".png",0,84);
	}

	protected JButton getButton(){
		return btn[btnX][btnY];
	}

	protected int getStatus(int x, int y){
		return sudokuArray[x][y][1];
	}

	protected int[][][] getSudokuArray(){
		return sudokuArray;
	}

	protected void changeCursor(){
		for(int row = 0; row<size; row++){
			for(int col = 0; col<size; col++){
				btn[row][col].setCursor(new Cursor(0));
				btn[col][row].setCursor(new Cursor(0));
			}
		}
	}

	protected void changePic(){
		for(int row = 0; row<size; row++){
			for(int col = row; col<size; col++){
				if(sudokuArray[row][col][1] == 1)
					sudokuArray[row][col][0] = 0;
				if(sudokuArray[col][row][1] == 1)
					sudokuArray[col][row][0] = 0;
			}
		}
	}

	protected void setSudoku(int solution[][][]){
		sudokuArray = solution;
	}

	protected void setSudokuArray(int value, boolean isAns, int x, int y){
		if(sudokuArray[x][y][0] == 0 && value != 0)
			ans++;
		if(sudokuArray[x][y][0] != 0 && value == 0)
			ans--;

		sudokuArray[x][y][0] = value;
		int num = 1;
		if(!isAns && value != 0)
			num = 0;
		sudokuArray[x][y][1] = num;
		sudokuArray[x][y][0] = value;
	}

	protected int getValue(int x, int y){
		return sudokuArray[x][y][0];
	}

	protected int getSize(){
		return size;
	}

	protected int getAns(){
		return ans;
	}

	protected void decompose(){
		pane.removeAll();
		board = null;
		sudokuArray = null;
		btn = null;
		gp = null;
	}

	private void sop(Object o) {
		System.out.println(o+"");
	}
}
