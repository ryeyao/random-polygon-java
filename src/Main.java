import view.MainView;
import view.RandomPolygonGen;
import view.SettingComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Rye on 14-3-8.
 */
public class Main {

    private RandomPolygonGen displayComponent;
    private SettingComponent settingComponent;
    private JSplitPane mainPane;
    private MainView mainView;


    public Main() {

        init();
    }

    private void init() {

        displayComponent = new RandomPolygonGen();

        settingComponent = new SettingComponent();
        settingComponent.setLayout(new FlowLayout());
        settingComponent.setSize(500, 500);

        mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, null, null);
        mainPane.setDividerLocation(0.5);
        mainPane.setRightComponent(displayComponent);
        mainPane.setLeftComponent(settingComponent);

        mainView = new MainView();
        mainView.setLocation(170, 150);
        mainView.setSize(1017, 500);
        mainView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainView.setContentPane(mainPane);

        JButton startBtn = new JButton("Start");
        startBtn.setLocation(370, 570);
        startBtn.setSize(20, 10);
        startBtn.addActionListener(new ActionListener() {
            private boolean pressed = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread displayThread = new Thread(displayComponent);
                if(!pressed) {
                    displayThread.start();
                    pressed = true;
                    ((JButton)e.getSource()).setText("Stop");
                } else {
                    displayComponent.setInterrupted(true);
                    ((JButton)e.getSource()).setText("Start");
                    pressed = false;
                }
            }
        });
        settingComponent.add(startBtn);
    }

    public void run() throws InterruptedException {
        mainView.setVisible(true);
    }

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        main.run();
    }
}
