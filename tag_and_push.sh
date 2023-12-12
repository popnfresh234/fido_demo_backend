echo "Tagging and Pushing Backend IMG"
sudo docker tag demo_backend:latest hub.webcomm.com.tw/alex-demo/demo_backend:latest
sudo docker push hub.webcomm.com.tw/alex-demo/demo_backend:latest
echo "Done"