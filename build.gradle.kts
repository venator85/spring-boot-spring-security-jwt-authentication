plugins {
    java
    war
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.nimbusds:nimbus-jose-jwt:9.25.6")

    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	runtimeOnly("com.h2database:h2")
}

group = "com.bezkoder"
version = "0.0.1-SNAPSHOT"
description = "spring-boot-security-jwt"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
