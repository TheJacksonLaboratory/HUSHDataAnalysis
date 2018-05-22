package org.jax.Parsers;

import java.util.Date;
/**
 * This class is used to parse CONCEPT_DIMENSION.txt
 * Priority: 4 (range 1-MaxInfinity, 1 is highest)
 */
public interface ConceptDimension {

    String concept_path();

    String concept_cd();

    String name_char();

    String concept_blob();

    Date update_date();

    Date download_date();

    Date import_date();

    String sourcesystem_cd();

    int upload_id();
}
