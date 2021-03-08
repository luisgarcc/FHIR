/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.code.BundleType;

/**
 * 
 * @author Luis A. García
 */
public class EverythingOperationTest extends FHIRServerTestBase {
    
    private static final boolean DEBUG = true;

    private Map<String, List<String>> createdResources;
    
    /**
     * 
     */
    public EverythingOperationTest() {
        createdResources = new HashMap<>();
    }
    
    /**
     * Create a Bundle of 895 resources of various kinds representing a patient's history and save the 
     * resource types and IDs of the created resources to eventually ensure that all resources are included
     * in an $everything invocation.
     * 
     * @throws Exception 
     */
    @Test(groups = { "fhir-operation" })
    public void testCreatePatientWithEverything() throws Exception {
        Bundle patientBundle = TestUtil.readLocalResource("everything-operation/Antonia30_Acosta403.json");
        Entity<Bundle> entity = Entity.entity(patientBundle, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = getWebTarget()
                .request()
                .post(entity, Response.class);
        
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        assertFalse(responseBundle.getEntry().isEmpty());
        for (Entry entry : responseBundle.getEntry()) {
            com.ibm.fhir.model.resource.Bundle.Entry.Response transactionResponse = entry.getResponse();
            assertEquals(transactionResponse.getStatus().getValue(), Integer.toString(Response.Status.CREATED.getStatusCode()));
            String[] locationElements = transactionResponse.getLocation().getValue().split("/");
            assertTrue(locationElements.length > 2, "Incorrect location URI format: " + transactionResponse.getLocation());
            String resourceType = locationElements[0];
            String resourceId = locationElements[1];
            List<String> resources = createdResources.computeIfAbsent(resourceType, k -> new ArrayList<>());
            resources.add(resourceId);
        }
    }

    /**
     * 
     */
    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePatientWithEverything" })
    public void testPatientEverything() {
        // Get the patient ID and invoke the $everything operation on it
        String patientId = createdResources.get("Patient").get(0);
        Response response = getWebTarget()
                .path("Patient/" + patientId +"/$everything")
                .request()
                .get(Response.class);

        // Ensure that the 895 resources are accounted for in the returning search set bundle 
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);
        BundleTest.assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 895);
        for (Entry entry : everythingBundle.getEntry()) {
            String fullURL = entry.getFullUrl().getValue();
            String[] locationElements = fullURL.replaceAll(getWebTarget().getUri().toString(), "").split("/");
            assertTrue(locationElements.length >= 2, "Incorrect full URL format: " + fullURL);
            String resourceType = locationElements[locationElements.length - 2];
            String resourceId = locationElements[locationElements.length - 1];
            List<String> resources = createdResources.get(resourceType);
            if (resources.remove(resourceId)) {
                println("Expected " + resourceType + ": " + resourceId);
            } else {
                println("Unkown " + resourceType + ": " + resourceId);
            }
        }
        
        // List all the resources pending removal for each type
        Set<java.util.Map.Entry<String, List<String>>> entries = createdResources.entrySet();
        List<String> keysToRemove = new ArrayList<>();
        for (java.util.Map.Entry<String, List<String>> entry : entries) {
            println(entry.getKey() + ": ");
            if (entry.getValue().isEmpty()) {
                println("- All removed!");
                keysToRemove.add(entry.getKey());
            } else {
                for (String id : entry.getValue()) {
                    println("- " + id);    
                }
            }
        }
        // TODO: Add support for retrieving these two that aren't currently part of the compartment resources
        keysToRemove.add(Practitioner.class.getSimpleName());
        keysToRemove.add(Organization.class.getSimpleName());
        
        // Remove all entries from the map that no longer have resources left to account for
        // we should have accounted for all resources of that type such that the map should be empty
        for (String key : keysToRemove) {
            createdResources.remove(key);
        }
        assertTrue(createdResources.isEmpty());
    }

    /**
     * 
     */
    @Test(groups = { "fhir-operation" })
    public void testPatientEverythingForNotExistingPatient() {
        Response response = getWebTarget()
                .path("Patient/some-unknown-id/$everything")
                .request()
                .get(Response.class);
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());        
    }
 
    private void println(String msg) {
        if (DEBUG) {
            System.out.println(msg);
        }
    }
}
