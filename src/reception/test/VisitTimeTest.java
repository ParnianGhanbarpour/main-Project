package reception.test;


import reception.model.da.VisitTimeDa;
import reception.model.entity.VisitTime;

public class VisitTimeTest {
        public static void main(String[] args) throws Exception {


            try (VisitTimeDa visitTimeDa = new VisitTimeDa()) {

                System.out.println(visitTimeDa.findByDoctor("farshad","rezaei"));
                System.out.println(visitTimeDa.findByExpertise("dermatologist"));
            }
        }
}

