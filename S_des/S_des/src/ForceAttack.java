import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ArrayList;

public class ForceAttack {
    private static final int threadNum = 5;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        BlockingQueue<Integer> keys = new ArrayBlockingQueue<>(1024);
        AtomicBoolean found = new AtomicBoolean(false);

        // ��������������Ķ�
        List<int[]> plaintexts = new ArrayList<>();
        List<int[]> ciphertexts = new ArrayList<>();

        // ��һ�����ġ����Ķ�
        plaintexts.add(new int[]{1, 0, 1, 0, 1, 0, 1, 0}); // ����1
        ciphertexts.add(new int[]{0, 0, 0, 1, 1, 0, 1, 1}); // ����1

        // �ڶ������ġ����Ķ�
        plaintexts.add(new int[]{1, 1, 0, 1, 1, 0, 1, 1}); // ����2
        ciphertexts.add(new int[]{0, 1, 1, 1, 0, 0, 0, 1}); // ����2

        // ��Ӹ�������ġ����Ķ�.....

        // ʹ��λ�������������п��ܵĶ������ַ���
        for (int i = 0; i < 1024; i++) {
            keys.add(i);
        }

        // ���̴߳���
        for (int i = 0; i < threadNum; i++) {
            System.out.println("�߳�" + i);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    SimpleDes s_des = new SimpleDes();
                    int[] keyBits = new int[10]; // ��ʼ�� keyBits ����

                    while (!keys.isEmpty() && !found.get()) {
                        try {
                            // ��ȡ��Կ
                            int key = keys.take();

                            // ʹ��λ�������ɶ������ַ���
                            for (int j = 0; j < 10; j++) {
                                keyBits[j] = (key >> (9 - j)) & 1;
                            }

                            // ��ÿ������-���ĶԳ��Խ���
                            for (int i = 0; i < plaintexts.size(); i++) {
                                int[] getPlaintext = SimpleDes.decrypt(ciphertexts.get(i), keyBits);
                                String getPlaintextToStr = Arrays.toString(getPlaintext);

                                if (getPlaintextToStr.equals(Arrays.toString(plaintexts.get(i)))) {
                                    found.set(true);
                                    executor.shutdown();
                                    System.out.println("��ȷ����Կ��" + Arrays.toString(keyBits));
                                    break; // �ҵ���ȷ��Կ���˳���ѭ��
                                }
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }

        executor.shutdown();

        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                // �����ʱ��˵��û���ҵ���Կ������������ִ����Ӧ�Ĳ���
                System.out.println("û���ҵ���ȷ����Կ");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
