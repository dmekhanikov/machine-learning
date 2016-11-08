package megabyte.ml.classifiers;

import lombok.Setter;
import megabyte.ml.Instance;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class DecisionTree {

    private TreeNode root;

    @Setter
    private int minNodeInstances = 10;

    private Random random = new Random(System.currentTimeMillis());

    public void train(List<Instance> trainingSet) {
        this.root = new TreeNode();
        buildTree(this.root, trainingSet);
    }

    public boolean classify(Instance instance) {
        TreeNode node = root;
        while (node.label == null) {
            int value = instance.get(node.featureNum);
            if (value < node.threshold) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return node.label;
    }

    private void buildTree(TreeNode node, List<Instance> instances) {
        if (instances.size() <= minNodeInstances || isOneClass(instances)) {
            node.label = majority(instances);
            return;
        }
        int instanceSize = instances.get(0).size();
        double minGini = 1;
        int bestF = -1;
        int bestT = -1;
        for (int f = 0; f < instanceSize; f++) {
            for (int t : featureValues(instances, f)) {
                double gini = avgGini(instances, f, t);
                if (gini < minGini) {
                    minGini = gini;
                    bestF = f;
                    bestT = t;
                }
            }
        }
        node.featureNum = bestF;
        node.threshold = bestT;
        int p = partition(instances, bestF, bestT);
        node.left = new TreeNode();
        buildTree(node.left, instances.subList(0, p));
        node.right = new TreeNode();
        buildTree(node.right, instances.subList(p, instances.size()));
    }

    private boolean isOneClass(List<Instance> instances) {
        boolean firstLabel = instances.get(0).getLabel();
        for (int i = 1; i < instances.size(); i++) {
            if (instances.get(i).getLabel() != firstLabel) {
                return false;
            }
        }
        return true;
    }

    // weighted Gini index for split using feature number f and threshold equal to t
    private double avgGini(List<Instance> instances, int f, int t) {
        int p = partition(instances, f, t);
        int n = instances.size();
        double gini1 = gini(instances.subList(0, p));
        double gini2 = gini(instances.subList(0, n));
        return (double) p / n * gini1 + (1 - (double) p / n) * gini2;
    }

    private double gini(List<Instance> instances) {
        long falseCount = instances.stream().filter(instance -> !instance.getLabel()).count();
        double p = (double) falseCount / instances.size();
        return 1 - Math.pow(p, 2) - Math.pow(1 - p, 2);
    }

    private int partition(List<Instance> instances, int f, int t) {
        int i = 0;
        for (int j = 0; j < instances.size(); j++) {
            if (instances.get(j).get(f) < t) {
                Collections.swap(instances, i, j);
                i++;
            }
        }
        return i;
    }

    // returns value of the majority of labels
    private boolean majority(List<Instance> instances) {
        long falseCount = instances.stream().filter(instance -> !instance.getLabel()).count();
        long trueCount = instances.size() - falseCount;
        if (falseCount == trueCount) {
            return random.nextBoolean();
        } else {
            return trueCount > falseCount;
        }
    }

    // returns set of features number f in the provided list of instances
    private Set<Integer> featureValues(List<Instance> instances, int f) {
        return instances.stream().map(instance -> instance.get(f)).collect(Collectors.toSet());
    }

    private static class TreeNode {
        int featureNum;
        int threshold;
        Boolean label; // not null for leafs

        TreeNode left;
        TreeNode right;
    }
}
