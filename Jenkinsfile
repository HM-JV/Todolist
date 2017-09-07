node { 
	stage('checkout') {
		git "https://github.com/HM-JV/Todolist.git"
		sh 'echo "do stuff before build"'
	}
	stage('test') {
		sh 'mvn test'
		junit(testResults: '**/target/**/TEST*.xml', allowEmptyResults: true)
	}
	stage('build') {
		sh 'mvn package -DskipTests'
	}
}