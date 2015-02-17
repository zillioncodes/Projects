package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.clusterers.SimpleKMeans;
import weka.core.DistanceFunction;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class ClusterUtils {

	/* Clustering parameters */
	static int numberOfClusters = 3;
	static int seedsForClustering = 5;
	static DistanceFunction distanceFunction = new ManhattanDistance();
	
	static File arffFile = null;
	static Map<Integer, Integer> clusteredData = new HashMap<Integer, Integer>();
	static Instances userData;

	public static void makeClusters() {

		BufferedReader reader = null;
		Instances dataWithoutLastAttribute = null;

		try {
			reader = new BufferedReader(new FileReader(arffFile));
			userData = new Instances(reader);
			System.out.println("Loaded arff file.");

			/* Ignore/remove the last attribute */
			String[] removeOptions = new String[2];
			removeOptions[0] = "-R"; // "range"
			removeOptions[1] = "9"; // 9th attribute
			Remove remove = new Remove(); // new instance of filter
			remove.setOptions(removeOptions); // set options
			remove.setInputFormat(userData); // inform filter about dataset
			dataWithoutLastAttribute = Filter.useFilter(userData, remove); // apply filter
			/* Use k-means */
			SimpleKMeans clusterer = new SimpleKMeans();

			clusterer.setDistanceFunction(distanceFunction);
			clusterer.setSeed(seedsForClustering);
			clusterer.setPreserveInstancesOrder(true);
			clusterer.setNumClusters(numberOfClusters);
			clusterer.buildClusterer(dataWithoutLastAttribute);

			/*- This array returns the cluster number (starting with 0) for each
			 *  instance. The array has as many elements as the number of instances */
			int[] assignments = clusterer.getAssignments();

			for (int instanceNum = 0; instanceNum < assignments.length; instanceNum++) {
				int clusterNum = assignments[instanceNum];
				clusteredData.put(instanceNum, clusterNum);
			}
			System.out.println("Clustering complete.");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static List<String> getOtherUsersInMyCluster(String userId) {
		List<String> userIds = new ArrayList<String>();
		int clusterNum = -1;

		if (userData != null) {
		
		/* Get instance no. corresponding to user ID */
		for (int i = 0; i < userData.numInstances(); i++) {
			String currentUserId = userData.instance(i).stringValue(8);
			if (currentUserId != null && currentUserId.equals(userId)) {
				clusterNum = clusteredData.get(i);
				break;
			}
		}

		/* Get all users in this cluster */
		for (int currentInstanceNum : clusteredData.keySet()) {
			if (clusterNum == clusteredData.get(currentInstanceNum)) {
				String currentUserId = userData.instance(currentInstanceNum).stringValue(8);
				if (!currentUserId.equals(userId))
					userIds.add(currentUserId);
			}
		}	
		} else {
			System.err.println("No data has been set for clustering.");
		}

		return userIds;
	}

	public static void createArffFile(Connection con, File file) {
		try {
			arffFile = file;
			Map<String, Map<Integer, Integer>> wekaDataMap = null;
			if (file.exists())
				file.delete();
			file.createNewFile();
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			String staticText = "@relation user_categories\n@attribute Science_and_Technology numeric\n@attribute Lifestyle numeric\n@attribute Entertainment numeric\n@attribute Travel numeric\n@attribute Business numeric\n@attribute Automobiles numeric\n@attribute World_news numeric\n@attribute Miscellaneous numeric\n@attribute userid string\n\n% Each data row corresponds to a single user\n@data";
			output.write(staticText);
			output.write("\n");
			DBUtils dbUtils = null;
			try {
				dbUtils = new DBUtils(con);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				try {

					wekaDataMap = dbUtils.PopulateUserDataForWekaFile();
					for (String id : wekaDataMap.keySet()) {
						String outputString = "";
						Map<Integer, Integer> mapOfCategFrequencies = wekaDataMap.get(id);
						for (int i = 0; i < DBUtils.numberOfCategories; i++) {
							if (i == 0)
								outputString = outputString + String.valueOf(mapOfCategFrequencies.get(i));
							else
								outputString = outputString + "," + String.valueOf(mapOfCategFrequencies.get(i));
						}
						outputString = outputString + "," + id;
						output.write(outputString);
						output.write("\n");
					}
					System.out.println("ARFF file created: " + file.getAbsolutePath());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (DBUtilException e) {
				e.printStackTrace();
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}