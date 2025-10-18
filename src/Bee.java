import java.util.Random;

class Bee{
	private int[][][] solution;
	private double fitness;
	private Subgrid[] subgrid;
	private Random rand = new Random();
	
	Bee (Subgrid[] subgrid){
		this.subgrid = subgrid;
	}

	Bee (int[][][] prob, Subgrid[] subgrid){
		solution = prob;
		this.subgrid = subgrid;

		for(int ctr = 0; ctr<subgrid.length; ctr++){
			int[] needed = neededNumbers(subgrid[ctr]);

			for(int y = subgrid[ctr].getStartY(), indexRand = needed.length, limY = y+subgrid[ctr].getDimY(); y<limY; y++){
				for(int x = subgrid[ctr].getStartX(), limX = x+subgrid[ctr].getDimX(); x<limX; x++){
					if(solution[y][x][1] == 1){
						int tmp = rand.nextInt(indexRand);
						solution[y][x][0] = needed[tmp];
						needed[tmp] = needed[indexRand-1];
						needed[indexRand-1] = solution[y][x][0];
						indexRand = indexRand-1;
					}
				}
			}
		}
	}

	protected void copyProblem(int[][][] prob){
		solution = prob;
	}

	protected void printResult(){
		for(int ctr = 0; ctr<solution.length; ctr++){
			for(int ctr1 = 0; ctr1<solution[ctr].length; ctr1++){
				System.out.print(solution[ctr][ctr1][0]+"");
			}
			System.out.println("");
		}
	}

	protected double getPenaltyValue(int fp){
		if (fp == 0) {
			return getFunctionPenalty_MissingWithSubgrid();
		} else if (fp == 1) {
			return getFunctionPenalty_SumProductWithSubgrid();
		} else {
			return getFunctionPenalty_SumProductWithoutSubgrid();
		}
	}

	private int getFunctionPenalty_MissingWithSubgrid() {
		int penalty = 0;
		int n = solution.length;
		
		int subSize = (int) Math.sqrt(n);
		customSet set = new customSet();

		for(int r = 0; r < n; r++){
			set.clear(); // clear the set of row
			for(int c = 0; c < n; c++){
				set.add((solution[r][c][0]));
			}
			penalty += (n - set.size());
		}

		for(int c = 0; c < n; c++){
			set.clear(); // clear the set of columns
			for(int r = 0; r < n; r++){
				set.add((solution[r][c][0]));
			}
			penalty += (n - set.size());
		}

		// check for subgrids
		 for (int sr = 0; sr < n; sr += subSize) {
			for (int sc = 0; sc < n; sc += subSize) {
				set.clear();
				for (int r = sr; r < sr + subSize; r++)
					for (int c = sc; c < sc + subSize; c++)
						set.add(solution[r][c][0]);
				penalty += (n - set.size());
			}
		}

		return penalty;
	}
	
	private double getFunctionPenalty_SumProductWithSubgrid() {
		return 
		getRowProductSumPenalty() +
		getColumnProductSumPenalty() + 
		getSubgridProductSumPenalty();
	}

	private double getFunctionPenalty_SumProductWithoutSubgrid() {
		return 
		getRowProductSumPenalty() +
		getColumnProductSumPenalty();
	}

	private double getRowProductSumPenalty() {
		int n = solution.length;
		double penalty = 0;

		// e.g. for a 4x4 grid, expectedSum = 1 + 2 + 3 + 4 = 10
		// which is 4 * (4 + 1) / 2.0 = 4 * (5) / 2.0 = 20/2.0 = 10 
		double expectedSum = n * (n + 1) / 2.0;
		
		// e.g. for a 4x4 grid, expectedProduct = 1 * 2 * 3 * 4 = 24
		double expectedProduct = 1.0;
		for(int i = 1; i <= n; i++) expectedProduct *= i;

		// get the product and sum penalty for rows
		for(int r = 0; r < n; r++) {
			double sum = 0, product = 1;
			for(int c = 0; c < n; c++) {
				sum += solution[r][c][0];
				product *= solution[r][c][0];
			}
			penalty += sum - expectedSum;
			penalty += product - expectedProduct;
		}
		return penalty;
	}

	private double getColumnProductSumPenalty() {
		int n = solution.length;
		double penalty = 0;

		double expectedSum = n * (n + 1) / 2.0;
		double expectedProduct = 1.0;
		for(int i = 1; i <= n; i++) expectedProduct *= i;

		// get the product and sum penalty for cols
		for(int c = 0; c < n; c++) {
			double sum = 0, product = 1;
			for(int r = 0; r < n; r++) {
				sum += solution[r][c][0];
				product *= solution[r][c][0];
			}
			penalty += sum - expectedSum;
			penalty += product - expectedProduct;
		}
		return penalty;
	}

	private double getSubgridProductSumPenalty() {
		int n = solution.length;
		int subSize = (int) Math.sqrt(n);

		double penalty = 0;
		double expectedSum = n * (n + 1) / 2.0;
		double expectedProduct = 1.0;
		for(int i = 1; i <= n; i++) expectedProduct *= i;

		// get the product and sum penalty for sub-grids
		for (int sr = 0; sr < n; sr += subSize) {
			for (int sc = 0; sc < n; sc += subSize) {
				double sum = 0, product = 1;
				for (int r = sr; r < sr + subSize; r++)
					for (int c = sc; c < sc + subSize; c++) {
						sum += solution[r][c][0];
						product *= solution[r][c][0];
					}
				penalty += sum - expectedSum;
				penalty += product - expectedProduct;
			}
		}
		return penalty;
	}

	protected int[][][] getSolution(){
		return solution;
	}

	protected void setFitness(double fit){
		fitness = fit;
	}

	protected double getFitness(){
		return fitness;
	}

	protected int getElement(int j){
		int row = j/solution.length, column=j%solution.length;
		if(solution[row][column][1] == 0)
			return 0;
		return solution[row][column][0];
	}
	
	protected int[] neededNumbers(Subgrid grid){
		int[] needed = new int[solution.length];
		int removed = 0;
		for(int ctr = 1; ctr <= solution.length; ctr++)
			needed[ctr-1] = ctr;
		for(int y = grid.getStartY(), limY = y+grid.getDimY(); y<limY; y++){
			for(int x = grid.getStartX(), limX = x+grid.getDimX(); x<limX; x++){
				if(solution[y][x][1] == 0){
					needed[solution[y][x][0]-1] = 0;
					removed = removed+1;
				}
			}
		}
		int[] neededNum = new int[solution.length-removed];
		for(int ctr = 0, ctr2 = 0; ctr<solution.length; ctr++){
			if(needed[ctr]>0){
				neededNum[ctr2] = needed[ctr];
				ctr2 = ctr2+1;
			}
		}
		return neededNum;
	}

	protected int[][][] getCopy(){
		int[][][] copy = new int[solution.length][solution.length][2];
		for(int ctr = 0; ctr<copy.length; ctr++){
			for(int ct = 0; ct<copy.length; ct++){
				copy[ctr][ct][0] = solution[ctr][ct][0];
				copy[ct][ctr][0] = solution[ct][ctr][0];
				copy[ctr][ct][1] = solution[ctr][ct][1];
				copy[ct][ctr][1] = solution[ct][ctr][1];
			}
		}
		return copy;
	}

	protected int[][][] swap(int[][][] solution, int subgridNum, int row, int column, int xij, int vij){
		this.solution = solution;
		for(int y = subgrid[subgridNum].getStartY(), limY = y+subgrid[subgridNum].getDimY(); y<limY; y++){
			for(int x = subgrid[subgridNum].getStartX(), limX = x+subgrid[subgridNum].getDimX(); x<limX; x++){
				if(solution[y][x][0] == vij){
					solution[y][x][0] = xij;
					solution[row][column][0] = vij;
					return solution;
				}
			}
		}
		return null;
	}
}