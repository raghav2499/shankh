package com.darzee.shankh.service;

import com.darzee.shankh.repo.DeviceInfoRepo;
import com.darzee.shankh.repo.BoutiqueRepo;
import com.darzee.shankh.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private BoutiqueRepo boutiqueRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private DeviceInfoRepo deviceInfoRepo;

    @Autowired
    private PushNotificationService pushNotificationService;

    public void sendFirstOrderNotifications() {
        List<Object[]> results = boutiqueRepo.findBoutiquesWithZeroOrdersAndDeviceToken();

        for (Object[] result : results) {
            Long boutiqueId = ((Number) result[0]).longValue();
            String boutiqueName = (String) result[1];
            String deviceToken = (String) result[3];

            String message = "Namaste " + boutiqueName + ", create your first order on Darzee App today.";
            sendPushNotification(deviceToken, message);
        }
    }

    private void sendPushNotification(String deviceToken, String message) {
        pushNotificationService.sendPushNotification(deviceToken, message);
    }
}