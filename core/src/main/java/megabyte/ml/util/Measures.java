package megabyte.ml.util;

import megabyte.ml.Instance;
import megabyte.ml.classifiers.Classifier;

import java.util.List;

public class Measures {

    public static double f1Measure(Classifier classifier, List<Instance> instances) {
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
}
