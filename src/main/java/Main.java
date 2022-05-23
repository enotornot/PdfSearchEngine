import com.google.gson.GsonBuilder;
import search_util.BooleanSearchEngine;
import search_util.PageEntry;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {

    private static final int PORT = 8989;
    private static final String PATH = "pdfs";
    private static final String WORD_TO_SEARCH = "бизнес";

    public static void main(String[] args) throws IOException {

        BooleanSearchEngine engine = new BooleanSearchEngine(new File(PATH));

        engine.search(WORD_TO_SEARCH)
                .stream()
                .sorted()
                .forEach(System.out::println);


        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(PORT);
                 Socket clientSocket = serverSocket.accept();
                 PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                String word = bufferedReader.readLine();

                List<PageEntry> list = engine.search(word);

                String jsonList = new GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .toJson(list);
                printWriter.println(jsonList);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}