package megabyte.ml.examples;

import megabyte.ml.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataReader {

    public static List<Instance> readInstances(String featuresFile, String labelsFile) throws IOException {
        List<Instance> instances = new ArrayList<>();
        try (BufferedReader featuresReader = new BufferedReader(new FileReader(featuresFile));
             BufferedReader labelsReader = new BufferedReader(new FileReader(labelsFile))) {
            for (String line = featuresReader.readLine(); line != null; line = featuresReader.readLine()) {
                String[] features = line.split(" ");
                Instance instance = new Instance(features.length);
                for (int f = 0; f < features.length; f++) {
                    int value = Integer.parseInt(features[f]);
                    instance.set(f, value);
                }
                boolean label = labelsReader.readLine().equals("1");
                instance.setLabel(label);
                instances.add(instance);
            }
        }
        return instances;
    }

    public static List<Instance> readInstancesFromResources(String featuresResource, String labelsResource) throws IOException {
        String featuresFile = getResource(featuresResource);
        String labelsFile = getResource(labelsResource);
        return readInstances(featuresFile, labelsFile);
    }

    public static String getResource(String file) {
        return DataReader.class.getClassLoader().getResource(file).getFile();
    }
}
