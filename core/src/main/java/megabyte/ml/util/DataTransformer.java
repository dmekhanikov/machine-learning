package megabyte.ml.util;

import megabyte.ml.Instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataTransformer {

    public static List<Instance> selectFeatures(List<Instance> instances, Collection<Integer> features) {
        List<Instance> newInstances = new ArrayList<>();
        for (Instance instance : instances) {
            Instance newInstance = new Instance(features.size());
            int i = 0;
            for (int f : features) {
                newInstance.set(i++, instance.get(f));
            }
            newInstance.setLabel(instance.getLabel());
            newInstances.add(newInstance);
        }
        return newInstances;
    }
}
