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

static def hipchatCommon(build, stage) {
    hipchatSend color: findHipchatColor(build),
            message: 'Build <a href="${env.URL}">' + stage +
                    ' #${env.BUILD_NUMBER}</a> in ${env.DURATION}: ' +
                    build.result + ' (${env.CHANGES_OR_CAUSE})'
}

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
                echo ' ~~~~ Starting Maven compilation...'
                deleteDir()
                checkout scm
                sh 'mvn -B -V compile'
            }
        }

        stage('Maven Tests') {
            agent any
            steps {
                echo ' ~~~~ Starting Maven tests...'
                sh 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Maven Package') {
            agent any
            steps {
                echo ' ~~~~ Starting Maven packaging...'
                sh 'mvn -B package'
            }
            post {
                always {
                    archiveArtifacts 'target/expvp-*.jar'
                }
            }
        }
    }

    post {
        always {
            hipchatCommon(currentBuild, 'Maven Build')
        }
    }
}
