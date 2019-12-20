import java.util.ArrayList;

import java.util.Arrays;

import java.util.List;


/**

 compute the probability of a variable given evidences.
         given  query the probability.
 */

public class VariableElimination implements Inference{

	public static int countmul=0;
	public static int countsum=0;
	NB network;

	public VariableElimination(NB network) {
		this.network = network;
	}



	/**

	 given input network and return the result value 

	 */

	@Override
	public String ask(String query) {
		String[] q = query.split("\\|");
		return ask(q[0], q[1]);

	}

	
	
	/**

	 * The underline variable elimination implementation.

	 */

	public String ask(String var, String observed) {
		Event target = network.parseEvent(var);
		Condition evidence = network.parseCondition(observed);


		List<Variable> order = new ArrayList<Variable>();
		for (Variable v : network.nodes.values())
			order.add(0, v);

		// For each variable, make it into a factor.
		List<Factor> factors = new ArrayList<Factor>();
		for (Variable v : order) {
			factors.add(new Factor(v, evidence));
			
			// plus action

			if (target.node != v && !evidence.mention(v)) {
				Factor temp = factors.get(0);
				for (int i = 1; i < factors.size(); i++) {
					temp = temp.join(factors.get(i));
					VariableElimination.countmul++;

					}
				VariableElimination.countsum++;

				temp.eliminate(v);
				factors.clear();
				factors.add(temp);
			}

		}



		// join action
		Factor a = factors.get(0);
		for (int i = 1; i < factors.size(); i++)
			a = a.join(factors.get(i));

		
		// Normalization the ans factor
		a.norm();

		

		//ans
		return String.format("%.6f", a.p.get(new Condition(Arrays.asList(target))));

	}

}