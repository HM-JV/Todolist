node {
    
    	stage('checkout') {
        	git "https://github.com/HM-JV/Todolist"
        	sh 'echo "do stuff before build"'
    	}

        stage('Prepare code') {
        	echo 'do checkout stuff'
        }

        stage('Testing') {
        	echo 'Testing'
        	echo 'Testing - publish coverage results'
        }

        stage('release') {
		sh 'docker build -t todolist .'
		sh 'docker rm -f todolist | true'
		sh 'docker run -d --name todolist -p 80:8080 todolist'
	}
}
