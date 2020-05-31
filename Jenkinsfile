pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                withGradle {
                    sh './gradlew app:assembleAmazonDebug'
               }
            }
        }
    }
}
