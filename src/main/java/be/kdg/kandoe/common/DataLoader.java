package be.kdg.kandoe.common;

import be.kdg.kandoe.domain.user.Gender;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.service.implementation.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private CustomUserDetailsService userService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.userService.addUser(new User("sven", "matthys", "sveneman", "matthys.sven@gmail.com", 6, 3, 1997, "testtest", Gender.Male, null));
        this.userService.addUser(new User("beau", "boeynaems", "beauke", "beau@gmail.com", 1, 2, 1996, "lollollol", Gender.Male, null));
        this.userService.addUser(new User("gino", "moukhi", "ginothepino", "gino@gmail.com", 4, 4, 1997, "snor123snor123", Gender.Male, null));
    }
}
