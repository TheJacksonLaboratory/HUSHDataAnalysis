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
import org.monarchinitiative.loinc2hpo.io.LoincAnnotationSerializationFactory;
import org.monarchinitiative.loinc2hpo.loinc.HpoTerm4TestOutcome;
import org.monarchinitiative.loinc2hpo.loinc.LOINC2HpoAnnotationImpl;
import org.monarchinitiative.loinc2hpo.loinc.LoincEntry;
import org.monarchinitiative.loinc2hpo.loinc.LoincId;
import org.monarchinitiative.loinc2hpo.testresult.PhenoSetUnionFind;
import org.monarchinitiative.phenol.formats.hpo.HpoOntology;
import org.monarchinitiative.phenol.io.obo.hpo.HpoOboParser;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.Buffer;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App {

    private final static Logger logger = LoggerFactory.getLogger(App.class);
    final static String WORKING_DIR = System.getProperty("user.dir");

    private static Map<LoincId, LOINC2HpoAnnotationImpl> testmap = new HashMap<>();
    private static Map<String, Term> hpoTermMap;
    private static Map<TermId, Term> hpoTermMap2;
    private static Map<LoincId, LOINC2HpoAnnotationImpl> annotationMap;
    private static PhenoSetUnionFind unionFind;

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

        LOINC2HpoAnnotationImpl annotation = annotationMap.get(loincId);
        if (annotation == null) {
            writer.write(record.patient_num() + "\t" + record.patient_num() + "\t" + record.concept_cd() + "\t" + "Annotation for this LOINC is not found\n");
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
                    writer.write(record.patient_num() + "\t" + record.patient_num() + "\t" + record.concept_cd() + "\t" + "Annotation for this LOINC is incomplete\n");
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
                    writer.write(record.patient_num() + "\t" + record.patient_num() + "\t" + record.concept_cd() + "\t" + "Annotation for this LOINC is incomplete\n");
                    return;
        }


        HpoTerm4TestOutcome outcome = annotation.loincInterpretationToHPO(interpretation);
        if (outcome == null) {
            writer.write(record.patient_num() + "\t" + record.patient_num() + "\t" + record.concept_cd() + "\t" + "Annotation for this LOINC is incomplete\n");
        } else {
            //System.out.println(record.patient_num() + "\t" + record.encounter_num() + "\t" + record.concept_cd() + "\t" + outcome.getHpoTerm().getName());
            writer.write(record.patient_num() + "\t" + record.encounter_num() + "\t" + record.concept_cd() + "\t" + outcome.isNegated() + "\t" + outcome.getHpoTerm().getName() + "\n");
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

    public static void main( String[] args ) {

        Options options = new Options();
        Option loadObservation = Option.builder()
                .argName("loadObser")
                .longOpt("loadObser")
                .hasArg(false)
                .desc("indicate to load the OBSERVATION_FACT table").build();
        Option convertObservation = Option.builder()
                .argName("convertObser")
                .longOpt("convertObser")
                .hasArg(false)
                .desc("indicate to convert the LOINC records to HPO").build();
        Option in = Option.builder()
                .argName("i")
                .longOpt("input")
                .hasArg()
                .desc("file to consume")
                .build();
        Option database = Option.builder()
                .argName("database")
                .longOpt("database")
                .hasArg()
                .desc("path to sqlite database")
                .build();
        options.addOption(loadObservation)
                .addOption(convertObservation)
                .addOption(in)
                .addOption(database);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("loadObser")) {
                System.out.println("request to load observation");
            }
            if (commandLine.hasOption("convertObser")) {
                System.out.println("request to convert observation");
            }
            if (commandLine.hasOption("database")) {
                System.out.println("database connection requested: " + commandLine.getOptionValue("database"));
            }
            if (commandLine.hasOption("i")) {
                System.out.println("input file provided: " + commandLine.getOptionValue("i"));
            }

        } catch (org.apache.commons.cli.ParseException e) {
            System.out.println("arguments error");
        }


        /**
        //codes to load observation into sqlite
        //takes about 6 hours
        try {
            SQLiteJDBCDriverConnection connection = new SQLiteJDBCDriverConnection(args[0]);
            TableImporter loader = new LoadObservation(connection.connect(), args[1]);
            loader.load();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
**/
/**
        //replace with your file path
        final String observationPath = "/Users/zhangx/Documents/HUSH+_UNC_JAX/Hush+UNC_JAX/HUSH+/OBSERVATION_FACT.txt";

        final String observationLoincPath =
                "/Users/zhangx/Documents/HUSH+_UNC_JAX/Hush+UNC_JAX/HUSH+/OBSERVATION_LOINC.txt";

        final String observationSamplePath =
                "/Users/zhangx/Documents/HUSH+_UNC_JAX/Hush+UNC_JAX/HUSH+/sample.csv";
        //max_nums(observationPath);

        //replate with your file path
        final String observation_concept_statistics = "/Users/zhangx/Documents/HUSH+_UNC_JAX/Hush+UNC_JAX/HUSH+/observation_fact_statistics.csv";

        //concept_freq(observation_concept_statistics);
        //cpt_category_count();

        final String patientPath =
                "/Users/zhangx/Documents/HUSH+_UNC_JAX/Hush+UNC_JAX/HUSH+/PATIENT_DIMENTION.csv";
        //Map<Integer, Patient> patientMap = parsePatientFile(patientPath);
        //System.out.println(patientMap.size());
        //TransformToFhir.s
        //patientMap.values().forEach(TransformToFhir.getFhirServer()::upload);

 String hushSqlite = "jdbc:sqlite:/Users/zhangx/Documents/HUSH+_UNC_JAX/HUSH+_UNC_JAX.sqlite";



        Set<String> predCodes;
        try (BufferedReader reader = new BufferedReader(new FileReader(App.class.getClassLoader().getResource("prednison.txt").getPath()))){
            predCodes = reader.lines().collect(Collectors.toSet());
            predCodes.stream().forEach(System.out::println);
 System.out.println("prednison set size: " + predCodes.size());
        } catch (FileNotFoundException e){
            return;
        } catch (IOException e){
            return;
        }

        Map<Integer, Integer> patientPredCounts = new HashMap<>();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("patientPrednisonCount.txt"));
            try (BufferedReader reader = new BufferedReader(new FileReader(observationPath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("\"encounter_num\"")) { //skip header
                        continue;
                    }
                    try {
                        ObservationFact observationFact = new ObservationFactLazyImpl(line);
                        if (!observationFact.concept_cd().startsWith("M")){
                            continue;
                        }
                        predisonPatient(observationFact, predCodes, patientPredCounts);
                    } catch (IllegalDataTypeException e) {
                        //e.printStackTrace();
                    } catch (MalformedLineException e) {
                        //e.printStackTrace();
                    }

                }

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
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        //import HPO
        String hpo_obo = "/Users/zhangx/git/human-phenotype-ontology/src/ontology/hp.obo";
        HpoOboParser hpoOboParser = new HpoOboParser(new File(hpo_obo));
        HpoOntology hpo = null;
        try {
            hpo = hpoOboParser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImmutableMap.Builder<String,Term> termmap = new ImmutableMap.Builder<>();
        ImmutableMap.Builder<TermId, Term> termMap2 = new ImmutableMap.Builder<>();
        if (hpo !=null) {
            List<Term> res = hpo.getTermMap().values().stream().distinct()
                    .collect(Collectors.toList());
            res.forEach( term -> {
                termmap.put(term.getName(),term);
                termMap2.put(term.getId(), term);
            });
        }
        hpoTermMap = termmap.build();
        hpoTermMap2 = termMap2.build();

        String tsvSingleFile = "/Users/zhangx/git/loinc2hpoAnnotation/Data/TSVSingleFile/annotations.tsv";
        try {
            annotationMap = LoincAnnotationSerializationFactory.parseFromFile(tsvSingleFile, hpoTermMap2, LoincAnnotationSerializationFactory.SerializationFormat.TSVSingleFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String loincCoreTablePath = "/Users/zhangx/Downloads/LOINC_2/LoincTableCore.csv";

        Map<LoincId, LoincEntry> loincIdLoincEntryMap = LoincEntry.getLoincEntryList(loincCoreTablePath);

        String outputfile = "observation_to_hpo.txt";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputfile));
        } catch (IOException e) {
            logger.error("cannot open file for writing: " + outputfile );
        }

        int count = 0;
        //int MAXLINE = 10;
        String recordLine = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(observationLoincPath))) {
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
                    if (count % 50 == 0) {
                        logger.trace("Loinc lines already tried: " + count);
                    }
                } catch (MalformedLineException e) {
                    //logger.error("Observation line is not parsed correctly: " + recordLine);
                    continue;
                } catch (IllegalDataTypeException e) {
                    //logger.error("illegal data types encountered: " + recordLine);
                    continue;
                }

            }
        } catch(FileNotFoundException e) {
            logger.error("cannot find file: " + observationPath);
        } catch (IOException e) {
            logger.error("database error to file: " + observationPath);
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loinc lines tried to transform: " + count);
**/
    }
}
