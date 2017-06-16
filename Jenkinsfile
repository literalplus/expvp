/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

static def findHipchatColor(build) {
    switch (build.result) {
        case "UNSTABLE":
            return "YELLOW"
        case "FAILED":
            return "RED"
        default:
            return "GREEN"
    }
}

def leBuild

pipeline {
    agent none // Don't block an agent while waiting for approval

    options {
        buildDiscarder(logRotator(numToKeepStr: '50'))
        disableConcurrentBuilds()
        skipDefaultCheckout()
    }

    tools {
        maven 'def-maven'
    }

    stages {
        stage('Maven Compile') {
            agent any
            steps {
                script {
                    leBuild = currentBuild
                }
                deleteDir()
                checkout scm
                sh 'mvn -B -V compile'
            }
        }

        stage('Maven Package') {
            agent any
            steps {
                sh 'mvn -B package'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    archiveArtifacts 'target/expvp-*.jar'
                }
            }
        }
    }


    post {
        always {
            script {
                node {
                    gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
                }
            }
            echo gitCommit
            hipchatSend color: findHipchatColor(leBuild),
                    message: "Built <a href='${currentBuild.absoluteUrl}'>Expvp " +
                            "#${currentBuild.number}</a> in ${currentBuild.durationString}: " +
                            "${currentBuild.result} [${gitCommit.take(6)}]"
        }
    }
}
