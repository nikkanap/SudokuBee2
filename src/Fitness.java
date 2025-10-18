class Fitness{
	
	Fitness(){}

	protected double calculateFitness(double penaltyValue){
		return 1/(1+penaltyValue);
	}
}