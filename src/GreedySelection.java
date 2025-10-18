class GreedySelection{

	GreedySelection(){}

	protected Bee greedySearch(Bee x, Bee v, int fp) {
		double newFitness=new Fitness().calculateFitness(v.getPenaltyValue(fp));
		
		if (x.getFitness()<=newFitness){
			x.copyProblem(v.getCopy());
			x.setFitness(newFitness);
		}
		return x;
	}
}