./gradlew clean
./gradlew bootJar
sudo docker rmi demo_backend
sudo docker build -t ajhollid/demo_backend .