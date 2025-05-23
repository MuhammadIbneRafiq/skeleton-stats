import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that maintains a dataset and provides statistical calculations
 * such as mean, median, mode, and variance. Supports adding and removing
 * data points.
 *
 * <p><b>Muhammad Rafiq, 1924214, 20 May</b></p>
 */
public class Statistician {

    private final List<Double> data = new ArrayList<>();
    private static final double EPSILON = 1e-6;

    /**
     * Adds a data point to the dataset.
     *
     * @param value the data point to add
     * @post the size of the dataset increases by one
     * @post the last element of the dataset is equal to {@code value}
     */
    public void addData(double value) {
        data.add(value);
    }

    /**
     * Removes all elements approximately equal to the specified value (within
     * EPSILON) from the dataset.
     *
     * @param value the target data point to remove
     * @return {@code true} if one or more elements were removed; {@code false}
     *         otherwise
     * @post no element in the dataset differs from {@code value} by at most
     *       EPSILON
     */
    public boolean removeData(double value) {
        boolean removed = false;
        var iterator = data.iterator();
        while (iterator.hasNext()) {
            if (Math.abs(iterator.next() - value) <= EPSILON) {
                iterator.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * Calculates the mean (average) of the dataset.
     *
     * @return the mean of all data points
     * @throws IllegalArgumentException if the dataset is empty
     */
    public double mean() {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Dataset is empty");
        }
        double sum = 0.0;
        for (double v : data) {
            sum += v;
        }
        return sum / data.size();
    }

    /**
     * Calculates the median of the dataset.
     *
     * @return the median value
     * @throws IllegalArgumentException if the dataset is empty
     */
    public double median() {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Dataset is empty");
        }
        List<Double> copy = new ArrayList<>(data);
        Collections.sort(copy);
        int n = copy.size();
        if (n % 2 == 0) {
            return (copy.get(n / 2 - 1) + copy.get(n / 2)) / 2.0;
        }
        return copy.get(n / 2);
    }

    /**
     * Calculates the mode (most frequent value) of the dataset. If multiple
     * values share the highest frequency, returns the smallest.
     *
     * @return the mode of the dataset
     * @throws IllegalArgumentException if the dataset is empty or all
     *         values are unique
     */
    public double mode() {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Dataset is empty");
        }
        Map<Double, Integer> freq = new HashMap<>();
        double bestValue = Double.MAX_VALUE;
        int bestCount = 0;
        for (double v : data) {
            double key = Math.round(v / EPSILON) * EPSILON;
            int count = freq.merge(key, 1, Integer::sum);
            if (count > bestCount || (count == bestCount && key < bestValue)) {
                bestCount = count;
                bestValue = key;
            }
        }
        if (bestCount <= 1) {
            throw new IllegalArgumentException("No mode: all values are unique");
        }
        return bestValue;
    }

    /**
     * Calculates the sample variance of the dataset.
     *
     * @return the sample variance
     * @throws IllegalArgumentException if the dataset is empty
     */
    public double variance() {
        int n = data.size();
        if (n == 0) {
            throw new IllegalArgumentException("Dataset is empty");
        }
        if (n == 1) {
            return 0.0;
        }
        double mean = mean();
        double sumSq = 0.0;
        for (double v : data) {
            double diff = v - mean;
            sumSq += diff * diff;
        }
        return sumSq / (n - 1);
    }

    /**
     * Returns the number of data points in the dataset.
     *
     * @return the dataset size
     */
    public int size() {
        return data.size();
    }
}
