/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.parameters.cache;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.core.TenantSpecificFileBasedCache;
import com.ibm.watsonhealth.fhir.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.model.resource.SearchParameter;
import com.ibm.watsonhealth.fhir.search.parameters.ParametersUtil;

/**
 * This class implements a cache of SearchParameters organized by tenantId. Each object stored in the cache will be a
 * two-level map of SearchParameters organized first by resource type, then by search parameter name.
 * 
 * @author padams
 * @author pbastide
 */
public class TenantSpecificSearchParameterCache extends TenantSpecificFileBasedCache<Map<String, Map<String, SearchParameter>>> {

    private static final String CLASSNAME = TenantSpecificSearchParameterCache.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static final String SP_FILE_BASENAME_JSON = "extension-search-parameters.json";

    public TenantSpecificSearchParameterCache() {
        super("SearchParameters");
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.core.TenantSpecificFileBasedCache#getCacheEntryFilename(java.lang.String)
     */
    @Override
    public String getCacheEntryFilename(String tenantId) {
        return FHIRConfiguration.getConfigHome() + FHIRConfiguration.CONFIG_LOCATION + File.separator + tenantId + File.separator + SP_FILE_BASENAME_JSON;
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.core.TenantSpecificFileBasedCache#createCachedObject(java.lang.String)
     */
    @Override
    public Map<String, Map<String, SearchParameter>> createCachedObject(File f) throws Exception {
        try {
            // Added logging to help diagnose issues while loading the files.
            log.fine("The file loaded is " + f.toURI());
            return ParametersUtil.populateSearchParameterMapFromFile(f);
        } catch (Throwable t) {
            // In R4, there are two files used with postfix JSON.
            // Default is to use JSON in R4
            throw new FHIROperationException("An error occurred while loading one of the tenant files: " + f.getAbsolutePath(), t);
        }
    }
}