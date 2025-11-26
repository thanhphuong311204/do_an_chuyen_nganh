package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Setting;
import com.appQLCT.AppQLCT.repository.core.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    public Setting getSetting(User user) {
        return settingRepository.findByUser(user)
                .orElseGet(() -> settingRepository.save(
                        Setting.builder()
                                .user(user)
                                .language("vi")
                                .currency("VND")
                                .darkMode(false)
                                .notificationEnabled(true)
                                .build()
                ));
    }

    public Setting updateSetting(User user, Setting newSetting) {
        Setting setting = getSetting(user);
        setting.setLanguage(newSetting.getLanguage());
        setting.setCurrency(newSetting.getCurrency());
        setting.setDarkMode(newSetting.getDarkMode());
        setting.setNotificationEnabled(newSetting.getNotificationEnabled());
        return settingRepository.save(setting);
    }
}
