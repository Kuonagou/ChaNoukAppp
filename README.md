# ChanoukApp

## Description du projet
ChanoukApp est une application de messagerie basée sur un système de surveillance de répertoire d'échange. L'application permet aux utilisateurs d'échanger des messages et de gérer leurs profils utilisateurs via une interface graphique.

## Membre de l'équipe
- Anouk Gouhier Dupuis et Charlotte Menou

## Technologies utilisées
- Java
- Swing
- JavaFX

## Architecture du projet

### Modèle de données
- **Message** : Classe de base représentant les messages échangés dans l'application.
- **User** : Classe de base représentant les utilisateurs de l'application.
- **Database** : Contient les références de l'ensemble des objets Message et Utilisateur. Cette classe est observable afin que l'IHM puisse être notifiée des modifications de son contenu.

### Gestion des fichiers et notifications
- **WatchableDirectory** : Entité responsable de la surveillance du répertoire d'échange. Les observateurs de cette entité sont notifiés lors de la création, modification ou suppression d'un message ou d'un utilisateur dans le répertoire surveillé.
- **EntityManager** : Classe responsable de la mise à jour de la base de données en fonction des notifications reçues par WatchableDirectory. Cette classe permet également de générer les fichiers Twit et Utilisateur dans le répertoire d'échange.

## Installation et exécution

### Prérequis
- Java JDK (version 8 ou supérieure)
- [Autres dépendances à préciser]

### Installation
1. Cloner le dépôt du projet
   ```
   git clone [URL_DU_DEPOT]
   ```
2. Importer le projet dans votre IDE préféré
3. Compiler le projet
