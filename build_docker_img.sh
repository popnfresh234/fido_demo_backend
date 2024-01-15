./gradlew clean
./gradlew bootJar

docker rmi demo_backend
docker build -t demo_backend .