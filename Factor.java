import java.util.ArrayList;

import java.util.HashMap;

import java.util.HashSet;

import java.util.List;

import java.util.Map;






/**

 factor is a matrix recording the probabilities of
variables in this set.

 */

public class Factor {



	public List<Variable> variables;
	public Map<Condition, Double> p;



	/**
	 *given variable with evidence build factor
	 */

	public Factor(Variable v, Condition evidence) {
		variables = new ArrayList<Variable>(v.parents);
		variables.add(v);
		p = new HashMap<Condition, Double>(v.probabilities);



		for (Event e : evidence) {
			if (variables.contains(e.node)) {
				Map<Condition, Double> newP = new HashMap<Condition, Double>();
				for (Condition c : p.keySet())
					if (c.contains(e))
						newP.put(c, p.get(c));
				p = newP;
				eliminate(e.node);
			}

		}

	}



	private Factor(List<Variable> v, Map<Condition, Double> p) {
		variables = v;
		this.p = p;
	}



	/**

	 * Index the factor by condition will return the corresponding probability.

	 */

	public Double get(Condition cond) {
		return p.get(cond);
	}



	/**

	 * Eliminate a variable from factor by plus action

	 */

	public void eliminate(Variable var) {

		if (!variables.remove(var))

			throw new RuntimeException("factor not contain the variable ");



		Map<Condition, Double> newP = new HashMap<Condition, Double>();
		for (Condition cond : Variable.allConditions(variables)) {
			newP.put(cond, 0.0);

		}

		for (Condition cond : newP.keySet())
			for (Condition oldC : p.keySet())
				if (oldC.contains(cond)) {
					newP.put(cond, newP.get(cond) + p.get(oldC));
				}
		p = newP;

	}



	/**

	 * Join two factor by point wise product.

	 * 

	 * A new factor will be generated form the two factors containing all variable involved.

	 * Its probability will be the product of the two factors.

	 */

	public Factor join(Factor other) {



		// Retrieve the variables;
		List<Variable> newVars = new ArrayList<Variable>(variables);
		newVars.addAll(other.variables);
		newVars = new ArrayList<Variable>(new HashSet<Variable>(newVars));



		// compute the joined probability table;

		Map<Condition, Double> newP = new HashMap<Condition, Double>();

		for (Condition cond : Variable.allConditions(newVars)) {

			Double prob = 1.0;



			for (Condition c : other.p.keySet())

				if (cond.contains(c)) {
					prob *= other.get(c);
				}


			for (Condition c : p.keySet())

				if (cond.contains(c)) {
					VariableElimination.countmul++;

					prob *= get(c);

				}

			newP.put(cond, prob);

		}



		return new Factor(newVars, newP);

	}



	/**

	 * Normalize the factor so probability sum to 1.

	 */

	public void norm() {

		Double sumP = 0.0;

		for (Double d : p.values()) {
			VariableElimination.countsum++;
			sumP += d;
		}


		Map<Condition, Double> newP = new HashMap<Condition, Double>();

		for (Condition c : p.keySet()) {

			newP.put(c, p.get(c) / sumP);

		}

		p = newP;

	}



	public String toString() {

		String ret = "";

		for (Condition c : p.keySet()) {

			ret += "\n" + c.toString() + ": " + p.get(c);

		}

		return ret;

	}

}