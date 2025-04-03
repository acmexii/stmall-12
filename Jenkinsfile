pipeline {
    agent any
 
    environment {
        REGISTRY = 'testuser.azurecr.io'
        SERVICES = 'order,delivery,product'
        AKS_CLUSTER = 'test-aks'
        RESOURCE_GROUP = 'test-rsrcgrp'
        AKS_NAMESPACE = 'default'
        AZURE_CREDENTIALS_ID = 'Azure-Cred'
        TENANT_ID = 'ff8a3ac5-e16a-40c3-b20f-296a5409459d'
    }

    stages {
        stage('Clone Repository') {
            steps {
                checkout scm
                script {
                    env.CHANGED_FILES = sh(script: 'git diff --name-only HEAD^ HEAD || true', returnStdout: true).trim()
                    echo "Changed Files:\n${env.CHANGED_FILES}"
                }
            }
        }

        stage('Build & Deploy Changed Services') {
            steps {
                script {
                    def services = SERVICES.tokenize(',')
                    def changedServices = []

                    for (service in services) {
                        def hasChange = env.CHANGED_FILES.split('\n').any { it.startsWith("${service}/src/") }
                        if (hasChange) {
                            changedServices << service
                        }
                    }

                    if (changedServices.isEmpty()) {
                        echo "No service changes detected. Nothing to build."
                    } else {
                        echo "Changed Services: ${changedServices.join(', ')}"

                        for (service in changedServices) {
                            dir(service) {
                                stage("Build - ${service}") {
                                    withMaven(maven: 'Maven') {
                                        sh 'mvn package -DskipTests'
                                    }
                                }

                                stage("Docker Build - ${service}") {
                                    def image = docker.build("${REGISTRY}/${service}:v${env.BUILD_NUMBER}")
                                }

                                stage("Azure Login - ${service}") {
                                    withCredentials([usernamePassword(credentialsId: env.AZURE_CREDENTIALS_ID, usernameVariable: 'AZURE_CLIENT_ID', passwordVariable: 'AZURE_CLIENT_SECRET')]) {
                                        sh 'az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET --tenant ${TENANT_ID}'
                                    }
                                }

                                stage("Push to ACR - ${service}") {
                                    sh "az acr login --name ${REGISTRY.split('\\.')[0]}"
                                    sh "docker push ${REGISTRY}/${service}:v${env.BUILD_NUMBER}"
                                }

                                stage("Deploy to AKS - ${service}") {
                                    sh "az aks get-credentials --resource-group ${RESOURCE_GROUP} --name ${AKS_CLUSTER}"

                                    sh """
                                    sed 's/latest/v${env.BUILD_ID}/g' kubernetes/deployment.yaml > output.yaml
                                    cat output.yaml
                                    kubectl apply -f output.yaml
                                    kubectl apply -f kubernetes/service.yaml
                                    rm output.yaml
                                    """
                                }
                            }
                        }
                    }
                }
            }
        }

        stage('CleanUp Images') {
            steps {
                script {
                    def services = SERVICES.tokenize(',')
                    for (service in services) {
                        def hasChange = env.CHANGED_FILES.split('\n').any { it.startsWith("${service}/src/") }
                        if (hasChange) {
                            sh "docker rmi ${REGISTRY}/${service}:v${env.BUILD_NUMBER}"
                            echo "Removed image for ${service}"
                        } else {
                            echo "No image cleanup needed for ${service} (no changes)"
                        }
                    }
                }
            }
        }
    }
}
