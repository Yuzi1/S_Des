import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class S_desWindow extends JFrame{
    private JButton encryptButton;
    private JButton decryptButton;

    public S_desWindow() {
        setTitle("S-DES加密解密工具");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // 使用BoxLayout布局，垂直排列

        encryptButton = new JButton("加密");
        decryptButton = new JButton("解密");

        panel.add(Box.createVerticalGlue()); // 顶部留空
        panel.add(encryptButton);
        panel.add(Box.createHorizontalStrut(80)); // 左侧留空
        panel.add(encryptButton);
        panel.add(Box.createVerticalStrut(20)); // 添加垂直间距
        panel.add(decryptButton);
        panel.add(Box.createVerticalGlue()); // 底部留空

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
