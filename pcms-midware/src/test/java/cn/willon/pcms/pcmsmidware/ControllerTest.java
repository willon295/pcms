package cn.willon.pcms.pcmsmidware;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ControllerTest
 *
 * @author Willon
 * @since 2019-04-07
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ControllerTest {


    @Autowired
    WebApplicationContext webApplicationContext;


    private MockMvc mockMvc;


    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void testChangeController() throws Exception {
        String result = mockMvc.perform(get("/change/28")).andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        System.out.println(result);

    }
}
