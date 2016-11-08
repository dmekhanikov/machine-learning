package megabyte.ml.examples;

import megabyte.ml.Instance;
import megabyte.ml.classifiers.DecisionTree;
import megabyte.ml.util.Measures;

import java.io.IOException;
import java.util.List;

public class DecisionTreeExample {

    public static void main(String[] args) throws IOException {
        new DecisionTreeExample().doMain();
    }

    private void doMain() throws IOException {
        List<Instance> trainSet = DataReader.readInstancesFromResources("random-forest/arcene_train.data", "random-forest/arcene_train.labels");
        DecisionTree decisionTree = new DecisionTree();
        int buildSetSize = trainSet.size() * 2 / 3;
        List<Instance> buildSet = trainSet.subList(0, buildSetSize);
        decisionTree.train(buildSet);
        List<Instance> pruneSet = trainSet.subList(buildSetSize, trainSet.size());
        decisionTree.prune(pruneSet);

        List<Instance> validSet = DataReader.readInstancesFromResources("random-forest/arcene_valid.data", "random-forest/arcene_valid.labels");
        double f1 = Measures.f1Measure(decisionTree, validSet);
        System.out.println("F1 Measure: " + f1);
    }
}
