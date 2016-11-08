package megabyte.ml.examples;

import megabyte.ml.Instance;
import megabyte.ml.classifiers.RandomForest;
import megabyte.ml.util.DataTransformer;
import megabyte.ml.util.Measures;

import java.io.IOException;
import java.util.List;

public class RandomForestExample {

    public static void main(String[] args) throws IOException {
        new RandomForestExample().doMain();
    }

    private void doMain() throws IOException {
        List<Instance> trainSet = DataReader.readInstancesFromResources("classifiers/train.data", "classifiers/train.labels");
        RandomForest randomForest = new RandomForest(100);
        List<Integer> features = randomForest.filterFeatures(trainSet, 0.01);
        System.out.println("Features count after filtering: " + features.size());
        trainSet = DataTransformer.selectFeatures(trainSet, features);
        randomForest.train(trainSet);

        List<Instance> validSet = DataReader.readInstancesFromResources("classifiers/valid.data", "classifiers/valid.labels");
        validSet = DataTransformer.selectFeatures(validSet, features);
        double f1 = Measures.f1Measure(randomForest, validSet);
        System.out.println("F1 Measure: " + f1);
    }
}
