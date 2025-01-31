PROJECT_NAME := Banking-0.0.1-SNAPSHOT
MAIN_CLASS := com.app.banking.BankingApplication
JAR = $(wildcard ./target/$(PROJECT_NAME).jar)
DEBUG = -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n

install: clean
	mvn install

clean:
	mvn clean

package: clean
	mvn -Dmaven.test.skip=true -Dspring.profiles.active=local package

unit-test: clean
	mvn test

run-local: package
	docker compose up -d

debug-local: package
	java -Denv=local -Dspring.profiles.active=local $(DEBUG)  -jar $(JAR)
