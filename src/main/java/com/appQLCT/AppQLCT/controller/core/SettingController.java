package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Setting;
import com.appQLCT.AppQLCT.service.core.SettingService;
import com.appQLCT.AppQLCT.service.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private UserService userService;

    @GetMapping
    public Setting getSettings() {
        User user = userService.getCurrentUser();
        return settingService.getSetting(user);
    }

    @PutMapping
    public Setting updateSettings(@RequestBody Setting req) {
        User user = userService.getCurrentUser();
        return settingService.updateSetting(user, req);
    }
}
