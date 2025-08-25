package com.klnsdr.axon.shs.mail;

import com.klnsdr.axon.mail.Mail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShsMailStore {
    private static final String NL = "\n";
    @Value("${spring.mail.username}")
    private String from;

    public Mail getConfirmationMailTemplate() {
        final Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject("Teilnahmebestätigung SHS");
        mail.setBody(
                "Hallo <<NAME>>," +
                        NL +
                        "wir freuen uns, dass du dich für unser Projekt „Schüler helfen Schülern“ interessierst und bestätigen dir hier deine erfolgreiche Registrierung." +
                        NL +
                        "Sobald wir eine passende Lernpatenschaft für dich haben, erhältst du eine Mail mit weiteren Infos von uns." +
                        NL +
                        "Wichtig: Wenn du Junior-Teacher bist und mehrere Fächer unterrichten möchtest, registriere dich für jedes weitere Fach separat." +
                        NL +
                        NL +
                        "Wir freuen uns auf dich!" +
                        NL +
                        "Dein ShS-Team"
        );
        return mail;
    }

    public Mail getPairTeacherMailTemplate() {
        final Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject("Neue Lernpatenschaft bei SHS");
        mail.setBody(
                "Hallo <<NAME>>," +
                        NL +
                        "schön, dass du dich bereiterklärt hast, bei „Schüler helfen Schülern“ mitzuarbeiten. Hiermit wird dir die Teilnahme als Junior-Teacher im Projekt bestätigt." +
                        NL +
                        "Du wirst in diesem Schuljahr (<<NAME_STUDENT>>, <<GRADE>>) in <<SUBJECT>> unterstützen." +
                        NL +
                        "Ihr/Sein Kontakt lautet: <<MAIL>>, <<TELEPHONE>>" +
                        NL +
                        "Komm am Dienstag nach den Ferien in der 5. Stunde in die Lerninsel, um deinen Partner oder deine Partnerin kennenzulernen und den Vertrag abzuholen." +
                        NL +
                        "Bei Fragen oder Problemen kannst du dich stets an shs-cjd@gmx.de wenden." +
                        NL +
                        NL +
                        "Viel Freude und Erfolg mit deiner Lernpatenschaft!" +
                        NL +
                        "Dein ShS-Team"
        );
        return mail;
    }

    public Mail getPairStudentMailTemplate() {
        final Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject("Neue Lernpatenschaft bei SHS");
        mail.setBody(
                "Hallo <<NAME>>," +
                        NL +
                        "herzlichen Glückwunsch zu deiner Lernpatenschaft. Hiermit wird dir die Teilnahme im Projekt „Schüler helfen Schülern“ bestätigt." +
                        NL +
                        "Du wirst in diesem Schuljahr von (<<NAME_TEACHER>>, <<GRADE>>) in <<SUBJECT>> unterstützt." +
                        NL +
                        "Ihr/Sein Kontakt lautet: <<MAIL>>, <<TELEPHONE>>" +
                        NL +
                        "Komm am Dienstag nach den Ferien in der 5. Stunde in die Lerninsel, um deinen Junior-Teacher kennenzulernen und den Vertrag abzuholen." +
                        NL +
                        "Bei Fragen oder Problemen kannst du dich stets an shs-cjd@gmx.de wenden." +
                        NL +
                        NL +
                        "Viel Freude und Erfolg mit deiner Lernpatenschaft!" +
                        NL +
                        "Dein ShS-Team"
        );
        return mail;
    }
}
