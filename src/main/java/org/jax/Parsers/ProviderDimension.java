package org.jax.Parsers;

import java.util.Date;
/**
 * This class is used to parse PROVIDER_DIMENSION.txt
 * Priority: 5 (range 1-MaxInfinity, 1 is highest)
 */
public interface ProviderDimension {
    String provider_path();

    String provider_id();

    String name();

    String firstName();

    String lastName();

    String middleNameInitial();

    Date update_date();

    Date download_date();

    Date import_date();

    String sourcesystem_cd();

    int upload_id();








}
