package Job;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Coupon.Coupon;
import Coupon.CouponsDAO;
import Coupon.CouponsDBDAO;
import Pool.ConnectionPool;

public class CouponExpirationDailyJob implements Runnable {

    private CouponsDAO couponsDAO;
    boolean StillRunning = true;

    public CouponExpirationDailyJob() {
        this.couponsDAO = new CouponsDBDAO();
    }

    @Override
    public void run() {
        while (StillRunning) {
            System.out.println("%%%%%%%%%%%%%%%%%%%% Daily Job Running %%%%%%%%%%%%%%%%%%%%");
            List<Coupon> expired = couponsDAO.getAllCoupons();
            new HashMap<>();
            for (Coupon coupon : expired) {
                if (coupon.getEnd_Date().isBefore(LocalDate.now())) {
                    int id = coupon.getId();
                    couponsDAO.deleteCoupon(id);
                    System.out.println("=========== Expired Coupons deleted " + id + " ===========");

                }
            }
            try {
                TimeUnit.SECONDS.sleep(10); // For testing un-comment this line.
                // TimeUnit.DAYS.sleep(1); // ^ comment this line after uncomment line 38.
            } catch (InterruptedException e) {
                e.getMessage();
            }

        }
    }

    public void stopJob() {
        StillRunning = false;
        try {
            ConnectionPool.getInstance().closeAllConnections();
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
