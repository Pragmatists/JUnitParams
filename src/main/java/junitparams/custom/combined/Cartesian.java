package junitparams.custom.combined;

import java.util.Arrays;
import java.util.List;

class Cartesian {

    static Object[] getCartesianProductOf(List<Object[]> array) {
        if (array == null || array.size() == 0) {
            return new Object[]{};
        }

        for (int i = 0; i < array.size() - 1; i++) {
            Object[] arrayOne = array.get(i);
            Object[] arrayTwo = array.get(i + 1);
            array.set(i + 1, cartesianProduct(arrayOne, arrayTwo));
        }

        return array.get(array.size() - 1);
    }

    private static Object[] cartesianProduct(Object[] arrayOne, Object[] arrayTwo) {
        int numberOfCombinations = arrayOne.length * arrayTwo.length;
        Object[] resultArray = new Object[numberOfCombinations][2];

        int i = 0;
        for (Object firstElement : arrayOne) {
            for (Object secondElement : arrayTwo) {
                resultArray[i] = getCartesianOfTwoElements(firstElement, secondElement);
                i++;
            }
        }

        return resultArray;
    }

    private static Object getCartesianOfTwoElements(Object objectOne, Object objectTwo) {
        if (!objectOne.getClass().isArray()) {
            return new Object[]{objectOne, objectTwo};
        }
        Object[] initialArray = (Object[]) objectOne;
        Object[] newArray = Arrays.copyOf(initialArray, initialArray.length + 1);
        newArray[newArray.length - 1] = objectTwo;
        return newArray;
    }
}
