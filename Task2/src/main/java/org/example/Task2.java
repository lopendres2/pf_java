package org.example;

import ru.pflb.mq.dummy.exception.DummyException;
import ru.pflb.mq.dummy.implementation.ConnectionImpl;
import ru.pflb.mq.dummy.interfaces.Connection;
import ru.pflb.mq.dummy.interfaces.Destination;
import ru.pflb.mq.dummy.interfaces.Producer;
import ru.pflb.mq.dummy.interfaces.Session;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Task2 {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: -jar target/Task2-1.0-SNAPSHOT-jar-with-dependencies.jar messages.dat");
            return;
        }

        String filePath = args[0];  // Путь к файлу передается как аргумент

        try (Connection connection = new ConnectionImpl()) {
            Session session = connection.createSession(true);
            Destination destination = session.createDestination("test");
            Producer producer = session.createProducer(destination);

            try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
                while (true) {
                    file.seek(0);
                    String line;
                    while ((line = file.readLine()) != null) {
                        producer.send(line);
                        System.out.println("Отправлено сообщение: " + line);
                        Thread.sleep(2000);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } catch (DummyException e) {
            throw new RuntimeException(e);
        }
    }
}

