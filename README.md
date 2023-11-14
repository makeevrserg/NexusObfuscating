### Install nexus repository
```bash
# Init docker compose
docker compsoe up
# If you don't have permissions 
sudo chmod 777 nexus-data
docker compsoe up
# Next, locate password. The user is admin
cat nexus-data/admin.password
```