import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CarHashMap implements CarMap {

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size = 0;
    private Entry[] array = new CarHashMap.Entry[INITIAL_CAPACITY];

    @Override
    public void put(CarOwner key, Car value) {
        if (size >= (array.length * LOAD_FACTOR)) {
            increaseArray();
        }
        boolean added = put(key, value, array);
        if (added) size++;
    }

    public boolean put(CarOwner key, Car value, Entry[] dst) {
        int position = getElementPosition(key, dst.length);
        if (dst[position] == null) {
            dst[position] = new Entry(value, key, null);
            return true;
        }
        Entry extendElement = dst[position];
        while (true) {
            if (extendElement.key.equals(key)) {
                extendElement.value = value;
                return false;
            }
            if (extendElement.next == null) {
                extendElement.next = new Entry(value, key, null);
                return true;
            }
            extendElement = extendElement.next;
        }
    }


    @Override
    public Car get(CarOwner key) {
        int position = getElementPosition(key, array.length);
        if (array[position].key.equals(key)) {
            return array[position].value;
        }
        Entry extendElement = array[position].next;
        while (extendElement != null) {
            if (extendElement.key.equals(key)) {
                return array[position].value;
            }
            extendElement = extendElement.next;
        }
        return null;
    }

    @Override
    public Set<CarOwner> keySet() {
        Set<CarOwner> set = new HashSet<>();
        for (Entry sector : array) {
            if (sector != null) {
                set.add(sector.key);
                Entry extend = sector.next;
                while (extend != null) {
                    set.add(extend.key);
                    extend = extend.next;
                }
            }
        }
        return set;
    }

    @Override
    public List<Car> values() {
        List<Car> list = new ArrayList<>();
        for (Entry sector : array) {
            if (sector != null) {
                list.add(sector.value);
                Entry extend = sector.next;
                while (extend != null) {
                    list.add(sector.value);
                    extend = extend.next;
                }
            }
        }
        return list;
    }

    @Override
    public boolean remove(CarOwner key) {
        int position = getElementPosition(key, array.length);
        if (array[position].key.equals(key)) {
            array[position] = array[position].next;
            return true;
        }
        Entry prev = array[position];
        Entry extend = array[position].next;
        while (extend != null) {
            if (extend.key.equals(key)) {
                prev.next = extend.next;
                return true;
            }
            prev = extend;
            extend = extend.next;
        }
        return false;
    }

    @Override
    public int size() {
        int size = 0;
        for (Entry sector : array) {
            if (sector != null) {
                size++;
                Entry extend = sector.next;
                while (extend != null) {
                    size++;
                    extend = extend.next;
                }
            }
        }
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        array = new CarHashMap.Entry[INITIAL_CAPACITY];
    }

    private void increaseArray() {
        CarHashMap.Entry[] newArray = new CarHashMap.Entry[array.length * 2];
        for (CarHashMap.Entry entry : array) {
            CarHashMap.Entry existedElement = entry;
            while (existedElement != null) {
//               put(existedElement.value, newArray);
                existedElement = existedElement.next;
            }
        }
        array = newArray;
    }

    private int getElementPosition(CarOwner key, int arrayLength) {
        return Math.abs(key.hashCode() % arrayLength);
    }

    private static class Entry {
        private Car value;
        private CarOwner key;
        private CarHashMap.Entry next;

        public Entry(Car value, CarOwner key, Entry next) {
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }
}
