package Job;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Coupon.Coupon;
import Coupon.CouponsDAO;
import Coupon.CouponsDBDAO;


public class CouponExpirationDailyJob implements Runnable {

    private CouponsDAO couponsDAO;
    boolean StillRunning = true;

    public CouponExpirationDailyJob(){
    this.couponsDAO = new CouponsDBDAO();
    }

    @Override
    public void run() {
    while(StillRunning){
        System.out.println("+++++++++++++++++++++++++++++++++");
        List<Coupon> expired = couponsDAO.getAllCoupons(); new HashMap<>();
        for (Coupon coupon : expired) {
            if(coupon.getEnd_Date().isBefore(LocalDate.now())){
                int id = coupon.getId();
                couponsDAO.deleteCoupon(id);
            }
        }
        try {
            System.out.println("TRY!$@!$");
            TimeUnit.SECONDS.sleep(10);
           // TimeUnit.DAYS.sleep(1);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

    }        
    }
    
    public void stop(){
        StillRunning = false;
    }

}
