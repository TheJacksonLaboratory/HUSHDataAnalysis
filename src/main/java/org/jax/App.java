package org.jax;

import org.hl7.fhir.dstu3.model.Coding;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {



        final String observationPath = "/Users/zhangx/Documents/HUSH+_UNC_JAX/Hush+UNC_JAX/HUSH+/OBSERVATION_FACT.txt";
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
}
