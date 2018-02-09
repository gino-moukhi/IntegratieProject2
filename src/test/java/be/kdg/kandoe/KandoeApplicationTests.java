package be.kdg.kandoe;

import be.kdg.kandoe.service.declaration.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


@RunWith(SpringRunner.class)
@SpringBootTest
public class KandoeApplicationTests {

	@Test
	public void contextLoads() {
		boolean loads = true;
		assertThat(loads, is(true));
	}

}
