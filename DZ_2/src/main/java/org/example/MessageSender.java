package org.example;

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
        String filePath = "messages.dat";  // Путь к файлу

        try (Connection connection = new ConnectionImpl()) {
            Session session = connection.createSession(true);
            Destination destination = session.createDestination("test");
            Producer producer = session.createProducer(destination);

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    producer.send(line);
                    System.out.println("Отправлено сообщение: " + line);
                    Thread.sleep(2000); // Ждем 2 секунды перед отправкой следующего сообщения
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } catch (DummyException e) {
            throw new RuntimeException(e);
        }
    }
}

