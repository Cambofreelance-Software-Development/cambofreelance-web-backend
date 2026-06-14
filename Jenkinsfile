pipeline {
    agent any
    parameters {
        gitParameter(
            name:           'BRANCH',
            type:           'PT_BRANCH',
            branchFilter:   'origin/(.*)',
            defaultValue:   'main',
            selectedValue:  'DEFAULT',
            sortMode:       'DESCENDING_SMART',
            description:    'Choose a branch to checkout'
        )
    }
    environment {
        GIT_REPO = 'github.com/Cambofreelance/authentication-service.git'
        TARGET_DIR = '/opt/uat/projects/micro-service'
        PROJECT_NAME = 'auth-service'
        IP_ADDRESS = '161.97.138.103'
    }
    stages {
        stage('Clone or Update Source Code') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'github', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_TOKEN')]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no root@${IP_ADDRESS} "
                            mkdir -p ${TARGET_DIR}

                            if [ ! -d '${TARGET_DIR}/${PROJECT_NAME}/.git' ]; then
                                echo 'Cloning repo...';
                                git clone -b ${params.BRANCH} https://${GIT_USER}:${GIT_TOKEN}@${GIT_REPO} ${TARGET_DIR}/${PROJECT_NAME}
                                if [ \$? -ne 0 ]; then
                                    echo 'Git clone failed!'
                                    exit 1
                                fi
                            else
                                echo 'Repo exists, updating...';
                                cd ${TARGET_DIR}/${PROJECT_NAME} && \
                                git fetch --all && \
                                git reset --hard origin/${params.BRANCH} && \
                                git checkout ${params.BRANCH} && \
                                git pull origin ${params.BRANCH}
                                if [ \$? -ne 0 ]; then
                                    echo 'Git update failed!'
                                    exit 1
                                fi
                            fi
                        "
                    """
                }
            }
        }

        stage('Deploy with Docker') {
            steps {
                sh """
                    ssh -o StrictHostKeyChecking=no root@${IP_ADDRESS} '
                        cd ${TARGET_DIR}/${PROJECT_NAME}
                        
                        # Check if docker-compose.yml exists
                        if [ ! -f docker-compose.yml ]; then
                            echo "Error: docker-compose.yml not found!"
                            exit 1
                        fi
                        
                        echo "Stopping existing services..."
                        docker compose down --remove-orphans
                        
                        echo "Cleaning up unused images..."
                        docker image prune -f
                        
                        echo "Building and starting services with image tag: ${params.IMAGE_TAG}..."
                        export IMAGE_TAG=${params.IMAGE_TAG}
                        export IMAGE_NAME=${params.IMAGE_NAME}
                        docker compose up -d --build
                        
                        if [ \$? -eq 0 ]; then
                            echo "Deployment successful!"
                            echo "Running services:"
                            docker compose ps
                            
                            # Health check (optional)
                            sleep 10
                            if docker compose ps | grep -q "Up"; then
                                echo "Services are running healthy"
                            else
                                echo "Warning: Some services may not be healthy"
                                docker compose logs --tail=20
                            fi
                        else
                            echo "Docker Compose deployment failed!"
                            docker compose logs --tail=50
                            exit 1
                        fi
                    '
                """
            }
        }

    }
}
