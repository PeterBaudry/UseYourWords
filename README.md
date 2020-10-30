# Use Your Words

### Installation
Clone le projet :
    
    git clone https://github.com/PeterBaudry/UseYourWords.git
        
Installer les dépendences maven (ligne de commande ou directement avec l'IDE de votre choix) :

    cd /dossier/installation/use-your-words
    mvn clean install
    
Installer les dépendences JavaScript :

    cd /dossier/installation/use-your-words/src/main/frontend
    npm install
    
### Démarrer le projet
Back (disponible sur 'http://localhost:8080') :
    
    cd /dossier/installation/use-your-words
    mvn spring-boot:run
    
Front (disponible sur 'http://localhost:3000') :
    
    cd /dossier/installation/use-your-words/src/main/frontend
    npm start