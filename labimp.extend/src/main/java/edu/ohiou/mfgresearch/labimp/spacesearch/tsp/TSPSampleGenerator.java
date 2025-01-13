package edu.ohiou.mfgresearch.labimp.spacesearch.tsp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import edu.ohiou.mfgresearch.labimp.spacesearch.DefaultSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.HeuristicException;
import edu.ohiou.mfgresearch.labimp.spacesearch.InformedSearcher;

public class TSPSampleGenerator {
	public int currentTSPSize;

	public int currentVariation;

	public String currentHeuristics;

	TSPSampleGenerator() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		int NofReplication=10;
		int[] city = new int[] {8};
//		int[] max = new int[] {55,70,90,120,200};
		int[] max = new int[] {5, 15, 30,100};
//		int[] max = new int[] {5};
		(new TSPSampleGenerator()).runSamples(city, max,NofReplication);
	}

	public void genCity(int cityN) {
		String cityName = new String();

		while (TravelingSalesman.cities.size() > 0) {
			TravelingSalesman.cities.remove(0);
		}
		for (int i = 0; i < cityN; i++) {
			cityName = "C" + (i + 1);
			TravelingSalesman.cities.add(cityName);
		}
	}

	public void genDistance(int cityN, int max, long seed) {

		//
		int randomN = 0;
		double temp = 0;

		//
		Random r = new Random();
		r.setSeed(seed);

		double result[][] = new double[cityN][cityN];
		for (int i = 0; i < cityN; i++) {
			for (int j = 0; j < cityN; j++) {
				if (i == j)
					result[i][j] = 0;
				else if (j > i) {
//					temp = getUniform(r.nextDouble(),max,50);
					temp =getNormal(r,max,500);
					randomN = (int) temp;
					result[i][j] = randomN;
					result[j][i] = randomN;
				} else
					continue;
				
			}
		}
		(new TSPSampleGenerator()).printOutMatrix(result);
		TravelingSalesman.distances = result;
		
//		for (int i = 0; i < cityN; i++) {
//			String tempStr="";
//			for (int j = 0; j < cityN; j++) {
//				tempStr=tempStr+result[i][j] + ",";
//			}
//			try {
//				(new TSPSampleGenerator()).appendFile(getfileName(),tempStr);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
	
	public double getUniform(double rn, double max, double min) {
		return min + rn * (max - min);
	}

	public double getNormal(Random r, double SD, double mean) {
		double temp1=0;
		double temp2=0;
        double temp3=10;

		while (temp3 >= 1){
		   temp1 = -1 + 2 * r.nextDouble();
		   temp2 = -1 + 2 *r.nextDouble();
		   temp3 = temp1 * temp1 + temp2 * temp2;
		}
		return mean + SD * temp1 * Math.sqrt(-2 * Math.log(temp3) / temp3);
	}
	
	public void printOutMatrix(double[][] matrix) {

	}

	void singleRun(int cityN, String Heuristics, int max, int replicationIndex) {
		long currentCPUTime = 0;
	
		currentTSPSize = cityN;
		currentVariation = max;
		currentHeuristics = Heuristics;
		genCity(cityN);
		genDistance(cityN, max, getSeed(replicationIndex));

		TravelingSalesman ts = new TravelingSalesman();

		try {
			ts.currentHeuristic = Heuristics;
			if (ts.currentHeuristic.equalsIgnoreCase(""))
				System.out.println("H: " + ts.getHeuristic().evaluate());
			else
				System.out.println("H: "
						+ ts.getHeuristic(ts.currentHeuristic).evaluate());
		} catch (HeuristicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ts.visitedPath = new LinkedList();
		ts.unvisitedCities = new LinkedList(TravelingSalesman.cities);
		TravelingSalesman goal = new TravelingSalesman();
		goal.visitedPath = new LinkedList(TravelingSalesman.cities);
		goal.unvisitedCities = new LinkedList();
		goal.totalDistance = 300000.;
		InformedSearcher searcher = new InformedSearcher(ts, goal);
		currentCPUTime = System.currentTimeMillis();
		searcher.runSpaceSearch();
		currentCPUTime = System.currentTimeMillis() - currentCPUTime;
		try {
			appendFile(getfileName(1,"") ,
					cityN + ",50," + max + "," + Heuristics + "," +replicationIndex +","
					+ searcher.closed.size() + "," + searcher.getOpen().size()+ ","
					+currentCPUTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void runSamples(int[] city, int[] max, int NofReplication) {
		genFile(getfileName(1,""));
		try {
			appendFile(getfileName(1,""),
					"Problem Size, Lower ubound, Upper ubound, Heurstics, Replication Index, Close Size, Open Size, Computation Time");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   	
		for (int i = 0; i < city.length; i++) {
			for (int j = 0; j < max.length; j++) {
				for (int k =1; k <= NofReplication; k++) {	
					DefaultSpaceState.current = 0;
					if (k==4)
					{System.out.println("Yoyo");}
					this.singleRun(city[i], "MinValuesHeuristic", max[j],k);
					genProblemData(city[i],max[j],k);
					this.singleRun(city[i], "MinColumnHeuristic", max[j],k);
					this.singleRun(city[i], "MinRowHeuristic", max[j],k);
					this.singleRun(city[i], "MinRowColHeuristic", max[j],k);
				}
			}
		}

	}

	void genProblemData(int city, int maxD,int replicationIndex) {

		String fileName = getfileName(0, "C" + city + "maxD"
				+ maxD  + "R"+ replicationIndex);
		genFile(fileName);
		String tempStr="";
		for (int i = 1; i <= this.currentTSPSize; i++) {
			tempStr = tempStr + "C" + i+",";
			}
		try {
			(new TSPSampleGenerator()).appendFile(fileName, tempStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		for (int i = 0; i < this.currentTSPSize; i++) {
			tempStr = "";
			for (int j = 0; j < this.currentTSPSize; j++) {
				tempStr = tempStr + TravelingSalesman.distances[i][j] + ",";
			}
			try {
				(new TSPSampleGenerator()).appendFile(fileName, tempStr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
	public static String getfileName(int dataType, String problemDataDescription) {
	  if (dataType==1)
		  return System.getProperty("user.home") + "/TSPsample/" + "Result.csv";
	  else{
		  return System.getProperty("user.home") + "/TSPsample/" + problemDataDescription +".csv";
	  }
//		String filePath = "";
//		filePath = System.getProperty("user.home") + "/TSPsample/";
//		filePath = filePath + this.currentTSPSize + "C"
//				+ this.currentMaxDistance + "maxD"
//				+ this.currentHeuristics;
//		filePath = filePath ;

	}

	long getSeed(int replicationIndex) {
		return 100000*replicationIndex;
	}
	
	static void appendFile(String fileName, String data) throws IOException {
		try {
			File file = new File(fileName);
			// FileOutputStream fos=new FileOutputStream(file);
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			// FileReader fr = new FileReader(file);
			// BufferedReader br = new BufferedReader(fr);
			bw.write(data);
			bw.newLine();
			bw.close();
		} catch (IOException io) {
			System.out.println(io.toString());
		}
	}
	
	static void genFile(String fileName) {
		File f = new File(System.getProperty("user.home") + "/TSPsample/");
		if (!f.exists())
			f.mkdir();
		
		f = new File(fileName);
		f.delete();
	}


}
