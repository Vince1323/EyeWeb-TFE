# Projet API de base Jorami 
## Remplacer StarterJwt par le nom du projet dans:
* com.jorami.StarterJwtApplication en com.jorami.projectName .
* com.jorami.projectName.StarterJwtApplication en com.jorami.projectname.ProjectNameApplication.
* spring.application.name=StarterJwt en spring.application.name=projectName.
* pom.xml dans les balises artifactId et name.
* [StarterJwt] sur le dossier racine.

Enfin, File > Invalidate Caches...

## Docker
* Adapter les différents noms avec le nom du projet dans le fichier docker-compose.yaml.
* Cliquer sur la double flèche verte au niveau de la première ligne (services:) ou, dans le Terminal, écrire la commande ````docker-compose up````
* Vérifier que le container et les images soient bien créées sur Docker Desktop.

## Tester le mailing
* Lancer Docker Desktop.
* Lancer le container Docker (soit via l'application Docker Desktop, soit via le fichier docker-compose du projet).
* Sur le navigateur, accéder à l'url [localhost:1080](http://localhost:1080/#/)

## Contributeur
- [Jérémi Dupont](https://github.com/jeremid)