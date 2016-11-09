package megabyte.ml.features;

import megabyte.ml.Instance;
import megabyte.ml.util.Measures;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    public List<Double> rankFeatures(List<Instance> instances) {
        List<Double> ranks = new ArrayList<>();
        int featuresCount = instances.get(0).size();
        double[] labels = getLabels(instances);
        for (int i = 0; i < featuresCount; i++) {
            double[] fValues = getFeatureValues(instances, i);
            double spearman = Measures.spearman(labels, fValues);
            ranks.add(Math.abs(spearman));
        }
        return ranks;
    }

    private double[] getLabels(List<Instance> instances) {
        double[] labels = new double[instances.size()];
        for (int i = 0; i < instances.size(); i++) {
            if (instances.get(i).getLabel()) {
                labels[i] = 1;
            } else {
                labels[i] = -1;
            }
        }
        return labels;
    }

    private double[] getFeatureValues(List<Instance> instances, int f) {
        double[] values = new double[instances.size()];
        for (int i = 0; i < instances.size(); i++) {
            values[i] = instances.get(i).get(f);
        }
        return values;
    }
}
