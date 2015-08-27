import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

public class Form extends JFrame {
    private JComboBox taskType = new JComboBox();
    private JTextField textTime = new JTextField();
    private JTextField textTotalTime = new JTextField();
    private JTextField textTotalCount = new JTextField();
    private JButton btnStart = new JButton("+1");
    private JLabel lblTimer = new JLabel("Timer");
    private JMenuBar menuBar = new JMenuBar();
    private Timer timer;
    private long timeForTask;
    private long taskCount;
    private long taskTime;
    private long timerSec;
    private boolean isRunning = false;
    private MediaPlayer mediaPlayer;
    private JFXPanel jfxpanel;

    private int width = 210;
    private int height = 120;
    private int indent = 10;

    Form() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        setSize(width, height);
        setLocation(1000, 500);
        add(taskType);
        add(textTime);
        add(textTotalTime);
        add(textTotalCount);
        add(btnStart);
        add(lblTimer);

        initTypes();
        initTextFields();
        initButton();
        initTimerLabel();
        initMenu();
        initTimer();
        setVisible(true);
    }

    private void initTypes() {
        String[] types = {"", "DB", "EXP", "IRR", "RR", "SXS", "TTR", "URL"};
        for (String type : types) {
            taskType.addItem(type);
        }
        taskType.setSize(55, 20);
        taskType.setLocation(indent, indent);
    }

    private void initTextFields() {
        Point p = taskType.getLocation();
        Dimension d = taskType.getSize();
        textTime.setLocation((int) (p.x + d.getWidth() + indent), p.y);
        textTime.setSize(d);
        textTotalTime.setLocation(p.x, (int) (p.y + d.getHeight() + indent));
        textTotalTime.setSize(d);
        textTotalTime.setEditable(false);
        textTotalCount.setLocation((int) (p.x + d.getWidth() + indent), (int) (p.y + d.getHeight() + indent));
        textTotalCount.setSize(d);
        textTotalCount.setEditable(false);
    }

    private void initButton() {
        Point p = textTime.getLocation();
        Dimension d = textTime.getSize();
        btnStart.setLocation((int) (p.x + d.getWidth() + indent), p.y);
        btnStart.setSize(60, d.height * 2 + indent);
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double secs;
                try {
                    secs = Double.parseDouble(textTime.getText()) * 1000 * 60;
                } catch (Exception ex) {
                    System.out.println(ex);
                    return;
                }
                mediaPlayer.stop();
                if (isRunning) {
                    taskCount++;
                    taskTime += timeForTask - timerSec;
                    timeForTask = (long) secs;
                    timerSec = (long) secs;
                    if (!timer.isRunning()) {
                        timer.start();
                    }
                } else {
                    taskCount = 0;
                    taskTime = 0;
                    timeForTask = (long) secs;
                    timerSec = (long) secs;
                    isRunning = true;
                    timer.start();
                }
                updateTimerText();
                updateTextFields();
            }
        });
    }

    private void initTimerLabel() {
        Font labelFont = lblTimer.getFont();
        String labelText = lblTimer.getText();
        int stringWidth = lblTimer.getFontMetrics(labelFont).stringWidth(labelText);
        int stringHeight = labelFont.getSize();
        lblTimer.setSize(stringWidth, stringHeight);
        lblTimer.setLocation((width - stringWidth) / 2, 75);
    }

    private void initMenu() {
        JMenu menu = new JMenu("Actions");
        menuBar.add(menu);
        JMenuItem menuItem = new JMenuItem("Stop");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isRunning = false;
                timer.stop();
            }
        });
        menu.add(menuItem);
        setJMenuBar(menuBar);
    }

    private void updateTimerText() {
        Long mins = timerSec / 60 / 1000;
        Long secs = timerSec / 1000 - mins * 60;
        String text = mins.toString() + ":" + String.format("%02d", secs);
        lblTimer.setText(text);
        Font labelFont = lblTimer.getFont();
        String labelText = lblTimer.getText();
        int stringWidth = lblTimer.getFontMetrics(labelFont).stringWidth(labelText);
        int stringHeight = labelFont.getSize();
        lblTimer.setSize(stringWidth, stringHeight);
        lblTimer.setLocation((width - stringWidth) / 2, 75);
    }

    private void initTimer() {
        jfxpanel = new JFXPanel();
        Media hit = new Media(Main.class.getResource("sound.mp3").toString());
        mediaPlayer = new MediaPlayer(hit);
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (timerSec == 5000) {
                    mediaPlayer.play();

                }
                if (timerSec - 1000 >= 0) {
                    timerSec -= 1000;
                } else {
                    timer.stop();
                }
                updateTimerText();
            }
        });
        timer.setRepeats(true);
    }

    private void updateTextFields() {
        textTotalCount.setText(Long.toString(taskCount));
        Double totalTime = (double) taskTime / 60000;
        textTotalTime.setText(String.format("%.2g%n", totalTime));
    }
}