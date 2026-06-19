pipeline {
    agent any

    // ─────────────────────────────────────────────────────────────────────────
    // Global environment variables shared across all stages.
    // IMAGE tag is derived from the Git commit SHA to guarantee immutability —
    // the same SHA always maps to the same image, making rollbacks deterministic.
    // ─────────────────────────────────────────────────────────────────────────
    environment {
        PROJECT_SERVICE  = "CMS Backend 🗃️"
        GIT_BRANCH = "deploy/dev"
        GIT_REPO_URL = "https://github.com/Cambofreelance-Software-Development/cambofreelance-web-backend.git"
        GIT_CREDENTIALS_ID = "github_credentials"

        // GitOps manifest repo — Argo CD / Flux watches this repo to apply changes to the cluster.
        GIT_REPO_MANIFEST_URL = "https://github.com/Cambofreelance-Software-Development/micro-manifest.git"
        GIT_REPO_MANIFEST_UPDATE_URL = "github.com/Cambofreelance-Software-Development/micro-manifest.git"
        GIT_MANIFEST_BRANCH = "dev"
        // Path inside the manifest repo where the Kustomize patch for this service lives.
        MANIFEST_FOLDER = "dev/overlays/cms-backend/patches"
        SERVICE_PATCH = "cms-backend-patch.yaml"

        IMAGE_REGISTRY = "nexus.cambofreelance.com/docker-hosted"
        FOLDER_REGISTRY = "ms/dev"
        IMAGE_NAME = "cms-backend-service"
        // Full registry path; the tag (:SHA) is appended in the Checkout stage.
        DOCKER_REPO_PATH = "${IMAGE_REGISTRY}/${FOLDER_REGISTRY}/${IMAGE_NAME}"

        // Spring active profile — switched to TARGET_ENV before the Docker build
        // so the correct config-server profile is baked into the image.
        SPRING_PROFILE = "src/main/resources/application.yml"
        TARGET_ENV = "dev"

        TELEGRAM_CHAT_ID  = '-1003570206702'
        TELEGRAM_TOPIC_ID = '1581'
    }

    stages {

        // ─────────────────────────────────────────────────────────────────────
        // Stage 1 — Checkout Code
        // Clones the application repository at the target branch and captures
        // commit metadata (full SHA, short SHA, commit message) as environment
        // variables used by later stages and the Telegram notification.
        // ─────────────────────────────────────────────────────────────────────
        stage('Checkout Code') {
            steps {
                echo "🔀 Checking out application branch ${env.GIT_BRANCH}"
                git branch:        env.GIT_BRANCH,
                    url:           env.GIT_REPO_URL,
                    credentialsId: env.GIT_CREDENTIALS_ID

                script {
                    def sha = env.GIT_COMMIT
                    env.GIT_COMMIT_MESSAGE = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
                    env.GIT_COMMIT_SHA    = sha
                    env.GIT_COMMIT_SHORT  = sha.take(7)
                    // Full image reference with SHA tag — used in Build, Push, Update Manifest, and Clean stages.
                    env.DOCKER_FULL_IMAGE = "${env.DOCKER_REPO_PATH}:${sha}"

                    echo "📋 Branch     : ${env.GIT_BRANCH}"
                    echo "📋 Full SHA   : ${env.GIT_COMMIT_SHA}"
                    echo "📋 Short SHA  : ${env.GIT_COMMIT_SHORT}"
                    echo "📋 Docker Tag : ${env.DOCKER_FULL_IMAGE}"
                }
            }
        }

        // ─────────────────────────────────────────────────────────────────────
        // Stage 2 — Build Info
        // Prints a single consolidated summary of every value that will drive
        // this pipeline run: source, image, runtime config, and build metadata.
        // Runs after Checkout so all SHA-derived variables are already resolved.
        // Nothing is mutated here — pure read/display only.
        // ─────────────────────────────────────────────────────────────────────
        stage('Build Info') {
            steps {
                script {
                    // Read the current active profile line from application.yml
                    // before it is rewritten, so the summary shows the source value.
                    def currentProfile = sh(
                        script: "grep 'active:' ${env.SPRING_PROFILE} | awk '{print \$2}'",
                        returnStdout: true
                    ).trim()

                    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
                    echo "📦  BUILD INFO — ${env.PROJECT_SERVICE}"
                    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
                    echo ""
                    echo "  🔢  Build Number    : #${env.BUILD_NUMBER}"
                    echo "  🌿  Branch          : ${env.GIT_BRANCH}"
                    echo "  🔑  Commit SHA      : ${env.GIT_COMMIT_SHA}"
                    echo "  🔖  Commit Message  : ${env.GIT_COMMIT_MESSAGE}"
                    echo ""
                    echo "  🐳  Docker Image    : ${env.DOCKER_FULL_IMAGE}"
                    echo "  📦  Registry        : ${env.IMAGE_REGISTRY}"
                    echo "  📂  Registry Folder : ${env.FOLDER_REGISTRY}"
                    echo ""
                    echo "  🍃  Spring Profile  : ${currentProfile} → will be updated to [ ${env.TARGET_ENV} ]"
                    echo "  📄  Profile File    : ${env.SPRING_PROFILE}"
                    echo ""
                    echo "  📁  Manifest Repo   : ${env.GIT_REPO_MANIFEST_URL}"
                    echo "  🌿  Manifest Branch : ${env.GIT_MANIFEST_BRANCH}"
                    echo "  📝  Patch File      : ${env.MANIFEST_FOLDER}/${env.SERVICE_PATCH}"
                    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
                }
            }
        }

        // ─────────────────────────────────────────────────────────────────────
        // Stage 3 — Update Spring Profile
        // Rewrites `spring.profiles.active` in application.yml to TARGET_ENV
        // (dev) before the Docker build runs so the correct Spring Cloud Config
        // profile is activated at runtime without needing a separate build per env.
        // ─────────────────────────────────────────────────────────────────────
        stage('Update Spring Profile') {
            steps {
                script {
                    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
                    echo "💮 Update Spring Profile"
                    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
                    sh '''
                        echo ""
                        echo "📄 Before:"
                        cat ${SPRING_PROFILE}

                        echo ""
                        # Replace whatever active profile is set with the target env.
                        sed -i "s/active: .*/active: ${TARGET_ENV}/" ${SPRING_PROFILE}

                        echo "📄 After:"
                        cat ${SPRING_PROFILE}

                        echo ""
                        echo "✅ Profile updated to: ${TARGET_ENV}"
                        echo ""
                    '''
                }
            }
        }

        // ─────────────────────────────────────────────────────────────────────
        // Stage 4 — Docker Build Image
        // Builds the Docker image using the Dockerfile at the repo root.
        // The image is tagged with the full Git commit SHA for traceability.
        // Fails fast if no Dockerfile is present.
        // ─────────────────────────────────────────────────────────────────────
        stage('Docker Build Image') {
            steps {
                script {
                    echo "Build Docker Image"

                    sh '''
                        echo "Current directory:"
                        pwd
                        echo ""

                        echo "Checking Dockerfile..."
                        if [ -f Dockerfile ]; then
                            echo "✅ Dockerfile found"
                            cat Dockerfile
                        else
                            echo "❌ Dockerfile not found!"
                            exit 1
                        fi

                        echo ""
                        echo "Building Docker image: ${DOCKER_FULL_IMAGE}"
                        docker build -t ${DOCKER_FULL_IMAGE} --no-cache .

                        echo ""
                        echo "✅ Docker images created:"
                        docker images | grep ${DOCKER_FULL_IMAGE}
                    '''
                }
            }
        }

        // ─────────────────────────────────────────────────────────────────────
        // Stage 5 — Push Docker Image
        // Pushes the built image to the Nexus private registry so it is
        // available to the Kubernetes cluster and can be pulled during deployment.
        // Registry credentials are handled by the Docker daemon on the agent
        // (pre-authenticated via `docker login` in the node setup).
        // ─────────────────────────────────────────────────────────────────────
        stage('Push Docker Image') {
            steps {
                script {
                    sh '''
                        echo "✅ Push Docker Image"
                        docker push ${DOCKER_FULL_IMAGE}
                    '''
                }
            }
        }

        // ─────────────────────────────────────────────────────────────────────
        // Stage 6 — Update Manifest
        // Clones the GitOps manifest repo (micro-manifest) and updates the
        // Kustomize patch file for this service with the new image tag.
        // Committing and pushing the change triggers Argo CD / Flux to roll out
        // the new image to the dev cluster automatically (GitOps pull model).
        // ─────────────────────────────────────────────────────────────────────
        stage('Update Manifest') {
            steps {
                script {
                    git branch: "${GIT_MANIFEST_BRANCH}",
                        credentialsId: "${GIT_CREDENTIALS_ID}",
                        url: "${GIT_REPO_MANIFEST_URL}"

                    withCredentials([usernamePassword(
                        credentialsId: "${GIT_CREDENTIALS_ID}",
                        usernameVariable: 'GIT_USERNAME',
                        passwordVariable: 'GIT_PASSWORD'
                    )]) {
                        sh """
                            cd ${MANIFEST_FOLDER}

                            # Replace only the `image:` line in the patch file, preserving indentation.
                            sed -E -i 's|^([[:space:]]*image:[[:space:]]*).*\$|\\1${DOCKER_FULL_IMAGE}|' "${SERVICE_PATCH}"

                            echo "Updated manifest:"
                            cat "${SERVICE_PATCH}"

                            git config user.email "jenkins@ci.local"
                            git config user.name "Jenkins"
                            git add "${SERVICE_PATCH}"

                            # `git diff --cached --quiet` exits 0 when nothing is staged.
                            # Skip commit+push if the image tag was already up to date.
                            if git diff --cached --quiet; then
                                echo "⚠️ No manifest change detected — image tag already up to date. Skipping commit."
                            else
                                git commit -m "Update ${SERVICE_PATCH} to ${DOCKER_FULL_IMAGE}"
                                # Push via HTTPS with injected credentials to avoid storing tokens in the repo.
                                git push https://${GIT_USERNAME}:${GIT_PASSWORD}@${GIT_REPO_MANIFEST_UPDATE_URL} HEAD:${GIT_MANIFEST_BRANCH}
                                echo "✅ Manifest pushed successfully."
                            fi
                        """
                    }
                }
            }
        }

        // ─────────────────────────────────────────────────────────────────────
        // Stage 7 — Clean Docker Image
        // Removes the locally built image from the Jenkins agent to reclaim
        // disk space. The image is already in Nexus so this is safe to do.
        // ─────────────────────────────────────────────────────────────────────
        stage('Clean Docker Image') {
            steps {
                script {
                    sh '''
                        echo "🧹 Cleaning Docker Image"
                        docker rmi ${DOCKER_FULL_IMAGE}
                    '''
                }
            }
        }
    }

    post {
        // Send a Telegram message to the team channel on pipeline success.
        success {
            script {
                sendTelegramNotification('SUCCESS')
            }
        }
        // Send a Telegram message on pipeline failure so the team is alerted immediately.
        failure {
            script {
                sendTelegramNotification('FAILED')
            }
        }
        // Always wipe the workspace after the run (success or failure) to prevent
        // stale files from polluting the next build on the same agent.
        always {
            script {
                echo "🧹 Cleaning workspace..."
                deleteDir()
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// HELPER — sendTelegramNotification
// Sends a formatted HTML message to the configured Telegram group/topic.
// Called from both the `success` and `failure` post blocks.
// BOT_TOKEN is injected from Jenkins credentials store at call time.
// ─────────────────────────────────────────────────────────────────────────────
def sendTelegramNotification(String status) {
    def emoji    = status == 'SUCCESS' ? '✅' : '❌'
    def buildUrl = env.BUILD_URL
    def buildNo  = env.BUILD_NUMBER

    def message = """
${emoji} <b>Building: ${env.PROJECT_SERVICE}</b>

🌿 <b>Branch:</b>  <code>${env.GIT_BRANCH}</code>
🔖 <b>Commit:</b>  <code>${env.GIT_COMMIT_MESSAGE}</code>
🐳 <b>Image:</b>   <code>${env.DOCKER_FULL_IMAGE}</code>
""".trim()

    withCredentials([
        string(credentialsId: 'telegram_bot_token', variable: 'BOT_TOKEN')
    ]) {
        sh """
            curl -s -X POST https://api.telegram.org/bot\${BOT_TOKEN}/sendMessage \\
            -d chat_id=${env.TELEGRAM_CHAT_ID} \\
            -d message_thread_id=${env.TELEGRAM_TOPIC_ID} \\
            -d parse_mode=HTML \\
            --data-urlencode text='${message}'
        """
    }
}
