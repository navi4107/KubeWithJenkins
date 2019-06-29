package com.example.demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.Response;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class KubernetesDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KubernetesDemoApplication.class, args);
	}

	@RestController
	class Endpoint {

		@SuppressWarnings("unchecked")
		@GetMapping("/")
		public String sayHello(@RequestParam("jobName") String name) throws IOException {
//curl  -v -X POST "http://hyperion:114d3271a3e02386a96eba286a588cc896@jenkinsscmidc01-dev.idc1.level3.com:8080/createItem?name=HYPERION_ITCDMTEST12_MASTER" --data-binary "@config.xml" -H "Content-Type: text/xml" 
			// https://stackoverflow.com/questions/49051397/create-jenkins-job-remotely-with-rest-api
			// https://stackoverflow.com/questions/49949373/how-to-update-jenkins-job-config-xml-file-using-curl

			RestTemplate restTemplate = new RestTemplate();

			String path = "C:\\Users\\ac39563\\Hyperion_DOTNET\\KubernetesDemo\\src\\main\\resources\\config.xml";

			String con = "<project>\n" + "<actions/>\n" + "<description/>\n"
					+ "<keepDependencies>false</keepDependencies>\n" + "<properties>\n"
					+ "<com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty plugin=\"gitlab-plugin@1.5.11\">\n"
					+ "<gitLabConnection>GitLab Prod</gitLabConnection>\n"
					+ "</com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty>\n"
					+ "<com.sonyericsson.rebuild.RebuildSettings plugin=\"rebuild@1.29\">\n"
					+ "<autoRebuild>false</autoRebuild>\n" + "<rebuildDisabled>false</rebuildDisabled>\n"
					+ "</com.sonyericsson.rebuild.RebuildSettings>\n" + "</properties>\n"
					+ "<scm class=\"hudson.plugins.git.GitSCM\" plugin=\"git@3.9.1\">\n"
					+ "<configVersion>2</configVersion>\n" + "<userRemoteConfigs>\n"
					+ "<hudson.plugins.git.UserRemoteConfig>\n" + "<url>\n"
					+ "git@NE1ITCPRHAS62.ne1.savvis.net:HYPERION_STG/demotest04.git\n" + "</url>\n"
					+ "<credentialsId>8cd4df93-b2a3-4e23-9425-7cad591b7016</credentialsId>\n"
					+ "</hudson.plugins.git.UserRemoteConfig>\n" + "</userRemoteConfigs>\n" + "<branches>\n"
					+ "<hudson.plugins.git.BranchSpec>\n" + "<name>*/master</name>\n"
					+ "</hudson.plugins.git.BranchSpec>\n" + "</branches>\n"
					+ "<doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>\n"
					+ "<gitTool>Default</gitTool>\n" + "<submoduleCfg class=\"list\"/>\n" + "<extensions/>\n"
					+ "</scm>\n" + "<assignedNode>jenkinsidckube-prod</assignedNode>\n" + "<canRoam>false</canRoam>\n"
					+ "<disabled>false</disabled>\n"
					+ "<blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\n"
					+ "<blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\n"
					+ "<jdk>(System)</jdk>\n" + "<triggers/>\n" + "<concurrentBuild>false</concurrentBuild>\n"
					+ "<builders>\n" + "<hudson.tasks.Shell>\n" + "<command>\n"
					+ "dotnet restore dotnet clean dotnet build --configuration Release echo 'dotnet got builded' docker --version docker login -u 'gitlab-ci-token' -p 'qx-T_b2QNB5TCtFJsE44' NE1ITCPRHAS62.ne1.savvis.net:4567 docker build -t NE1ITCPRHAS62.ne1.savvis.net:4567/hyperion_stg/demotest04:0.2 . echo 'image got created' docker push NE1ITCPRHAS62.ne1.savvis.net:4567/hyperion_stg/demotest04:0.2 echo 'image got pushed'\n"
					+ "</command>\n" + "</hudson.tasks.Shell>\n" + "</builders>\n" + "<publishers/>\n"
					+ "<buildWrappers/>\n" + "</project>";

			String auth = "hyperion" + ":" + "kZiuCcf31mfmqMvGs98HF6S6";
			HttpHeaders httpHeaders = new HttpHeaders();

			httpHeaders.add("Authorization", "Basic " + DatatypeConverter.printBase64Binary(auth.getBytes()));

			System.out.println("Basic " + DatatypeConverter.printBase64Binary(auth.getBytes()));

			HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
			ResponseEntity<CrumbResponse> response = restTemplate.exchange(
					"http://jenkinsscmidc01-prod:8080/crumbIssuer/api/json", HttpMethod.GET, httpEntity,
					CrumbResponse.class);

			CrumbResponse crumb = response.getBody();
			System.out.println(crumb.getCrumb());

			// http://35.225.10.53:8080/createItem?name=dotnettest
			String jobURI = "http://jenkinsscmidc01-prod:8080/createItem?name=" + name;

			// restTemplate.exchange(requestEntity, responseType)

			System.out.println(crumb.getCrumbRequestField());
			HttpHeaders httpHeaders1 = new HttpHeaders();
			httpHeaders1.add("Authorization", "Basic " + DatatypeConverter.printBase64Binary(auth.getBytes()));
			System.out.println(DatatypeConverter.printBase64Binary(auth.getBytes()));
			httpHeaders1.setContentType(MediaType.APPLICATION_XML);
			httpHeaders1.add("Jenkins-Crumb", crumb.getCrumb());

			HttpEntity<String> httpEntity1 = new HttpEntity<>(con, httpHeaders1);

			restTemplate.exchange(jobURI, HttpMethod.POST, httpEntity1, Object.class);

			return null;
		}

	}

}
