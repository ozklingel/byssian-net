package BayesianNetwork;


/**

 * A common interface to be implemented by inference methods i.e.

 * VariableElimination 

 */

public interface Inference {



	public String ask(String query);

}