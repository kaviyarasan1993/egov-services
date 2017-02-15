node("slave") {
    def app = "";
    def commit_id="";
    echo "${env.JOB_NAME}"
    def service_name = "${env.JOB_BASE_NAME}";
	stage("Build"){
	    checkout scm
	    sh "git rev-parse --short HEAD > .git/commit-id".trim()
        commit_id = readFile('.git/commit-id')
        docker.image("egovio/ci").inside {
                dir("${service_name}"){
                    def settings_exists = fileExists("settings.xml");
                    if (settings_exists){
                        sh "cd ${service_name}; mvn clean package -s settings.xml";
                    } else {
                        sh "cd ${service_name}; mvn clean package";
                    }
                }
        }
    }

	stage("Archive Results"){
        archive "${service_name}/target/*.jar"
    }

    docker.withRegistry("https://registry.hub.docker.com", "dockerhub") {
        stage("Build docker image"){
            dir("${service_name}") {
                app = docker.build("egovio/${service_name}")
            }
        }

        stage("Build publish docker image"){
            app.tag("${BUILD_ID}-${commit_id}")
            app.push()
        }
    }
}
