package Port;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        ReentrantLock locker = new ReentrantLock();
        Port port = new Port(3, 5000, 1000, locker);

        List<Ship> ships = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            ships.add(new Ship("Корабль " + i, 150, 200, port ));
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Ship ship : ships){
            try {
                ship.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Все корабли завершили своё задание");
    }
}