package org.java.collect;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CollectionPerformanceExperiment {

    static private final Integer[] elements = createElements();
    static private long controlSum = 0;
    static private volatile List<Integer> resultList;

    private static Integer[] createElements(){
        Integer[] result = new Integer[10_000];
        for(int i = 0; i < 10_000; i++){
            result[i] = i;
        }
        return result;
    }

    static private void rightAdd(List<Integer> list){
        for(int i = 0; i < 10_000; i++){
            list.add(elements[i]);
        }
    }

    static private void leftAdd(List<Integer> list){
        for(int i = 0; i < 10_000; i++){
            list.addFirst(elements[i]);
        }
    }

    static private void middleAdd(List<Integer> list){
        for(int i = 0; i < 10_000; i++){
            list.add(list.size() / 2, elements[i]);
        }
    }

    static private long getByIndex(List<Integer> list){
        long sum = 0;
        for(int i = 0; i < 10_000; i++){
            sum += list.get(i);
        }
        return sum;
    }

    static private long leftRemove(List<Integer> list){
        long sum = 0;
        while(!list.isEmpty()){
            sum += list.removeFirst();
        }
        return sum;
    }

    static private long rightRemove(List<Integer> list){
        long sum = 0;
        while(!list.isEmpty()){
            sum += list.removeLast();
        }
        return sum;
    }

    private static long getSum(List<Integer> list){
        long sum = 0;
        for(int element : list){
            sum += element;
        }
        return sum;
    }

    private static void useResult(List<Integer> list, long sum){
        controlSum += sum;
        resultList = list;
    }

    private static long calcMiddle(long[] result){
        long sum = 0;
        for(long element : result){
            sum += element;
        }
        return sum / result.length;
    }

    private static long rightAddTest(boolean arrayList){
        long[] result = new long[5];
        for(int i = 0; i < 7; i++){
            List<Integer> list;
            if(arrayList){
                list = new ArrayList<>();
            } else {
                list = new LinkedList<>();
            }

            long start = System.nanoTime();
            rightAdd(list);
            long stop = System.nanoTime();

            if(i >= 2){
                result[i - 2] = stop - start;
            }
            useResult(list, getSum(list));
        }
        return calcMiddle(result);
    }

    private static long leftAddTest(boolean arrayList){
        long[] result = new long[5];
        for(int i = 0; i < 7; i++){
            List<Integer> list;
            if(arrayList){
                list = new ArrayList<>();
            } else {
                list = new LinkedList<>();
            }

            long start = System.nanoTime();
            leftAdd(list);
            long stop = System.nanoTime();

            if(i >= 2){
                result[i - 2] = stop - start;
            }
            useResult(list, getSum(list));
        }
        return calcMiddle(result);
    }

    private static long middleAddTest(boolean arrayList){
        long[] result = new long[5];
        for(int i = 0; i < 7; i++){
            List<Integer> list;
            if(arrayList){
                list = new ArrayList<>();
            } else {
                list = new LinkedList<>();
            }

            long start = System.nanoTime();
            middleAdd(list);
            long stop = System.nanoTime();

            if(i >= 2){
                result[i - 2] = stop - start;
            }
            useResult(list, getSum(list));
        }
        return calcMiddle(result);
    }

    private static long getByIndexTest(boolean arrayList){
        long[] result = new long[5];
        for(int i = 0; i < 7; i++){
            List<Integer> list;
            if(arrayList){
                list = new ArrayList<>();
            } else {
                list = new LinkedList<>();
            }
            rightAdd(list);

            long start = System.nanoTime();
            long sum = getByIndex(list);
            long stop = System.nanoTime();

            if(i >= 2){
                result[i - 2] = stop - start;
            }
            useResult(list, sum);
        }
        return calcMiddle(result);
    }

    private static long leftRemoveTest(boolean arrayList){
        long[] result = new long[5];
        for(int i = 0; i < 7; i++){
            List<Integer> list;
            if(arrayList){
                list = new ArrayList<>();
            } else {
                list = new LinkedList<>();
            }
            rightAdd(list);

            long start = System.nanoTime();
            long sum = leftRemove(list);
            long stop = System.nanoTime();

            if(i >= 2){
                result[i - 2] = stop - start;
            }
            useResult(list, sum);
        }
        return calcMiddle(result);
    }

    private static long rightRemoveTest(boolean arrayList){
        long[] result = new long[5];
        for(int i = 0; i < 7; i++){
            List<Integer> list;
            if(arrayList){
                list = new ArrayList<>();
            } else {
                list = new LinkedList<>();
            }
            rightAdd(list);

            long start = System.nanoTime();
            long sum = rightRemove(list);
            long stop = System.nanoTime();

            if(i >= 2){
                result[i - 2] = stop - start;
            }
            useResult(list, sum);
        }
        return calcMiddle(result);
    }

    static public void main(String[] args){
        controlSum = 0;
        StringBuilder table = new StringBuilder();
        table.append("Среднее время пяти измерений, нс\n");
        table.append("Добавления: пустой список -> 10_000 элементов.\n");
        table.append("Чтение и удаления: исходный список из 10_000 элементов.\n\n");
        table.append("Операция | Элементов | ArrayList, нс | LinkedList, нс\n");

        table.append("Добавление в конец | 10000 | ").append(rightAddTest(true)).append(" | ").append(rightAddTest(false)).append("\n");

        table.append("Добавление в начало | 10000 | ").append(leftAddTest(true)).append(" | ").append(leftAddTest(false)).append("\n");

        table.append("Вставка в середину | 10000 | ").append(middleAddTest(true)).append(" | ").append(middleAddTest(false)).append("\n");

        table.append("Последовательный доступ | 10000 | ").append(getByIndexTest(true)).append(" | ").append(getByIndexTest(false)).append("\n");

        table.append("Удаление всех из начала | 10000 | ").append(leftRemoveTest(true)).append(" | ").append(leftRemoveTest(false)).append("\n");

        table.append("Удаление всех из конца | 10000 | ").append(rightRemoveTest(true)).append(" | ").append(rightRemoveTest(false)).append("\n");

        System.out.println(table);
        System.out.println("Контрольная сумма: " + controlSum);
        System.out.println("\nВывод по тестам:");
        System.out.println("Добавление в конец ArrayList имеет сложность O*(1)");
        System.out.println("LinkedList добавляет элементы за O(1)");
        System.out.println("Добавление в начало в ArrayList требудет свдига всех элементов вправо, поэтому стоимость на n добавлений O(n^2)");
        System.out.println("LinkedList выполняет это за O(1) поэтому для n добавлений O(n)");
        System.out.println("При вставке в середину по индексу ArrayList сдвигает элементы, а LinkedList ищет узел, поэтому одна вставка у обоих имеет стоимость O(n)");
        System.out.println("Последовательный доступ по пиндексу ко всем элементам ArrayList занимает O(n), а LinkedList за O(n^2) потому что повторно ищется узел через другие узлы");
        System.out.println("Удаление всех элементов с конца имеет стоимость O(n) у наших контейнеров");
        System.out.println("LinkedList полезен когда присутсвует много запросов на добавление в начало или конец, но не взять по индексу");
    }
}
