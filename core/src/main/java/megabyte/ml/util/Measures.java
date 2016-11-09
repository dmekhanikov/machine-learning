package megabyte.ml.util;

import megabyte.ml.Instance;
import megabyte.ml.classifiers.Classifier;

import java.util.*;

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

    public static double pearson(double[] x, double[] y) {
        return cov(x, y) / Math.sqrt(D(x) * D(y));
    }

    public static double cov(double[] x, double[] y) {
        int n = x.length;
        double ex = E(x);
        double ey = E(y);
        double cov = 0;
        for (int i = 0; i < n; i++) {
            cov += (x[i] - ex) * (y[i] - ey) / n;
        }
        return cov;
    }

    public static double E(double[] x) {
        int n = x.length;
        double e = 0;
        for (double a : x) {
            e += a / n;
        }
        return e;
    }

    public static double D(double[] x) {
        return cov(x, x);
    }

    // y should be in {1, -1}
    public static double informationGain(double[] x, double[] y) {
        List<Pair<Double, Double>> cases = new ArrayList<>();
        int n = x.length;
        for (int i = 0; i < n; i++) {
            cases.add(new Pair<>(x[i], y[i]));
        }
        int p = Lists.partition(cases, c -> c.second() == -1);
        return entropy(cases) - entropy(cases.subList(0, p)) / p - entropy(cases.subList(p, n)) / (n - p);
    }

    public static <T> double entropy(List<T> x) {
        Map<T, Integer> hist = new HashMap<>();
        for (T a : x) {
            Integer count = hist.get(a);
            if (count == null) {
                count = 0;
            }
            hist.put(a, count + 1);
        }
        double h = 0;
        int n = x.size();
        for (int count : hist.values()) {
            double p = (double) count / n;
            h -= p * Math.log(p) / Math.log(2);
        }
        return h;
    }
}
