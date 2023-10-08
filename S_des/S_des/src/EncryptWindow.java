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
        setTitle("S-DES����");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());


        // ����һ�����ѡ���ı������ͱ�ǩ
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

        // ����һ��������ڰ�װִ�м��ܺͷ��ذ�ť
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        executeButton = new JButton("ִ�м���");
        JButton returnButton = new JButton("����");

        buttonPanel.add(executeButton);
        buttonPanel.add(returnButton);

        // ����ť�����ӵ�����������
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // ���������ʾ����
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        // �������ʾ������ӵ��������ϱ�
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // ִ�м��ܰ�ť�ļ�����
        executeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String plaintext = inputText.getText();
                String key = keyText.getText();
                String result = "";


                // ��ȡ��������
                String type = inputType.getSelectedItem();
                System.out.println(type);

                if ("������".equals(type)) {
                    // �����������ĺ���Կ�Ƿ���8λ��10λ�Ķ������ַ���
                    if (plaintext.length() != 8 || key.length() != 10 || !SimpleDes.isValidBinary(plaintext) || !SimpleDes.isValidBinary(key)) {
                        result = "��������Чλ�������ĺ���Կ��";
                    } else {
                        // �����ĺ���Կת��Ϊ��������
                        int[] plaintextBits = new int[8];
                        int[] keyBits = new int[10];
                        for (int i = 0; i < 8; i++) {
                            plaintextBits[i] = Integer.parseInt(Character.toString(plaintext.charAt(i)));
                        }
                        for (int i = 0; i < 10; i++) {
                            keyBits[i] = Integer.parseInt(Character.toString(key.charAt(i)));
                        }

                        // ʹ��S-DES�ļ��ܷ���
                        int[] ciphertextBits = SimpleDes.encrypt(plaintextBits, keyBits);

                        // �����ܽ��ת��Ϊ�ַ���
                        result = "���ܽ��: ";
                        for (int bit : ciphertextBits) {
                            result += bit;
                        }
                    }
                }
                else if ("ASCII����".equals(type)) {
                    System.out.println(plaintext);
                    // ��ASCII����ת��Ϊ������
                    String binaryPlaintext = SimpleDes.asciiToBinaryString(plaintext);

                    System.out.println(binaryPlaintext);
                    System.out.println(binaryPlaintext.length());
                    System.out.println(SimpleDes.isValidBinary(binaryPlaintext));
                    System.out.println(SimpleDes.isValidBinary(key));

                    if (binaryPlaintext.length() !=8  || key.length() != 10 || !SimpleDes.isValidBinary(binaryPlaintext) || !SimpleDes.isValidBinary(key)) {
                        result = "��������Чλ�������ĺ���Կ��";
                    }
                    else {
                        // �����ĺ���Կת��Ϊ��������
                        int[] plaintextBits = new int[8];
                        int[] keyBits = new int[10];
                        for (int i = 0; i < 8; i++) {
                            plaintextBits[i] = Integer.parseInt(Character.toString(binaryPlaintext.charAt(i)));
                        }
                        for (int i = 0; i < 10; i++) {
                            keyBits[i] = Integer.parseInt(Character.toString(key.charAt(i)));
                        }

                        // ʹ��S-DES�ļ��ܷ���
                        int[] ciphertextBits = SimpleDes.encrypt(plaintextBits, keyBits);
                        System.out.println(Arrays.toString(ciphertextBits));

                        // �����ܽ��ת��Ϊ�ַ���
                        result = "���ܽ��: "+ SimpleDes.binaryStringToAscii(ciphertextBits);
                    }
                }



                resultArea.setText(result);
            }

            // ��������Ƿ�Ϊ��Ч�Ķ������ַ���

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
