package com.example.projet_quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ClientController {

    @FXML
    public Label labelConsole;
    @FXML
    public Label adresse;
    @FXML
    public Label nom;
    @FXML
    public Label code_jeu;
    @FXML
    public TextField textFieldCodeJeu;
    @FXML
    private TextArea textAreaConsole;
    @FXML
    private TextField textFieldHostAdress;
    @FXML
    private Button buttonConnect;
    @FXML
    private TextArea textAreaConnectedUsers;
    @FXML
    private TextField textFieldPortNumber;
    @FXML
    private TextField textFieldUserName;
    @FXML
    private Label labelCurrentUser;
    @FXML
    private Button buttonDisconnect;
    @FXML
    private Label connecte_par;
    @FXML
    private Label Numero_port;

    @FXML
    private Label labelConnectedUsers;
    @FXML
    private TextField textFieldInput;
    public static String USERNAME;
    private static int PORT;
    private static String HOST;
    private static  String Code_jeu;
    Socket socket;
    Scanner input;
    PrintWriter output;
    boolean etat=false;

    public ClientController() {
    }

    @FXML
    private void initialize() {
        // Masquer les éléments non nécessaires lors de l'initialisation
        this.textAreaConsole.setVisible(false);
        this.textAreaConsole.setEditable(false);
        this.textAreaConnectedUsers.setVisible(false);
        this.textAreaConnectedUsers.setEditable(false);
        this.textFieldInput.setVisible(false);
        this.textFieldInput.setEditable(false);
        this.labelCurrentUser.setVisible(false);
        this.connecte_par.setVisible(false);
        this.labelCurrentUser.setVisible(false);
        this.labelConsole.setVisible(false);


        this.buttonConnect.setOnAction((event) -> {
            if (!this.textFieldUserName.getText().equals("")) {
                PORT = Integer.parseInt(this.textFieldPortNumber.getText());
                HOST = this.textFieldHostAdress.getText();
                USERNAME = this.textFieldUserName.getText();
                Code_jeu=this.textFieldCodeJeu.getText();
                this.labelCurrentUser.setText(USERNAME);
                this.buttonConnect.setDisable(true);
                this.buttonDisconnect.setDisable(false);
                this.textFieldInput.setEditable(true);
                this.textFieldInput.setDisable(false);


                this.connect();
                    // Affichage des éléments nécessaires après la connexion
                    this.textAreaConsole.setVisible(true);
                    this.textAreaConsole.setEditable(true);
                    this.textAreaConnectedUsers.setVisible(true);
                    this.textAreaConnectedUsers.setEditable(true);
                    this.textFieldInput.setVisible(true);
                    this.textFieldInput.setEditable(true);
                    this.labelCurrentUser.setVisible(true);
                    this.labelConsole.setVisible(true);
                    this.connecte_par.setVisible(true);
                    this.buttonConnect.setVisible(true);
                    this.textFieldHostAdress.setVisible(false);
                    this.textFieldUserName.setVisible(false);
                    this.textFieldPortNumber.setVisible(false);
                    this.buttonDisconnect.setVisible(true);

                    this.adresse.setVisible(false);
                    this.Numero_port.setVisible(false);
                    this.nom.setVisible(false);
                    this.code_jeu.setVisible(false);
                    this.textFieldCodeJeu.setVisible(false);



            } else {
                // Affichage d'une alerte si le nom d'utilisateur n'est pas saisi
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Information");
                alert.setHeaderText((String)null);
                alert.setContentText("veuillez saisir un nom");
                alert.showAndWait();
            }

        });

        this.buttonDisconnect.setOnAction((event) -> {
            try {
                this.buttonConnect.setDisable(false);
                this.buttonDisconnect.setDisable(true);
                this.disconnect();
            } catch (Exception var3) {
                this.textAreaConsole.appendText("Déconnexion impossible, une erreur s'est produite \n");
            }

        });
        // Gestion de l'événement lors de l'appui sur la touche "Entrée" dans le champ de saisie
        this.textFieldInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    if (ClientController.this.textFieldInput.getText().equals("/DISCONNECT")) {
                        try {
                            ClientController.this.disconnect();
                        } catch (IOException var4) {
                            var4.printStackTrace();
                        }

                        ClientController.this.textFieldInput.clear();
                    } else if (ClientController.this.textFieldInput.getText().equals("/QUIT")) {
                        try {
                            ClientController.this.disconnect();
                        } catch (IOException var3) {
                            var3.printStackTrace();
                        }

                        System.exit(1);
                    } else if (ClientController.this.textFieldInput.getText().equals("/HELP")) {
                        ClientController.this.textFieldInput.clear();
                        ClientController.this.textAreaConsole.appendText("Commandes disponibles :\\n/SCORE - affiche les scores\\n/DISCONNECT - se déconnecter du serveur\\n/SERVERCOMMANDS - envoie des commandes serveur aux utilisateurs en ligne\\n/QUIT - se déconnecter et quitter le programme\\n");
                        ClientController.this.buttonDisconnect.setDisable(true);
                        ClientController.this.buttonConnect.setDisable(false);
                    } else {
                        String text = ClientController.this.textFieldInput.getText();
                        ClientController.this.output.println(ClientController.USERNAME + ": " + text);
                        ClientController.this.output.flush();
                        ClientController.this.textFieldInput.clear();
                    }
                }

            }
        });
    }

    public void connect() {
        try {
            Socket socket = new Socket(HOST, PORT);
            this.textAreaConsole.appendText("Vous etes connectés: " + HOST + ":" + PORT + "\n");
            Runnable chatClient = () -> {
                try {
                    try {
                        this.input = new Scanner(socket.getInputStream());
                        this.output = new PrintWriter(socket.getOutputStream());
                        this.output.flush();

                        while(true) {
                            this.receieve();
                        }
                    } finally {
                        socket.close();
                    }
                } catch (Exception var16) {
                    System.out.print(var16 + "Une erreur s'est produite avec l'E/S");
                } finally {
                    try {
                        socket.close();
                    } catch (IOException var14) {
                        var14.printStackTrace();
                    }

                }

            };

            // Envoi du nom d'utilisateur et du code de jeu au serveur
            PrintWriter tempOutput = new PrintWriter(socket.getOutputStream());
            tempOutput.println(USERNAME+","+Code_jeu);
            tempOutput.flush();
            // Démarrage du thread client pour la communication avec le serveur
            Thread clientThread = new Thread(chatClient);
            clientThread.start();
        } catch (Exception var5) {
            System.out.print(var5);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Information");
            alert.setHeaderText((String)null);
            alert.setContentText("Le serveur ne répond pas, vérifiez s'il est en ligne");
            alert.showAndWait();
        }

    }


    public void receieve() {
        if (this.input.hasNext()) {
            String message = this.input.nextLine();
            if (message.contains("#CLEAR")) {
                this.textAreaConnectedUsers.setText("");
                System.out.println(message);
            } else if (message.contains("#USERNAME")) {
                String temp1 = message.substring(9);
                temp1 = temp1.replace("[", "");
                temp1 = temp1.replace("]", "");
                System.out.println(message);
                this.textAreaConnectedUsers.appendText(temp1 + "\n");

            } else if (message.contains("/SERVERCOMMANDS")) {
                this.textAreaConsole.appendText("Commandes disponibles :\\n/SCORE - affiche les scores\\n/DISCONNECT - se déconnecter du serveur\\n/SERVERCOMMANDS - envoie des commandes serveur aux utilisateurs en ligne\\n/QUIT - se déconnecter et quitter le programme\\n");
                System.out.println(message);
            } else {
                System.out.println(message);
                this.textAreaConsole.appendText(message + "\n");
            }
            if(message.equals("/IncorrectCode")){
                System.out.println("le code est erroné");

                this.textAreaConnectedUsers.setVisible(false);
                this.textAreaConnectedUsers.setEditable(false);
                this.textFieldInput.setVisible(false);
                this.textFieldInput.setEditable(false);
                this.labelCurrentUser.setVisible(false);
                this.labelConsole.setVisible(false);
                this.labelCurrentUser.setVisible(false);
                this.connecte_par.setVisible(false);

                this.textAreaConsole.setText("code erroné, veuillez saisir le code correct");

        }}

    }

    public void disconnect() throws IOException {
        try {
            this.output.println("/DISCONNECT " + USERNAME + " est déconnecté");
            this.output.flush();
            this.socket.close();
            this.labelCurrentUser.setText("");
        } catch (Exception var2) {
            this.textAreaConsole.setText("fonction de déconnexion est échoué \n");
        }

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Information");
        alert.setHeaderText((String)null);
        alert.setContentText("Vous etes déconnectés");
        alert.showAndWait();
    }

    public void send(String message) {
        try {
            this.output.println(USERNAME + ": " + message);
            this.output.flush();
            this.textFieldInput.clear();
        } catch (Exception var3) {
            this.output.println("Message non envoyé ! - Quelque chose s'est mal passé...\n");
        }

    }
}

