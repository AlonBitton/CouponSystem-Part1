package Job;

import java.time.LocalDate;
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

    /*
     * Daily job, checks all the coupuns exists in program DB,
     * search for expired Coupuns and delete the.
     * Default sleep time set to 10 seconds for testing.
     */
    @Override
    public void run() {
        while (StillRunning) {
            System.out.println("%%%%%%%%%%%%%%%%%%%% Daily Job Running %%%%%%%%%%%%%%%%%%%%");
            List<Coupon> expired = couponsDAO.getAllCoupons();

            for (Coupon coupon : expired) {
                if (coupon.getEnd_Date().isBefore(LocalDate.now())) {
                    int id = coupon.getId();
                    couponsDAO.deleteCoupon(id);
                    System.out.println("=========== Expired Coupons deleted " + id + " ===========");
                }
            }
            try {
                TimeUnit.SECONDS.sleep(10);  /* For testing job time set for 10 seconds. */
                 /* TimeUnit.DAYS.sleep(1); //  uncomment this line after comment line 36 to set
                 thread sleep time for 1 day. */
            } catch (Exception e) {
                e.getMessage();
            }

        }

    }

    /* Stop method for the daily job.
    set the StillRunning boolean to false.
    Close all remaning connections. */
    public void stopJob() {
        StillRunning = false;
        try {
            ConnectionPool.getInstance().closeAllConnections();
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
