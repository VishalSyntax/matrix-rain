import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

public class MatrixWallpaper extends JPanel implements ActionListener {
    private static final int COLUMNS = 100;
    private static final int CHAR_SIZE = 14;
    private static final char[] CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    
    private final int[] drops = new int[COLUMNS];
    private final Random random = new Random();
    private final Timer timer = new Timer(100, this);
    
    public MatrixWallpaper() {
        setBackground(Color.BLACK);
        setOpaque(false);
        for (int i = 0; i < COLUMNS; i++) {
            drops[i] = random.nextInt(20) - 20;
        }
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Courier", Font.BOLD, CHAR_SIZE));
        
        int width = getWidth();
        int height = getHeight();
        int colWidth = width / COLUMNS;
        
        for (int i = 0; i < COLUMNS; i++) {
            int x = i * colWidth;
            int y = drops[i] * CHAR_SIZE;
            
            if (y > 0 && y < height) {
                g2d.setColor(new Color(0, 255, 0, random.nextInt(100) + 155));
                g2d.drawString(String.valueOf(CHARS[random.nextInt(CHARS.length)]), x, y);
            }
            
            if (y > height && random.nextFloat() > 0.975f) {
                drops[i] = 0;
            }
            drops[i]++;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setUndecorated(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setAlwaysOnTop(false);
            
            MatrixWallpaper wallpaper = new MatrixWallpaper();
            frame.add(wallpaper);
            frame.setVisible(true);
            
            // Embed behind desktop icons
            WinDef.HWND progman = User32.INSTANCE.FindWindow("Progman", null);
            User32.INSTANCE.SendMessage(progman, 0x052C, null, null);
            
            WinDef.HWND workerw = null;
            User32.INSTANCE.EnumWindows((hwnd, data) -> {
                WinDef.HWND p = User32.INSTANCE.FindWindowEx(hwnd, null, "SHELLDLL_DefView", null);
                if (p != null) {
                    workerw = User32.INSTANCE.FindWindowEx(null, hwnd, "WorkerW", null);
                    return false;
                }
                return true;
            }, null);
            
            if (workerw != null) {
                WinDef.HWND javaWindow = new WinDef.HWND(Native.getComponentPointer(frame));
                User32.INSTANCE.SetParent(javaWindow, workerw);
            }
        });
    }
}