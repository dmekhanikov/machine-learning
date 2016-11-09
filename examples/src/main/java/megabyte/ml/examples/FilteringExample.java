package megabyte.ml.examples;

import megabyte.ml.Instance;
import megabyte.ml.classifiers.LibSVMWrapper;
import megabyte.ml.features.Filter;
import megabyte.ml.util.DataTransformer;
import megabyte.ml.util.Measures;
import megabyte.ml.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FilteringExample {

    public static void main(String[] args) throws IOException {
        new FilteringExample().doMain();
    }

    private void doMain() throws IOException {
        List<Instance> trainSet = DataReader.readInstancesFromResources("classifiers/train.data", "classifiers/train.labels");
        Filter filter = new Filter();
        filter.setMeasure(Filter.IG);
        List<Double> ranks = filter.rankFeatures(trainSet);
        List<Pair<Double, Integer>> featuresRanking = new ArrayList<>();
        for (int i = 0; i < ranks.size(); i++) {
            featuresRanking.add(new Pair<>(ranks.get(i), i));
        }
        Collections.sort(featuresRanking, Pair.firstComparator());
        Collections.reverse(featuresRanking);
        List<Integer> features = featuresRanking.stream().map(Pair::second).collect(Collectors.toList());
        int c = features.size() / 100;
        double[] xs = new double[c];
        double[] ys = new double[c];
        for (int i = 0; i < c; i++) {
            int count = i * 100;
            double f1 = svmF1Measure(trainSet, features.subList(0, count));
            System.out.println(count + " features: " + f1);
            xs[i] = count;
            ys[i] = f1;
        }
        Plotter.plot(xs, ys);

        List<Instance> validSet = DataReader.readInstancesFromResources("classifiers/valid.data", "classifiers/valid.labels");
        int featuresCount = 1300;
        double f1 = svmF1Measure(validSet, features.subList(0, featuresCount));
        System.out.println("F1 measure on valid set with " + featuresCount + " features: " + f1);
    }

    private double svmF1Measure(List<Instance> instances, List<Integer> features) {
        List<Instance> filteredInstances = DataTransformer.selectFeatures(instances, features);
        int n = instances.size();
        List<Instance> trainSet = filteredInstances.subList(0, n * 2 / 3);
        List<Instance> validSet = filteredInstances.subList(n * 2 / 3, n);
        LibSVMWrapper svm = new LibSVMWrapper();
        svm.train(trainSet);
        return Measures.f1Measure(svm, validSet);
    }
}
