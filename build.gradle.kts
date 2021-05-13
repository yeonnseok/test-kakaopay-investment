import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.asciidoctor.convert") version "1.5.12"
	kotlin("jvm") version "1.4.32"
	kotlin("plugin.spring") version "1.4.32"
	kotlin("plugin.jpa") version "1.4.32"
	jacoco
}

jacoco {
	toolVersion = "0.8.5"
}

group = "com.test.kakaopay"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.google.code.gson:gson:2.8.6")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	runtimeOnly("com.h2database:h2")
	asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("io.rest-assured:rest-assured:3.3.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
	reports {
		html.isEnabled = true
		xml.isEnabled = false
		csv.isEnabled = false
	}
	finalizedBy("jacocoTestCoverageVerification")
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			enabled = true

			limit {
				counter = "INSTRUCTION"
				value = "COVEREDRATIO"
				minimum = "0.85".toBigDecimal()
			}
		}
	}
}

val snippetsDir = file("build/generated-snippets")

tasks {
	test {
		outputs.dir(snippetsDir)
	}

	asciidoctor {
		inputs.dir(snippetsDir)
		dependsOn(test)
	}

	asciidoctor {
		copy { from("${asciidoctor.get().outputDir}/html5")
			into("src/main/resources/static/docs")
		}
	}

	build {
		dependsOn(asciidoctor)
	}
}
