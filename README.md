## IBM Watson Health FHIR Server

### Overview
The Watson Health FHIR Server provides a modular implementation version DSTU2 (1.0.2) of the [HL7 FHIR specification](https://www.hl7.org/fhir/dstu2/http.html) with a focus on performance and configurability.

CODE_REMOVED

CODE_REMOVED

CODE_REMOVED

CODE_REMOVED

### Running the Watson Health FHIR Server
CODE_REMOVED

CODE_REMOVED

Alternatively, we are now building an Ubuntu-based docker image of the Watson Health FHIR Server (along-side the installer).
To pull the latest fhir-server image:
1. `docker login wh-fhir-server-snapshots-docker-local.artifactory.swg-devops.com`
2. `docker pull wh-fhir-server-snapshots-docker-local.artifactory.swg-devops.com/fhir-server`
Release versions will be available from `wh-fhir-server-releases-docker-local.artifactory.swg-devops.com`.

If you intend to use these artifacts in a service or solution, please enter a dependency in [ClearingHouse](https://clearinghousev2.raleigh.ibm.com/CHNewCHRDM/CCHMServlet#&nature=wlhNDE&deliverableId=857565F0B78F11E88857DCC2171712A1).

### Building on top of the Watson Health FHIR Server
Once you've been added to the `afaas-wh-fhir-server-read` BlueGroup controlling access to Artifactory, you can add dependencies to our jar files by adding our repositories to your maven config. One common pattern is to add the repositories to your parent pom and your Artifactory username/apikey to your personal setting (e.g. ~/.m2/settings.xml).

Sample snippet for pom.xml:
```
<repositories>
    <repository>
        <id>na-artifactory-3rdparty</id>
CODE_REMOVED
    </repository>
    <repository>
        <id>na-artifactory-snapshots</id>
CODE_REMOVED
    </repository>
    <repository>
        <id>na-artifactory-releases</id>
CODE_REMOVED
    </repository>
</repositories>
```

Sample snippet for settings.xml:
```
<servers>
    <server>
      <username></username>
      <password></password>
      <id>na-artifactory-snapshots</id>
    </server>
    <server>
      <username></username>
      <password></password>
      <id>na-artifactory-3rdparty</id>
    </server>
    <server>
      <username></username>
      <password></password>
      <id>na-artifactory-releases</id>
    </server>
</servers>
```

Once configured, you can add dependencies on specific fhir-server artifacts like this:
```
<dependencies>
    <dependency>
      <groupId>com.ibm.watsonhealth.fhir</groupId>
      <artifactId>fhir-model</artifactId>
      <version>${fhir.version}</version>
    </dependency>
    ...
```

If you intend to use these artifacts in a service or solution, please enter a dependency in [ClearingHouse](https://clearinghousev2.raleigh.ibm.com/CHNewCHRDM/CCHMServlet#&nature=wlhNDE&deliverableId=857565F0B78F11E88857DCC2171712A1).

### Contributing to the Watson Health FHIR Server
For details on how to obtain and build the FHIR Server code, please see the [wiki](wiki).

CODE_REMOVED

**Note to developers:**  
To associate a commit with a github issue, add "issue #nnn -" on the front of your commit message, like this:  

        git commit -m "issue #123 - Finished the FHIR Server code"  

If you forget to include your work item number in your commit message, you can always "amend" the commit message
by running this command prior to pushing your changes to the server:  

        git commit --amend  
        (this will open up your editor and allow you to change the commit message)
