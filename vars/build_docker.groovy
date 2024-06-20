def checkoutRepo(branch, repoUrl) {
    checkout scm: [$class: 'GitSCM', branches: [[name: branch]], userRemoteConfigs: [[url: repoUrl]]]
}

def dockerLogin(credentialsId) {
    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'DOCKER_HUB_USR', passwordVariable: 'DOCKER_HUB_PSW')]) {
        bat "docker login -u %DOCKER_HUB_USR% -p %DOCKER_HUB_PSW%"
    }
}

def buildAndPushImage(directory, imageName, buildNumber, tag = 'latest') {
    dir(directory) {
        def image = docker.build("${imageName}:${buildNumber}")
        bat "docker push ${imageName}:${buildNumber}"
        bat "docker push ${imageName}:${tag}"
    }
}
