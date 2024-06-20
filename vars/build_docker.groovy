println "build_docker.groovy loaded"

def checkoutRepo(String branch, String repoUrl) {
    println "Executing checkoutRepo"
    checkout scm: [$class: 'GitSCM', branches: [[name: branch]], userRemoteConfigs: [[url: repoUrl]]]
}

def dockerLogin(String credentialsId) {
    println "Executing dockerLogin"
    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'DOCKER_HUB_USR', passwordVariable: 'DOCKER_HUB_PSW')]) {
        bat "docker login -u %DOCKER_HUB_USR% -p %DOCKER_HUB_PSW%"
    }
}

def buildAndPushImage(String directory, String imageName, String buildNumber, String tag = 'latest') {
    println "Executing buildAndPushImage in directory: ${directory}, imageName: ${imageName}, buildNumber: ${buildNumber}, tag: ${tag}"
    dir(directory) {
        def image = docker.build("${imageName}:${buildNumber}")
        bat "docker push ${imageName}:${buildNumber}"
        bat "docker push ${imageName}:${tag}"
    }
}
