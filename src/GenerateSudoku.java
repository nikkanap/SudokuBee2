import java.util.HashSet;
import java.util.Random;

class GenerateSudoku{
	private int[][][] sudoku;
	private Random rand=new Random();

	private int n, totalCells, numGiven, numEmpty;

	GenerateSudoku(int[][][] sudoku, int percentOfGivenCells, int chosenGrid){
		sop("Entered Generate sudoku");
		this.sudoku=sudoku;
		this.n = sudoku.length;
		this.totalCells = n * n;
		this.numGiven = (int) Math.round(totalCells * (percentOfGivenCells / 100.0));
		this.numEmpty = totalCells - numGiven;

		if(chosenGrid == 1) {
			sop("Creating from empty grid");
			generateFromEmpty();
			return;
		} 

		sop("Creating from filled grid");
		generateFromFilled();
	}

	private void generateFromEmpty() {
		// Randomly select which cells to KEEP (given cells)
        HashSet<Integer> emptyIndices = new HashSet<>();
        while (emptyIndices.size() < numEmpty) {
            emptyIndices.add(rand.nextInt(totalCells)); // pick a random cell index
        }

		// mark chosen cells as empty
        for (int index : emptyIndices) {
            int row = index / n;
            int col = index % n;

            sudoku[row][col][0] = 0;
            sudoku[row][col][1] = 1;
        }

		// mark the rest as given
		for(int ctr=0; ctr<n; ctr++){
			for(int ct=0; ct<n; ct++){
				if(sudoku[ctr][ct][0] != 0 && sudoku[ctr][ct][1] == 1) {
					sudoku[ctr][ct][1] = 0;
				}
			}
		}
	}

	private void generateFromFilled() {
        // remove cells for empty
        HashSet<Integer> emptyIndices = new HashSet<>();
		sop("numEmpty = " + numEmpty + ", emptIndices.size() = " + emptyIndices.size());
        while (emptyIndices.size() < numEmpty) {
			sop("entered while loop inside GenerateSudoku");
            int row = rand.nextInt(n);
            int col = rand.nextInt(n);
			sop("grid value[0] = " + sudoku[row][col][0]);
			sop("grid value[1] = " + sudoku[row][col][1]);
            if (sudoku[row][col][0] != 0 && sudoku[row][col][1] == 1) {
				sop("adding empty to grid["+row+"]["+col+"][0]");
                sudoku[row][col][0] = 0;
                sudoku[row][col][1] = 1;
                emptyIndices.add(row * n + col);
            }
        }

		// mark the rest as given
		for(int ctr=0; ctr<n; ctr++){
			for(int ct=0; ct<n; ct++){
				if(sudoku[ctr][ct][0] != 0 && sudoku[ctr][ct][1] == 1) {
					sudoku[ctr][ct][1] = 0;
				}
			}
		}
	}

	protected int[][][] getSudoku(){
		return sudoku;
	}

	private void sop(Object obj){
		System.out.println(obj+"");
	}
}