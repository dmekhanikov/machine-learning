package megabyte.ml.features;

import megabyte.ml.Instance;
import megabyte.ml.util.Measures;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    public static final int SPEARMAN = 1;
    public static final int PEARSON = 2;
    public static final int IG = 3;

    private int measure = SPEARMAN;

    public List<Double> rankFeatures(List<Instance> instances) {
        List<Double> ranks = new ArrayList<>();
        int featuresCount = instances.get(0).size();
        double[] labels = getLabels(instances);
        for (int i = 0; i < featuresCount; i++) {
            double[] fValues = getFeatureValues(instances, i);
            ranks.add(getRank(fValues, labels));
        }
        return ranks;
    }

    private double getRank(double[] fValues, double[] labels) {
        double rank;
        switch (measure) {
            case SPEARMAN:
                rank = Math.abs(Measures.spearman(fValues, labels));
                break;
            case PEARSON:
                rank = Math.abs(Measures.pearson(fValues, labels));
                break;
            case IG:
                rank = Measures.informationGain(fValues, labels);
                break;
            default:
                throw new IllegalStateException("Measure has an invalid value");
        }
        if (Double.isNaN(rank)) {
            return 0;
        } else {
            return rank;
        }
    }

    public void setMeasure(int measure) {
        if (measure != SPEARMAN && measure != PEARSON && measure != IG) {
            throw new IllegalArgumentException();
        }
        this.measure = measure;
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
