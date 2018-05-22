package org.jax.Parsers;


/**
 * This class is used to parse VISIT_DIMENSION.txt
 * Priority: 1 (range 3-MaxInfinity, 1 is highest)
 */
public interface VisitDimension {

    int encounter_num();

    int patient_num();

    String inOut_cd();

}
