package reception.test;

import reception.model.da.VisitTimeDa;

import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) throws Exception {
        LocalDateTime visitTime1 = LocalDateTime.of(2023,9,15,16,0,0);
        System.out.println(visitTime1);
        System.out.println(visitTime1.plusMinutes(30));

        VisitTimeDa visitTimeDa = new VisitTimeDa();
        System.out.println(visitTimeDa.findValidTime(visitTime1, 15));
    }
}
