package org.java.collect;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class CustomArrayListTest {

    @Test
    void checkEmptyArray(){
        CustomList<String> array = new CustomArrayList<>();
        assertEquals(0,array.size());
        assertTrue(array.isEmpty());
    }
    @Test
    void checkOrder(){
        CustomList<String> array = new CustomArrayList<>();
        array.add("a");
        array.add("b");
        array.add("c");
        assertEquals("a",array.get(0));
        assertEquals("b",array.get(1));
        assertEquals("c",array.get(2));
    }
    @Test
    void checkCapacityExtends(){
        CustomList<String> array = getFullArray(20);
        assertEquals(20,array.size());
        for (int i = 0; i < 20; i++){
            assertEquals(String.valueOf(i),array.get(i));
        }
    }
    @Test
    void checkGetAny(){
        CustomList<String> array = getFullArray(20);
        assertEquals("0", array.get(0));
        assertEquals("10", array.get(10));
        assertEquals("19", array.get(19));
    }

    @Test
    void checkOutIndexes(){
        CustomList<String> array = getFullArray(20);
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(array.size()));
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(array.size()));
    }

    @ParameterizedTest
    @ValueSource(ints = {0,15,29})
    void checkRemove(int index){
        CustomList<String> array = getFullArray(30);
        assertEquals(String.valueOf(index), array.remove(index));
        assertEquals(29, array.size());
        for(int i = 0; i < index; i++){
            assertEquals(String.valueOf(i), array.get(i));
        }
        for(int i = index; i < 29; i++){
            assertEquals(String.valueOf(i+1), array.get(i));
        }

    }

    @Test
    void checkNullAdd(){
        CustomArrayList<String> array = new CustomArrayList<>();
        assertThrows(NullPointerException.class, () -> array.add(null));
    }
    @Test
    void checkIterator(){
        CustomList<String> array = getFullArray(20);
        Iterator iterator = array.iterator();
        for(int i =0; iterator.hasNext(); i++){
            assertEquals(String.valueOf(i), iterator.next());
        }
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    private CustomArrayList<String> getFullArray(int size){
        CustomArrayList<String> array = new CustomArrayList<>();
        for (int i = 0; i < size ; i++){
            array.add(String.valueOf(i));
        }
        return array;
    }
}
