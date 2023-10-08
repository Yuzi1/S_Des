import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class S_desWindow extends JFrame{
    private JButton encryptButton;
    private JButton decryptButton;

    public S_desWindow() {
        setTitle("S-DES");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,40,40)); // ʹ��BoxLayout���֣���ֱ����

        JLabel jLabel = new JLabel("S-DES���ܽ��ܹ���");
        jLabel.setForeground(new Color(000000));
        jLabel.setFont(new Font("����",Font.PLAIN,30));
        jLabel.setBounds(100,50,300,70);

        panel.add(jLabel);

        //���ܡ����ܰ�ť
        encryptButton = new JButton("���ܹ���");
        decryptButton = new JButton("���ܹ���");



        panel.add(encryptButton);
        panel.add(decryptButton);

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
