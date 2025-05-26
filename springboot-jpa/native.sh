#mvn -Pnative spring-boot:build-image
set -o xtrace
mvn -Pnative native:compile
