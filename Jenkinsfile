pipeline {
    agent none

    stages {
        stage('Build') {
            agent
            {
                docker {
                    image 'maven:3.8.4-openjdk-17-slim'
                    reuseNode true
                }
            }
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
