package megabyte.ml.util;

import megabyte.ml.Instance;
import megabyte.ml.classifiers.Classifier;

import java.util.ArrayList;
import java.util.Collections;
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

    public static double spearman(double[] x, double[] y) {
        List<Pair<Double, Integer>> xRanks = new ArrayList<>();
        int n = x.length;
        for (int i = 0; i < n; i++) {
            xRanks.add(new Pair<>(x[i], i));
        }
        List<Pair<Double, Integer>> yRanks = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            yRanks.add(new Pair<>(y[i], i));
        }
        Collections.sort(xRanks, Pair.firstComparator());
        Collections.sort(yRanks, Pair.firstComparator());
        double rho = 1;
        for (int i = 0; i < n; i++) {
            double q = Math.pow(xRanks.get(i).second() - yRanks.get(i).second(), 2) / n / (n * n - 1);
            rho -= q;
        }
        return rho;
    }
}
