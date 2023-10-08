import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class EncryptWindow extends JFrame {
    private JTextField inputText;
    private JTextField keyText;
    private JButton executeButton;
    private JTextArea resultArea;
    private Choice inputType;

    public EncryptWindow() {
        setTitle("S-DES加密");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());


        // 创建一个面板选择、文本输入框和标签
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(3, 2));

        JLabel inputTypeLabel = new JLabel("输入类型:");
        inputType = new Choice();
        inputType.add("二进制");
        inputType.add("ASCII编码");
        textPanel.add(inputTypeLabel);
        textPanel.add(inputType);


        JLabel inputLabel = new JLabel("明文 (8位):");
        inputText = new JTextField();
        JLabel keyLabel = new JLabel("密钥 (10位):");
        keyText = new JTextField();

        textPanel.add(inputLabel);
        textPanel.add(inputText);
        textPanel.add(keyLabel);
        textPanel.add(keyText);

        // 将文本面板添加到主面板的北边
        panel.add(textPanel, BorderLayout.NORTH);

        // 创建一个面板用于包装执行加密和返回按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        executeButton = new JButton("执行加密");
        JButton returnButton = new JButton("返回");

        buttonPanel.add(executeButton);
        buttonPanel.add(returnButton);

        // 将按钮面板添加到主面板的中央
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // 创建结果显示区域
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        // 将结果显示区域添加到主面板的南边
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // 执行加密按钮的监听器
        executeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String plaintext = inputText.getText();
                String key = keyText.getText();
                String result = "";


                // 获取输入类型
                String type = inputType.getSelectedItem();
                System.out.println(type);

                if ("二进制".equals(type)) {
                    // 检查输入的明文和密钥是否是8位和10位的二进制字符串
                    if (plaintext.length() != 8 || key.length() != 10 || !SimpleDes.isValidBinary(plaintext) || !SimpleDes.isValidBinary(key)) {
                        result = "请输入有效位数的明文和密钥。";
                    } else {
                        // 将明文和密钥转换为整数数组
                        int[] plaintextBits = new int[8];
                        int[] keyBits = new int[10];
                        for (int i = 0; i < 8; i++) {
                            plaintextBits[i] = Integer.parseInt(Character.toString(plaintext.charAt(i)));
                        }
                        for (int i = 0; i < 10; i++) {
                            keyBits[i] = Integer.parseInt(Character.toString(key.charAt(i)));
                        }

                        // 使用S-DES的加密方法
                        int[] ciphertextBits = SimpleDes.encrypt(plaintextBits, keyBits);

                        // 将加密结果转换为字符串
                        result = "加密结果: ";
                        for (int bit : ciphertextBits) {
                            result += bit;
                        }
                    }
                }
                else if ("ASCII编码".equals(type)) {
                    System.out.println(plaintext);
                    // 将ASCII编码转换为二进制
                    String binaryPlaintext = SimpleDes.asciiToBinaryString(plaintext);

                    System.out.println(binaryPlaintext);
                    System.out.println(binaryPlaintext.length());
                    System.out.println(SimpleDes.isValidBinary(binaryPlaintext));
                    System.out.println(SimpleDes.isValidBinary(key));

                    if (binaryPlaintext.length() !=8  || key.length() != 10 || !SimpleDes.isValidBinary(binaryPlaintext) || !SimpleDes.isValidBinary(key)) {
                        result = "请输入有效位数的明文和密钥。";
                    }
                    else {
                        // 将明文和密钥转换为整数数组
                        int[] plaintextBits = new int[8];
                        int[] keyBits = new int[10];
                        for (int i = 0; i < 8; i++) {
                            plaintextBits[i] = Integer.parseInt(Character.toString(binaryPlaintext.charAt(i)));
                        }
                        for (int i = 0; i < 10; i++) {
                            keyBits[i] = Integer.parseInt(Character.toString(key.charAt(i)));
                        }

                        // 使用S-DES的加密方法
                        int[] ciphertextBits = SimpleDes.encrypt(plaintextBits, keyBits);
                        System.out.println(Arrays.toString(ciphertextBits));

                        // 将加密结果转换为字符串
                        result = "加密结果: "+ SimpleDes.binaryStringToAscii(ciphertextBits);
                    }
                }



                resultArea.setText(result);
            }

            // 检查输入是否为有效的二进制字符串

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
