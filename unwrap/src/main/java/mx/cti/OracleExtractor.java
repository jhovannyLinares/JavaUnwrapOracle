package mx.cti;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class OracleExtractor extends LoadConfig {

    public static void main(String[] args) {

        executeExtraction();

        // GenereteJson app = new GenereteJson();
        // app.run();
    }

    public static void executeExtraction() {

        Connection conn = Connect.getConnection();

        try {
            readPlSql(conn);
            System.out.println("OK: Extracción finalizada.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }

    }

    private static void readPlSql(Connection conn) throws SQLException {

        String sql = " SELECT OWNER, OBJECT_NAME, OBJECT_TYPE FROM ALL_OBJECTS " +
        // " WHERE OBJECT_TYPE IN ('JOB','TYPE BODY','PACKAGE
        // BODY','TRIGGER','PACKAGE','PROCEDURE','FUNCTION','TYPE') " +
        // " WHERE OWNER = 'INSIS_GEN_V10' " +
        //" OBJECT_TYPE IN ('JOB','TYPE BODY','TRIGGER','TYPE') " +
                " ORDER BY OWNER, OBJECT_TYPE, OBJECT_NAME ";

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        System.out.println("OK: Extracción de metadatos finalizada.");
        Collection<AllObjects> allObjects = new ArrayList<>();
        AllObjects ao2 = null;
        while (rs.next()) {

            ao2 = new AllObjects();

            ao2.setOwner(rs.getString("OWNER"));
            ao2.setName(rs.getString("OBJECT_NAME"));
            ao2.setType(rs.getString("OBJECT_TYPE"));

            allObjects.add(ao2);
        }

        rs.close();
        System.out.println("OK: traduccion de objetos finalizada.");
        int size = allObjects.size();
        int i = 0;
        int avance = 0;

        System.out.println("OK: inicia tratamiento de datos");
        for (AllObjects ao : allObjects) {

            i++;
            writeObject(conn, ao.getOwner(), ao.getName(), ao.getType());

            if ((avance + 5) <= i * 100 / size) {
                avance = i * 100 / size;
                System.out.println(avance + "% completado");
            }

        }
    }

    private static void writeObject(Connection conn, String owner, String name, String type) {

        //if (type.contains("BODY"))
            //return;

        if (type.contains("SYNONYM"))
            return;

        if (type.contains("SEQUENCE"))
            return;

        if (type.contains("INDEX"))
            return;

        if (type.contains("JAVA"))
            return;
        
        if (type.contains("VIEW"))
            return;


        File file = new File(outputDir + "/" + owner + "/" + type + "/" + name + ".sql");

        if (file.exists()) {
            // System.out.println("📝 Archivo ya existe: " + file.getAbsolutePath());
            return;
        }

        //String ddl = extractDDL(conn, owner, name, type);
        String ddl = extractDDL_X_Line(conn, owner, name, type);

       

        saveToFile(name, type, owner, ddl);

        if (ddl != null && ddl.contains("wrapped")) {
            String unwrapped = unwrapIfNeeded(ddl);
            type = type.concat("_BODY");
            saveToFile(name, type, owner, unwrapped);
        }
    }

    private static String extractDDL_X_Line(Connection conn, String owner, String objectName, String objectType) {
        
        String ddl = "";
        String sql = "SELECT TEXT FROM ALL_SOURCE \r\n" + //
                        "WHERE OWNER = ? \r\n" + //
                        "AND NAME = ? \r\n" + //
                        "AND TYPE = ? \r\n" + //
                        "ORDER BY LINE ASC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, owner);
            stmt.setString(2, objectName);
            stmt.setString(3, objectType);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ddl = ddl.concat(rs.getString("TEXT"));
            }
        } catch (SQLException e) {
            System.err.println("⚠️ No se pudo extraer DDL de " + objectName + ": " + e.getMessage());
            ddl = "No se pudo extraer DDL";
        }
        return ddl;
    }

    private static String extractDDL(Connection conn, String owner, String objectName, String objectType) {

        String ddl = "";
        String sql = "SELECT DBMS_METADATA.GET_DDL(?, ?, ?) AS DDL FROM DUAL";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, objectType);
            stmt.setString(2, objectName);
            stmt.setString(3, owner);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ddl = rs.getString("DDL");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ No se pudo extraer DDL de " + objectName + ": " + e.getMessage());
            ddl = "No se pudo extraer DDL";
        }
        return ddl;
    }

    private static String unwrapIfNeeded(String ddl) {

        if (ddl != null && ddl.contains("wrapped")) {

            try {
                ddl = Unwrap.unwraper(ddl);
            } catch (Exception e) {
                System.err.println("❌ Error al desencriptar DDL: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("🔓 DDL desencriptado: ");
        }
        return ddl;
    }

    private static void saveToFile(String name, String type, String owner, String content) {
        if (content == null || content.trim().isEmpty())
            return;

        String filename = outputDir + "/" + owner + "/" + type + "/" + name + ".sql";

        File ficheroDatos = new File(filename);
        // Crear directorios si no existen
        ficheroDatos.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
            System.out.println("📝 Archivo guardado: " + filename);
        } catch (IOException e) {
            System.err.println("❌ Error al escribir archivo " + filename + " : " + e.getMessage());
        }
    }

}
