def checkoutRepo() {
    checkout scm: [$class: 'GitSCM', branches: [[name: 'main']], userRemoteConfigs: [[url: 'https://github.com/sanish11/DockerBanking.git']]]
}

def dockerLogin() {
    withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'DOCKER_HUB_USR', passwordVariable: 'DOCKER_HUB_PSW')]) {
        bat "docker login -u %DOCKER_HUB_USR% -p %DOCKER_HUB_PSW%"
    }
}

def buildAndPushBackendImage() {
    dir('src/BankingSystemBackend') {
        def backendImage = docker.build("sanish1212/bankingsystembackend:${env.BUILD_NUMBER}")
        bat "docker push sanish1212/bankingsystembackend:${env.BUILD_NUMBER}"
        bat "docker push sanish1212/bankingsystembackend:latest"
    }
}

def buildAndPushFrontendImage() {
    dir('src/BankingSystemFrontend') {
        def frontendImage = docker.build("sanish1212/bankingsystemfrontend:${env.BUILD_NUMBER}")
        bat "docker push sanish1212/bankingsystemfrontend:${env.BUILD_NUMBER}"
        bat "docker push sanish1212/bankingsystemfrontend:latest"
    }
}

