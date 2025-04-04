package com.bobocode.se;

import java.lang.reflect.Field;
import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * A generic comparator that is comparing a random field of the given class. The field is either primitive or
 * {@link Comparable}. It is chosen during comparator instance creation and is used for all comparisons.
 * <p>
 * If no field is available to compare, the constructor throws {@link IllegalArgumentException}
 *
 * @param <T> the type of the objects that may be compared by this comparator
 *<p><p>
 *  <strong>TODO: to get the most out of your learning, <a href="https://www.bobocode.com">visit our website</a></strong>
 *  <p>
 *
 * @author Stanislav Zabramnyi
 */
public class RandomFieldComparator<T> implements Comparator<T> {

    private final Field comparingField;
    private final Class<T> targetType;

    public RandomFieldComparator(Class<T> targetType) {
        this.targetType = requireNonNull(targetType);
        this.comparingField = findRandomComparableField(targetType);
    }

    private Field findRandomComparableField(Class<T> targetType) {
        List<Field> comparableFields = Arrays.stream(targetType.getDeclaredFields())
                .filter(field -> Comparable.class.isAssignableFrom(field.getType()) || field.getType().isPrimitive())
                .toList();

        if (comparableFields.isEmpty()) {
            throw new IllegalArgumentException("No comparable fields found in class " + targetType.getName());
        }

        Random random = new Random();
        return comparableFields.get(random.nextInt(comparableFields.size()));
    }

    /**
     * Compares two objects of the class T by the value of the field that was randomly chosen. It allows null values
     * for the fields, and it treats null value greater than a non-null value.
     *
     * @param o1
     * @param o2
     * @return positive int in case of first parameter {@param o1} is greater than second one {@param o2},
     *         zero if objects are equals,
     *         negative int in case of first parameter {@param o1} is less than second one {@param o2}.
     */
    @Override
    public int compare(T o1, T o2) {
        try {
            Objects.requireNonNull(o1);
            Objects.requireNonNull(o2);
            return compareValues(o1, o2);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access field value", e);
        }
    }

    private int compareValues(T o1, T o2) throws IllegalAccessException {
        comparingField.setAccessible(true);

        Object value1 = comparingField.get(o1);
        Object value2 = comparingField.get(o2);

        if (value1 == null && value2 == null) return 0;
        if (value1 == null) return 1;
        if (value2 == null) return -1;

        return ((Comparable) value1).compareTo(value2);
    }

    /**
     * Returns the name of the randomly-chosen comparing field.
     */
    public String getComparingFieldName() {
        return comparingField.getName();
    }

    /**
     * Returns a statement "Random field comparator of class '%s' is comparing '%s'" where the first param is the name
     * of the type T, and the second parameter is the comparing field name.
     *
     * @return a predefined statement
     */
    @Override
    public String toString() {
        return String.format("Random field comparator of class '%s' is comparing '%s'",
                comparingField.getDeclaringClass().getSimpleName(), comparingField.getName());
    }
}
