import java.io.*;
import java.util.Arrays;
import java.util.Iterator;



public class Main {



	/**author:oz klingel

main class to run the program.

	 */

	public static void main(String[] args) throws IOException {
		NB net = new NB();


		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					"/a.txt"));
			String line = reader.readLine();

			while(line!=null) {///sof
				line = reader.readLine();

				String[] sp1 = line.split(":");
				String[] sp2=sp1[1].split(",");
				line = reader.readLine();

				for (int i = 0; i < sp2.length; i++) {

					///run every var

					line = reader.readLine();
					String[] sp3 = line.split(" ");
					String varn=sp3[1];
					System.out.println(varn);
					line = reader.readLine();
					String[] sp5 = line.split(":");
					String[] val = {"T","F"};
					line = reader.readLine();
					String[] sp6 = line.split(":");
					String[] par = sp6[1].split(",");

					line = reader.readLine();
					line = reader.readLine();

					while(!line.isEmpty())//cpt
					{
						int  shave=line.indexOf("=");
						String[] psik=line.split(",");//=true,0.002
						String ans="";//the ans format ""M = T: 0.2", "M = F: 0.8"

						if (par[0].contains("none")&&val.length==2) {
							line.replace("=", "");
							line.replace(" ", "");

							double a=1-Double.parseDouble(psik[1]);
							ans+=varn+"="+"T"+":"+psik[1]+","+varn+"="+"F"+":"+a;
							System.out.println(ans);
							String []sof=ans.split(",");
							System.out.println(Arrays.toString(val));
							net.addNode(varn, val, new String[0],sof);

						}
						if (par[0].equalsIgnoreCase("none")&&val.length==3) {
							line.replace("=", "");
							double a=1-Double.parseDouble(psik[2])+Double.parseDouble(psik[1]);
							ans+=varn+"="+"T"+":"+psik[1]+","+varn+"="+"F"+":"+a;
							String []sof=ans.split(",");
							net.addNode(varn, val, new String[0], sof);

						}
						if(par.length==2&&val.length==2) {

							line.replace("=", "");
							String[] psik1=line.split(",");
							double a=1-Double.parseDouble(psik1[3]);

							ans+=varn+"=T"+","+par[0]+"=T"+","+par[1]+"=T"+":"+psik1[3]+"#";
							ans+=varn+"=F"+","+par[0]+"=T"+","+par[1]+"=T"+":"+a+"#";
							line = reader.readLine();

							line.replace("=", "");
							String[] psik2=line.split(",");
							double a2=1-Double.parseDouble(psik2[3]);

							ans+=varn+"=T"+","+par[0]+"=T"+","+par[1]+"=F"+":"+psik2[3]+"#";
							ans+=varn+"=F"+","+par[0]+"=T"+","+par[1]+"=F"+":"+a2+"#";
							line = reader.readLine();

							line.replace("=", "");
							String[] psik3=line.split(",");
							double a3=1-Double.parseDouble(psik3[3]);

							ans+=varn+"=T"+","+par[0]+"=F"+","+par[1]+"=T"+":"+psik3[3]+"#";
							ans+=varn+"=F"+","+par[0]+"=F"+","+par[1]+"=T"+":"+a3+"#";

							line = reader.readLine();

							line.replace("=", "");
							String[] psik4=line.split(",");
							double a4=1-Double.parseDouble(psik4[3]);

							ans+=varn+"=T"+","+par[0]+"=F"+","+par[1]+"=F"+":"+psik4[3]+"#";
							ans+=varn+"=F"+","+par[0]+"=F"+","+par[1]+"=F"+":"+a4;

							String []sof=ans.split("#");
							net.addNode(varn, val, par, sof);
						}
						if(par.length==1&&!par[0].contains("none")&&val.length==2) {
							line.replace("=", "");
							String[] psik1=line.split(",");
							double a=1-Double.parseDouble(psik1[2]);

							ans+=varn+"=T"+","+par[0]+"=T"+":"+psik1[2]+"#";
							ans+=varn+"=F"+","+par[0]+"=T"+":"+a+"#";
							line = reader.readLine();

							line.replace("=", "");
							String[] psik2=line.split(",");
							double a2=1-Double.parseDouble(psik2[2]);

							ans+=varn+"=T"+","+par[0]+"=F"+":"+psik2[2]+"#";
							ans+=varn+"=F"+","+par[0]+"=F"+":"+a2+"#";

							String []sof=ans.split("#");
							net.addNode(varn, val, par, sof);


						}




						line = reader.readLine();

					}


				}
				line = reader.readLine();
				line = reader.readLine();
				line = reader.readLine();
				line = reader.readLine();
				FileWriter fileWriter = new FileWriter("C:\\Users\\97254\\eclipse-workspace\\gkn\\samplefile2.txt");
PrintWriter print=new PrintWriter("C:\\Users\\97254\\eclipse-workspace\\gkn\\samplefile2.txt");
				while(line!=null) {

					line = reader.readLine();
					if(line!=null) 
					{
					String li=line.replace("true","");
					li=li.replace("=","");
				    li.replace("p","");

					System.out.println(li);
					int n_lines = 1;
					String inference = "VE";
					Inference inferenceMethod;
					StringBuilder answers = new StringBuilder();
					if (inference.split("\\s+")[0].equals("VE")) {
						inferenceMethod = new VariableElimination(net);

					}  else {
						throw new RuntimeException();
					}

					while (--n_lines >= 0) {
					answers.append(inferenceMethod.ask(parseQuery(li) + "\n"));
                    System.out.println(answers);
                    print.print(answers.toString()+"\n");
                    print.print(VariableElimination.countmul+"\n"+VariableElimination.countsum+"\n");
                    System.out.println(VariableElimination.countmul+"\n");
					}
					
				}}
				fileWriter.close();
				print.close();
				

				reader.close();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}











	}



	


	public static String parseQuery(String query) {

		String[] q = query.split("\\(")[1].split("\\)")[0].split("\\|");



		String ret = convert(q[0]) + " | ";



		if (q.length > 1) {

			String[] evidences = q[1].split(",");

			for (int i = 0; i < evidences.length; i++)

				ret += convert(evidences[i]) + ", ";

			if (evidences.length > 0)

				ret = ret.substring(0, ret.length() - 2);

		}

		return ret;

	}
	

	public static String convert(String s) {

		s = s.toUpperCase();

		if (s.startsWith("-"))

			return s.substring(1) + "=F";

		else

			return s + "=T";

	}

	

	}

