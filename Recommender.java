package CODTECHITSOLN;


import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class Recommender {
    public static void main(String[] args) {
        // Suppressing Log4j warnings
        System.setProperty("org.apache.logging.log4j.level", "ERROR");

        try {
            // Loading data from ratings.csv using classpath
            File dataFile = new File(Recommender.class.getClassLoader().getResource("ratings.csv").toURI());
            System.out.println("Loading file: " + dataFile.getAbsolutePath());

            // Printing file content
            System.out.println("ratings.csv content:");
            BufferedReader br = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Line: [" + line + "]");
            }
            br.close();

            // Loading data model
            DataModel model = new FileDataModel(dataFile);

            //Printing data model stats
            System.out.println("Number of users: " + model.getNumUsers());
            System.out.println("Number of items: " + model.getNumItems());

            // Defining user similarity using Pearson correlation
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

            // Defining neighborhood (users with similarity > 0.0)
            UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.0, similarity, model);

            //Printing neighborhood size for User 1
            long[] neighbors = neighborhood.getUserNeighborhood(1);
            System.out.println("Number of similar users for User 1: " + neighbors.length);
            for (long neighbor : neighbors) {
                System.out.println("Similar user ID: " + neighbor);
            }

            // Creating recommender
            UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            // Getting 3 recommendations for user ID 1
            List<RecommendedItem> recommendations = recommender.recommend(1, 3);

            // Displaying recommendations
            System.out.println("Recommendations for User 1:");
            if (recommendations.isEmpty()) {
                System.out.println("No recommendations found for User 1.");
            } else {
                for (RecommendedItem recommendation : recommendations) {
                    System.out.println("Item ID: " + recommendation.getItemID() + ", Predicted Rating: " + recommendation.getValue());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}