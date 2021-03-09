/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0 
 */
package com.ibm.fhir.operation.everything;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.exception.FHIRSearchException;

/**
 * 
 */
public class EverythingOperationTest {

    
    private EverythingOperation everythingOperation;

    /**
     * 
     */
    public EverythingOperationTest() {
        everythingOperation = new EverythingOperation();
    }

    /**
     * 
     */
    @Test
    public void testEverythingOperation() {
        EverythingOperation exportOperation = new EverythingOperation();
        OperationDefinition operationDefinition = exportOperation.buildOperationDefinition();
        assertNotNull(operationDefinition);
    }

    /**
     * @throws IOException 
     * @throws FHIRParserException 
     * @throws FHIRSearchException 
     */
    @Test
    public void testConvertParametersType() throws FHIRParserException, IOException, FHIRSearchException {
        Parameters parameters = loadParametersFile("parameters-type.json");
        List<String> overrideTypes = everythingOperation.getOverridenIncludedResourceTypes(parameters);
        assertEquals(2, overrideTypes.size());
        assertTrue(overrideTypes.contains("CarePlan"));
        assertTrue(overrideTypes.contains("CareTeam"));
    }

    /**
     * @throws IOException 
     * @throws FHIRParserException 
     */
    @Test
    public void testConvertParametersStart() throws FHIRParserException, IOException {
        Parameters parameters = loadParametersFile("parameters-start.json");
        MultivaluedMap<String, String> queryParameters = everythingOperation.parseQueryParameters(parameters);
        assertEquals(SearchConstants.MAX_PAGE_SIZE + "", queryParameters.getFirst(SearchConstants.COUNT));
        assertEquals(1, queryParameters.get(EverythingOperation.DATE_QUERY_PARAMETER).size());
        assertTrue(queryParameters.get(EverythingOperation.DATE_QUERY_PARAMETER).contains(EverythingOperation.STARTING_FROM + "1970-11-04"));
    }

    /**
     * @throws IOException 
     * @throws FHIRParserException 
     */
    @Test
    public void testConvertParametersEnd() throws FHIRParserException, IOException {
        Parameters parameters = loadParametersFile("parameters-end.json");
        MultivaluedMap<String, String> queryParameters = everythingOperation.parseQueryParameters(parameters);
        assertEquals(SearchConstants.MAX_PAGE_SIZE + "", queryParameters.getFirst(SearchConstants.COUNT));
        assertEquals(1, queryParameters.get(EverythingOperation.DATE_QUERY_PARAMETER).size());
        assertTrue(queryParameters.get(EverythingOperation.DATE_QUERY_PARAMETER).contains(EverythingOperation.UP_UNTIL+ "1971-01-01"));
    }

    /**
     * @throws IOException 
     * @throws FHIRParserException 
     */
    @Test
    public void testConvertParametersStartEnd() throws FHIRParserException, IOException {
        Parameters parameters = loadParametersFile("parameters-start-end.json");
        MultivaluedMap<String, String> queryParameters = everythingOperation.parseQueryParameters(parameters);
        assertEquals(SearchConstants.MAX_PAGE_SIZE + "", queryParameters.getFirst(SearchConstants.COUNT));
        assertEquals(2, queryParameters.get(EverythingOperation.DATE_QUERY_PARAMETER).size());
        assertTrue(queryParameters.get(EverythingOperation.DATE_QUERY_PARAMETER).contains(EverythingOperation.STARTING_FROM + "1970-11-04"));
        assertTrue(queryParameters.get(EverythingOperation.DATE_QUERY_PARAMETER).contains(EverythingOperation.UP_UNTIL+ "1971-01-01"));
    }

    /**
     * @throws IOException 
     * @throws FHIRParserException 
     */
    @Test
    public void testConvertParametersStartEndCount() throws FHIRParserException, IOException {
        Parameters parameters = loadParametersFile("parameters-start-end-count.json");
        MultivaluedMap<String, String> queryParameters = everythingOperation.parseQueryParameters(parameters);
        assertEquals("10", queryParameters.getFirst(SearchConstants.COUNT));
        assertEquals(2, queryParameters.get(EverythingOperation.DATE_QUERY_PARAMETER).size());
        assertTrue(queryParameters.get(EverythingOperation.DATE_QUERY_PARAMETER).contains(EverythingOperation.STARTING_FROM + "1970-11-04"));
        assertTrue(queryParameters.get(EverythingOperation.DATE_QUERY_PARAMETER).contains(EverythingOperation.UP_UNTIL+ "1971-01-01"));
    }

    /**
     * @throws IOException 
     * @throws FHIRParserException 
     */
    @Test
    public void testConvertParametersStartEndCountSince() throws FHIRParserException, IOException {
        Parameters parameters = loadParametersFile("parameters-start-end-count-since.json");
        MultivaluedMap<String, String> queryParameters = everythingOperation.parseQueryParameters(parameters);
        assertEquals("10", queryParameters.getFirst(SearchConstants.COUNT));
        assertEquals(2, queryParameters.get(EverythingOperation.DATE_QUERY_PARAMETER).size());
        assertTrue(queryParameters.get(EverythingOperation.DATE_QUERY_PARAMETER).contains(EverythingOperation.STARTING_FROM + "1970-11-04"));
        assertTrue(queryParameters.get(EverythingOperation.DATE_QUERY_PARAMETER).contains(EverythingOperation.UP_UNTIL+ "1971-01-01"));
        assertEquals(EverythingOperation.STARTING_FROM + "2017-01-01T00:00Z", queryParameters.getFirst(EverythingOperation.LAST_UPDATED_QUERY_PARAMETER));
    }

    private Parameters loadParametersFile(String file) throws IOException, FHIRParserException {
        try (InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream("/" + file))) {
            return FHIRParser.parser(Format.JSON).parse(reader);
        }
    }
}