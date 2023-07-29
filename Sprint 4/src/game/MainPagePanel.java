package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * class for the home page of the game app
 */
public class MainPagePanel extends JPanel {

    /**
     * Constructor
     * @param screenDimension screen dimension of user
     */
    public MainPagePanel(Dimension screenDimension){
        int screenWidth = screenDimension.width;
        int screenHeight = screenDimension.height;

        //setting preferences for the main window panel
        this.setBackground(new Color(77, 100, 110).brighter());
        this.setLayout(null);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));

        //setting the background image for the home page
        this.addImage("homepage.jpeg", 0, 0, screenWidth, screenHeight);

        //setting the title image for the home page
        this.addImage("coin.png", screenWidth/2 - 570, screenHeight/2 - 350, 1141, 290);

        // Button for Player Vs Player
        JButton pvpButton = createPlayButton(screenDimension, "Play with a Friend", false);
        pvpButton.setBounds((screenWidth - 300) / 2, screenHeight/2, 300, 60);

        // Button for Player Vs Computer
        JButton pvcButton = createPlayButton(screenDimension, "Play against CPU", true);
        pvcButton.setBounds((screenWidth - 300) / 2, (screenHeight/2 + 100), 300, 60);

        MainWindow.getInstance().pack();
    }

    /**
     * method for adding images onto display panel
     */
    public void addImage(String imgName, int xPos, int yPos, int horizontalSize, int verticalSize){
        URL image = MainWindow.class.getClassLoader().getResource(imgName);
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setBounds(xPos, yPos, horizontalSize, verticalSize);
        this.add(imageLabel);
        this.setComponentZOrder(imageLabel, 0);
    }

    /**
     * method for creation of button
     */
    public JButton createPlayButton(Dimension screenDimension, String text, boolean cpuMode){
        JButton playButton = new JButton(text);
        Font pvpFont = new Font("Times New Roman", Font.BOLD, 30); //setting font size for button text
        playButton.setFont(pvpFont);

        //setting button's background and border
        Color buttonColour = new Color(215, 182, 138);
        playButton.setBackground(buttonColour);
        playButton.setOpaque(true);
        playButton.setFocusPainted(false);
        playButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));

        this.add(playButton);  //button appears on top of all the other components
        this.setComponentZOrder(playButton, 0);
        playButton.setVisible(true);

        MainWindow mainWindow = MainWindow.getInstance();



        //defining the action of displaying game board page once button is clicked
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //defining the other player to differentiate game modes for different buttons
                Player otherPlayer;
                if (cpuMode){
                    otherPlayer = new ComputerPlayer();
                }
                else{
                    otherPlayer = new Player();
                }
                mainWindow.getContentPane().removeAll();

                Game.getInstance().setPlayers(new Player(), otherPlayer);
                GameBoard.voidInstance();
                mainWindow.setupPlayPageWindow();
                mainWindow.getContentPane().revalidate();
                mainWindow.getContentPane().repaint();

                Thread backgroundThread = new PerformThread();
                backgroundThread.start();
            }

        });

        return playButton;
    }


}


