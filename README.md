# HushDataAnalysis

**Data**
This repository stores source code to analyze the electronic health records on ~15,000 patients that visited University of North Carolina for asthma patient. The raw dataset contains 8 separate tables that stores patient information, provider information, lab tests, medications, diagnosis etc. The raw data is not stored here for privacy reasons.

**Goal**
Our goal is to use this dataset to demonstrate how to use the loinc2hpo tool (link: https://github.com/monarch-initiative/loinc2hpo) and the loinc2hpoAnnotation library (link: https://github.com/TheJacksonLaboratory/loinc2hpoAnnotation).

Specially, we hope to :

1). parse the dataset. The majority of data (lab tests, diagnosis, medication, procedure etc) is all meshed into one giant table (~10G). We need to parse this table. Some cleaning and filtering is also needed because some records do not match the schema of the original tables. 

2) semantically integrate lab tests with the loinc2hpo tool. Lab tests are pretty heterogeneous because different lab tests may have the same medical meaning, therefore, treating them differently does not make sense. We aggregated lab tests based on their medical implications and created patient models with a set of HPO terms, such as "Eosinophilia", "Hyperkalemia", and their frequencities. For example, patient X was tested to be "Eosinophilia" 10 times in the past 5 years, "Hyperkalemia" for 20 times in the past 5 years. 

3) determine the relationships of HPO-encoded phenotypic abnormalities and asthma severity. To achieve this goal, we analyzed patient medication history, hospital visits, diagnosis codes and HPO-encoded abnormal phenotypes. We classified patients into severe and non-severe groups based on medication prescriptions. When we analyzed the statistical differences in their phenotypes. Lastly, we built predictive models for asthma severity from the HPO-encoded phenotypes using machine learning techniques. 

**Methodologies**

For Goal 1), we wrote custom Java codes to parse the data and imported them into a SQLite database. We chose Java because the task is too complicated and the file is too large for other tools, say R. The purpose of importing them into a database is to facilitate data selection and joining for initial analysis. Source code is mainly in the java/ folder. 

For Goal 2), we wrote custom Java codes mainly with the loinc2hpo library and the loinc2hpoAnnotation map. Source code is in the java/ folder. 

For Goal 3), we mainly utilized R, particularly with the tidyrverse package and the caret package. Source code is mainly in the r_script/ folder. 

