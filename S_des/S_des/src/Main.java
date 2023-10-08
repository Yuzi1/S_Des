import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                S_desWindow win = new S_desWindow();
                win.setVisible(true);
            }
        });
    }
}