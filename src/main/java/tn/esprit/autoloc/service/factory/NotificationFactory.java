package tn.esprit.autoloc.service.factory;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.service.observer.EmailNotificationObserver;
import tn.esprit.autoloc.service.observer.IReservationObserver;
import tn.esprit.autoloc.service.observer.SmsNotificationObserver;

@Component
public class NotificationFactory {

    private final EmailNotificationObserver emailNotificationObserver;
    private final SmsNotificationObserver smsNotificationObserver;

    public NotificationFactory(EmailNotificationObserver emailNotificationObserver,
                               SmsNotificationObserver smsNotificationObserver) {
        this.emailNotificationObserver = emailNotificationObserver;
        this.smsNotificationObserver = smsNotificationObserver;
    }

    public IReservationObserver creerObservateur(String canal) {
        if ("EMAIL".equalsIgnoreCase(canal)) {
            return emailNotificationObserver;
        }

        if ("SMS".equalsIgnoreCase(canal)) {
            return smsNotificationObserver;
        }

        throw new IllegalArgumentException("Canal de notification inconnu : " + canal);
    }
}