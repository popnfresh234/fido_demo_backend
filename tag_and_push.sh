echo "Tagging and Pushing Backend IMG"
docker tag demo_backend:latest hub.webcomm.com.tw/alex-demo/demo_backend:latest
docker push hub.webcomm.com.tw/alex-demo/demo_backend:latest
echo "Done"