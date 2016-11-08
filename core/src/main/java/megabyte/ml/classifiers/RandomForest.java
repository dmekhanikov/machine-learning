package megabyte.ml.classifiers;

import megabyte.ml.Instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomForest implements Classifier {

    private List<DecisionTree> trees = new ArrayList<>();
    private Random random = new Random(System.currentTimeMillis());

    public RandomForest(int treesCount) {
        for (int i = 0; i < treesCount; i++) {
            DecisionTree tree = new DecisionTree();
            tree.setRandomizeFeatures(true);
            tree.setMinNodeInstances(1);
            trees.add(tree);
        }
    }

    public void train(List<Instance> instances) {
        for (DecisionTree tree : trees) {
            tree.train(sample(instances));
        }
    }

    @Override
    public boolean classify(Instance instance) {
        int trueVotes = 0;
        for (DecisionTree tree : trees) {
            if (tree.classify(instance)) {
                trueVotes++;
            }
        }
        int falseVotes = trees.size() - trueVotes;
        return trueVotes > falseVotes;
    }

    private List<Instance> sample(List<Instance> instances) {
        List<Instance> result = new ArrayList<>();
        int n = instances.size();
        for (int i = 0; i < n; i++) {
            Instance instance = instances.get(random.nextInt(n));
            result.add(instance);
        }
        return result;
    }
}
