package org.example;

import ru.pflb.mq.dummy.exception.DummyException;
import ru.pflb.mq.dummy.implementation.ConnectionImpl;
import ru.pflb.mq.dummy.interfaces.Connection;
import ru.pflb.mq.dummy.interfaces.Destination;
import ru.pflb.mq.dummy.interfaces.Producer;
import ru.pflb.mq.dummy.interfaces.Session;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MessageSender {
    public static void main(String[] args) {

        List<String> messages = Arrays.asList("Четыре", "Пять", "Шесть");

        try (Connection connection = new ConnectionImpl()) {
            Session session = connection.createSession(true);
            Destination destination = session.createDestination("test");
            Producer producer = session.createProducer(destination);


            Iterator<String> iterator = messages.iterator();
            while (iterator.hasNext()) {
                String message = iterator.next();
                producer.send(message);
                System.out.println("Отправлено сообщение: " + message);
                // Ждем 2 секунды перед отправкой следующего сообщения
                Thread.sleep(2000);
            }
        } catch (DummyException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

