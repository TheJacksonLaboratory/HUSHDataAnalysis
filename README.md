# HushDataAnalysis

**Data**

This repository stores source code used to analyze the electronic health records on 15,681 patients that visited University of North Carolina for asthma patient. It is a command line app written in Java that can import the i2b2 style tabular tables into a relational database, transform the lab test records into HPO terms, perform HPO hierarchical inference and so on (refer to the main class). Java was preferred because the tasks are relatively complicated and cubersome in R. The repository can be packaged into a Jar to run in the terminal. The raw dataset contains 8 separate tables that stores patient information, provider information, lab tests, medications, diagnosis etc. The raw data is not stored here for privacy reasons. If you are interested in exploring this dataset, please contact us and we will refer you to relevant parties at UNC, whom you will sign data use agreement with and get data from.

**Goal**
Our goal is to use this dataset to demonstrate how to integrate lab tests with the loinc2hpo tool (link: https://github.com/monarch-initiative/loinc2hpo) and the loinc2hpoAnnotation library (link: https://github.com/TheJacksonLaboratory/loinc2hpoAnnotation) and use the resulting patient profiles for association studies.

Specially, we hope to :

1) parse the dataset. The majority of data (lab tests, diagnosis, medication, procedure etc) is all meshed into one giant table (~10G). We need to parse this table. Some cleaning and filtering is also needed because some records do not match the schema of the original tables. 

2) semantically integrate lab tests with the loinc2hpo tool. Lab tests are pretty heterogeneous because different lab tests may have the same medical meaning, therefore, treating them differently does not make sense. We aggregated lab tests based on their medical implications and created patient models with a set of HPO terms, such as "Eosinophilia", "Hyperkalemia", and their frequencities. For example, patient X was tested to be "Eosinophilia" 10 times in the past 5 years, "Hyperkalemia" for 20 times in the past 5 years. 

3) determine the relationships of HPO-encoded phenotypic abnormalities with two medical outcomes, frequent prednisone prescription and acute asthma diagnosis. To achieve this goal, we analyzed patient medication history, hospital visits, diagnosis codes and HPO-encoded abnormal phenotypes. We classified patients into one group that received frequent prednisone prescription and the other group without such prescriptions. Alternatively, we classified patients into two groups based on whether acute asthma diagnosis was assigned. Then we applied logistic regression to analyze the associations of either prednisone prescription or acute asthma diagnosis with each phenotype. The associated phenotypes can be considered adverse drug effects of prednisone and biomarkers for acute asthma diagnosis. Lastly, we built predictive models for asthma severity from the HPO-encoded phenotypes using machine learning techniques (not included in the npj Digital Medicine paper). 

**Methodologies**

For Goal 1), we wrote custom Java codes to parse the data and imported them into a SQLite database. We chose Java because the task is too complicated and the file is too large for other tools, say R. The purpose of importing them into a database is to facilitate data query and joining for initial analysis. Source code is mainly in the java/ folder. 

For Goal 2), we wrote custom Java codes mainly with the loinc2hpo library and the loinc2hpoAnnotation map. Source code is in the java/ folder. For both Goal 1) and 2), one can run our java code from terminal with approprate option tags and parameters. 

For Goal 3), we mainly utilized R, particularly with the tidyrverse package and the caret package. Source code is mainly in the r_script/ folder. 


Note: the data/ folder does not contain all results generated from the scripts.
