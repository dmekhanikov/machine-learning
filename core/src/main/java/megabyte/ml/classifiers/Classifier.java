package megabyte.ml.classifiers;

import megabyte.ml.Instance;

public interface Classifier {
    boolean classify(Instance instance);
}
