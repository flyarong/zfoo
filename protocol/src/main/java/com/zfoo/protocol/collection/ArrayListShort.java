/*
 * Copyright (C) 2020 The zfoo Authors
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.protocol.collection;

import java.util.*;

/**
 * @author godotg
 * @version 3.0
 */
public class ArrayListShort implements List<Short> {

    private short[] array;
    private int size;

    public ArrayListShort(int initialCapacity) {
        this.array = new short[initialCapacity];
    }

    public ArrayListShort(short[] array) {
        this.array = array;
        this.size = array.length;
    }

    public void fromList(List<Short> list) {
        if (CollectionUtils.isEmpty(list)) {
            this.size = 0;
            return;
        }
        this.size = list.size();
        this.array = ArrayUtils.shortToArray(list);
    }

    public List<Short> toList() {
        if (isEmpty()) {
            return new ArrayList<>();
        }
        return ArrayUtils.toList(toArrayPrimitive());
    }

    public short[] toArrayPrimitive() {
        return Arrays.copyOf(array, size);
    }

    private int getCapacity() {
        return array.length - size;
    }

    private void ensureCapacity(int capacity) {
        var remainCapacity = getCapacity();
        if (capacity > remainCapacity) {
            array = Arrays.copyOf(array, size + capacity + 16);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of range, size = " + size);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object ele) {
        return indexOfPrimitive(ArrayUtils.shortValue((Short) ele)) >= 0;
    }

    @Override
    public Short get(int index) {
        return getPrimitive(index);
    }

    public short getPrimitive(int index) {
        Objects.checkIndex(index, size);
        return array[index];
    }

    @Override
    public Short set(int index, Short ele) {
        var old = array[index];
        setPrimitive(index, ArrayUtils.shortValue(ele));
        return old;
    }

    public void setPrimitive(int index, short ele) {
        Objects.checkIndex(index, size);
        array[index] = ele;
    }

    @Override
    public boolean add(Short ele) {
        return addPrimitive(ArrayUtils.shortValue(ele));
    }

    public boolean addPrimitive(short ele) {
        ensureCapacity(1);
        array[size++] = ele;
        return true;
    }

    @Override
    public void add(int index, Short ele) {
        addPrimitive(index, ArrayUtils.shortValue(ele));
    }

    public void addPrimitive(int index, short ele) {
        checkIndexForAdd(index);
        ensureCapacity(1);
        System.arraycopy(array, index, array, index + 1, size - index);
        array[index] = ele;
        size++;
    }

    @Override
    public boolean addAll(Collection<? extends Short> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return false;
        }
        ensureCapacity(collection.size());
        for (var ele : collection) {
            add(ele);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Short> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return false;
        }
        checkIndexForAdd(index);
        ensureCapacity(collection.size());
        var appendArray = ArrayUtils.shortToArray(new ArrayList<>(collection));
        var appendLength = appendArray.length;

        var moved = size - index;
        if (moved > 0) {
            System.arraycopy(array, index, array, index + appendLength, moved);
        }

        System.arraycopy(appendArray, 0, array, index, appendLength);
        size += appendLength;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        var index = indexOf(o);
        if (index < 0) {
            return false;
        }
        remove(index);
        return true;
    }

    @Override
    public Short remove(int index) {
        return removeAt(index);
    }

    public short removeAt(int index) {
        Objects.checkIndex(index, size);
        var oldValue = array[index];
        var newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(array, index + 1, array, index, newSize - index);
        }
        size--;
        return oldValue;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return false;
        }
        var list = toList();
        var flag = list.removeAll(collection);
        fromList(list);
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        var list = toList();
        var flag = list.retainAll(collection);
        fromList(list);
        return flag;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public Object[] toArray() {
        return toList().toArray();
    }

    @Override
    public <T> T[] toArray(T[] arrays) {
        return toList().toArray(arrays);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return toList().containsAll(collection);
    }

    public int indexOfPrimitive(short ele) {
        for (var i = 0; i < size; i++) {
            if (array[i] == ele) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOfPrimitive(short ele) {
        for (var i = size - 1; i >= 0; i--) {
            if (array[i] == ele) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexOf(Object ele) {
        return indexOfPrimitive(ArrayUtils.shortValue((Short) ele));
    }

    @Override
    public int lastIndexOf(Object ele) {
        return lastIndexOfPrimitive(ArrayUtils.shortValue((Short) ele));
    }

    @Override
    public Iterator<Short> iterator() {
        return new FastIterator();
    }

    @Override
    public ListIterator<Short> listIterator() {
        return new FastListIterator(0);
    }

    @Override
    public ListIterator<Short> listIterator(int index) {
        return new FastListIterator(index);
    }

    @Override
    public List<Short> subList(int fromIndex, int toIndex) {
        return ArrayUtils.toList(array).subList(fromIndex, toIndex);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append('[');
        for (var i = 0; i < size; i++) {
            builder.append(array[i]);
            if (i < size - 1) {
                builder.append(", ");
            }
        }
        builder.append(']');
        return builder.toString();
    }

    private class FastIterator implements Iterator<Short> {
        int cursor;       // index of next element to return
        int lastCursor = -1; // index of last element returned; -1 if no such

        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public Short next() {
            if (cursor >= size) {
                throw new NoSuchElementException();
            }
            lastCursor = cursor;
            return array[cursor++];
        }

        @Override
        public void remove() {
            if (lastCursor < 0) {
                throw new IllegalStateException();
            }
            removeAt(lastCursor);
            cursor = lastCursor;
            lastCursor = -1;
        }
    }

    private class FastListIterator extends FastIterator implements ListIterator<Short> {
        FastListIterator(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public Short previous() {
            if (cursor <= 0) {
                throw new NoSuchElementException();
            }
            cursor--;
            lastCursor = cursor;
            return array[cursor];
        }

        @Override
        public void set(Short e) {
            if (lastCursor < 0) {
                throw new IllegalStateException();
            }
            setPrimitive(lastCursor, ArrayUtils.shortValue(e));
        }

        @Override
        public void add(Short e) {
            addPrimitive(cursor++, e);
            lastCursor = -1;
        }
    }
}
