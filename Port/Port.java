package Port;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;

public class Port {

    private int dockQuantity;
    private final int containersCapacity;
    private int currentContainersQuantity;
    private Lock locker;

    ConcurrentMap<Integer, Optional<Thread>> ships = new ConcurrentHashMap<>();

    public Port(int dockQty, int containersCapacity, int currentContainersQty, Lock locker) {
        this.dockQuantity = dockQty;
        this.locker = locker;
        for (int i = 0; i < dockQty; i++) {
            ships.put(i, Optional.empty());
        }
        this.containersCapacity = containersCapacity;
        this.currentContainersQuantity = currentContainersQty;
    }

    public int getCountCapacity() {
        return getContainersCapacity() - getCurrentContainersQty();
    }

    public int getContainersCapacity() {
        return containersCapacity;
    }

    public int getCurrentContainersQty() {
        return currentContainersQuantity;
    }

    public void addContainer() {
        currentContainersQuantity++;
    }

    public void takeConatiner() {
        currentContainersQuantity--;
    }

    public void CheckingCapacity(int containersToLeave, int containersToTake) {
        locker.lock();
        if (containersToLeave > getCountCapacity()) {
            containersToLeave -= getCountCapacity();
            this.currentContainersQuantity = this.containersCapacity;
        } else {
            this.currentContainersQuantity += containersToLeave;
            containersToLeave = 0;
        }
        if (containersToTake > getCountCapacity()) {
            containersToTake -= getContainersCapacity();
            this.currentContainersQuantity = 0;
        } else {
            this.currentContainersQuantity -= containersToTake;
            containersToTake = 0;
        }
        locker.unlock();
    }


    public void askPermission() {
        boolean dockIsEmpty = false;
        while (!dockIsEmpty) {
            for (Integer dock : ships.keySet()) {
                if (ships.get(dock).equals(Optional.empty())) {
                    ships.put(dock, Optional.of(Thread.currentThread()));
                    System.out.println(Thread.currentThread().getName() + " попал в порт");
                    dockIsEmpty = true;
                    break;
                }
            }
        }
    }

    public void returnPermission() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " покинул пристань");

        System.out.println("Текущее кол-во контейнеров в порту: " + currentContainersQuantity);

        for (Integer dock : ships.keySet()) {
            if (ships.get(dock).equals(Optional.of(Thread.currentThread()))) {
                ships.put(dock, Optional.empty());
            }
        }
    }
}