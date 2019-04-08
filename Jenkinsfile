pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage('Begin Deployment') {
            steps {
                echo 'Hello world !'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('End Deployment') {
            steps {
                echo 'good bye !'
            }
        }
    }
}
