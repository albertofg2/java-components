/**
 * This class is part of the Programming the Internet of Things
 * project, and is available via the MIT License, which can be
 * found in the LICENSE file at the top level of this repository.
 * 
 * You may find it more helpful to your design to adjust the
 * functionality, constants and interfaces (if there are any)
 * provided within in order to meet the needs of your specific
 * Programming the Internet of Things project.
 */ 

 package programmingtheiot.gda.app;

 import java.util.logging.Level;
 import java.util.logging.Logger;
 import programmingtheiot.gda.system.SystemPerformanceManager;
 
 public class GatewayDeviceApp
 {
	 private static final Logger _Logger = Logger.getLogger(GatewayDeviceApp.class.getName());
	 
	 private SystemPerformanceManager sysPerfMgr;  // Agregamos la referencia a SystemPerformanceManager
	 
	 public static final long DEFAULT_TEST_RUNTIME = 60000L;
	 
	 public GatewayDeviceApp(String[] args)
	 {
		 super();
		 _Logger.info("Initializing GDA...");
		 
		 parseArgs(args);
		 
		 // Inicializamos el SystemPerformanceManager
		 this.sysPerfMgr = new SystemPerformanceManager();
	 }
	 
	 public static void main(String[] args)
	 {
		 GatewayDeviceApp gwApp = new GatewayDeviceApp(args);
		 
		 gwApp.startApp();
		 
		 try {
			 Thread.sleep(DEFAULT_TEST_RUNTIME);
		 } catch (InterruptedException e) {
			 // ignore
		 }
		 
		 gwApp.stopApp(0);
	 }
	 
	 public void startApp()
	 {
		 _Logger.info("Starting GDA...");
		 
		 try {
			 // Llamamos a startManager() para iniciar el manager de rendimiento
			 sysPerfMgr.startManager();
			 
			 _Logger.info("GDA started successfully.");
		 } catch (Exception e) {
			 _Logger.log(Level.SEVERE, "Failed to start GDA. Exiting.", e);
			 
			 stopApp(-1);
		 }
	 }
	 
	 public void stopApp(int code)
	 {
		 _Logger.info("Stopping GDA...");
		 
		 try {
			 // Llamamos a stopManager() para detener el manager de rendimiento
			 sysPerfMgr.stopManager();
			 
			 _Logger.log(Level.INFO, "GDA stopped successfully with exit code {0}.", code);
		 } catch (Exception e) {
			 _Logger.log(Level.SEVERE, "Failed to cleanly stop GDA. Exiting.", e);
		 }
		 
		 System.exit(code);
	 }
	
	// Métodos privados
	 private void parseArgs(String[] args)
	 {
		 String configFile = null;
		 
		 if (args != null) {
			 _Logger.log(Level.INFO, "Parsing {0} command line args.", args.length);
			 
			 for (String arg : args) {
				 if (arg != null) {
					 arg = arg.trim();
					 
					 // Agregar lógica de argumentos si es necesario
				 }
			 }
		 } else {
			 _Logger.info("No command line args to parse.");
		 }
		 
		 initConfig(configFile);
	 }
 
	 private void initConfig(String configFile)
	 {
		 _Logger.log(Level.INFO, "Attempting to load configuration: {0}", (configFile != null ? configFile : "Default."));
		 
		 // Aquí irá la carga de la configuración si fuera necesario
	 }
 }
 