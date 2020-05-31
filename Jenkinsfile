pipeline {
    agent any

    tools {
            gradle 'Gradle 6.5-rc-1'
        }

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
