import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    final static int COUNT_HELPER = 2;
    final static int CALLS_PER_DAY = 30;
    final static int QUEUE_SIZE = 5;
    final static int FREQ_OF_CALLS = 500;
    final static int DURATION_OF_CONVERSATION = 3000;

    public static void main(String[] args) {
        BlockingQueue<String> conversations = new ArrayBlockingQueue<>(QUEUE_SIZE);

        Thread ATS = new Thread(() -> {
            for (int i = 1; i <= CALLS_PER_DAY; i++) {
                try {
                    conversations.put(Integer.toString(i));
                    System.out.println("Звонок " + i);
                    Thread.sleep(FREQ_OF_CALLS);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        ATS.start();

        List<Thread> helpers = new ArrayList<>();
        for (int i = 0; i < COUNT_HELPER; i++) {
            helpers.add(new Thread(() -> {
                for (int j = 0; j < CALLS_PER_DAY / COUNT_HELPER; j++) {
                    try {
                        System.out.println("Обрабатываем звонок " + conversations.take());
                        Thread.sleep(DURATION_OF_CONVERSATION);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }));
            helpers.get(i).start();
        }
    }
}