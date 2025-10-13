import java.util.Random;
class GenerateSudoku{
	private int[][][] sudoku;
	private Random rand=new Random();
	GenerateSudoku(int[][][] sudoku){
		this.sudoku=sudoku;
		for(int ctr=0; ctr<sudoku.length; ctr++){
			for(int ct=ctr; ct<sudoku.length; ct++){
				double first=rand.nextDouble(), second=rand.nextDouble();
				if(first>1-second){
					this.sudoku[ct][ctr][0]=0;
					this.sudoku[ct][ctr][1]=1;
					}
				else{
					this.sudoku[ct][ctr][1]=0;
					}
				if(ct!=ctr && first>1-second){
					this.sudoku[ctr][ct][0]=0;
					this.sudoku[ctr][ct][1]=1;
					}
				else if(ct!=ctr){
					this.sudoku[ctr][ct][1]=0;
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