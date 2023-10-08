import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class DecryptWindow extends JFrame {
    private JTextField inputText;
    private JTextField keyText;
    private JButton executeButton;
    private JButton returnButton;
    private JTextArea resultArea;
    private Choice inputType;

    public DecryptWindow() {
        setTitle("S-DES����");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());


        // ����һ��������ڰ�װ�ı������ͱ�ǩ
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(3, 2));

        JLabel inputTypeLabel = new JLabel("��������:");
        inputType = new Choice();
        inputType.add("������");
        inputType.add("ASCII����");
        textPanel.add(inputTypeLabel);
        textPanel.add(inputType);

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
                String ciphertext  = inputText.getText();
                String key = keyText.getText();
                String result = "";

                // ��ȡ��������
                String type = inputType.getSelectedItem();


                if("������".equals(type)){
                    // �����������ĺ���Կ�Ƿ���8λ��10λ�Ķ������ַ���
                    if (ciphertext.length() != 8 || key.length() != 10 || !SimpleDes.isValidBinary(ciphertext) || !SimpleDes.isValidBinary(key)) {
                        result = "��������Чλ�������ĺ���Կ��";
                    } else {
                        // �����ĺ���Կת��Ϊ��������
                        int[] ciphertextBits = new int[8];
                        int[] keyBits = new int[10];
                        for (int i = 0; i < 8; i++) {
                            ciphertextBits[i] = Integer.parseInt(Character.toString(ciphertext.charAt(i)));
                        }
                        for (int i = 0; i < 10; i++) {
                            keyBits[i] = Integer.parseInt(Character.toString(key.charAt(i)));
                        }

                        // ����
                        int[] plaintextBits = SimpleDes.decrypt(ciphertextBits, keyBits);

                        // �����ܽ��ת��Ϊ�ַ���
                        result = "���ܽ��: ";
                        for (int bit : plaintextBits) {
                            result += bit;
                        }
                    }
                } else if ("ASCII����".equals(type)) {
                    // ��ASCII����ת��Ϊ������
                    String binaryCiphertext = SimpleDes.asciiToBinaryString(ciphertext);

                    System.out.println(binaryCiphertext);
                    System.out.println(binaryCiphertext.length());
                    System.out.println(SimpleDes.isValidBinary(binaryCiphertext));
                    System.out.println(SimpleDes.isValidBinary(key));
                    // ��������λ���Ƿ�Ϸ�
                    if (binaryCiphertext.length() != 8 || key.length() != 10 || !SimpleDes.isValidBinary(binaryCiphertext) || !SimpleDes.isValidBinary(key)) {
                        result = "��������Чλ���Ķ��������ĺ���Կ��";
                    } else {
                        int[] ciphertextBits = new int[8];
                        int[] keyBits = new int[10];
                        for (int i = 0; i < 8; i++) {
                            ciphertextBits[i] = Integer.parseInt(Character.toString(binaryCiphertext.charAt(i)));
                        }
                        for (int i = 0; i < 10; i++) {
                            keyBits[i] = Integer.parseInt(Character.toString(key.charAt(i)));
                        }

                        // ����
                        int[] plaintextBits = SimpleDes.decrypt(ciphertextBits, keyBits);

                        // �����ܽ��ת��ΪASCII�ַ���
                        result = "���ܽ��: " + SimpleDes.binaryStringToAscii(plaintextBits);
                    }
                }
                resultArea.setText(result);
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
