package org.java.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Math.max;


/**
 * Самораширяющийся контейнер
 *
 * @param <E> Тип доавляемых объектов
 */
public class CustomArrayList<E> implements CustomList<E> {

    private class CustomIterator implements Iterator<E>{
        int pos = -1;
        @Override
        public boolean hasNext() {
            return pos + 1 < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            pos++;
            return (E) massive[pos];
        }
    }

    Object[] massive = null;
    int size = 0;
    int capacity = 0;

    /**
     * Конструктор без параметров capacity = 10
     */
    public CustomArrayList(){
        capacity = 10;
        massive = new Object[10];
    }

    /**
     * @param capacity начальное значение размера контейнера
     */
    public CustomArrayList(int capacity){
        if (capacity >= 0){
            this.capacity = capacity;
        }
        massive = new Object[capacity];
    }

    private void incCapacity(){
        int newCapacity = Math.toIntExact(Math.round(capacity * 1.5));
        newCapacity = max(1,newCapacity);
        Object[] newMassive = new Object[newCapacity];

        System.arraycopy(massive,0,newMassive,0,capacity);

        massive = newMassive;
        capacity = newCapacity;
    }

    @Override
    public Iterator<E> iterator() {
        return new CustomIterator();
    }

    /**
     * Добавляет элемент в конец и увеличивает размер емкость в 1.5 раза если надо
     * @param element добавляемый элемент не null
     * @throws NullPointerException если element null
     */
    @Override
    public void add(E element) {
        if (element == null){
            throw new NullPointerException("the element must not be null!");
        }

        if (size >= capacity){
            incCapacity();
        }

        massive[size] = element;
        size++;
    }

    /**
     * Возвращает элемент по индексу
     * @param index индекс от 0 до size
     * @return возвращает элемент по индексу переданного типа
     * @throws IndexOutOfBoundsException index не в [0, size)
     */
    @Override
    public E get(int index) {
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        return (E) massive[index];
    }

    /**
     * Удаляет элемент по индексу и сдивгает элементы справа от него на 1
     * @param index индекс элемента которого удаляем
     * @return возвращает удаленный элемент
     * @throws IndexOutOfBoundsException index не в [0, size)
     */
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }

        Object delElement = massive[index];
        System.arraycopy(massive,index+1, massive, index, size - index - 1);
        massive[size-1] = null;
        size--;
        return (E) delElement;
    }

    /**
     * @return количество элементов в контейнере
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * @return true если контейнер пуст
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
