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

        // 创建多个明、密文对
        List<int[]> plaintexts = new ArrayList<>();
        List<int[]> ciphertexts = new ArrayList<>();

        // 第一个明文、密文对
        plaintexts.add(new int[]{1, 0, 1, 0, 1, 0, 1, 0}); // 明文1
        ciphertexts.add(new int[]{0, 0, 0, 1, 1, 0, 1, 1}); // 密文1

        // 第二个明文、密文对
        plaintexts.add(new int[]{1, 1, 0, 1, 1, 0, 1, 1}); // 明文2
        ciphertexts.add(new int[]{0, 1, 1, 1, 0, 0, 0, 1}); // 密文2

        // 添加更多的明文、密文对.....

        // 使用位操作来生成所有可能的二进制字符串
        for (int i = 0; i < 1024; i++) {
            keys.add(i);
        }

        // 多线程处理
        for (int i = 0; i < threadNum; i++) {
            System.out.println("线程" + i);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    SimpleDes s_des = new SimpleDes();
                    int[] keyBits = new int[10]; // 初始化 keyBits 数组

                    while (!keys.isEmpty() && !found.get()) {
                        try {
                            // 获取密钥
                            int key = keys.take();

                            // 使用位操作生成二进制字符串
                            for (int j = 0; j < 10; j++) {
                                keyBits[j] = (key >> (9 - j)) & 1;
                            }

                            // 对每个明文-密文对尝试解密
                            for (int i = 0; i < plaintexts.size(); i++) {
                                int[] getPlaintext = SimpleDes.decrypt(ciphertexts.get(i), keyBits);
                                String getPlaintextToStr = Arrays.toString(getPlaintext);

                                if (getPlaintextToStr.equals(Arrays.toString(plaintexts.get(i)))) {
                                    found.set(true);
                                    executor.shutdown();
                                    System.out.println("正确的密钥：" + Arrays.toString(keyBits));
                                    break; // 找到正确密钥后退出内循环
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
                // 如果超时，说明没有找到密钥，可以在这里执行相应的操作
                System.out.println("没有找到正确的密钥");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
