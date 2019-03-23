# Projet API #
## M2 ACSI - Cours Architecture des SI ##

Ce programme représente l'ensemble du projet API mis en place.
Celui-ci est composé de trois services différents :
- Un service Tache, gérant les tâches
- Un service Participant, gérant les participants des tâches
- Un service Eureka, représentant le service de connexion entre les deux services précédents.


### Création des exécutables ###
Pour lancer ces services, il faut tout d'abord commencer par créer les exécutables :
Dans le repertoire PROJET-API-Eureka
> mvn clean install

Dans le repertoire PROJET-API-Tache
> mvn clean install

Dans le repertoire PROJET-API-Participant
> mvn clean install


### Lancement des éxécutables ###
Pour lancer le programme, lancer les 3 jar dans l'ordre suivant :

Dans le repertoire PROJET-API-Eureka
java -jar target/projet-api-eureka-1.0.jar

Dans le repertoire PROJET-API-Tache
> java -jar target/projet-api-tache-1.0.jar

Dans le repertoire PROJET-API-Participant
java -jar target/projet-api-participant-1.0.jar


### Acces ###
Le service Tache est accessible sur le port 8082.
Le service Participant est accesible sur le port 8080.


@Auteur : Anaïs THIRIOT
