package megabyte.ml.examples;

import megabyte.ml.Instance;
import megabyte.ml.classifiers.DecisionTree;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DecisionTreeExample {

    public static void main(String[] args) throws IOException {
        new DecisionTreeExample().doMain();
    }

    private void doMain() throws IOException {
        String trainDataFile = getResource("random-forest/arcene_train.data");
        String trainLabelsFile = getResource("random-forest/arcene_train.labels");
        List<Instance> trainSet = readInstances(trainDataFile, trainLabelsFile);
        DecisionTree decisionTree = new DecisionTree();
        decisionTree.train(trainSet);

        String validDataFile = getResource("random-forest/arcene_valid.data");
        String validLabelsFile = getResource("random-forest/arcene_valid.labels");
        List<Instance> validSet = readInstances(validDataFile, validLabelsFile);
        double f1 = f1Measure(decisionTree, validSet);
        System.out.println("F1 Measure: " + f1);
    }

    private double f1Measure(DecisionTree classifier, List<Instance> instances) {
        int tp = 0, fp = 0, fn = 0;
        for (Instance instance : instances) {
            boolean label = classifier.classify(instance);
            if (instance.getLabel() && !label) {
                fp++;
            } else if (!instance.getLabel() && label) {
                fn++;
            } else if (instance.getLabel() && label) {
                tp++;
            }
        }
        double precision = (double) tp / (tp + fp);
        double recall = (double) tp / (tp + fn);
        return 2 * (precision * recall) / (precision + recall);
    }

    private List<Instance> readInstances(String featuresFile, String labelsFile) throws IOException {
        List<Instance> instances = new ArrayList<>();
        try (BufferedReader featuresReader = new BufferedReader(new FileReader(featuresFile));
             BufferedReader labelsReader = new BufferedReader(new FileReader(labelsFile))) {
            for (String line = featuresReader.readLine(); line != null; line = featuresReader.readLine()) {
                String[] features = line.split(" ");
                Instance instance = new Instance(features.length);
                for (int f = 0; f < features.length; f++) {
                    int value = Integer.parseInt(features[f]);
                    instance.set(f, value);
                }
                boolean label = labelsReader.readLine().equals("1");
                instance.setLabel(label);
                instances.add(instance);
            }
        }
        return instances;
    }

    private String getResource(String file) {
        return getClass().getClassLoader().getResource(file).getFile();
    }
}
