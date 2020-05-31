pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                node {
                  withGradle {
                    sh './gradlew build'
                  }
                }
            }
        }
    }
}
