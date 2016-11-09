package megabyte.ml.examples;

import megabyte.ml.Instance;
import megabyte.ml.features.Filter;
import megabyte.ml.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilteringExample {

    public static void main(String[] args) throws IOException {
        List<Instance> testSet = DataReader.readInstancesFromResources("classifiers/train.data", "classifiers/train.labels");
        Filter filter = new Filter();
        List<Double> ranks = filter.rankFeatures(testSet);
        List<Pair<Double, Integer>> featuresRanking = new ArrayList<>();
        for (int i = 0; i < ranks.size(); i++) {
            featuresRanking.add(new Pair<>(ranks.get(i), i));
        }
        Collections.sort(featuresRanking, Pair.firstComparator());
        Collections.reverse(featuresRanking);
        for (int i = 0; i < featuresRanking.size(); i++) {
            Pair<Double, Integer> p = featuresRanking.get(i);
            System.out.println(i + ": Feature #" + p.second() + ": " + p.first());
        }
    }
}
