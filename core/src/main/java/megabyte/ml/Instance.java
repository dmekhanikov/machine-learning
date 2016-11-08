package megabyte.ml;

import lombok.Getter;
import lombok.Setter;

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
}
