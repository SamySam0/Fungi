package driver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import board.*;
import cards.Card;
import cards.Pan;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

// Animation of Earth rotating around the sun. (Hello, world!)
public class GraphicalGame extends Application {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;

    private BorderPane borderPane;

    private HBox sidePlayer1;
    private HBox sidePlayer2;
    private HBox handPlayer1;
    private HBox handPlayer2;
    private HBox decayPlayer1;
    private HBox decayPlayer2;

    private HBox decayPile;
    private HBox forestPile;
    private HBox forestList;

    private Label p1ScoreLabel;
    private Label p2ScoreLabel;

    private static Player p1, p2;
    private boolean p1plays = true;
    private Player currentPlayer = p1;

    public static void main(String[] args) {

        Board.initialisePiles();
        Board.setUpCards();
        Board.getForestCardsPile().shufflePile();

        // Populate forest
        for (int i = 0; i < 8; i++) {
            Board.getForest().add(Board.getForestCardsPile().drawCard());
        }
        // Initialise players and populate player hands
        p1 = new Player();
        p2 = new Player();
        p1.addCardtoHand(Board.getForestCardsPile().drawCard());
        p1.addCardtoHand(Board.getForestCardsPile().drawCard());
        p1.addCardtoHand(Board.getForestCardsPile().drawCard());
        p2.addCardtoHand(Board.getForestCardsPile().drawCard());
        p2.addCardtoHand(Board.getForestCardsPile().drawCard());
        p2.addCardtoHand(Board.getForestCardsPile().drawCard());

        launch(args);
    }


    public void createButtons(double mainWindowX, double mainWindowY) {
        this.btn1 = new Button();
        this.btn1.setText("Take a card from Forest");
        this.btn1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                // New window (Stage)
                Stage newWindow = new Stage();

                Label actionDescription =
                        new Label("Enter the number of the card you\nwant to take:");
                actionDescription.setStyle(
                        "-fx-font-size:17; -fx-text-fill: #532c17; -fx-font-weight: bold;");
                actionDescription.setPadding(new Insets(10, 0, 10, 10));

                TextField cardToTakeTField = new TextField("1");
                cardToTakeTField.setMaxWidth(100);

                Label actionStatus = new Label("Status: Pending...");
                actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: white;");
                actionStatus.setPadding(new Insets(15, 0, 7, 0));

                Button validButton = new Button();
                validButton.setText("Play");
                validButton.setStyle(
                        "-fx-background-color: linear-gradient(#efbf61, #e0b869); -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: #615133;");
                validButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        boolean succesfullMove = false;
                        try {
                            int cardToTake = Integer.parseInt(cardToTakeTField.getText());
                            if (currentPlayer.takeCardFromTheForest(cardToTake)) {
                                succesfullMove = true;
                            }
                        } catch (Exception e) {
                            ;
                        }

                        if (succesfullMove) {
                            actionStatus.setText("Status: Passed.");
                            actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: green;");

                            if (Board.getForest().size() > 0) {
                                Board.updateDecayPile();
                            }
                            if (Board.getForestCardsPile().pileSize() > 0) {
                                Board.getForest().add(Board.getForestCardsPile().drawCard());
                            }

                            p1plays = !p1plays;
                            if (p1plays) {
                                currentPlayer = p1;
                            } else {
                                currentPlayer = p2;
                            }

                            updateFullGame();
                            newWindow.close();
                        } else {
                            actionStatus.setText("Status: Failed.");
                            actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: #8d0000;");
                        }

                    }
                });

                HBox playField = new HBox();
                playField.getChildren().addAll(cardToTakeTField, validButton);
                playField.setPadding(new Insets(8, 0, 0, 0));
                playField.setSpacing(20);
                playField.setAlignment(Pos.CENTER);

                HBox statusLabel = new HBox();
                statusLabel.getChildren().add(actionStatus);
                // statusLabel.setPadding(new Insets(2, 140, 0, 0));
                statusLabel.setAlignment(Pos.CENTER);

                BorderPane secondaryPane = new BorderPane();
                secondaryPane.setTop(actionDescription);
                secondaryPane.setBottom(statusLabel);
                secondaryPane.setCenter(playField);

                secondaryPane.setStyle(
                        "-fx-background-image: url('file:img/window.jpg'); -fx-background-size: 300 140;");


                Scene secondScene = new Scene(secondaryPane, 300, 140);

                // New window (Stage)
                newWindow.setTitle("Action Time");
                newWindow.setScene(secondScene);

                // Set position of second window, related to primary window.
                newWindow.setX(mainWindowX + 100);
                newWindow.setY(mainWindowY + 100);

                newWindow.show();
                newWindow.setResizable(false);
            }

        });

        this.btn2 = new Button();
        this.btn2.setText("Take all cards from Decay");
        this.btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                // New window (Stage)
                Stage newWindow = new Stage();

                Label actionDescription = new Label("Take all cards from decay:");
                actionDescription.setStyle(
                        "-fx-font-size:17; -fx-text-fill: #532c17; -fx-font-weight: bold;");
                actionDescription.setPadding(new Insets(10, 0, 10, 10));

                Label actionStatus = new Label("Status: Pending...");
                actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: white;");
                actionStatus.setPadding(new Insets(15, 0, 7, 0));

                Button validButton = new Button();
                validButton.setText("Play");
                validButton.setStyle(
                        "-fx-background-color: linear-gradient(#efbf61, #e0b869); -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: #615133;");
                validButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        boolean succesfullMove = false;
                        if (currentPlayer.takeFromDecay()) {
                            succesfullMove = true;
                        }

                        if (succesfullMove) {
                            actionStatus.setText("Status: Passed.");
                            actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: green;");

                            if (Board.getForest().size() > 0) {
                                Board.updateDecayPile();
                            }
                            if (Board.getForestCardsPile().pileSize() > 0) {
                                Board.getForest().add(Board.getForestCardsPile().drawCard());
                            }

                            p1plays = !p1plays;
                            if (p1plays) {
                                currentPlayer = p1;
                            } else {
                                currentPlayer = p2;
                            }

                            updateFullGame();
                            newWindow.close();
                        } else {
                            actionStatus.setText("Status: Failed.");
                            actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: #4f0000;");
                        }
                    }

                });


                HBox playField = new HBox();
                playField.getChildren().addAll(validButton);
                playField.setPadding(new Insets(8, 0, 0, 0));
                playField.setAlignment(Pos.CENTER);

                HBox statusLabel = new HBox();
                statusLabel.getChildren().add(actionStatus);
                // statusLabel.setPadding(new Insets(2, 140, 0, 0));
                statusLabel.setAlignment(Pos.CENTER);

                BorderPane secondaryPane = new BorderPane();
                secondaryPane.setTop(actionDescription);
                secondaryPane.setBottom(statusLabel);
                secondaryPane.setCenter(playField);

                secondaryPane.setStyle(
                        "-fx-background-image: url('file:img/window.jpg'); -fx-background-size: 300 140;");


                Scene secondScene = new Scene(secondaryPane, 300, 140);

                // New window (Stage)
                newWindow.setTitle("Action Time");
                newWindow.setScene(secondScene);

                // Set position of second window, related to primary window.
                newWindow.setX(mainWindowX + 100);
                newWindow.setY(mainWindowY + 100);

                newWindow.show();
                newWindow.setResizable(false);
            }

        });

        this.btn3 = new Button();
        this.btn3.setText("Cook 3 or more identical mushrooms");
        this.btn3.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                // New window (Stage)
                Stage newWindow = new Stage();

                Label actionDescription = new Label(
                        "What ingredients are you cooking?\nType the position of the cards in your hand\n(use commas to separate):");
                actionDescription.setStyle(
                        "-fx-font-size:14; -fx-text-fill: #532c17; -fx-font-weight: bold;");
                actionDescription.setPadding(new Insets(10, 0, 10, 10));

                TextField cardToTakeTField = new TextField("1,2,5");
                cardToTakeTField.setMaxWidth(100);

                Label actionStatus = new Label("Status: Pending...");
                actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: white;");
                actionStatus.setPadding(new Insets(20, 0, 8, 0));

                Button validButton = new Button();
                validButton.setText("Play");
                validButton.setStyle(
                        "-fx-background-color: linear-gradient(#efbf61, #e0b869); -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: #615133;");
                validButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        boolean succesfullMove = false;
                        List<Character> acceptedChar = Arrays.asList('0', '1', '2', '3', '4', '5',
                                '6', '7', '8', '9', ',');

                        String input = cardToTakeTField.getText();
                        for (int i = 0; i < input.length(); i++) {
                            if (!acceptedChar.contains(input.charAt(i))) {
                                input = input.replace(input.charAt(i), ',');
                            }
                        }

                        try {
                            String[] inputIndexes = input.split(",");
                            ArrayList<Card> listofCards = new ArrayList<Card>();

                            // Delete duplicates from inputIndexes :
                            inputIndexes = new LinkedHashSet<>(Arrays.asList(inputIndexes))
                                    .toArray(new String[] {});

                            for (String s : inputIndexes) {
                                listofCards.add(currentPlayer.getHand()
                                        .getElementAt(Integer.parseInt(s) - 1));
                            }
                            if (currentPlayer.cookMushrooms(listofCards)) {
                                succesfullMove = true;
                            }
                        } catch (Exception e) {
                            ;
                        }


                        if (succesfullMove) {
                            actionStatus.setText("Status: Passed.");
                            actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: green;");

                            if (Board.getForest().size() > 0) {
                                Board.updateDecayPile();
                            }
                            if (Board.getForestCardsPile().pileSize() > 0) {
                                Board.getForest().add(Board.getForestCardsPile().drawCard());
                            }

                            p1plays = !p1plays;
                            if (p1plays) {
                                currentPlayer = p1;
                            } else {
                                currentPlayer = p2;
                            }

                            updateFullGame();
                            newWindow.close();
                        } else {
                            actionStatus.setText("Status: Failed.");
                            actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: #4f0000;");
                        }

                    }
                });

                HBox playField = new HBox();
                playField.getChildren().addAll(cardToTakeTField, validButton);
                playField.setPadding(new Insets(8, 0, 0, 0));
                playField.setSpacing(20);
                playField.setAlignment(Pos.CENTER);

                HBox statusLabel = new HBox();
                statusLabel.getChildren().add(actionStatus);
                // statusLabel.setPadding(new Insets(2, 140, 0, 0));
                statusLabel.setAlignment(Pos.CENTER);

                BorderPane secondaryPane = new BorderPane();
                secondaryPane.setTop(actionDescription);
                secondaryPane.setBottom(statusLabel);
                secondaryPane.setCenter(playField);

                secondaryPane.setStyle(
                        "-fx-background-image: url('file:img/window.jpg'); -fx-background-size: 300 160;");


                Scene secondScene = new Scene(secondaryPane, 300, 160);

                // New window (Stage)
                newWindow.setTitle("Action Time");
                newWindow.setScene(secondScene);

                // Set position of second window, related to primary window.
                newWindow.setX(mainWindowX + 100);
                newWindow.setY(mainWindowY + 100);

                newWindow.show();
                newWindow.setResizable(false);
            }

        });

        this.btn4 = new Button();
        this.btn4.setText("Sell 2 or more identical mushrooms");
        this.btn4.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                // New window (Stage)
                Stage newWindow = new Stage();

                Label actionDescription =
                        new Label("Type mushroom name which you will to sell, and the amount:");
                actionDescription.setStyle(
                        "-fx-font-size:14; -fx-text-fill: #532c17; -fx-font-weight: bold;");
                actionDescription.setPadding(new Insets(10, 0, 10, 10));

                VBox nameBox = new VBox();
                TextField cardToSellTField = new TextField("Morel");
                cardToSellTField.setMaxWidth(100);
                Label nameOfCard = new Label("Name of muschroom:");
                nameOfCard.setStyle("-fx-font-size: 11; -fx-text-fill: white;");
                nameBox.getChildren().addAll(nameOfCard, cardToSellTField);

                VBox amountBox = new VBox();
                TextField AmountToSellTField = new TextField("3");
                AmountToSellTField.setMaxWidth(80);
                Label amountOfCard = new Label("Number to sell:");
                amountOfCard.setStyle("-fx-font-size: 11; -fx-text-fill: white;");
                amountBox.getChildren().addAll(amountOfCard, AmountToSellTField);

                Label actionStatus = new Label("Status: Pending...");
                actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: white;");
                actionStatus.setPadding(new Insets(20, 0, 8, 0));

                Button validButton = new Button();
                validButton.setText("Play");
                validButton.setStyle(
                        "-fx-background-color: linear-gradient(#efbf61, #e0b869); -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: #615133;");
                validButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        boolean succesfullMove = false;
                        String mushType = cardToSellTField.getText();

                        try {
                            int mushNumber = Integer.parseInt(AmountToSellTField.getText());
                            if (currentPlayer.sellMushrooms(mushType, mushNumber)) {
                                succesfullMove = true;
                            }
                        } catch (Exception e) {
                            ;
                        }

                        if (succesfullMove) {
                            actionStatus.setText("Status: Passed.");
                            actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: green;");

                            if (Board.getForest().size() > 0) {
                                Board.updateDecayPile();
                            }
                            if (Board.getForestCardsPile().pileSize() > 0) {
                                Board.getForest().add(Board.getForestCardsPile().drawCard());
                            }

                            p1plays = !p1plays;
                            if (p1plays) {
                                currentPlayer = p1;
                            } else {
                                currentPlayer = p2;
                            }

                            updateFullGame();
                            newWindow.close();
                        } else {
                            actionStatus.setText("Status: Failed.");
                            actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: #4f0000;");
                        }

                    }
                });

                HBox playField = new HBox();
                playField.getChildren().addAll(nameBox, amountBox, validButton);
                playField.setPadding(new Insets(8, 0, 0, 0));
                playField.setSpacing(20);
                playField.setAlignment(Pos.CENTER);

                HBox statusLabel = new HBox();
                statusLabel.getChildren().add(actionStatus);
                // statusLabel.setPadding(new Insets(2, 140, 0, 0));
                statusLabel.setAlignment(Pos.CENTER);

                BorderPane secondaryPane = new BorderPane();
                secondaryPane.setTop(actionDescription);
                secondaryPane.setBottom(statusLabel);
                secondaryPane.setCenter(playField);

                secondaryPane.setStyle(
                        "-fx-background-image: url('file:img/window.jpg'); -fx-background-size: 300 148;");


                Scene secondScene = new Scene(secondaryPane, 380, 148);

                // New window (Stage)
                newWindow.setTitle("Action Time");
                newWindow.setScene(secondScene);

                // Set position of second window, related to primary window.
                newWindow.setX(mainWindowX + 100);
                newWindow.setY(mainWindowY + 100);

                newWindow.show();
                newWindow.setResizable(false);
            }

        });

        this.btn5 = new Button();
        this.btn5.setText("Put down 1 pan");
        this.btn5.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                // New window (Stage)
                Stage newWindow = new Stage();

                Label actionDescription = new Label("Put one pan down:");
                actionDescription.setStyle(
                        "-fx-font-size:17; -fx-text-fill: #532c17; -fx-font-weight: bold;");
                actionDescription.setPadding(new Insets(10, 0, 10, 10));

                Label actionStatus = new Label("Status: Pending...");
                actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: white;");
                actionStatus.setPadding(new Insets(15, 0, 7, 0));

                Button validButton = new Button();
                validButton.setText("Play");
                validButton.setStyle(
                        "-fx-background-color: linear-gradient(#efbf61, #e0b869); -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: #615133;");
                validButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        boolean succesfullMove = false;
                        if (currentPlayer.putPanDown()) {
                            succesfullMove = true;
                        }
                        if (succesfullMove) {
                            actionStatus.setText("Status: Passed.");
                            actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: green;");

                            if (Board.getForest().size() > 0) {
                                Board.updateDecayPile();
                            }
                            if (Board.getForestCardsPile().pileSize() > 0) {
                                Board.getForest().add(Board.getForestCardsPile().drawCard());
                            }

                            p1plays = !p1plays;
                            if (p1plays) {
                                currentPlayer = p1;
                            } else {
                                currentPlayer = p2;
                            }

                            updateFullGame();
                            newWindow.close();
                        } else {
                            actionStatus.setText("Status: Failed.");
                            actionStatus.setStyle("-fx-font-size:14; -fx-text-fill: #4f0000;");
                        }
                    }

                });


                HBox playField = new HBox();
                playField.getChildren().addAll(validButton);
                playField.setPadding(new Insets(8, 0, 0, 0));
                playField.setAlignment(Pos.CENTER);

                HBox statusLabel = new HBox();
                statusLabel.getChildren().add(actionStatus);
                // statusLabel.setPadding(new Insets(2, 140, 0, 0));
                statusLabel.setAlignment(Pos.CENTER);

                BorderPane secondaryPane = new BorderPane();
                secondaryPane.setTop(actionDescription);
                secondaryPane.setBottom(statusLabel);
                secondaryPane.setCenter(playField);

                secondaryPane.setStyle(
                        "-fx-background-image: url('file:img/window.jpg'); -fx-background-size: 300 140;");


                Scene secondScene = new Scene(secondaryPane, 300, 140);

                // New window (Stage)
                newWindow.setTitle("Action Time");
                newWindow.setScene(secondScene);

                // Set position of second window, related to primary window.
                newWindow.setX(mainWindowX + 100);
                newWindow.setY(mainWindowY + 100);

                newWindow.show();
                newWindow.setResizable(false);
            }

        });

        // Button styles :
        this.btn1.setStyle(
                "-fx-background-color: linear-gradient(#efbf61, #e0b869); -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: #615133;");
        this.btn2.setStyle(
                "-fx-background-color: linear-gradient(#efbf61, #e0b869); -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: #615133;");
        this.btn3.setStyle(
                "-fx-background-color: linear-gradient(#efbf61, #e0b869); -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: #615133;");
        this.btn4.setStyle(
                "-fx-background-color: linear-gradient(#efbf61, #e0b869); -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: #615133;");
        this.btn5.setStyle(
                "-fx-background-color: linear-gradient(#efbf61, #e0b869); -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: #615133;");



    }


    public void updatePlayersHand(Player player, int playerNumber) {

        if (playerNumber == 1) {
            this.handPlayer1.getChildren().clear();
        } else {
            this.handPlayer2.getChildren().clear();
        }

        for (int index = 0; index < player.getHand().size(); index++) {
            if (playerNumber == 1) {
                this.handPlayer1.getChildren()
                        .add(new ImageView(new Image("file:img/"
                                + player.getHand().getElementAt(index).getName() + ".png",
                                115 * 0.62, 200 * 0.62, false, false)));
            } else {
                this.handPlayer2.getChildren()
                        .add(new ImageView(new Image("file:img/"
                                + player.getHand().getElementAt(index).getName() + ".png",
                                115 * 0.62, 200 * 0.62, false, false)));
            }
        }
    }

    public void updatePlayersDecay(Player player, int playerNumber) {

        if (playerNumber == 1) {
            this.decayPlayer1.getChildren().clear();
        } else {
            this.decayPlayer2.getChildren().clear();
        }

        for (int index = 0; index < player.getDisplay().size(); index++) {
            if (playerNumber == 1) {
                this.decayPlayer1.getChildren()
                        .add(new ImageView(new Image("file:img/"
                                + player.getDisplay().getElementAt(index).getName() + ".png",
                                115 * 0.62, 200 * 0.62, false, false)));
            } else {
                this.decayPlayer2.getChildren()
                        .add(new ImageView(new Image("file:img/"
                                + player.getDisplay().getElementAt(index).getName() + ".png",
                                115 * 0.62, 200 * 0.62, false, false)));
            }
        }
    }

    public void updateTopDecayPile() {
        this.decayPile.getChildren().clear();
        if (Board.getDecayPile().size() > 0) {
            this.decayPile.getChildren()
                    .add(new ImageView(
                            new Image("file:img/" + Board.getDecayPile().get(0).getName() + ".png",
                                    115 * 0.8, 200 * 0.8, false, false)));
        }
    }

    public void updateTopForestPile() {
        this.forestPile.getChildren().clear();
        if (!Board.getForestCardsPile().isEmpty()) {
            this.forestPile.getChildren().add(new ImageView(
                    new Image("file:img/pile.png", 115 * 0.8, 200 * 0.8, false, false)));
            this.forestPile.getChildren()
                    .add(new ImageView(new Image(
                            "file:img/" + Board.getForestCardsPile().peek().getName() + ".png",
                            115 * 0.8, 200 * 0.8, false, false)));
        }
    }

    public void updateForest() {
        this.forestList.getChildren().clear();
        for (int index = Board.getForest().size() - 1; index >= 0; index--) {
            this.forestList.getChildren()
                    .add(new ImageView(new Image(
                            "file:img/" + Board.getForest().getElementAt(index).getName() + ".png",
                            115 * 0.8, 200 * 0.8, false, false)));
        }
    }

    public void updateScores() {
        this.p1ScoreLabel.setText("Player 1: " + p1.getScore() + "p");
        this.p2ScoreLabel.setText("Player 2: " + p2.getScore() + "p");
    }

    public void endGame() {
        Label endGameLabel = new Label();
        if (p1.getScore() < p2.getScore()) {
            endGameLabel.setText("End! Player 2 WINS!");
        } else if (p1.getScore() > p2.getScore()) {
            endGameLabel.setText("End! Player 1 WINS!");
        } else {
            endGameLabel.setText("End! It's a DRAW!");
        }
        endGameLabel.setStyle("-fx-font-size: 60; -fx-text-fill: white;");
        borderPane.setCenter(endGameLabel);

        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> System.exit(0));
        delay.play();
    }

    public void updateFullGame() {
        this.updatePlayersHand(p1, 1);
        this.updatePlayersHand(p2, 2);
        this.updatePlayersDecay(p1, 1);
        this.updatePlayersDecay(p2, 2);
        this.updateTopDecayPile();
        this.updateTopForestPile();
        this.updateForest();
        this.updateScores();
        this.forestList.setPadding(new Insets(105, 0, 0, 12));
        if (Board.getForestCardsPile().isEmpty()) {
            endGame();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Fungi Game");

        this.borderPane = new BorderPane();
        this.createButtons(primaryStage.getX(), primaryStage.getY());


        this.handPlayer1 = new HBox();
        this.handPlayer1.setSpacing(9.5);
        this.handPlayer1.setPrefWidth(580);
        this.handPlayer1.setMaxWidth(580);

        this.decayPlayer1 = new HBox();
        this.decayPlayer1.setSpacing(9.5);
        this.decayPlayer1.setPrefWidth(580);
        this.decayPlayer1.setMaxWidth(580);

        this.sidePlayer1 = new HBox();
        this.sidePlayer1.setSpacing(52);
        this.sidePlayer1.setPadding(new Insets(70, 0, 0, 90));
        this.sidePlayer1.getChildren().addAll(this.decayPlayer1, this.handPlayer1);


        this.handPlayer2 = new HBox();
        this.handPlayer2.setSpacing(9.5);
        this.handPlayer2.setPrefWidth(580);
        this.handPlayer2.setMaxWidth(580);

        this.decayPlayer2 = new HBox();
        this.decayPlayer2.setSpacing(9.5);
        this.decayPlayer2.setPrefWidth(580);
        this.decayPlayer2.setMaxWidth(580);

        this.sidePlayer2 = new HBox();
        this.sidePlayer2.setSpacing(52);
        this.sidePlayer2.setPadding(new Insets(0, 0, 38, 90));
        this.sidePlayer2.getChildren().addAll(this.decayPlayer2, this.handPlayer2);


        this.decayPile = new HBox();
        this.decayPile.setPadding(new Insets(120, 62, 0, 0));

        this.forestPile = new HBox();
        this.forestPile.setPadding(new Insets(118, 0, 0, 62));
        this.forestPile.setSpacing(-100);

        this.forestList = new HBox();
        this.forestList.setAlignment(Pos.BASELINE_CENTER);
        this.forestList.setSpacing(30);

        this.p1ScoreLabel = new Label();
        this.p1ScoreLabel.setText("Player 1: " + p1.getScore() + "p");
        this.p1ScoreLabel
                .setStyle("-fx-text-fill: #e0b869; -fx-font-size: 14; -fx-font-weight: bold;");
        this.p1ScoreLabel.setPadding(new Insets(0, 0, 0, 100));
        this.p2ScoreLabel = new Label();
        this.p2ScoreLabel.setText("Player 2: " + p2.getScore() + "p");
        this.p2ScoreLabel
                .setStyle("-fx-text-fill: #e0b869; -fx-font-size: 14; -fx-font-weight: bold;");
        this.p2ScoreLabel.setPadding(new Insets(0, 0, 0, 5));


        // while (Board.getForest().size() > 0) {
        updateFullGame();
        this.forestList.setPadding(new Insets(105, 80, 0, 0));

        HBox actionButtons = new HBox();
        actionButtons.setPadding(new Insets(15, 12, 15, 12));
        actionButtons.setSpacing(15);
        actionButtons.setStyle("-fx-background-color: #5d2b12;");
        actionButtons.setPrefWidth(1380);

        actionButtons.getChildren().addAll(btn1, btn2, btn3, btn4, btn5, this.p1ScoreLabel,
                this.p2ScoreLabel);
        actionButtons.setAlignment(Pos.BASELINE_CENTER);


        FlowPane buttons = new FlowPane();
        buttons.getChildren().addAll(sidePlayer2, actionButtons);

        borderPane.setTop(sidePlayer1);
        borderPane.setRight(decayPile);
        borderPane.setBottom(buttons);
        borderPane.setLeft(forestPile);
        borderPane.setCenter(forestList);
        borderPane.setStyle(
                "-fx-background-image: url('file:img/game_background_2.jpg'); -fx-background-size: 1380 800;");

        // Create a Scene <= Pane
        Scene gameScene = new Scene(borderPane, 1380, 800);

        // Stage <= Scene
        primaryStage.setScene(gameScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
