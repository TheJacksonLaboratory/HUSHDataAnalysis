package org.jax;

import org.hl7.fhir.dstu3.model.Coding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.Buffer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App {

    private final static Logger logger = LoggerFactory.getLogger(App.class);

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

     static void concept_freq(String path) {
        //Queue<CPT_Component> priority_queue = new PriorityQueue<>();
        Map<String, Integer> cpt_map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (cpt_map.containsKey(line)) {
                    cpt_map.put(line, cpt_map.get(line) + 1);
                } else {
                    cpt_map.put(line, 1);
                }
            }
            reader.close();
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

         //frequency count histogram
         //top 500, step size 20
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

         try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/accumulated_cpt_count.csv"))) {
             //write out stack to a file for visualization
             writer.write("step,count\n");
             int i = 0;
             for (Integer count : count_hist.stream().collect(Collectors.toList())) {
                 writer.write(String.format("%d,%d\n", i + 20, count));
                 i = i + 20;
             }
             writer.close();
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

    public static void main( String[] args )
    {

        //replace with your file path
        final String observationPath = "/Users/zhangx/Documents/HUSH+_UNC_JAX/Hush+UNC_JAX/HUSH+/OBSERVATION_FACT.txt";
        //max_nums(observationPath);

        //replate with your file path
        final String observation_concept_statistics = "/Users/zhangx/Documents/HUSH+_UNC_JAX/Hush+UNC_JAX/HUSH+/observation_fact_statistics.csv";

        concept_freq(observation_concept_statistics);

    }
}
