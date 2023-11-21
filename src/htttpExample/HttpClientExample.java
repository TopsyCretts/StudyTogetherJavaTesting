package htttpExample;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpClientExample {
    public static String result;

    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) throws Exception {

        HttpClientExample obj = new HttpClientExample();

        try {
            System.out.println("------------------------------------ Sending GET Auth");
            obj.getReqAuth();
            System.out.println("--------------------------------------- Sending POST Auth");
            obj.postReqAuth();
            System.out.println("---------------------------------------Testing Sending GET Main");
            obj.getReqMain();
            System.out.println("---------------------------------------Testing Sending GET Lk");
            obj.getReqLk();
            Thread.sleep(10000);
        } finally {
            obj.close();
        }
    }

    private void close() throws IOException {
        httpClient.close();
    }

    private void getReqAuth() throws Exception {

        HttpGet request = new HttpGet("https://www.ystu.ru/WPROG/auth.php");

        // add request headers
        request.addHeader(HttpHeaders.HOST, "www.ystu.ru");
        request.addHeader(HttpHeaders.CONNECTION, "keep-alive");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                result = EntityUtils.toString(entity);
                System.out.println(result);
            }

        }

    }

    private void postReqAuth() throws Exception {

        HttpPost post = new HttpPost("https://www.ystu.ru/WPROG/auth1.php");
        post.addHeader(HttpHeaders.CONNECTION, "keep-alive");
        post.addHeader(HttpHeaders.LOCATION, "http://www.ystu.ru/WPROG/main.php");
        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("codeYSTU", findId()));
        urlParameters.add(new BasicNameValuePair("login", "borisovgd.22"));
        urlParameters.add(new BasicNameValuePair("password", "Zgwtc,15"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        CloseableHttpResponse response = httpClient.execute(post);
        System.out.println(EntityUtils.toString(response.getEntity()));


    }
    static String findId(){
        String [] resultAr = result.split("codeYSTU\" value=\"");
        String id = resultAr[1].split("\"")[0];
        return id;

    }
    private void getReqMain() throws Exception {

        HttpGet request = new HttpGet("https://www.ystu.ru/WPROG/main.php");

        // add request headers
        request.addHeader(HttpHeaders.HOST, "www.ystu.ru");
        request.addHeader(HttpHeaders.CONNECTION, "keep-alive");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                //result = EntityUtils.toString(entity);
                result = IOUtils.toString(entity.getContent(), "cp1251");
                String res = new String(result.getBytes("UTF-8"));
                BufferedWriter writer = new BufferedWriter(new FileWriter("main"));
                writer.write(res);
                writer.flush();
                System.out.println(result);
            }

        }

    }
    private void getReqLk() throws Exception {

        HttpGet request = new HttpGet("https://www.ystu.ru/WPROG/lk/lkstud.php");

        // add request headers
        request.addHeader(HttpHeaders.HOST, "www.ystu.ru");
        request.addHeader(HttpHeaders.CONNECTION, "keep-alive");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                //result = EntityUtils.toString(entity);
                result = IOUtils.toString(entity.getContent(), "cp1251");
                String res = new String(result.getBytes("UTF-8"));
                BufferedWriter writer = new BufferedWriter(new FileWriter("lk"));
                writer.write(res);
                writer.flush();
                System.out.println(result);
            }

        }

    }
    public static void getData (String path) throws IOException {
        Document document = Jsoup.parse(new File(path));

        Elements element = document.getElementsByTag("title");
        System.out.println(element);


    }
}
