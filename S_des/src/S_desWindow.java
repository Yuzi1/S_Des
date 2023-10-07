import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class S_desWindow extends JFrame{
    private JButton encryptButton;
    private JButton decryptButton;

    public S_desWindow() {
        setTitle("S-DES���ܽ��ܹ���");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // ʹ��BoxLayout���֣���ֱ����

        encryptButton = new JButton("����");
        decryptButton = new JButton("����");

        panel.add(Box.createVerticalGlue()); // ��������
        panel.add(encryptButton);
        panel.add(Box.createHorizontalStrut(80)); // �������
        panel.add(encryptButton);
        panel.add(Box.createVerticalStrut(20)); // ��Ӵ�ֱ���
        panel.add(decryptButton);
        panel.add(Box.createVerticalGlue()); // �ײ�����

        getContentPane().add(panel, BorderLayout.CENTER);

        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openEncryptWindow();
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openDecryptWindow();
            }
        });
    }

    private void openEncryptWindow() {
        EncryptWindow encryptWindow = new EncryptWindow();
        encryptWindow.setVisible(true);
        dispose();
    }

    private void openDecryptWindow() {
        DecryptWindow decryptWindow = new DecryptWindow();
        decryptWindow.setVisible(true);
        dispose();
    }
}
