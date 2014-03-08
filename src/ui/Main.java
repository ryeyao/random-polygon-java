package ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import static java.lang.Thread.sleep;

/**
 * Created by Rye on 14-3-8.
 */
public class Main {
//    private JPanel mainPanel;
//    private JSplitPane mainPane;
//    private RandomPolygonGen displayComponent;
//    private JPanel settingComponent;
//    private JButton button1;

    private RandomPolygonGen displayComponent;
    private JPanel settingComponent;
    private JSplitPane mainPane;
    private JFrame mainView;

    private JSlider maxEdgeNumSlider;
    private JLabel maxEdgeNumSliderLabel;
    private JLabel maxEdgeLabelValue;

    private JSlider minDiamSlider;
    private JLabel minDiamSliderLebel;
    private JLabel MinDiamLabelValue;

    private JSlider maxDiamSlider;
    private JLabel maxDiamSliderLabel;
    private JLabel maxDiamLabelValue;

    private JSlider minCoverageRatioSlider;
    private JLabel minCoverageRatioSliderLabel;
    private JLabel minCovergeeRatioLabelValue;

    private JSlider iterCountSlider;
    private JLabel iterCountSliderLabel;
    private JLabel iterCountLabelValue;

    private JSlider containerWidthSlider;
    private JLabel containerWidthSliderLabel;
    private JLabel containerWidthLabelValue;

    private JSlider containerHeightSlider;
    private JLabel containerHeightSliderLabel;
    private JLabel containerHeightLabelValue;

    private JTextField stepXTextField;
    private JTextField stepYTextField;
    private JTextField expandTryTextField;
    private JTextField expandStepTextField;

    private JButton startButton;
    private JButton saveButton;
//    private Thread displayThread;

    public Main() {

        displayComponent = new RandomPolygonGen();
        init();
    }

    private void cleanup() {
//        displayThread = new Thread(displayComponent);
    }

    private void disableSliders() {
        containerHeightSlider.setEnabled(false);
        containerWidthSlider.setEnabled(false);
        iterCountSlider.setEnabled(false);
        maxDiamSlider.setEnabled(false);
        maxEdgeNumSlider.setEnabled(false);
        minCoverageRatioSlider.setEnabled(false);
        minDiamSlider.setEnabled(false);
    }

    private void enableSliders() {

        containerHeightSlider.setEnabled(true);
        containerWidthSlider.setEnabled(true);
        iterCountSlider.setEnabled(true);
        maxDiamSlider.setEnabled(true);
        maxEdgeNumSlider.setEnabled(true);
        minCoverageRatioSlider.setEnabled(true);
        minDiamSlider.setEnabled(true);
    }

    private void prepareComponents() {
        // maxEdgeNum
        maxEdgeNumSliderLabel = new JLabel("最大边数：" + displayComponent.getMaxEdgeNum(), JLabel.CENTER);
        Hashtable mensTableLabel = new Hashtable();
        mensTableLabel.put(3, new JLabel("3"));
        mensTableLabel.put(100, new JLabel("100"));
        maxEdgeNumSlider = new JSlider(3, 100, displayComponent.getMaxEdgeNum());
        maxEdgeNumSlider.setLabelTable(mensTableLabel);
        maxEdgeNumSlider.setPaintLabels(true);
        maxEdgeNumSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider)e.getSource();
                maxEdgeNumSliderLabel.setText("最大边数: " + String.valueOf(slider.getValue()));
                displayComponent.setMaxEdgeNum(slider.getValue());
            }
        });

        // minDiam
        minDiamSliderLebel = new JLabel("最小外接圆直径：" + displayComponent.getMinRadius() * 2, JLabel.CENTER);
        Hashtable mdsTableLabel = new Hashtable();
        mdsTableLabel.put(2, new JLabel("2"));
        mdsTableLabel.put(100, new JLabel("100"));
        minDiamSlider = new JSlider(2, 100, displayComponent.getMinRadius() * 2);
        minDiamSlider.setLabelTable(mdsTableLabel);
        minDiamSlider.setPaintLabels(true);
        minDiamSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider)e.getSource();
                minDiamSliderLebel.setText("最小外接圆直径: " + String.valueOf(slider.getValue()));
                if(slider.getValue() > maxDiamSlider.getValue()) {
                    maxDiamSlider.setValue(slider.getValue());
                    maxDiamSliderLabel.setText("最大外接圆直径: " + String.valueOf(maxDiamSlider.getValue()));
                    displayComponent.setMaxEdgeNum(maxDiamSlider.getValue() / 2);
                }
                displayComponent.setMinRadius(slider.getValue() / 2);
            }
        });

        // maxDiam
        maxDiamSliderLabel = new JLabel("最大外接圆直径：" + displayComponent.getMinRadius() * 2 , JLabel.CENTER);
        Hashtable madsTableLabel = new Hashtable();
        madsTableLabel.put(2, new JLabel("2"));
        madsTableLabel.put(200, new JLabel("200"));
        maxDiamSlider = new JSlider(2, 200, displayComponent.getMaxRadius() * 2);
        maxDiamSlider.setLabelTable(madsTableLabel);
        maxDiamSlider.setPaintLabels(true);
        maxDiamSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider)e.getSource();
                maxDiamSliderLabel.setText("最大外接圆直径: " + String.valueOf(slider.getValue()));
                if(slider.getValue() < minDiamSlider.getValue()) {
                    minDiamSlider.setValue(slider.getValue());
                    minDiamSliderLebel.setText("最小外接圆直径: " + String.valueOf(minDiamSlider.getValue()));
                    displayComponent.setMinRadius(minDiamSlider.getValue() / 2);
                }
                displayComponent.setMaxRadius(slider.getValue() / 2);
            }
        });

        //minCoverageRatio
        minCoverageRatioSliderLabel = new JLabel("最小填充率：" + (int)(displayComponent.getMinCoverageRatio() * 100), JLabel.CENTER);
        Hashtable mcrsTableLabel = new Hashtable();
        mcrsTableLabel.put(1, new JLabel("1%"));
        mcrsTableLabel.put(100, new JLabel("100%"));
        minCoverageRatioSlider = new JSlider(1, 100, (int)(displayComponent.getMinCoverageRatio() * 100));
        minCoverageRatioSlider.setLabelTable(mcrsTableLabel);
        minCoverageRatioSlider.setPaintLabels(true);
        minCoverageRatioSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider)e.getSource();
                minCoverageRatioSliderLabel.setText("最小填充率: " + String.valueOf(slider.getValue()) + "%");
                displayComponent.setMinCoverageRatio(slider.getValue());
            }
        });

        //containerWidth
        containerWidthSliderLabel = new JLabel("填充区域宽度：" + displayComponent.getContainerWidth(), JLabel.CENTER);
        Hashtable cwsTableLabel = new Hashtable();
        cwsTableLabel.put(10, new JLabel("10"));
        cwsTableLabel.put(10000, new JLabel("10000"));
        containerWidthSlider = new JSlider(10, 10000, displayComponent.getContainerWidth());
        containerWidthSlider.setLabelTable(cwsTableLabel);
        containerWidthSlider.setPaintLabels(true);
        containerWidthSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider)e.getSource();
                containerWidthSliderLabel.setText("填充区域宽度: " + String.valueOf(slider.getValue()));
                displayComponent.setContainerWidth(slider.getValue());
            }
        });

        //containerHeigth
        containerHeightSliderLabel = new JLabel("填充区域高度：" + displayComponent.getContainerHeight(), JLabel.CENTER);
        Hashtable chsTableLabel = new Hashtable();
        chsTableLabel.put(10, new JLabel("10"));
        chsTableLabel.put(10000, new JLabel("10000"));
        containerHeightSlider = new JSlider(10, 10000, displayComponent.getContainerHeight());
        containerHeightSlider.setLabelTable(chsTableLabel);
        containerHeightSlider.setPaintLabels(true);
        containerHeightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider)e.getSource();
                containerHeightSliderLabel.setText("填充区域高度: " + String.valueOf(slider.getValue()));
                displayComponent.setContainerHeight(slider.getValue());
            }
        });

        //iterCount
        iterCountSliderLabel = new JLabel("迭代次数：" + displayComponent.getIterCount(), JLabel.CENTER);
        Hashtable icsTableLabel = new Hashtable();
        icsTableLabel.put(5, new JLabel("5"));
        icsTableLabel.put(250000, new JLabel("250000"));
        iterCountSlider = new JSlider(5, 250000, displayComponent.getIterCount());
        iterCountSlider.setLabelTable(icsTableLabel);
        iterCountSlider.setPaintLabels(true);
        iterCountSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider)e.getSource();
                iterCountSliderLabel.setText("迭代次数: " + String.valueOf(slider.getValue()));
                displayComponent.setIterCount(slider.getValue());
            }
        });

        // startButton
        startButton = new JButton("Start");
        startButton.setLocation(370, 570);
        startButton.setSize(20, 10);
        startButton.addActionListener(new ActionListener() {
            private boolean pressed = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pressed) {
                    System.out.println("Start pressed");
                    saveButton.setEnabled(false);
                    disableSliders();
                    Thread displayThread = new Thread(displayComponent);
                    displayThread.start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Object done = displayComponent.getDone();
                                synchronized (done) {
                                    done.wait();
                                }
                                saveButton.setEnabled(true);
                                enableSliders();
                                startButton.setText("Start");
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }).start();
//                    iterCountSlider.setVisible(false);
                    pressed = true;
                    ((JButton) e.getSource()).setText("Stop");
                } else {
                    System.out.println("Stop pressed");
                    displayComponent.setInterrupted(true);
                    enableSliders();
                    saveButton.setEnabled(false);
                    ((JButton) e.getSource()).setText("Start");
                    pressed = false;
                }
            }
        });

        // saveButton
        saveButton = new JButton("Save");
        saveButton.setLocation(370, 570);
        saveButton.setSize(20, 10);
        saveButton.addActionListener(new ActionListener() {
            private boolean pressed = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                displayComponent.saveToXmlFormatFile();
            }
        });
        saveButton.setEnabled(false);
//        stepXTextField = new JTextField();
//        stepYTextField = new JTextField();
//        expandTryTextField = new JTextField();
//        expandStepTextField = new JTextField();
    }

    private void init() {

        prepareComponents();


        settingComponent = new JPanel();
        settingComponent.setLayout(new GridLayout(0, 2));
        settingComponent.setSize(100, 100);
        settingComponent.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        settingComponent.add(maxEdgeNumSliderLabel);
        settingComponent.add(maxEdgeNumSlider);
//        settingComponent.add(maxEdgeLabelValue);

        settingComponent.add(minDiamSliderLebel);
        settingComponent.add(minDiamSlider);

        settingComponent.add(maxDiamSliderLabel);
        settingComponent.add(maxDiamSlider);

        settingComponent.add(minCoverageRatioSliderLabel);
        settingComponent.add(minCoverageRatioSlider);

        settingComponent.add(iterCountSliderLabel);
        settingComponent.add(iterCountSlider);

        settingComponent.add(containerWidthSliderLabel);
        settingComponent.add(containerWidthSlider);

        settingComponent.add(containerHeightSliderLabel);
        settingComponent.add(containerHeightSlider);

        settingComponent.add(startButton);
        settingComponent.add(saveButton);

//        settingComponent.add(stepXTextField);
//        settingComponent.add(stepYTextField);
//        settingComponent.add(expandTryTextField);
//        settingComponent.add(expandStepTextField);

        mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, null, null);
        mainPane.setDividerLocation(0.5);
        mainPane.setRightComponent(displayComponent);
        mainPane.setLeftComponent(settingComponent);

        mainView = new JFrame();
        mainView.setTitle("随机多边形填充v1.0");
        mainView.setLocation(130, 150);
        mainView.setSize(1030, 545);
        mainView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainView.setContentPane(mainPane);

    }

    public void run() throws InterruptedException {
        mainView.setVisible(true);
    }

    public static void main(String[] args) throws InterruptedException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Main().run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
