package com.codesanook.util;

import java.util.*;

public class SetUtils {


    public static <T> T[] union(T[] array1, T[] array2) {
        return (T[]) union(Arrays.asList(array1), Arrays.asList(array2)).toArray();
    }


    public static <T> List<T> union(List<T> list1, List<T> list2) {

        Set<T> set1 = new HashSet();
        for (T item : list1) {
            if (!set1.contains(item)) {
                set1.add(item);
            }
        }


        for (T item : list2) {
            if (set1.contains(item)) continue;

            set1.add(item);
        }


        List<T> unionResult = new ArrayList<>();
        for (T item : set1) {
            unionResult.add(item);
        }
        return unionResult;
    }


    public static <T> T[] intersect(T[] array1, T[] array2) {
        return (T[]) intersect(Arrays.asList(array1), Arrays.asList(array2)).toArray();
    }

    public static <T> List<T> intersect(List<T> list1, List<T> list2) {

        Set<T> set1 = new HashSet();
        for (T item : list1) {
            if (!set1.contains(item)) {
                set1.add(item);
            }
        }

        ArrayList<T> intersectResult = new ArrayList<>();

        for (T item : list2) {
            if (set1.contains(item)) {
                intersectResult.add(item);
            }
        }
        return intersectResult;
    }


    public static <T> T[] subtract(T[] array1, T[] subtractByArray2) {
        return (T[]) subtract(Arrays.asList(array1), Arrays.asList(subtractByArray2)).toArray();
    }

    public static <T> List<T> subtract(List<T> list1, List<T> subtractBylist2) {

        Set<T> set2 = new HashSet();
        for (T item : subtractBylist2) {
            if (!set2.contains(item)) {
                set2.add(item);
            }
        }


        ArrayList<T> subtractResult = new ArrayList<>();
        for (T item : list1) {
            if (!set2.contains(item)) {
                subtractResult.add(item);
            }
        }

        return subtractResult;
    }
}

