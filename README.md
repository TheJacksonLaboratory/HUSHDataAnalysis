# HushToFhir

**May Hackathon pitch**
Mapping of UNC Hush+ data to Human Phenotype Ontology terms for explorative clustering

Expected outcome: SOP for normalizing clinical data via Loinc2Hpo mapping and text mining and cleansing/coding of data prior to machine learning/clustering

To achieve our hackathon goal, we should clean the data and transform it to a format that we can use for clustering. I propose we consider the following steps:

- Parse UNC HUSH+ data and transform them into FHIR resources. There will be a lot cleaning work to do. For example, some lab tests are not coded in LOINC so they need to be filtered. We also need to parse the tests into Java objects, such as converting strings to Java dates, LOINC strings to LoincId etc. Once we are done with parsing and cleaning, it is fairly easy to transform them into FHIR resources (observation and patient are minimally required)

- Store FHIR resources locally. We can store FHIR resources as json files on our laptop. A better way, however, may be to store them in a local FHIR server. Fortunately it is fairly easy to install a local FHIR server. Follow the following tutorial to do so: http://hapifhir.io/doc_jpa.html Once the local server is set up, we can store all resources there so that 1) they are interconnected (one resource has links to related resources) and 2) it reflect the real-world better. 

- Select most frequent/most important LOINC tests to annotate before the hackathon. We can target at least one to a few hundred LOINC tests to annotate so that once we have transformed the data to FHIR resources, we can convert them into HPO terms. This step does not need to be STEP 3. We probably should do this one immediately. 
(@ Peter, we received a CSV file from Kimberley a while ago which is aggregated data on the tests. I think that file has everything I proposed here, so we can bypass this step for now.)

- Think about how to cluster patient based on their HPO phenotypes. If we are successful in above steps, we will have a collection of asthma patients that are described in a list of HPO terms. Each set of LOINC tests (annotated with same HPO terms) have categorical outcomes, for example hyperglycemia, hypoglycemia, not abnormality of blood glucose concentration or NA (missing data). We should think about the best ways to do clustering on categorical data. 

- Explore other information in the dataset. Need to further work on this idea. 
