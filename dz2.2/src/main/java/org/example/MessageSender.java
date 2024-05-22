import ru.pflb.mq.dummy.exception.DummyException;
import ru.pflb.mq.dummy.implementation.ConnectionImpl;
import ru.pflb.mq.dummy.interfaces.Connection;
import ru.pflb.mq.dummy.interfaces.Destination;
import ru.pflb.mq.dummy.interfaces.Producer;
import ru.pflb.mq.dummy.interfaces.Session;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MessageSender {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: -jar target/dz2.2-1.0-SNAPSHOT-jar-with-dependencies.jar messages.dat");
            return;
        }

        String filePath = args[0];  // Путь к файлу передается как аргумент

        while (true) {
            try (Connection connection = new ConnectionImpl()) {
                Session session = connection.createSession(true);
                Destination destination = session.createDestination("test");
                Producer producer = session.createProducer(destination);

                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                String line;
                while (true) {
                    while ((line = reader.readLine()) != null) {
                        producer.send(line);
                        System.out.println("Отправлено сообщение: " + line);
                        Thread.sleep(2000); // Ждем 2 секунды перед отправкой следующего сообщения
                    }
                    reader.close();
                    reader = new BufferedReader(new FileReader(filePath)); // Сбрасываем указатель в начало файла
                }
            } catch (IOException | DummyException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
