# Application Desktop Quiz Java

Ce repository contient une application desktop Quiz Java développée dans le cadre d'un projet. L'application permet aux utilisateurs de tester leurs connaissances en Java à travers des quiz interactifs. Voici une description des deux interfaces principales de l'application :

## Interface 1 : Page de connexion

Cette interface est la première page que les utilisateurs voient lorsqu'ils lancent l'application. Elle offre les fonctionnalités suivantes :

- **Adresse** : Un champ pré-rempli avec une valeur par défaut pour l'adresse du serveur.
- **Port** : Un champ pré-rempli avec une valeur par défaut pour le numéro de port du serveur.
- **Nom** : Un champ où les utilisateurs peuvent entrer leur nom.
- **Code de jeu** : Un champ où les utilisateurs peuvent entrer le code du jeu.
- **Validation du code de jeu** : Si le code de jeu est incorrect, une alerte s'affiche pour informer l'utilisateur de l'erreur.
- **Connexion** : Un bouton pour se connecter au serveur et passer à la deuxième interface.

## Interface 2 : Page de quiz et de suivi des joueurs

Après s'être connecté avec succès, les utilisateurs accèdent à cette interface où ils peuvent répondre aux questions du quiz. Voici les fonctionnalités principales de cette interface :

- **Questions** : Les questions du quiz sont affichées à l'écran pour que les utilisateurs puissent y répondre.
- **Message en cas de réponse correcte** : Si un utilisateur répond correctement à une question, un message apparaît dans la console de tous les joueurs connectés.
- **Liste des joueurs et scores** : Une liste des joueurs connectés est affichée, montrant leur nom et leur score respectif.

---

Nous avons utilisé IntelliJ IDEA pour le développement de l'application desktop et Scene Builder pour la conception des interfaces graphiques. De plus, les sockets ont été utilisés pour établir la communication entre le client et le serveur.

Pour plus de détails sur l'installation, la configuration et l'exécution de l'application, veuillez consulter les instructions spécifiques dans les dossiers correspondants.

N'hésitez pas à explorer le repository et à contribuer au développement de l'application Quiz Java pour l'améliorer davantage !

