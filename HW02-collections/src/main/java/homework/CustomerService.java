package homework;


import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private final TreeMap<Customer, String> map;

    public CustomerService() {
        this.map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));
    }

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Map.Entry<Customer, String> entry = map.firstEntry();
        return entry == null
                ? null
                : new AbstractMap.SimpleEntry<>(new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores()), entry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higherEntry = map.higherEntry(customer);
        return higherEntry == null
                ? null
                : new AbstractMap.SimpleEntry<>(new Customer(higherEntry.getKey().getId(), higherEntry.getKey().getName(), higherEntry.getKey().getScores()), higherEntry.getValue());
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
