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

### Publish obfuscated module

```bash
# Build jar files meant to be obfuscated
./gradlew :api:bundleDebugAar
./gradlew :api:bundleReleaseAar
./gradlew :api:jvmJar
# Obfuscate
./gradlew :api:OBFUSCATING_TASK_NAME
# Next publish
./gradlew :api:publish
```

### Check published repo is accessible

```bash
./gradlew :api:check
```