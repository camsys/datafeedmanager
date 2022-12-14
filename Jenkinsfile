#!/usr/bin/env groovy

pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '3'))
        disableConcurrentBuilds()
        timeout(time: 1, unit: 'HOURS')
        //timestamps()
	}

    tools {
        jdk 'openjdk-11'
        maven 'mvn'
        dockerTool 'default-docker'
	}

    environment {
        POM_VERSION = getVersion()
        JAR_NAME = getJarName()
        AWS_ECS_COMPATIBILITY = 'FARGATE'
        AWS_ECS_NETWORK_MODE = 'awsvpc'
        AWS_ECS_CPU = '256'
        AWS_ECS_MEMORY = '512'
        AWS_ECS_TASK_DEFINITION_PATH = './ecs/container-definition-update-image.json'
	}

    stages {
        stage('Build & Test') {
		  steps {
			withMaven(options: [artifactsPublisher(), mavenLinkerPublisher(), dependenciesFingerprintPublisher(disabled: true), jacocoPublisher(disabled: true), junitPublisher(disabled: true)]) {
			  sh "mvn -B -U clean package"
			}
		  }
		}

        stage('Build Docker Image') {
		  steps {
			withCredentials([string(credentialsId: 'AWS_MNDOT_DATAFEEDMANAGER_REPOSITORY_URL_SECRET', variable: 'AWS_ECR_URL')]) {
			  script {
				docker.build("${AWS_ECR_URL}:${POM_VERSION}", "--build-arg JAR_FILE=${JAR_NAME} .")
			  }
			}
		  }
		}

        stage('Push Image to ECR') {
		  steps {
			withCredentials([string(credentialsId: 'AWS_MNDOT_DATAFEEDMANAGER_REPOSITORY_URL_SECRET', variable: 'AWS_ECR_URL'),
			    string(credentialsId: 'AWS_MNDOT_ECR_REGION', variable: 'AWS_ECR_REGION')]) {
			  withAWS(region: "${AWS_ECR_REGION}", credentials: 'MNDOT-ECR-AWS-CREDENTIALS') {
				script {
				  def login = ecrLogin()
				  sh('#!/bin/sh -e\n' + "${login}") // hide logging
				  docker.image("${AWS_ECR_URL}:${POM_VERSION}").push()
				}
			  }
			}
		  }
		}

        stage('Deploy in ECS') {
          steps {
            withCredentials([string(credentialsId: 'AWS_MNDOT_ECS_EXECUTION_ROLE_SECRET', variable: 'AWS_ECS_EXECUTION_ROLE'),
                string(credentialsId: 'AWS_MNDOT_DATAFEEDMANAGER_REPOSITORY_URL_SECRET', variable: 'AWS_ECR_URL'),
                string(credentialsId: 'AWS_MNDOT_DATAFEEDMANAGER_CLUSTER_SECRET', variable: 'AWS_ECS_CLUSTER'),
                string(credentialsId: 'AWS_MNDOT_DATAFEEDMANAGER_TASK_DEFINITION_SECRET', variable: 'AWS_ECS_TASK_DEFINITION'),
                string(credentialsId: 'AWS_MNDOT_DATAFEEDMANAGER_SERVICE_SECRET', variable: 'AWS_ECS_SERVICE'),
                string(credentialsId: 'AWS_MNDOT_DATAFEEDMANAGER_DB_URL', variable: 'AWS_DATASOURCE_URL'),
                string(credentialsId: 'AWS_MNDOT_DATAFEEDMANAGER_DB_USERNAME', variable: 'AWS_DATASOURCE_USERNAME'),
                string(credentialsId: 'AWS_MNDOT_DATAFEEDMANAGER_DB_PASSWORD', variable: 'AWS_DATASOURCE_PASSWORD'),
                string(credentialsId: 'AWS_MNDOT_ECR_REGION', variable: 'AWS_ECR_REGION')]) {
                withAWS(region: "${AWS_ECR_REGION}", credentials: 'MNDOT-ECR-AWS-CREDENTIALS') {
                    script {
                        updateContainerDefinitionJson()
                        sh("/usr/local/bin/aws ecs register-task-definition --region ${AWS_ECR_REGION} --family ${AWS_ECS_TASK_DEFINITION} --execution-role-arn ${AWS_ECS_EXECUTION_ROLE} --requires-compatibilities ${AWS_ECS_COMPATIBILITY} --network-mode ${AWS_ECS_NETWORK_MODE} --cpu ${AWS_ECS_CPU} --memory ${AWS_ECS_MEMORY} --container-definitions file://${AWS_ECS_TASK_DEFINITION_PATH}")
                        def taskRevision = sh(script: "/usr/local/bin/aws ecs describe-task-definition --task-definition ${AWS_ECS_TASK_DEFINITION} | egrep \"revision\" | tr \"/\" \" \" | awk '{print \$2}' | sed 's/\"\$//'", returnStdout: true)
                        sh("/usr/local/bin/aws ecs update-service --cluster ${AWS_ECS_CLUSTER} --service ${AWS_ECS_SERVICE} --task-definition ${AWS_ECS_TASK_DEFINITION}")
                    }
                }
            }
          }
        }
    }

    post {
            always {
                withCredentials([string(credentialsId: 'AWS_MNDOT_DATAFEEDMANAGER_REPOSITORY_URL_SECRET', variable: 'AWS_ECR_URL')]) {
                    junit allowEmptyResults: true, testResults: 'target/surfire-reports/*.xml'
                    deleteDir()
                    sh "docker rmi ${AWS_ECR_URL}:${POM_VERSION}"
                }
            }
    }
}

def getJarName() {
    def jarName = getName() + '-' + getVersion() + '.jar'
    echo "jarName: ${jarName}"
    return  jarName
}

def getVersion() {
    def pom = readMavenPom file: './pom.xml'
    return pom.version
}

def getName() {
    def pom = readMavenPom file: './pom.xml'
    return pom.name
}

def updateContainerDefinitionJson() {
    def containerDefinitionJson = readJSON file: AWS_ECS_TASK_DEFINITION_PATH, returnPojo: true
    containerDefinitionJson[0]['image'] = "${AWS_ECR_URL}:${POM_VERSION}".inspect()
    for(secret in containerDefinitionJson[0]['secrets']){
        if(secret['name'].equals("SPRING_DATASOURCE_URL")){
            secret['valueFrom'] = "${AWS_DATASOURCE_URL}".inspect()
        }
        if(secret['name'].equals("SPRING_DATASOURCE_USERNAME")){
            secret['valueFrom'] = "${AWS_DATASOURCE_USERNAME}".inspect()
        }
        if(secret['name'].equals("SPRING_DATASOURCE_PASSWORD")){
            secret['valueFrom'] = "${AWS_DATASOURCE_PASSWORD}".inspect()
        }
    }
    echo "task definiton json: ${containerDefinitionJson}"
    writeJSON file: AWS_ECS_TASK_DEFINITION_PATH, json: containerDefinitionJson
}