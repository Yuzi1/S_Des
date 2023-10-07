import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DecryptWindow extends JFrame {
    private JTextField inputText;
    private JTextField keyText;
    private JButton executeButton;
    private JButton returnButton;
    private JTextArea resultArea;

    public DecryptWindow() {
        setTitle("S-DES解密");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 创建一个面板用于包装文本输入框和标签
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(2, 2));

        JLabel inputLabel = new JLabel("密文 (8位):");
        inputText = new JTextField();
        JLabel keyLabel = new JLabel("密钥 (10位):");
        keyText = new JTextField();

        textPanel.add(inputLabel);
        textPanel.add(inputText);
        textPanel.add(keyLabel);
        textPanel.add(keyText);

        // 将文本面板添加到主面板的北边
        panel.add(textPanel, BorderLayout.NORTH);

        // 创建一个面板用于包装执行解密和返回按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        executeButton = new JButton("执行解密");
        returnButton = new JButton("返回");

        buttonPanel.add(executeButton);
        buttonPanel.add(returnButton);

        // 将按钮面板添加到主面板的中央
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // 创建结果显示区域
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        // 将结果显示区域添加到主面板的南边
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // 执行解密按钮的监听器
        executeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = inputText.getText();
                String key = keyText.getText();
                String result = "";

                // 检查输入的密文和密钥是否是8位和10位的二进制字符串
                if (input.length() != 8 || key.length() != 10 || !isValidBinary(input) || !isValidBinary(key)) {
                    result = "请输入有效位数的二进制密文和密钥。";
                } else {
                    // 将密文和密钥转换为整数数组
                    int[] ciphertextBits = new int[8];
                    int[] keyBits = new int[10];
                    for (int i = 0; i < 8; i++) {
                        ciphertextBits[i] = Integer.parseInt(Character.toString(input.charAt(i)));
                    }
                    for (int i = 0; i < 10; i++) {
                        keyBits[i] = Integer.parseInt(Character.toString(key.charAt(i)));
                    }

                    // 使用S-DES的解密方法
                    int[] plaintextBits = SimpleDes.decrypt(ciphertextBits, keyBits);

                    // 将解密结果转换为字符串
                    result = "解密结果: ";
                    for (int bit : plaintextBits) {
                        result += bit;
                    }
                }

                resultArea.setText(result);
            }

            // 检查输入是否为有效的二进制字符串
            private boolean isValidBinary(String input) {
                for (char c : input.toCharArray()) {
                    if (c != '0' && c != '1') {
                        return false;
                    }
                }
                return true;
            }
        });

        // 返回按钮的监听器
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 返回到 S_desWindow
                dispose();  // 关闭当前窗口
                S_desWindow sDesWindow = new S_desWindow();
                sDesWindow.setVisible(true);  // 打开 S_desWindow
            }
        });

        getContentPane().add(panel);
    }
}
