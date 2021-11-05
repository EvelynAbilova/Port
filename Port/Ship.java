package Port;


public class Ship extends Thread {
    int containersToTake;
    int containersToLeave;
    final Port port;

    int currentContainersQuantity;

    public Ship(String name, int containersToTake, int containersToLeave, Port port) {
        super(name);
        this.containersToTake = containersToTake;
        this.containersToLeave = containersToLeave;
        this.port = port;
        currentContainersQuantity = containersToLeave;
        start();
    }

    @Override
    public void run() {
        port.askPermission();
        port.CheckingCapacity(containersToLeave, containersToTake);
        port.returnPermission();
        System.out.println(Thread.currentThread().getName() + " выполнил своё задание");
    }
}