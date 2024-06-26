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

def buildAndPush(String directory, String imageName, String tag) {
    def buildNumber = env.BUILD_NUMBER
    println "Executing buildAndPushImage in directory: ${directory}, imageName: ${imageName}, buildNumber: ${buildNumber}, tag: ${tag}"
    dir(directory) {
        bat "docker build -t ${imageName}:${buildNumber} ."
        bat "docker tag ${imageName}:${buildNumber} ${imageName}:${tag}"
        bat "docker push ${imageName}:${buildNumber}"
        bat "docker push ${imageName}:${tag}"
    }
}
