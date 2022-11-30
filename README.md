# CouponSystemApp
<Alon Bitton>
Jhon Bryce Coupon System Project Phase 1.

     ** To run this Project run MainTest.java ** 
     The entire project is automated, CreateDB with {@link #createDB()} & {@link #DropDB} using ScriptRunner.
     All the Companies, Customers, and Coupons are generated with random values for check proof of the existing methods and purchase limits.
     Default daily job set for 10 seconds for testing purposes.
     To change go to -> {@link #CouponExpirationDailyJob().run() line 38-39 }
     The program will automatically shut down after 25 seconds.