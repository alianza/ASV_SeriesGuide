pipeline {
    agent any

    stages {
        stage('Clone') {
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
