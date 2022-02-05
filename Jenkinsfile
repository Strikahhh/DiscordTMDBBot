pipeline {
    agent {
        docker {
            image '3.8.4-openjdk-17-slim'
            reuseNode true
        }
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Deploy') {
            steps {
                sh '''
                    docker build -t discord-tmdb-bot .
                    docker rm -f discord-tmdb-bot
                    docker run --name discord-tmdb-bot --restart=unless-stopped discord-tmdb-bot
                '''
            }
        }
    }
}
