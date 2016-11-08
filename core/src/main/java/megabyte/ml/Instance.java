package megabyte.ml;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public class Instance {

    private int[] features;

    @Getter
    @Setter
    private Boolean label;

    public Instance(int len) {
        this.features = new int[len];
    }

    public void set(int i, int val) {
        this.features[i] = val;
    }

    public int get(int i) {
        return this.features[i];
    }

    public int size() {
        return features.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Instance instance = (Instance) o;

        if (!Arrays.equals(features, instance.features)) return false;
        return label != null ? label.equals(instance.label) : instance.label == null;

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(features);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }
}
