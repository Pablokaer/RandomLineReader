import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        String filePath = "phrases.txt";
        int intervalInSeconds = 10;
        try {
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);

            if (!lines.isEmpty()) {
                Random random = new Random();
                String lastPhrase = null;

                while (true) {
                    String randomLine;

                    do {
                        randomLine = lines.get(random.nextInt(lines.size()));
                    } while (randomLine.equals(lastPhrase));

                    lastPhrase = randomLine;

                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String formattedDateTime = now.format(formatter);
                    System.out.print("\r" + randomLine + " - Last Update: " + formattedDateTime);

                    Thread.sleep(intervalInSeconds * 1000);
                }
            } else {
                System.out.println("O arquivo está vazio.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("A execução foi interrompida: " + e.getMessage());
        }
    }
}