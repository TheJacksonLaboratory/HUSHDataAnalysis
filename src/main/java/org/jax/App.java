package org.jax;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.cli.*;
import org.hl7.fhir.dstu3.model.Patient;
import org.jax.Exception.IllegalDataTypeException;
import org.jax.Exception.MalformedLineException;
import org.jax.Fhir.TransformToFhir;
import org.jax.Parsers.*;
import org.jax.database.LoadObservation;
import org.jax.database.SQLiteJDBCDriverConnection;
import org.jax.database.TableImporter;
import org.monarchinitiative.loinc2hpo.codesystems.Code;
import org.monarchinitiative.loinc2hpo.codesystems.Loinc2HPOCodedValue;
import org.monarchinitiative.loinc2hpo.exception.MalformedLoincCodeException;
import org.monarchinitiative.loinc2hpo.io.HpoOntologyParser;
import org.monarchinitiative.loinc2hpo.io.LoincAnnotationSerializationFactory;
import org.monarchinitiative.loinc2hpo.loinc.HpoTerm4TestOutcome;
import org.monarchinitiative.loinc2hpo.loinc.LOINC2HpoAnnotationImpl;
import org.monarchinitiative.loinc2hpo.loinc.LoincEntry;
import org.monarchinitiative.loinc2hpo.loinc.LoincId;
import org.monarchinitiative.loinc2hpo.patientmodel.BagOfTerms;
import org.monarchinitiative.loinc2hpo.patientmodel.BagOfTermsWithFrequencies;
import org.monarchinitiative.loinc2hpo.testresult.PhenoSetUnionFind;
import org.monarchinitiative.phenol.base.PhenolException;
import org.monarchinitiative.phenol.formats.hpo.HpoOntology;
import org.monarchinitiative.phenol.io.obo.hpo.HpOboParser;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App {

    private final static Logger logger = LoggerFactory.getLogger(App.class);
    final static String WORKING_DIR = System.getProperty("user.dir");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static Map<LoincId, LOINC2HpoAnnotationImpl> testmap = new HashMap<>();
    private static HpoOntology hpo;
    private static Map<String, Term> hpoTermMap;
    private static Map<TermId, Term> hpoTermMap2;
    private static Map<LoincId, LOINC2HpoAnnotationImpl> annotationMap;
    private static PhenoSetUnionFind unionFind;
    private static Map<LoincId, LoincEntry> loincIdLoincEntryMap;

    static void max_nums(String observationPath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(observationPath))) {
            String newLine = reader.readLine();
            long maxencounter_num = 0L;
            long maxPatient_num = 0L;
            int lineCount = 0;
            while ((newLine = reader.readLine()) !=null) {
                lineCount++;
                String encounter_num_string = newLine.split(",")[0];
                String patient_num_string = newLine.split(",")[1];
                long encounter_num ;
                long patient_num;

                try {
                    encounter_num = Long.parseLong(encounter_num_string);
                    if (encounter_num > maxencounter_num) {
                        maxencounter_num = encounter_num;
                        System.out.println(String.format("new max number-- line: %d\t num: %d ",
                                lineCount, encounter_num));
                    }

                } catch (Exception e) {
                    System.out.println("Non-num of encounter_num found: " + encounter_num_string);
                }

                try {
                    patient_num = Long.parseLong(patient_num_string);
                    if (patient_num > maxPatient_num) {
                        maxPatient_num = patient_num;
                        System.out.println(String.format("new max patient number-- line: %d\t num: %d",
                                lineCount, patient_num ));
                    }
                } catch (Exception e ) {
                    System.out.print("Non-num of patient_num found: " + patient_num_string);
                }

            }

            System.out.println("final outcome--max encounter_num: " + maxencounter_num);
            System.out.println("final outcome--max patient_num: " + maxPatient_num);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    /**
     * caculate frequency of different concepts in the OBSERVATION_FACT table
     * @param path
     */
     static void concept_freq(String path) {
        //Queue<CPT_Component> priority_queue = new PriorityQueue<>();
         //read in data, count how many time each concept occurred
        Map<String, Integer> cpt_map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("\"")) {
                    line = line.substring(1, line.length());
                }
                if (line.endsWith("\"")) {
                    line = line.substring(0, line.length() - 1);
                }
                if (cpt_map.containsKey(line)) {
                    cpt_map.put(line, cpt_map.get(line) + 1);
                } else {
                    cpt_map.put(line, 1);
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("file not found: " + path);

        } catch (IOException e) {

            logger.error("IO exception");
        }

        List<CPT_Component> list = cpt_map.entrySet()
                .stream()
                .map(p -> new CPT_Component(p.getKey(), p.getValue()))
                .collect(Collectors.toList());
        //sort components by frequency count, largest to smallest
        Collections.sort(list, new Comparator<CPT_Component>() {
            @Override
            public int compare(CPT_Component o1, CPT_Component o2) {
                return o2.count - o1.count;
            }
        });

         //calculate total counts
         int total_count = list.stream().mapToInt(p -> p.count).sum();
         //calculate how many times the top 100 concepts occurred
         int top_100 = list.stream().limit(100).mapToInt(p -> p.count).sum();

         //print out information
         list.stream().limit(10).forEach(System.out::println);
         //total count: 54683532
         System.out.println("total count: " + total_count);
         System.out.println("top 100 count: " + top_100);
         System.out.println("Fraction of top 100 concept: " + (0.0 + top_100) / total_count);
         //list.stream().limit(100).forEach(System.out::println);


        //output the count per concept, from most frequent to lease frequent
         String cpt_counts_path = WORKING_DIR + File.separator + "data" + File.separator + "cpt_counts.csv";
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(cpt_counts_path))) {
             writer.write("# this table describes the count for each observation concept in OBSERVATION_FACTS.txt ##\n");
             writer.write("cpt,count\n");
             list.forEach(p -> {
                 try {
                     writer.write(p.name + "," + p.count + "\n");
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             });
         } catch (IOException e) {
             e.printStackTrace();
         }


         //output cumulative counts
         Stack<Integer> count_hist = new Stack<>();
         for (int i = 0; i < 500; i = i + 20) {
             int previous_sum = 0;
             int current_step;
             if (!count_hist.isEmpty()) {
                 previous_sum = count_hist.peek();
             }
             //calculate counts in current step size [i, i+20)
             current_step = list.stream().limit(i + 20).skip(i).mapToInt(p -> p.count).sum();
             //put accumulated count to stack
             count_hist.push(previous_sum + current_step);
             System.out.println("Accumulated Frequency count:");
             System.out.println(String.format("[%d - %d]: %d", i, i + 20, count_hist.peek()));
         }

         String cpt_cumulative_counts_path = WORKING_DIR + File.separator + "data" + File.separator + "cumulative_cpt_count.csv";
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(cpt_cumulative_counts_path))) {
             //write out stack to a file for visualization
             writer.write("## this table describes the cumulative counts (from most frequent to least frequent) for each distinct observation in OBSERVATION_FACT.txt. Step 20 means top 20; step 100 means top 100. Refer to graph for visualization. Skip this line when read in data ##\n");
             writer.write("step,count\n");
             int i = 0;
             for (Integer count : count_hist.stream().collect(Collectors.toList())) {
                 writer.write(String.format("%d,%d\n", i + 20, count));
                 i = i + 20;
             }
         } catch (IOException e) {
             e.printStackTrace();
         }


         //calculate counts by concept categories, e.g. VITAL, LOINC, ICD9, ICD10...
         String cpt_counts = WORKING_DIR + File.separator + "data" + File.separator + "cpt_counts.csv";
         Map<String, Integer> cpt_category_count = new HashMap<>();
         list.forEach(c -> {
             String cat = c.name.split(":|\\|")[0];
             if (cpt_category_count.containsKey(cat)) {
                 cpt_category_count.put(cat, cpt_category_count.get(cat) + c.count);
             } else {
                 cpt_category_count.put(cat, c.count);
             }
         });
         String cpt_category_count_path = WORKING_DIR + File.separator + "data" + File.separator + "cpt_category_count.csv";

         try (BufferedWriter writer = new BufferedWriter(new FileWriter(cpt_category_count_path))) {
             writer.write("## this table describes the counts for each observation type (medication, LOINC, vital signs, ICD9, ICD 10 etc) in the OBSERVATION_FACT.txt file. Refer to the graph for visualization. Skip this line when reading in data ##\n");
             writer.write("category,count\n");
             cpt_category_count.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                     .forEachOrdered(p -> {
                 try {
                     writer.write(p.getKey() + "," + p.getValue() + "\n");
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             });
         } catch (IOException e) {
             e.printStackTrace();
         }

    }

    //Caculate the counts of different concept categories
    static void cpt_category_count() {
        String cpt_counts = WORKING_DIR + File.separator + "data" + File.separator + "cpt_counts.csv";
        Map<String, Integer> cpt_category_count = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(cpt_counts))) {
            String line;
            while ((line = reader.readLine()) != null ) {
                if (line.startsWith("cpt,count")) {

                } else {
                    String name = line.split(",")[0];
                    int count = Integer.parseInt(line.split(",")[1]);
                    String cat = name.split(":|\\|")[0];
                    if(cpt_category_count.containsKey(cat)) {
                        cpt_category_count.put(cat, cpt_category_count.get(cat) + count);
                    } else {
                        cpt_category_count.put(cat, count);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String cpt_category_count_path = WORKING_DIR + File.separator + "data" + File.separator + "cpt_category_count.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cpt_category_count_path))) {
            writer.write("category,count\n");
            cpt_category_count.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEachOrdered(p -> {
                try {
                    writer.write(p.getKey() + "," + p.getValue() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static class CPT_Component implements Comparable<CPT_Component> {

        private String name;
        private int count;

        protected CPT_Component(String name, int count) {
            this.name = name;
            this.count = count;
        }

        @Override
        public int compareTo(CPT_Component o) {
            return this.count - o.count;
        }
        @Override
        public String toString() {
            return this.name + ": " + this.count;
        }
    }

    public static Map<Integer, Patient> patientMap(String patientFilePath) {


        return null;
    }

    public static void obs2pheno(String obsPath) {


    }

    public static Map<Integer, Patient> parsePatientFile(String path) {
        Map<Integer, Patient> patientList = new LinkedHashMap<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))){
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("\"patient_num\"")) {
                    PatientDimension patientDimension = new PatientDimensionImpl(line);
                    Patient patient = TransformToFhir.getPatient(line);
                    if (patientList.containsKey(patientDimension.patient_num())){
                        logger.error("duplicate patient num: " + patientDimension.patient_num());
                    }
                    patientList.putIfAbsent(patientDimension.patient_num(), patient);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return patientList;
    }

    /**
     * Convert a LOINC record to HPO
     * @param record
     * @param writer
     * @param annotationMap
     * @param loincEntryMap
     * @throws IOException
     */
    public static void convertObservation(ObservationFact record, BufferedWriter writer, Map<LoincId, LOINC2HpoAnnotationImpl> annotationMap, Map<LoincId, LoincEntry> loincEntryMap) throws IOException {

        LoincId loincId = null;
        try {
            loincId = new LoincId(record.concept_cd().split(":")[1]);
        } catch (MalformedLoincCodeException e) {
            logger.error("malformed loinc id: " + record.concept_cd());
            return;
        } catch (Exception e) {
            logger.error("concept_cd formation error: " + record.concept_cd());
            return;
        }

        writer.write(record.encounter_num() + "\t" +
                        record.patient_num() + "\t" +
                        record.concept_cd() + "\t" +
                        record.provider_id() + "\t" +
                        dateFormat.format(record.start_date()) + "\t" +
                        record.modifier_cd() + "\t");

        LOINC2HpoAnnotationImpl annotation = annotationMap.get(loincId);
        if (annotation == null) {
            writer.write("\t\t"+ true + "\t" + "LOINC NOT Annotated\n");
            return;
        }
        //String valueType = record.valtype_cd();
        Code interpretation = new Code();
        interpretation.setSystem(Loinc2HPOCodedValue.CODESYSTEM);
        char interpretChar = record.valueflag_cd();
        switch (interpretChar) {
            case Character.MIN_VALUE:
                if (loincEntryMap.get(loincId).isPresentOrd()) {
                    interpretation.setCode("Neg");
                } else if (loincEntryMap.get(loincId).getScale().toLowerCase().equals("qn")) {
                    interpretation.setCode("N");
                } else {
                    writer.write("\t\t"+ true + "\t" + "Annotation Incomplete\n");
                    return;
                }
                break;
            case 'L':
                interpretation.setCode("L");
                break;
            case 'H':
                interpretation.setCode("H");
                break;
            case 'A':
                if (loincEntryMap.get(loincId).isPresentOrd()) {
                    interpretation.setCode("Pos");
                } else if (loincEntryMap.get(loincId).getScale().toLowerCase().equals("qn")) {
                    interpretation.setCode("A");
                }
                default:
                    writer.write("\t\t"+ true + "\t" + "Annotation Incomplete\n");
                    return;
        }

        HpoTerm4TestOutcome outcome = annotation.loincInterpretationToHPO(interpretation);
        if (outcome == null) {
            writer.write("\t\t"+ true + "\t" + "Annotation Incomplete\n");
        } else {
            writer.write(outcome.isNegated() + "\t" + outcome.getHpoTerm().getName() + "\t" + false +  "\t\n");
        }
    }

    public Set<String> prednisonCodeSet(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        return reader.lines().collect(Collectors.toSet());
    }

    /**
     * Check whether a record indicates a patient is prescribed with predisone; if so, increment the count for the patient by one.
     * @param observationFact
     * @param prednisonSet
     * @param patientPredCounts
     */
    public static void predisonPatient(ObservationFact observationFact, Set<String> prednisonSet, Map<Integer, Integer> patientPredCounts) {

        String mdctnCode = observationFact.concept_cd().trim().toLowerCase();
        if (mdctnCode.startsWith("mdctn:")) {
            mdctnCode = mdctnCode.substring(6, mdctnCode.length());
        }
        if (mdctnCode.endsWith("|ADS") || mdctnCode.endsWith("|ads")) {
            mdctnCode = mdctnCode.substring(0, mdctnCode.length() - 4);
        }

        int patientNum = observationFact.patient_num();
 //System.out.println("looking for: " + mdctnCode);
        if (prednisonSet.contains(mdctnCode) && patientPredCounts.containsKey(patientNum)){
            patientPredCounts.put(patientNum, patientPredCounts.get(patientNum) + 1);
        } else if (prednisonSet.contains(mdctnCode) && !patientPredCounts.containsKey(patientNum)) {
            patientPredCounts.put(patientNum, 1);
        } else {
            //
        }

        /**
        //Map<Integer, Integer> sortedMap = new LinkedHashMap<>();
        patientPredCounts.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(e -> {
                    try {
                        writer.write(e.getKey() + "\t" + e.getValue() + "\n");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                });
                //.forEachOrdered(e -> sortedMap.putIfAbsent(e.getKey(), e.getValue()));
         **/
    }

    static void loinc2hpo(String observationPath, String outputPath) throws IOException {

        //open a writer to output file
        BufferedWriter writer;
        if (outputPath == null) {
            writer = new BufferedWriter(new OutputStreamWriter(System.out));
        } else {
            writer = new BufferedWriter(new FileWriter(outputPath));
        }
        writer.write("encounter_num \tpatient_num\tconcept_cd\tprovider_id\tstart_date\tmodifier_cd\tisNegated\thpoTerm\tmapFailed\terror\n");
        //open a reader to input file
        BufferedReader reader = new BufferedReader(new FileReader(observationPath));
        int count = 0;
        String recordLine;
        while ((recordLine = reader.readLine()) != null) {
            if (recordLine.startsWith("\"encounter_num\"")) { //skip header
                continue;
            }
            ObservationFact observationFact = null;
            try {
                observationFact = new ObservationFactLazyImpl(recordLine);
                if (!observationFact.concept_cd().toLowerCase().startsWith("loinc")) {
                    continue;
                }
                convertObservation(observationFact, writer, annotationMap, loincIdLoincEntryMap);
                count++;
                //logger.trace("count: " + count);
                if (count % 1000 == 0) {
                    logger.trace("Loinc lines already tried: " + count);
                }
            } catch (MalformedLineException e) {
                logger.error("Observation line is not parsed correctly: " + recordLine);
                continue;
            } catch (IllegalDataTypeException e) {
                logger.error("illegal data types encountered: " + recordLine);
                continue;
            }

        }

        writer.close();
        reader.close();

        System.out.println("Loinc lines tried to transform: " + count);
    }

    static void loadObser(String inputPath, String databasePath) {
        try {
            SQLiteJDBCDriverConnection connection = new SQLiteJDBCDriverConnection("jdbc:sqlite:" + databasePath);
            TableImporter loader = new LoadObservation(connection.connect(), inputPath);
            loader.load();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void importHPO(String path) throws IOException, PhenolException {
//        HpOboParser hpoOboParser = new HpOboParser(new File(path));
//        HpoOntology hpo = hpoOboParser.parse();
//
//        ImmutableMap.Builder<String,Term> termmap = new ImmutableMap.Builder<>();
//        ImmutableMap.Builder<TermId, Term> termMap2 = new ImmutableMap.Builder<>();
//        if (hpo !=null) {
//            List<Term> res = hpo.getTermMap().values().stream().distinct()
//                    .collect(Collectors.toList());
//            res.forEach( term -> {
//                termmap.put(term.getName(),term);
//                termMap2.put(term.getId(), term);
//            });
//        }
//        hpoTermMap = termmap.build();
//        hpoTermMap2 = termMap2.build();
        HpoOntologyParser parser = new HpoOntologyParser(path);
        parser.parseOntology();
        hpo = parser.getOntology();
        hpoTermMap = parser.getTermMap();
        hpoTermMap2 = parser.getTermMap2();

    }

    static void importLoinc(String loincCoreTablePath) {
        loincIdLoincEntryMap = LoincEntry.getLoincEntryList(loincCoreTablePath);
    }

    static void importAnnotation(String loinc2hpoAnnotationPath) throws Exception {
        annotationMap = LoincAnnotationSerializationFactory.parseFromFile(loinc2hpoAnnotationPath, hpoTermMap2, LoincAnnotationSerializationFactory.SerializationFormat.TSVSingleFile);
    }

    public static Set<String> importPrednisoneCodes() throws FileNotFoundException {
        Set<String> predCodes;
        BufferedReader reader = new BufferedReader(new InputStreamReader(App.class.getClassLoader().getResourceAsStream("prednison.txt")));
        predCodes = reader.lines().collect(Collectors.toSet());
        return predCodes;
    }

    static void prednisoneCounts(String obserPath, String outputPath) throws IOException {
        Writer writer;
        if (outputPath == null) {
            writer = new BufferedWriter(new OutputStreamWriter(System.out));
        } else {
            writer = new BufferedWriter(new FileWriter(outputPath));
        }
        writer.write("patient_num\tprscbCount\n");

        Map<Integer, Integer> patientPredCounts = new HashMap<>();
        Set<String> predCodes = importPrednisoneCodes();
        BufferedReader reader = new BufferedReader(new FileReader(obserPath));
        String line = reader.readLine(); //skip header
        int count = 0;
        while ((line = reader.readLine()) != null) {
            count++;
            ObservationFact observationFact = null;
            try {
                observationFact = new ObservationFactLazyImpl(line);
            } catch (IllegalDataTypeException e) {
                logger.error("Illegal data type encounted: " + line);
                continue;
            } catch (MalformedLineException e) {
                logger.info("Malformed line: " + line);
                continue;
            }
            if (!observationFact.concept_cd().startsWith("M")) {
                continue;
            }
            predisonPatient(observationFact, predCodes, patientPredCounts);
            if (count % 10000 == 0) {
                System.out.println("# lines processed: " + count);
            }
        }

        patientPredCounts.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue((a, b) -> b - a)) //reverse order by prescribe times
                .forEachOrdered(e -> {
                    try {
                        writer.write(e.getKey() + "\t" + e.getValue() + "\n");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                });

        writer.close();
        reader.close();
    }

    static void annotationStats(Map<LoincId, LOINC2HpoAnnotationImpl> annotationMap, String outputPath) throws IOException {

        BufferedWriter writer;
        if (outputPath == null) {
            writer = new BufferedWriter(new OutputStreamWriter(System.out));
        } else {
            writer = new BufferedWriter(new FileWriter(outputPath));
        }

        Map<Term, ArrayList<LoincId>> termLOINCcounts = new HashMap<>();
        annotationMap.values().forEach(loinc2HpoAnnotation -> {
            loinc2HpoAnnotation
                    .getCandidateHpoTerms()
                    .values()
                    .stream()
                    .distinct()
                    .map(HpoTerm4TestOutcome::getHpoTerm)
                    .distinct()
                    .forEach(hpo -> {
                        if (! termLOINCcounts.containsKey(hpo)) {
                            termLOINCcounts.put(hpo, new ArrayList<>());
                        }
                        termLOINCcounts.get(hpo).add(loinc2HpoAnnotation.getLoincId());
                    });
        });

        writer.write("hpo\tloincCounts\n");

        termLOINCcounts.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e-> e.getValue().size()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Term, Integer>comparingByValue().reversed())
                .forEachOrdered(s -> {
                    try {
                        writer.write(s.getKey().getName() + "\t" + s.getValue() + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


        /**
        //write out LOINC lists mapped to one HPO term
        termLOINCcounts.entrySet()
                .stream()
                .forEach(e -> {
                    try{
                        writer.write(e.getKey().getName());
                        writer.write("\t");
                        e.getValue().forEach(p -> {
                            try {
                              writer.write(p.toString() + "|");
                            } catch (Exception excep) {
                                System.out.println("output error");
                            }
                        });
                        writer.write("\n");
                    } catch (Exception excep) {
                        System.out.println("output error");
                    }

                });
**/
        writer.close();
    }

    /**
     * Count the number of ICD codes
     * @param observationPath
     * @param outputPath
     * @throws Exception
     */
    public static void ICDcounts(String observationPath, String outputPath) throws Exception{
        //final String observationPath = "/Users/zhangx/Documents/HUSH+_UNC_JAX/Hush+UNC_JAX/HUSH+/OBSERVATION_FACT.txt";

        BufferedReader reader = new BufferedReader(new FileReader(observationPath));
        String line;
        ObservationFact observationFact;
        String icdcode;
        String concept;

        //for each ICD concept, count how many times they were used in the entire dataset
        //i.e. if a patient is diagnosed multiple times in different hospital visits, the count will be incremented each time.
        Map<String, Integer> icd_9_total = new HashMap<>();
        Map<String, Integer> icd_10_total = new HashMap<>();

        //for each ICD concept, get all the patients diagnosed with them
        Map<String, Set<Integer>> icd_9_patients = new HashMap<>();
        Map<String, Set<Integer>> icd_10_patients = new HashMap<>();

        line = reader.readLine();//skip header
        while((line = reader.readLine()) != null) {
            observationFact = new ObservationFactLazyImpl(line);
            concept = observationFact.concept_cd();
            if (concept.startsWith("ICD9:")) {
                icdcode = concept.split(":")[1].split("\\.")[0];
                icd_9_total.putIfAbsent(icdcode, 0);
                icd_9_total.put(icdcode, icd_9_total.get(icdcode) + 1);

                icd_9_patients.putIfAbsent(icdcode, new HashSet<>());
                icd_9_patients.get(icdcode).add(observationFact.patient_num());
            } else if (concept.startsWith("ICD10:")) {
                icdcode = concept.split(":")[1].split("\\.")[0];
                icd_10_total.putIfAbsent(icdcode, 0);
                icd_10_total.put(icdcode, icd_10_total.get(icdcode) + 1);

                icd_10_patients.putIfAbsent(icdcode, new HashSet<>());
                icd_10_patients.get(icdcode).add(observationFact.patient_num());
            } else {
                //ignore
            }
        }

        BufferedWriter writer;
        if (outputPath == null) {
            writer = new BufferedWriter(new OutputStreamWriter(System.out));
        } else {
            writer = new BufferedWriter(new FileWriter(outputPath));
        }

        writer.write("ICD\tcode\toccurrence\tno.patients\n");
        icd_9_total.entrySet().stream().sorted(Map.Entry.comparingByValue((a, b) -> b - a))
                .forEachOrdered(e -> {
                    try {
                        writer.write("ICD9" + "\t" + e.getKey() + "\t" + e.getValue() + "\t" + icd_9_patients.get(e.getKey()).size());
                        writer.write("\n");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                });

        icd_10_total.entrySet().stream().sorted(Map.Entry.comparingByValue((a, b) -> b - a))
                .forEachOrdered(e -> {
                    try {
                        writer.write("ICD10" + "\t" + e.getKey() + "\t" + e.getValue() + "\t" + icd_10_patients.get(e.getKey()).size());
                        writer.write("\n");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                });

//        icd_9_total.entrySet().stream().sorted(Map.Entry.comparingByValue((a, b) -> b - a))
//                .limit(50)
//                .forEachOrdered(e -> System.out.println(e.getKey() + "\t" + e.getValue()));
//
//        icd_10_total.entrySet().stream().sorted(Map.Entry.comparingByValue((a, b) -> b - a))
//                .limit(50)
//                .forEachOrdered(e -> System.out.println(e.getKey() + "\t" + e.getValue()));
//
//        icd_9_patients.entrySet().stream().sorted(Map.Entry.comparingByValue((a,b) -> b.size() - a.size()))
//                .limit(50)
//                .forEachOrdered(e-> System.out.println(e.getKey() + "\t" + e.getValue().size()));
//
//        icd_10_patients.entrySet().stream().sorted(Map.Entry.comparingByValue((a,b) -> b.size() - a.size()))
//                .limit(50)
//                .forEachOrdered(e-> System.out.println(e.getKey() + "\t" + e.getValue().size()));
    }

    static void infer(String input, String output, String hpoPath) throws IOException, PhenolException {
        System.out.println("enter function for infer");
        BufferedWriter writer;
        if (output == null) {
            writer = new BufferedWriter(new OutputStreamWriter(System.out));
        } else {
            writer = new BufferedWriter(new FileWriter(output));
        }

        Map<String, Term> termMapForR = termMap4R(hpoPath);
        Map<String, BagOfTermsWithFrequencies> patientsModels = parseOriginalPatientTerms(input, termMapForR);

        patientsModels.values().forEach(p -> p.infer());

        writer.write("patient_num\thpoTerm\thpoTermFreq\n");
        patientsModels.values().stream().forEach(p ->
                p.getInferredTermCounts().entrySet().forEach(entry -> {
                    try {
                        writer.write(p.getPatientId());
                        writer.write("\t");
                        writer.write(hpoTermMap2.get(entry.getKey()).getName());
                        writer.write("\t");
                        writer.write(entry.getValue().toString());
                        writer.write("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
        //System.out.println("total number of patients: " + patientsModels.size());
        //System.out.println(patientsModels.get("162684198").getInferredTermCounts());
        writer.close();
    }

    private static Map<String, BagOfTermsWithFrequencies> parseOriginalPatientTerms(String path, Map<String, Term> termMapForR) throws IOException {

        Map<String, BagOfTermsWithFrequencies> patientModels = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = reader.readLine(); //skip header
        String patientid;
        boolean isNegated;
        String term;
        int frequency;
        while ((line = reader.readLine()) != null) {
            String[] elements = line.split(",");
            if (!termMapForR.containsKey(elements[2])) {
                System.out.println("illegal hpo term found: " + elements[2]);
                System.out.println("check whether hpo is up-to-date; check whether R forms an unusual name");
                continue;
            }
            if (!elements[3].matches("\\d+")) {
                System.out.println("invalid term frequency: " + line);
                continue;
            }
            patientid = elements[0];
            isNegated = Boolean.valueOf(elements[1]);
            term = elements[2];
            frequency = Integer.parseInt(elements[3]);

            if (isNegated) {
                continue;
            }
 //System.out.println(patientid + "\t" + termMapForR.get(term).getId().getIdWithPrefix() + "\t" + frequency);

            patientModels.putIfAbsent(patientid, new BagOfTermsWithFrequencies(patientid, hpo));
            patientModels.get(patientid).addTerm(termMapForR.get(term).getId(), frequency);
        }

        return patientModels;
    }

    private static Map<String, Term> termMap4R(String hpoPath) throws IOException, PhenolException {
//        HpoOntologyParser parser = new HpoOntologyParser(hpoPath);
//        parser.parseOntology();
//        //Map<String, Term> termMap = parser.getTermMap();
        Map<String, Term> termMapForR = new HashMap<>(); //
        if (hpoTermMap == null) {
            importHPO(hpoPath);
        }
        hpoTermMap.entrySet().forEach(e -> termMapForR.putIfAbsent(e.getKey().replaceAll("\\W", "\\."), e.getValue()));
        return termMapForR;
    }


    public static void main( String[] args ) {

        Options options = new Options();
        Option loadObservation = Option.builder("l")
                .longOpt("loadObser")
                .hasArg(false)
                .desc("to load the OBSERVATION_FACT table").build();
        Option convertObservation = Option.builder("l2h")
                .longOpt("loinc2hpo")
                .argName("File")
                .hasArg(false)
                .desc("to convert LOINC records to HPO").build();
        Option prednisone = Option.builder("p")
                .longOpt("prednisone")
                .hasArg(false)
                .desc("to get prednisone prescribe times per patient")
                .build();
        Option in = Option.builder("i")
                .longOpt("input")
                .hasArg()
                .argName("File")
                .desc("file to consume")
                .build();
        Option database = Option.builder("d")
                .longOpt("database")
                .hasArg()
                .argName("File")
                .desc("path to sqlite database")
                .build();
        Option output = Option.builder("o")
                .longOpt("output")
                .optionalArg(true)
                .hasArg()
                .argName("File")
                .desc("output path")
                .build();
        Option help = Option.builder("help")
                .hasArg(false)
                .desc("usage information")
                .build();
        Option annotation = Option.builder("a")
                .hasArg()
                .argName("File")
                .longOpt("annotation")
                .desc("LOINC annotation file path")
                .build();
        Option annotationStat = Option.builder("stat")
                .hasArg(false)
                .desc("stats of annotation file")
                .build();
        Option hpo = Option.builder("hpo")
                .hasArg()
                .argName("File")
                .desc("hpo.obo file path")
                .build();
        Option loinc = Option.builder("loinc")
                .hasArg()
                .argName("File")
                .desc("LOINC core table path")
                .build();
        Option icd = Option.builder("icd")
                .hasArg(false)
                .desc("icd statistics")
                .build();

        Option infer = Option.builder("infer")
                .hasArg(false)
                .desc("infer with HPO hierarchy")
                .build();

        options.addOption(loadObservation)
                .addOption(convertObservation)
                .addOption(prednisone)
                .addOption(in)
                .addOption(database)
                .addOption(output)
                .addOption(help)
                .addOption(annotation)
                .addOption(annotationStat)
                .addOption(hpo)
                .addOption(loinc)
                .addOption(icd)
                .addOption(infer);

        HelpFormatter formatter = new HelpFormatter();

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);

            //print out all the arguments in command line
            //Arrays.stream(commandLine.getOptions()).map(option -> option.getOpt() + "\t" + option.getValue() + "\n").forEach(System.out::print);

            //load OBSERVATION_FACT to database
            if (commandLine.hasOption("loadObser")) {
                //System.out.println("request to load observation");
                if (!commandLine.hasOption("i") || commandLine.hasOption("d")){
                    formatter.printHelp("Hush2Fhir", options);
                }
                loadObser(commandLine.getOptionValue("i"), commandLine.getOptionValue("d"));
            }

            //convert LOINC records to HPO terms
            if (commandLine.hasOption("l2h")) {
                //System.out.println("request to convert observation");
                //require input (OBSERVATION_FACT), output, annotation file, hpo and LOINC
                if (!commandLine.hasOption("i") || !commandLine.hasOption("a") || !commandLine.hasOption("hpo")) {
                    formatter.printHelp("HushToFhir", options);
                    return;
                }

                try {
                    importHPO(commandLine.getOptionValue("hpo"));
                } catch (IOException e) {
                    System.out.println("cannot import hpo");
                } catch (PhenolException e) {
                    e.printStackTrace();
                }

                try {
                    importLoinc(commandLine.getOptionValue("loinc"));
                } catch (Exception e) {
                    System.out.println("cannot import LOINC core table");
                }

                try{
                    importAnnotation(commandLine.getOptionValue("a"));
                } catch (Exception e) {
                    System.out.println("cannot import annotation file");
                }

                try {
                    loinc2hpo(commandLine.getOptionValue("i"), commandLine.getOptionValue("o"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (commandLine.hasOption("p")) {
                if (!commandLine.hasOption("i")) {
                    System.out.println("OBSERVATION_FACT table is required");
                }
                try {
                    prednisoneCounts(commandLine.getOptionValue("i"), commandLine.getOptionValue("o"));
                } catch (IOException e) {
                    System.out.println("cannot import OBSERVATION_FACT table");
                }
            }

            if (commandLine.hasOption("stat")) {
                if (!commandLine.hasOption("a")) {
                    System.out.println("annotation file is required");
                }
                if (!commandLine.hasOption("hpo")) {
                    System.out.println("hpo.obo file is required");
                }
                try {
                    importHPO(commandLine.getOptionValue("hpo"));
                } catch (IOException e) {
                    System.out.println("cannot import hpo");
                    return;
                } catch (PhenolException e) {
                    e.printStackTrace();
                }
                try{
                    importAnnotation(commandLine.getOptionValue("a"));
                } catch (Exception e) {
                    System.out.println("cannot import annotation file");
                    return;
                }
                try {
                    annotationStats(annotationMap, commandLine.getOptionValue("o"));
                } catch (IOException e) {
                    System.out.println("failed to output stats, try again");
                    return;
                }
            }

            if (commandLine.hasOption("icd")) {
                try {
                    ICDcounts(commandLine.getOptionValue("i"), commandLine.getOptionValue("o"));
                } catch (Exception e) {
                    System.err.println("cannot produce ICD statistics");
                }
            }

            //print out help message
            if (commandLine.hasOption("help")) {
                formatter.printHelp("HushToFhir", options);
            }

            if (!commandLine.getArgList().isEmpty()) {
                System.out.println("Unrecognized arguments:");
                commandLine.getArgList().forEach(System.out::println);
            }

            if (commandLine.hasOption("infer")) {
                if (!commandLine.hasOption("i")) {
                    System.out.println("file for patient HPO terms (and counts) is required");
                    return;
                }
                if (!commandLine.hasOption("hpo")) {
                    System.out.println("hpo file path is required");
                    return;
                }

                try {
                    infer(commandLine.getOptionValue("i"), commandLine.getOptionValue("o"), commandLine.getOptionValue("hpo"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (PhenolException e) {
                    e.printStackTrace();
                }
            }

        } catch (org.apache.commons.cli.ParseException e) {
            System.out.println("arguments error");
            formatter.printHelp("HushToFhir", options);
        }

    }
}
