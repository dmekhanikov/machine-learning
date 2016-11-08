package megabyte.ml.classifiers;

import megabyte.ml.Instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomForest implements Classifier {

    private int treesCount;
    private List<DecisionTree> trees;
    private Random random = new Random(System.currentTimeMillis());

    public RandomForest(int treesCount) {
        this.treesCount = treesCount;
    }

    public void train(List<Instance> instances) {
        this.trees = new ArrayList<>();
        for (int i = 0; i < treesCount; i++) {
            DecisionTree tree = createTree();
            tree.train(sample(instances));
            trees.add(tree);
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

    public List<Integer> filterFeatures(List<Instance> instances, double eps) {
        List<DecisionTree> filterTrees = new ArrayList<>();
        List<List<Instance>> dataSets = new ArrayList<>();
        for (int i = 0; i < treesCount; i++) {
            List<Instance> dataSet = sample(instances);
            dataSets.add(dataSet);
            DecisionTree tree = createTree();
            tree.train(dataSet);
            filterTrees.add(tree);
        }
        double oob = outOfBag(filterTrees, dataSets, instances);
        List<Integer> features = new ArrayList<>();
        int featuresCount = instances.get(0).size();
        for (int i = 0; i < featuresCount; i++) {
            int f = i; // to make it effective final
            List<Integer> initialValues = instances.stream()
                    .map(instance -> instance.get(f))
                    .collect(Collectors.toList());
            shuffleFeatures(instances, f);
            double shuffledOOB = outOfBag(filterTrees, dataSets, instances);
            if (shuffledOOB - oob > eps) {
                features.add(f);
                for (int j = 0; j < instances.size(); j++) {
                    int value = initialValues.get(j);
                    instances.get(j).set(f, value);
                }
            }
        }
        return features;
    }

    private void shuffleFeatures(List<Instance> instances, int f) {
        for (int i = 0; i < instances.size(); i++) {
            int j = random.nextInt(i + 1);
            Instance i1 = instances.get(i);
            Instance i2 = instances.get(j);
            int tmp = i1.get(f);
            i1.set(f, i2.get(f));
            i2.set(f, tmp);
        }
    }

    private double outOfBag(List<DecisionTree> filterTrees, List<List<Instance>> dataSets, List<Instance> instances) {
        double oob = 0;
        for (Instance instance : instances) {
            int errors = 0;
            int oobTreesCount = 0;
            for (int i = 0; i < dataSets.size(); i++) {
                if (!dataSets.get(i).contains(instance)) {
                    oobTreesCount++;
                    DecisionTree tree = filterTrees.get(i);
                    if (tree.classify(instance) != instance.getLabel()) {
                        errors++;
                    }
                }
            }
            if (oobTreesCount > 0) {
                oob += (double) errors / oobTreesCount;
            }
        }
        return oob;
    }

    private DecisionTree createTree() {
        DecisionTree tree = new DecisionTree();
        tree.setRandomizeFeatures(true);
        tree.setMinNodeInstances(1);
        return tree;
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
