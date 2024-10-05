package reception.test;


import reception.model.da.VisitTimeDa;

import java.time.LocalDateTime;

public class VisitTimeTest {
        public static void main(String[] args) throws Exception {

            try (VisitTimeDa visitTimeDa = new VisitTimeDa()) {

                System.out.println(visitTimeDa.findByDoctor("farshad","rezaei"));
                System.out.println(visitTimeDa.findByExpertise("dermatologist"));
                System.out.println(visitTimeDa.findByDateTime(LocalDateTime.of(2023,9,15,16,0,0)));

            }
        }
}

