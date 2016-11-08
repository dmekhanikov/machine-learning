package megabyte.ml.examples;

import megabyte.ml.Instance;
import megabyte.ml.classifiers.Classifier;
import megabyte.ml.classifiers.DecisionTree;
import megabyte.ml.util.Measures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        int buildSetSize = trainSet.size() * 2 / 3;
        List<Instance> buildSet = trainSet.subList(0, buildSetSize);
        decisionTree.train(buildSet);
        List<Instance> pruneSet = trainSet.subList(buildSetSize, trainSet.size());
        decisionTree.prune(pruneSet);

        String validDataFile = getResource("random-forest/arcene_valid.data");
        String validLabelsFile = getResource("random-forest/arcene_valid.labels");
        List<Instance> validSet = readInstances(validDataFile, validLabelsFile);
        double f1 = Measures.f1Measure(decisionTree, validSet);
        System.out.println("F1 Measure: " + f1);
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
