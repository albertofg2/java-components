/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

 package programmingtheiot.gda.system;

 import java.util.concurrent.Executors;
 import java.util.concurrent.ScheduledExecutorService;
 import java.util.concurrent.TimeUnit;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 
 import programmingtheiot.common.ConfigConst;
 import programmingtheiot.common.ConfigUtil;
 import programmingtheiot.common.IDataMessageListener;
 import programmingtheiot.data.SystemPerformanceData;
 import programmingtheiot.cda.system.SystemCpuUtilTask;
 import programmingtheiot.cda.system.SystemMemUtilTask;
 
 /**
  * SystemPerformanceManager class to handle system performance.
  */
 public class SystemPerformanceManager
 {
	 // Logger for this class
	 private static final Logger _Logger = Logger.getLogger(SystemPerformanceManager.class.getName());
	 
	 // Private variables
	 private int pollRate = ConfigConst.DEFAULT_POLL_CYCLES;
	 private IDataMessageListener dataMsgListener = null;
	 
	 private ScheduledExecutorService scheduler;
	 private SystemCpuUtilTask cpuUtilTask;
	 private SystemMemUtilTask memUtilTask;
 
	 // Constructor
	 public SystemPerformanceManager()
	 {
		 // Load configuration
		 ConfigUtil configUtil = ConfigUtil.getInstance();
		 this.pollRate = configUtil.getInteger(
				 ConfigConst.GATEWAY_DEVICE,
				 ConfigConst.POLL_CYCLES_KEY,
				 ConfigConst.DEFAULT_POLL_CYCLES
		 );
 
		 if (this.pollRate <= 0) {
			 this.pollRate = ConfigConst.DEFAULT_POLL_CYCLES;
		 }
 
		 // Initialize tasks
		 this.cpuUtilTask = new SystemCpuUtilTask();
		 this.memUtilTask = new SystemMemUtilTask();
		 
		 // Initialize the scheduler
		 this.scheduler = Executors.newSingleThreadScheduledExecutor();
		 
		 _Logger.info("SystemPerformanceManager initialized with poll rate: " + this.pollRate);
	 }
 
	 // Method to handle telemetry (CPU and Memory usage)
	 public void handleTelemetry()
	 {
		 // Get CPU and Memory utilization
		 double cpuUtil = cpuUtilTask.getTelemetryValue();
		 double memUtil = memUtilTask.getTelemetryValue();
 
		 // Log the performance data
		 _Logger.info(String.format("System Performance - CPU: %.2f%%, Memory: %.2f%%", cpuUtil, memUtil));
		 
		 // If there is a listener, pass the telemetry data
		 if (dataMsgListener != null) {
			 SystemPerformanceData perfData = new SystemPerformanceData(cpuUtil, memUtil);
			 dataMsgListener.handleSensorMessage(perfData);
		 }
	 }
 
	 // Method to set the data message listener
	 public void setDataMessageListener(IDataMessageListener listener)
	 {
		 this.dataMsgListener = listener;
	 }
 
	 // Method to start the manager and schedule telemetry task
	 public void startManager()
	 {
		 _Logger.info("Starting SystemPerformanceManager...");
		 // Schedule the handleTelemetry method to run at regular intervals
		 scheduler.scheduleAtFixedRate(this::handleTelemetry, 0, pollRate, TimeUnit.SECONDS);
		 _Logger.info("SystemPerformanceManager started successfully.");
	 }
 
	 // Method to stop the manager and shut down the scheduler
	 public void stopManager()
	 {
		 _Logger.info("Stopping SystemPerformanceManager...");
		 try {
			 scheduler.shutdown();
			 if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
				 scheduler.shutdownNow();
			 }
			 _Logger.info("SystemPerformanceManager stopped successfully.");
		 } catch (InterruptedException e) {
			 _Logger.warning("SystemPerformanceManager shutdown interrupted.");
			 scheduler.shutdownNow();
		 }
	 }
 }
 