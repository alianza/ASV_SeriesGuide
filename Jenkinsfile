// Powered by Infostretch 

timestamps {

node () {

	stage ('SeriesGuide ASV - Checkout') {
 	 checkout([$class: 'GitSCM', branches: [[name: '*/dev']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'f7beb77f-0e3d-4edb-8b95-f4f991846ff2', url: 'https://github.com/alianza/ASV_SeriesGuide.git']]]) 
	}
	stage ('SeriesGuide ASV - Build') {
 	
// Unable to convert a build step referring to "hudson.plugins.android__emulator.AndroidEmulator". Please verify and convert manually if required.
// Unable to convert a build step referring to "hudson.plugins.gradle.Gradle". Please verify and convert manually if required.
// Unable to convert a build step referring to "hudson.plugins.android__emulator.InstallBuilder". Please verify and convert manually if required.
// Unable to convert a build step referring to "hudson.plugins.gradle.Gradle". Please verify and convert manually if required.
		// JUnit Results
		junit 'app\build\outputs\androidTest-results\connected\flavors\AMAZON\*.xml' 
	}
}
}