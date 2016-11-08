package megabyte.ml.examples;

import megabyte.ml.Instance;
import megabyte.ml.classifiers.LibSVMWrapper;
import megabyte.ml.util.Measures;

import java.io.IOException;
import java.util.List;

public class SVMExample {

    private void doMain() throws IOException {
        List<Instance> trainSet = DataReader.readInstancesFromResources("classifiers/train.data", "classifiers/train.labels");
        LibSVMWrapper svm = new LibSVMWrapper();
        svm.train(trainSet);

        List<Instance> validSet = DataReader.readInstancesFromResources("classifiers/valid.data", "classifiers/valid.labels");
        double f1 = Measures.f1Measure(svm, validSet);
        System.out.println("F1 Measure: " + f1);
    }

    public static void main(String[] args) throws IOException {
        new SVMExample().doMain();
    }
}
