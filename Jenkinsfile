pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                node {
                  label 'Gradle'
                  withGradle {
                    sh './gradlew build'
                  }
                }
            }
        }
    }
}