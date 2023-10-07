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
        setTitle("S-DES����");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // ����һ��������ڰ�װ�ı������ͱ�ǩ
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(2, 2));

        JLabel inputLabel = new JLabel("���� (8λ):");
        inputText = new JTextField();
        JLabel keyLabel = new JLabel("��Կ (10λ):");
        keyText = new JTextField();

        textPanel.add(inputLabel);
        textPanel.add(inputText);
        textPanel.add(keyLabel);
        textPanel.add(keyText);

        // ���ı������ӵ������ı���
        panel.add(textPanel, BorderLayout.NORTH);

        // ����һ��������ڰ�װִ�н��ܺͷ��ذ�ť
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        executeButton = new JButton("ִ�н���");
        returnButton = new JButton("����");

        buttonPanel.add(executeButton);
        buttonPanel.add(returnButton);

        // ����ť�����ӵ�����������
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // ���������ʾ����
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        // �������ʾ������ӵ��������ϱ�
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // ִ�н��ܰ�ť�ļ�����
        executeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = inputText.getText();
                String key = keyText.getText();
                String result = "";

                // �����������ĺ���Կ�Ƿ���8λ��10λ�Ķ������ַ���
                if (input.length() != 8 || key.length() != 10 || !isValidBinary(input) || !isValidBinary(key)) {
                    result = "��������Чλ���Ķ��������ĺ���Կ��";
                } else {
                    // �����ĺ���Կת��Ϊ��������
                    int[] ciphertextBits = new int[8];
                    int[] keyBits = new int[10];
                    for (int i = 0; i < 8; i++) {
                        ciphertextBits[i] = Integer.parseInt(Character.toString(input.charAt(i)));
                    }
                    for (int i = 0; i < 10; i++) {
                        keyBits[i] = Integer.parseInt(Character.toString(key.charAt(i)));
                    }

                    // ʹ��S-DES�Ľ��ܷ���
                    int[] plaintextBits = SimpleDes.decrypt(ciphertextBits, keyBits);

                    // �����ܽ��ת��Ϊ�ַ���
                    result = "���ܽ��: ";
                    for (int bit : plaintextBits) {
                        result += bit;
                    }
                }

                resultArea.setText(result);
            }

            // ��������Ƿ�Ϊ��Ч�Ķ������ַ���
            private boolean isValidBinary(String input) {
                for (char c : input.toCharArray()) {
                    if (c != '0' && c != '1') {
                        return false;
                    }
                }
                return true;
            }
        });

        // ���ذ�ť�ļ�����
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ���ص� S_desWindow
                dispose();  // �رյ�ǰ����
                S_desWindow sDesWindow = new S_desWindow();
                sDesWindow.setVisible(true);  // �� S_desWindow
            }
        });

        getContentPane().add(panel);
    }
}
