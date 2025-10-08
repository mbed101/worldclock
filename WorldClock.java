import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.border.*;

public class WorldClockGUI extends JFrame {
    private java.util.List<ClockPanel> clockPanels;
    private Timer timer;
    
    // Popular timezones to display
    private static final String[] TIMEZONES = {
        "America/New_York",
        "America/Los_Angeles",
        "Europe/London",
        "Europe/Paris",
        "Asia/Tokyo",
        "Asia/Dubai",
        "Asia/Singapore",
        "Australia/Sydney",
        "Pacific/Auckland"
    };
    
    public WorldClockGUI() {
        setTitle("World Clock - Multiple Timezones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 60, 114), 
                                                      0, getHeight(), new Color(42, 82, 152));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("World Time Zones", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Clock grid
        JPanel clockGrid = new JPanel(new GridLayout(2, 4, 15, 15));
        clockGrid.setOpaque(false);
        clockGrid.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        clockPanels = new ArrayList<>();
        for (String timezone : TIMEZONES) {
            ClockPanel clockPanel = new ClockPanel(timezone);
            clockPanels.add(clockPanel);
            clockGrid.add(clockPanel);
        }
        
        mainPanel.add(clockGrid, BorderLayout.CENTER);
        add(mainPanel);
        
        // Update clocks every second
        timer = new Timer(1000, e -> updateClocks());
        timer.start();
        updateClocks();
    }
    
    private void updateClocks() {
        for (ClockPanel panel : clockPanels) {
            panel.updateTime();
        }
    }
    
    // Inner class for individual clock panel
    class ClockPanel extends JPanel {
        private String timezone;
        private JLabel timeLabel;
        private JLabel dateLabel;
        private JLabel cityLabel;
        private DateTimeFormatter timeFormatter;
        private DateTimeFormatter dateFormatter;
        
        public ClockPanel(String timezone) {
            this.timezone = timezone;
            this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            this.dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd yyyy");
            
            setLayout(new BorderLayout(5, 5));
            setBackground(new Color(255, 255, 255, 230));
            setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
            ));
            
            // City name
            String cityName = timezone.substring(timezone.lastIndexOf('/') + 1).replace('_', ' ');
            cityLabel = new JLabel(cityName, SwingConstants.CENTER);
            cityLabel.setFont(new Font("Arial", Font.BOLD, 16));
            cityLabel.setForeground(new Color(40, 40, 40));
            
            // Time display
            timeLabel = new JLabel("--:--:--", SwingConstants.CENTER);
            timeLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
            timeLabel.setForeground(new Color(20, 100, 180));
            
            // Date display
            dateLabel = new JLabel("---", SwingConstants.CENTER);
            dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            dateLabel.setForeground(new Color(80, 80, 80));
            
            // Timezone info
            JLabel tzLabel = new JLabel(timezone.split("/")[0], SwingConstants.CENTER);
            tzLabel.setFont(new Font("Arial", Font.ITALIC, 10));
            tzLabel.setForeground(new Color(120, 120, 120));
            
            add(cityLabel, BorderLayout.NORTH);
            
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setOpaque(false);
            centerPanel.add(timeLabel, BorderLayout.CENTER);
            centerPanel.add(dateLabel, BorderLayout.SOUTH);
            add(centerPanel, BorderLayout.CENTER);
            
            add(tzLabel, BorderLayout.SOUTH);
        }
        
        public void updateTime() {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timezone));
            timeLabel.setText(now.format(timeFormatter));
            dateLabel.setText(now.format(dateFormatter));
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            WorldClockGUI clock = new WorldClockGUI();
            clock.setVisible(true);
        });
    }
}
