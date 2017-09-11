node {
    try {
        notifyBuild('STARTED')

    	stage('checkout') {
        	git "https://github.com/HM-JV/Todolist.git"
        	sh 'echo "do stuff before build"'
    	}

        stage('Prepare code') {
            echo 'do checkout stuff'
        }

        stage('Testing') {
            echo 'Testing'
            echo 'Testing - publish coverage results'
			sh 'mvn test'
        }

        stage('Staging') {
            echo 'Deploy Stage'
			sh 'mvn package -DskipTests'
        }
		
		stage('release') {
			sh 'docker build -t todolist .'
			sh 'docker rm -f todolist | true'
			sh 'docker run -d --name todolist -p 80:8080 todolist'
		}

  } catch (e) {
    // If there was an exception thrown, the build failed
    currentBuild.result = "FAILED"
    throw e
  } finally {
    // Success or failure, always send notifications
    notifyBuild(currentBuild.result)
  }
}

def notifyBuild(String buildStatus = 'STARTED') {
  // build status of null means successful
  buildStatus =  buildStatus ?: 'SUCCESSFUL'

  // Variable par defaut.
  def colorName = 'RED'
  def colorCode = '#FF0000'
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
  def summary = "${subject} (${env.BUILD_URL})"

  // Definition des couleurs en fonction de l'état
  if (buildStatus == 'STARTED') {
    color = 'YELLOW'
    colorCode = '#FFFF00'
  } else if (buildStatus == 'SUCCESSFUL') {
    color = 'GREEN'
    colorCode = '#00FF00'
  } else {
    color = 'RED'
    colorCode = '#FF0000'
  }

  // Envoie la notifications dans Slack
  slackSend (color: colorCode, message: summary)
}
