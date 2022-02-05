pipeline {
    agent any

    stages {
        stage('Configure') {
            steps {
                withCredentials([file(credentialsId: 'bot-config', variable: 'BOT_CONFIG')]) {
                    sh 'cp $BOT_CONFIG src/main/resources/config.properties'
                }
            }
        }
        stage('Build') {
            agent {
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
