node {
    def app
    try {
        notifyBuild('STARTED')

    	stage('checkout') {

        	git "https://github.com/HM-JV/Todolist.git"
        	sh 'mvn clean'
        	sh 'echo "do stuff before build"'
    	}
        stage('Prepare code') {
            echo 'do checkout stuff'
        }
        stage('UnitTest'){
            echo 'Testing'
            echo 'Testing - publish coverage results'
			sh 'mvn test'
        }
        stage('Staging') {
            echo 'Deploy Stage'
        	sh 'mvn package -DskipTests'
        }
        stage('release') {
			def app = docker.build("todolist")
		}
        stage('Testing') {
            sh 'docker rm -f todolist | true'
            def imgTest = docker.image('todolist'.run(""-p 8080:8080")
            //sh 'docker run -d --name todolist -p 8080:8080 todolist'
            sh 'chmod 755 Lib/chromedriver-linux'
            sh 'mvn -Dtest=TaskFonctionnelleTest test'
            sh 'docker rm -f todolist | true'
        }
        stage('deploy(test serveur)') {
        	sh 'docker build -t todolist .'
        	sh 'docker rm -f todolist | true'
        	def imgFonct = docker.image('todolist'.run(""-p 8080:8080")
        	//sh 'docker run -d --name todolist -p 80:8080 todolist'
        }
        stage('deploy(Production)'){
        //Envoie du projet .jar dans Docker.HUB
            sh 'docker push hbjv/todolist'
            docker.withRegistry("https://hub.docker.com/r/hbjv", 'docker-hub-credentials')
            sh 'docker -H=165.227.133.134:2375 rm -f dockerhbjv | true'
            sh 'docker -H=165.227.133.134:2375 pull hbjv/todolist'
            sh 'docker -H=165.227.133.134:2375 run -d --name dockerhbjv -p 8079:8080 hbjv/todolist'
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
