package org.jax.Fhir;

import ca.uhn.fhir.rest.client.api.IGenericClient;

/**
 * The class model a FhirServer: given a base URL, resources are uploaded or downloaded
 */
public interface FhirServer extends FhirResourceDownload, FhirResourceUpload {
    String getBaseAddress();
    IGenericClient restifulGenericClient();
}
