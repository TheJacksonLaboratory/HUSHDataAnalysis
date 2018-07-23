// select all the ICD records and remove the detailed code in ICD
CREATE TABLE IF NOT EXISTS ICD AS SELECT * FROM OBSERVATION_FACT WHERE concept_cd LIKE 'ICD%';
UPDATE ICD SET icd=SUBSTR(concept_cd, 1, INSTR(concept_cd, '.') - 1);

//count the no. of patient for according to ICD codes (classes)
SELECT icd, COUNT(DISTINCT(patient_num)) AS count FROM ICD GROUP BY icd ORDER BY count DESC ;

//asthma ICD10 code: J45 ICD9: 493   total 5946 patients
SELECT COUNT(DISTINCT(patient_num)) FROM ICD WHERE icd LIKE 'ICD10:J45%' OR icd LIKE 'ICD9:493%';

//also include codes for breezing difficulties 14949
SELECT COUNT(DISTINCT(patient_num)) FROM ICD WHERE icd LIKE 'ICD10:J45%' OR icd LIKE 'ICD9:493%' OR icd LIKE 'ICD9:786%' OR icd LIKE 'ICD10:R06';

SELECT * FROM ICD WHERE patient_num IN (SELECT DISTINCT(patient_num) FROM ICD WHERE icd = 'ICD10:J45' OR icd = 'ICD9:493')  AND patient_num IN (SELECT DISTINCT(patient_num) FROM ICD WHERE icd = 'ICD10:N18' OR icd = 'ICD9:585');