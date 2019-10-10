import java.awt.*;
import javax.swing.*;

public class RobotEscape
{
    static MazePanel panel;
    static JButton wallButton, eraseButton, placeButton, runButton;
    static JLabel helpLabel;

    public static void main(String[] args) 
    {  
        JFrame frame = new JFrame("Robot Maze");

        panel = new MazePanel(10, 10);
        panel.setBounds(10, 50, panel.getPreferredSize().width, panel.getPreferredSize().height);
        frame.add(panel);

        wallButton = new JButton("Draw Walls");
        wallButton.setBounds(10, 10, 130, 30);
        frame.add(wallButton);
        wallButton.addActionListener(e -> setMode(MazePanel.Mode.Draw) );

        eraseButton = new JButton("Erase Walls");
        eraseButton.setBounds(150, 10, 130, 30);
        frame.add(eraseButton);
        eraseButton.addActionListener(e -> setMode(MazePanel.Mode.Erase) );

        placeButton = new JButton("Place Robot");
        placeButton.setBounds(290, 10, 130, 30);
        frame.add(placeButton);
        placeButton.addActionListener(e -> setMode(MazePanel.Mode.Place) );

        runButton = new JButton("Run Robot");
        runButton.setBounds(430, 10, 130, 30);
        frame.add(runButton);
        runButton.addActionListener(e -> setMode(MazePanel.Mode.Run) );

        helpLabel = new JLabel("");
        helpLabel.setBounds(10, 60 + panel.getHeight(), panel.getWidth(), 20);
        frame.add(helpLabel);

        setMode(MazePanel.Mode.None);

        frame.setSize(panel.getWidth() + 40,panel.getHeight() + 150);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }  

    static void setMode(MazePanel.Mode m)
    {
        panel.setMode(m);
        wallButton.setBackground(m == MazePanel.Mode.Draw ? Color.yellow : Color.white);
        eraseButton.setBackground(m == MazePanel.Mode.Erase ? Color.yellow : Color.white);
        placeButton.setBackground(m == MazePanel.Mode.Place ? Color.yellow : Color.white);
        runButton.setBackground(m == MazePanel.Mode.Run ? Color.yellow : Color.white);
        switch (m)
        {
            case Draw:
                helpLabel.setText("Draw ALONG edges to CREATE maze walls.");
                break;
            case Erase:
                helpLabel.setText("Draw ACROSS edges to ERASE maze walls.");
                break;
            case Place:
                helpLabel.setText("Click an empty cell to place a robot, or click a robot to turn it.");
                break;
            case Run:
                helpLabel.setText("Click a robot to make it run. It will run until it escapes.");
                break;
            default:
                helpLabel.setText("Pick a mode button.");
                break;
        }
    }
}  