package com.example.projet_quiz;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class Serveur {

    private static Map<String, Integer> names = new HashMap<String, Integer>();
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    private static Iterator<Map.Entry<String, String>> entryIter;
    private static Map.Entry<String, String> currentEntry;
    private static Map<String, String> qaMap = new HashMap<String, String>();
    private static final int PORT = 3001;

    public static void main(String[] args) throws IOException {
        System.out.println("Le quiz est commencé");

        // Envoyer aléatoirement une question
        new sendQuestionThread().start();
        ServerSocket listener = new ServerSocket(PORT);

        try {
            while (true) {
                // Accepter les connexions des clients et démarrer un nouveau thread de gestion
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    // Méthode pour envoyer un message à tous les clients connectés
    private static void sendMessage(String message) {
        for (PrintWriter writer : writers) {
            writer.println(message);
            writer.flush();
        }
    }

    // Obtenir les utilisateurs en ligne actuels et envoyer les informations à tous les utilisateurs connectés
    public static void getOnlineUsers() {
        try {
            // Effacer la liste précédente
            sendMessage("#CLEAR");
            for (String users : names.keySet()) {
                sendMessage("#USERNAME" + users + " " + names.get(users));
            }
        } catch (Exception exception) {
            System.out.println("getOnlineUsers a échoué");
        }
    }


    // Obtenir les utilisateurs en ligne actuels avec leur score et envoyer les informations à tous les utilisateurs connectés
    public static void getScore() {
        try {
            for (String users : names.keySet()) {
                sendMessage(users + " a " + names.get(users) + " points !");
            }
        } catch (Exception exception) {
            System.out.println("getScore a échoué");
        }
    }

    // Classe pour gérer les threads
    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;
        String code_jeu;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                // Créer des flux de caractères pour le socket
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);


                while (true) {
                    String[] userInfo = input.readLine().split(",");
                    name = userInfo[0];
                    code_jeu = userInfo[1];
                    System.out.println(code_jeu);
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.containsKey(name)) {
                            names.put(name, 0);
                            break;
                        }
                    }
                }
                if (code_jeu.equals("11111")) {
                    // Informer tous les clients de l'arrivée de cet utilisateur
                    sendMessage(name + " a rejoint le serveur");

                    // Ajouter le PrintWriter à la liste
                    writers.add(output);

                    // Mettre à jour la liste des utilisateurs en ligne actuels
                    getOnlineUsers();

                    // Accepter les messages du client et les diffuser à tous les autres clients
                    while (true) {
                        receive();
                    }
                } else {
                    output.println("/IncorrectCode");
                    sendMessage("/IncorrectCode");
                    System.out.println("Code incorrect");
                    output.flush();
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                // Lors de la fermeture du client, supprimer le nom, le writer et fermer le socket
                if (name != null) {
                    names.remove(name);
                }
                if (output != null) {
                    writers.remove(output);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.print(e);
                }
            }
        }

        // Recevoir les messages des clients
        private void receive() throws IOException {
            String message = input.readLine();
            if (message == null) {
                return;
            } else if (message != null && message.endsWith(currentEntry.getValue().trim().toLowerCase())) {

                // Récompenser le joueur en cas de réponse correcte
                sendMessage(name + " a donné la bonne réponse ! 1 point gagné !");
                System.out.println("[Serveur] - " + name + " a donné la bonne réponse ! 1 point gagné !");
                names.put(name, names.get(name) + 1);
                // Impossible de gagner plus de points pour cette question
                currentEntry.setValue("en attente de la prochaine question");

                getOnlineUsers();
            } else if (message.contains("/SCORE")) {
                System.out.println(currentEntry.getValue());
                getScore();
                getOnlineUsers();
            } else if (message.contains("/QUIT")) {
                getScore();
                getOnlineUsers();
            } else if (message.contains("/DISCONNECT")) {
                getScore();
                getOnlineUsers();
            }
            System.out.println(message);

            // Envoyer à tous les clients
            for (PrintWriter writer : writers) {
                writer.println(message);
            }
        }
    }

    // Classe pour envoyer des questions aux utilisateurs
    public static class sendQuestionThread extends Thread {
        public void run() {

            // Initialiser les données à partir du fichier texte dans la hashmap
            loadQuestions();

            // Envoyer les questions
            System.out.println("Serveur de quiz actif - envoi des questions");
            while (true) {
                try {
                    generateQuestion();

                    // Afficher dans le serveur
                    System.out.print("[Serveur] - " + currentEntry.getKey() + currentEntry.getValue() + "\n");

                    // Envoyer la question aux clients
                    sendMessage("[Question] - " + currentEntry.getKey() + "\n");

                    // Temps pour répondre à la question "30 secondes"
                    Thread.sleep(30000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Charger les questions et les réponses à partir du fichier texte "questions.txt"
    private static void loadQuestions() {
        try {
            File file = new File("src/main/resources/com/example/projet_quiz/questions.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String key;
            while ((key = br.readLine()) != null) {
                qaMap.put(key, br.readLine());
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("Fichier introuvable");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Charger une question aléatoire via l'itérateur et la hashmap en tant que paire clé/valeur
    private static String generateQuestion() {
        if (entryIter == null || !entryIter.hasNext()) {
            entryIter = qaMap.entrySet().iterator();
        }
        currentEntry = entryIter.next();
        return currentEntry.getKey();
    }
}
