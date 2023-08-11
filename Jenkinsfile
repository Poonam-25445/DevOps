pipeline {
    agent any

    environment{
       MAVEN_HOME = '${M2_HOME}'
    }
    
    triggers {
        cron('H/30 16 * * 5')
    }
  
    options{
         skipDefaultCheckout()
         buildDiscarder(logRotator(daysToKeepStr: '', numToKeepStr: '10'))
         timeout(time: 180, unit: 'MINUTES')
    }

    stages {
         stage('Clone Repository') {
            steps {
                // Clean workspace before cloning
                cleanWs()
                
                // Clone the repository
                git branch: 'master', url: 'https://github.com/Poonam-25445/DevOps.git'
            }
         }
        
        stage('Build') {
            steps {
                // To run Maven on a Windows agent, use
                bat "mvn -Dmaven.test.failure.ignore=true clean install -DskipTests"
            }
        }
         
        stage('SonarQube analysis') {
            steps {
              withSonarQubeEnv('SonarQube') {
                bat 'mvn clean package -DskipTests sonar:sonar'
              }
            }
        }
        
        stage("Quality Gate") {
            steps {
              timeout(time: 1, unit: 'HOURS') {
                waitForQualityGate abortPipeline: true
              }
            }
        }
        
        stage('Run Selenium Tests') {
            steps {
                bat "mvn test -Dmaven.test.failure.ignore=true"
            }
            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                    testNG reportFilenamePattern: 'target/surefire-reports/testng-results.xml'
                }
            }
        }
        
        stage("Publish to Artifactory") {
            steps {
                rtMavenDeployer(
                    id:'deployer',
                    serverId: 'Jfrog_Artifactory',
                    releaseRepo: 'NAGPDevOpsRepo',
                    snapshotRepo: 'NAGPDevOpsRepo'
                )
                rtMavenRun(
                    pom: 'pom.xml',
                    goals: 'clean install',
                    deployerId: 'deployer'
                )
                rtPublishBuildInfo(
                    serverId: 'Jfrog_Artifactory',
                )
            }
        }
    }
}
