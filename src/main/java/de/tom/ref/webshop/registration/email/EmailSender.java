package de.tom.ref.webshop.registration.email;

public interface EmailSender {
    public void send(String to, String subject, String text);
}
