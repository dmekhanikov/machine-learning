package megabyte.ml.classifiers;

import libsvm.*;
import lombok.Setter;
import megabyte.ml.Instance;

import java.util.List;

public class LibSVMWrapper implements Classifier {

    private svm_model model;

    @Setter
    private double gamma = 0.5;
    @Setter
    private double nu = 0.5;
    @Setter
    private double C = 100;
    @Setter
    private int cache_size = 20000;
    @Setter
    private double eps = 0.001;

    public void train(List<Instance> instances) {
        svm_problem problem = new svm_problem();
        problem.l = instances.size();
        problem.y = new double[problem.l];
        problem.x = new svm_node[problem.l][];
        for (int i = 0; i < problem.l; i++) {
            Instance instance = instances.get(i);
            problem.x[i] = new svm_node[instance.size()];
            for (int j = 0; j < instance.size(); j++) {
                svm_node node = new svm_node();
                node.index = j;
                node.value = instance.get(j);
                problem.x[i][j] = node;
            }
            if (instance.getLabel()) {
                problem.y[i] = 1;
            } else {
                problem.y[i] = -1;
            }
        }
        svm_parameter param = new svm_parameter();
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.LINEAR;
        param.gamma = this.gamma;
        param.nu = this.nu;
        param.C = this.C;
        param.eps = this.eps;
        param.cache_size = this.cache_size;
        this.model = svm.svm_train(problem, param);
    }

    @Override
    public boolean classify(Instance instance) {
        svm_node[] nodes = new svm_node[instance.size()];
        for (int i = 0; i < instance.size(); i++) {
            svm_node node = new svm_node();
            node.index = i;
            node.value = instance.get(i);
            nodes[i] = node;
        }
        double[] valueEstimate = new double[1];
        svm.svm_predict_values(model, nodes, valueEstimate);
        return valueEstimate[0] > 0;
    }
}
