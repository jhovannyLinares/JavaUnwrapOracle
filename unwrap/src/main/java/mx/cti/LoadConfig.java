package mx.cti;

import java.io.*;
import java.util.Properties;

public class LoadConfig {

    public static String dbUrl;
    public static String dbUser;
    public static String dbPassword;
    public static String outputDir;

    // Profundidad máxima de exploración (default: 5)
    public static int maxDepth = 5;

    // Lista opcional de esquemas a incluir (separados por coma). Si no se
    // especifica, usa solo el schema indicado o el del usuario.
    public static String includeSchemas;

    // Activa salida detallada
    public static boolean verbose = true;

    public static String schema = "INSIS_GEN_V10";

    public static void loadConfig() {

        try (InputStream input = OracleExtractor.class.getClassLoader().getResourceAsStream("config.env")) {
            Properties prop = new Properties();
            prop.load(input);
            dbUrl = prop.getProperty("db.url");
            dbUser = prop.getProperty("db.user");
            dbPassword = prop.getProperty("db.password");
            outputDir = prop.getProperty("output.dir");

            File dir = new File(outputDir);
            if (!dir.exists())
                dir.mkdirs();

        } catch (IOException ex) {
            System.err.println("❌ No se pudo cargar el archivo de configuración: " + ex.getMessage());
            System.exit(1);
        }

    }

}
