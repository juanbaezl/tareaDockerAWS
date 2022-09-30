package co.edu.escuelaing.app;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        staticFiles.location("/public");
        port(getPort());
        post("/log", (req, res) -> {
            res.header("Access-Control-Allow-Origin","*");
            res.type("application/json");
            return doPost(req.queryParams("value"));
        });
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    public static String doPost(String value) {
        String linea = "";
        try {
            String data = "value="+value;
            URL url = new URL("http://logservice1:3500/api/backend?"+data);
            System.out.println(url.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.getOutputStream().write(data.getBytes("UTF-8"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            linea = reader.readLine();
            reader.close();
        } catch (MalformedURLException me) {
            System.err.println("MalformedURLException: " + me);
        } catch (IOException ioe) {
            System.err.println("IOException:  " + ioe);
        }
        return linea;
    }
}
