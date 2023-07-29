package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame{

    /**
     * Singleton instance of the class
     */
    private static MainWindow mainWindow;

    /**
     * accessing the dimensions of the screen
     */
    private Dimension screenDimension;

    /**
     * instantiating the panel for the main window display
     */
    private JPanel mainPanel;

    /**
     * text label for player 1
     */
    private JLabel playerLabel1;

    /**
     * text label for player 2
     */
    private JLabel playerLabel2;

    /**
     * text label for state of move of player 1
     */
    private JLabel playerStateOfMoveLabel1;

    /**
     * text label for state of move of player 2
     */
    private JLabel playerStateOfMoveLabel2;

    /**
     * text label for mill formation info
     */
    private JLabel millLabel;


    /**
     * private constructor
     */
    private MainWindow(){
        screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        mainPanel  = new JPanel();
    }

    /**
     * static getter
     * @return singleton instance
     */
    public static MainWindow getInstance(){
        if ( mainWindow == null) {
            mainWindow = new MainWindow();
            mainWindow.setTitle("9 Men's Morris");
            mainWindow.setResizable(false);
        }
        return mainWindow;
    }

    /**
     * Resets the mainWindow instance
     */
    public static void voidInstance() {
        mainWindow = null;
    }

    /**
     * Setting the main home page preferences
     */
    public void setupHomePageWindow(){
        //setting the dimensions of the main window frame based on the screen size
        mainWindow.setBounds(0, 0, screenDimension.width, screenDimension.height);  //window size is of the screen size
        mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);  //ensuring opening of application in fullscreen
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //default closing action

        MainPagePanel mainPage = new MainPagePanel(screenDimension);
        mainWindow.getContentPane().add(mainPage);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    /**
     * sets the game page with the board and tokens
     */
    public void setupPlayPageWindow(){


        // RGB values for Main Window display
        int redComponentValueMainWindow = 77;
        int greenComponentValueMainWindow = 100;
        int blueComponentValueMainWindow = 110;

        //setting preferences for the main window panel
        mainPanel.setBackground(new Color(redComponentValueMainWindow, greenComponentValueMainWindow, blueComponentValueMainWindow).brighter());
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(screenDimension.width, screenDimension.height));

        //setting preferences for the title text and adding it
        JLabel titleText = new JLabel("9 MEN'S MORRIS");

        // RGB values for the Title 9 Men's Morris display
        int redComponentValueTitle = 84;
        int greenComponentValueTitle = 56;
        int blueComponentValueTitle = 23;

        titleText.setForeground(new Color(redComponentValueTitle, greenComponentValueTitle, blueComponentValueTitle));
        int fontSizeTitleText = 50;
        titleText.setFont(new Font("Times New Roman", Font.BOLD, fontSizeTitleText));
        titleText.setVisible(true);

        // Adjustment values for Title 9 Men's Morris display
        int widthAdjustment = 400;
        int yCoordinate = 20;
        int widthTitleText = 600;
        int heightTitleText = 50;

        titleText.setBounds((screenDimension.width - widthAdjustment) / 2, yCoordinate, widthTitleText, heightTitleText);
        mainPanel.add(titleText);
        addAllItems();
    }

    /**
     * Creation and addition of the all items (tokens, intersections, game board, buttons) into the game
     */
    public void addAllItems(){


        //creating the return button
        String returnText = "Return";
        JButton backButton = generateButton(returnText);
        int buttonX = screenDimension.width - 130;
        int buttonY = 20;
        int buttonWidth = 110;
        int buttonHeight = 40;
        backButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);

        //creation and addition of game board into the main window panel
        GameBoard gameBoard = GameBoard.getInstance();
        gameBoard.setDimensionsOfBoard(screenDimension);
        gameBoard.addIntersections(gameBoard.getWidth(),gameBoard.getHeight());  //adding intersection points
        mainPanel.add(gameBoard);

        //getting board dimensions to place other components according to the board height
        int boardSpacing = gameBoard.getBounds().y;
        int boardHeight = gameBoard.getBounds().height;


        Game game = Game.getInstance();

        //Adding both player's Tokens to the Panel container
        int tokenSpacing = 0;
        int tokenSpacingIncrementValue =  boardHeight/9 - 5;

        // Token placement adjustments
        double player1TokenWidthAdjustment = 4.5;
        int tokenHeightAdjustment = 7;
        int player2TokenAdjustment = 4;
        int totalTokenCount = 9;

        for (int i = 1; i <= totalTokenCount; i++){
            Token tokenPlayer1 = new Token((int) (screenDimension.width/player1TokenWidthAdjustment),screenDimension.height/ tokenHeightAdjustment + tokenSpacing, Color.BLACK);
            mainPanel.add(tokenPlayer1);
            game.getPlayer1().addToken(tokenPlayer1);

            Token tokenPlayer2 = new Token((3*screenDimension.width)/player2TokenAdjustment,screenDimension.height/tokenHeightAdjustment +  tokenSpacing, Color.WHITE);
            mainPanel.add(tokenPlayer2);
            game.getPlayer2().addToken(tokenPlayer2);

            tokenSpacing += tokenSpacingIncrementValue;
        }

        //y-coordinate of the player level based on board height
        int textYCoord = boardHeight + boardSpacing - 30;

        // Mill Formed Label to alert user a mill is formed
        int yCoordinate = gameBoard.getY() - 50;
        int widthMillText = 950;
        int heightMillText = 50;
        int millLabelTextSize = 35;
        millLabel = new JLabel("");
        millLabel.setVisible(true);
        millLabel.setBounds((screenDimension.width - widthMillText)/2 , yCoordinate, widthMillText, heightMillText);
        millLabel.setFont(new Font ("Times New Roman", Font.ITALIC, millLabelTextSize));
        mainPanel.add(millLabel);
        Color darkPink = new Color(180, 48, 87);
        millLabel.setForeground(darkPink);


        // Font adjustments
        int playerLabelFontSize = 21;
        int playerLabelFontWidth = 100;
        int playerLabelFontHeight = 40;
        int playerStateOfMoveLabelFontSize = 20;

        //adding the player 1 text label under all black tokens
        playerLabel1 = new JLabel("Player 1");
        playerLabel1.setForeground(Color.BLACK);    //color of the text
        playerLabel1.setFont(new Font("Player 1", Font.BOLD, playerLabelFontSize));   //size and style of the text
        playerLabel1.setVisible(true);
        playerLabel1.setBounds((int) (screenDimension.width/4.5 - 15), textYCoord, playerLabelFontWidth, playerLabelFontHeight);   //size of the label bounds
        playerLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel1.setVerticalAlignment(SwingConstants.CENTER);
        mainPanel.add(playerLabel1);

        int ySpacingBelowLabel = 30;
        playerStateOfMoveLabel1  = new JLabel("PLACING");
        playerStateOfMoveLabel1.setForeground(Color.BLACK);
        playerStateOfMoveLabel1.setFont(new Font("PLACING", Font.HANGING_BASELINE, playerStateOfMoveLabelFontSize));   //size and style of the text
        playerStateOfMoveLabel1.setVisible(true);
        playerStateOfMoveLabel1.setBounds((int) (screenDimension.width/4.5 - 15),textYCoord + ySpacingBelowLabel, playerLabelFontWidth, playerLabelFontHeight);
        playerStateOfMoveLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        playerStateOfMoveLabel1.setVerticalAlignment(SwingConstants.CENTER);
        mainPanel.add(playerStateOfMoveLabel1);


        //adding the player 2 text label under all white tokens
        playerLabel2 = new JLabel("Player 2");
        playerLabel2.setForeground(Color.WHITE);
        playerLabel2.setFont(new Font("Player 2", Font.BOLD, playerLabelFontSize));
        playerLabel2.setVisible(true);
        playerLabel2.setBounds((3*screenDimension.width)/4 - 15, textYCoord, playerLabelFontWidth, playerLabelFontHeight);
        playerLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel2.setVerticalAlignment(SwingConstants.CENTER);
        mainPanel.add(playerLabel2);

        playerStateOfMoveLabel2  = new JLabel("PLACING");
        playerStateOfMoveLabel2.setForeground(Color.WHITE);
        playerStateOfMoveLabel2.setFont(new Font("PLACING", Font.HANGING_BASELINE, playerStateOfMoveLabelFontSize));   //size and style of the text
        playerStateOfMoveLabel2.setVisible(true);
        playerStateOfMoveLabel2.setBounds((3*screenDimension.width)/4 - 15,textYCoord + ySpacingBelowLabel, playerLabelFontWidth, playerLabelFontHeight);
        playerStateOfMoveLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        playerStateOfMoveLabel2.setVerticalAlignment(SwingConstants.CENTER);
        mainPanel.add(playerStateOfMoveLabel2);

        if (Game.getInstance().getPlayer2().typeIsComputer()){
            playerLabel1.setText("YOU");
            playerLabel2.setText("CPU");
        }

        // adding the background to the game
        URL image = MainWindow.class.getClassLoader().getResource("gamepage.jpeg");
        JLabel background = new JLabel(new ImageIcon(image));
        background.setBounds(0, 0, screenDimension.width, screenDimension.height);
        mainPanel.add(background);

        //setting the game board to be displayed at the bottom of all other elements
        mainPanel.setComponentZOrder(gameBoard, mainPanel.getComponentCount()-1);
        mainPanel.setComponentZOrder(background, mainPanel.getComponentCount()-1);

        // Adding the panel container that has all the tokens and intersection points to the frame
        mainWindow.getContentPane().add(mainPanel);
        mainWindow.setVisible(true);    //application window visibility
    }

    /**
     * Generating a button.
     * The settings of the return button are very different from the others hence, a new method needs to be created for it
     */
    public JButton generateButton(String text){
        JButton button = new JButton(text);
        Font returnText = new Font("Times New Roman", Font.BOLD, 25); //font size for button text
        button.setFont(returnText);
        button.setForeground(Color.WHITE);

        //setting button's background and border
        Color foreground = new Color(89, 69, 63);
        button.setBackground(foreground);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));

        mainPanel.add(button);
        button.setVisible(true);

        //action performed when button is clicked
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //defining the other player to differentiate game modes for different buttons
                String[] returnOptions = {"Quit Game", "Continue Playing"};
                String returnDialogText = "Are you sure you want to quit the game\nand return to home page?";
                var selectedOption = JOptionPane.showOptionDialog(null, returnDialogText, "", 0, 3, null, returnOptions, returnOptions[0]);

                //option to quit the game and return to home page
                if (selectedOption == 0) {
                    //resetting the entire state of the game by resetting every component
                    Game.voidInstance(); //reset game instance to null
                    getContentPane().removeAll();
                    dispose();
                    MainWindow.voidInstance(); //reset MainWindow instance to null
                    mainWindow = MainWindow.getInstance();
                    getContentPane().revalidate();
                    getContentPane().repaint();
                    setupHomePageWindow();  //set a new game page
                }
            }
        });
        return button;
    }


    /**
     * get Player 1 label
     * @return Player 1 label
     */
    public JLabel getPlayerLabel1(){
        return  playerLabel1;
    }

    /**
     * get Player 2 label
     * @return Player 2 label
     */
    public JLabel getPlayerLabel2(){
        return  playerLabel2;
    }

    /**
     * get Player 1 state of move label
     * @return Player 1 state of move label
     */
    public JLabel getPlayerStateOfMoveLabel1() {
        return playerStateOfMoveLabel1;
    }

    /**
     * get Player 2 state of move label
     * @return Player 2 state of move label
     */
    public JLabel getPlayerStateOfMoveLabel2(){
        return playerStateOfMoveLabel2;
    }

    /**
     * Getter for mill label
     * @return mill label
     */
    public JLabel getMillLabel(){
        return millLabel;
    }
}
