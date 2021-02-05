package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.MailSender;
import com.softserve.itacademy.service.Reminder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MailReminder implements Reminder {
    private final MailSender mailSender;
    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;

    public MailReminder(MailSender mailSender, MaterialRepository materialRepository, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.materialRepository = materialRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void remind() {
        List<Integer> groupIds = materialRepository.findAllDueDateTimeExpiring();
        log.info("Selected {} groups with expiring date material", groupIds.size());
        if (!groupIds.isEmpty()) {
            List<User> usersByGroupIds = userRepository.findAllByGroupIds(groupIds);
            if (!usersByGroupIds.isEmpty()) {
                usersByGroupIds.forEach(user -> mailSender.send(user.getEmail(), "SoftClass Lection work time expiring",
                        "Hello" + user.getName() + "! Check Your's courses on SoftClass. Looks like Your work time on some lections is expiring."));
            }
        }
    }
}
