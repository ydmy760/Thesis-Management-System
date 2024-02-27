import com.software.userApp.impl.UsersOperationImpl;
import com.software.userApp.vo.MessageResult;
import com.software.userApp.vo.Result;
import com.software.userApp.vo.StateCode;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Transactional
public class UsersOperationImplTest {
    static UsersOperationImpl usersOperation;
    static JdbcTemplate jdbcTemplate;

    @BeforeClass
    public static void init() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .continueOnError(true)
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        usersOperation = new UsersOperationImpl();
        ReflectionTestUtils.setField(usersOperation, "jdbcTemplate", jdbcTemplate);
    }

    @Test
    public void addUserTest() {
        MessageResult messageResult = usersOperation.addUser("1003", "jack", true, "user", "123");
        Assert.assertEquals(StateCode.DATABASE_FAILURE, messageResult.getCode());
        Assert.assertEquals("无权限", messageResult.getMsg());
        MessageResult messageResult1 = usersOperation.addUser("1001", "jack", true, "user", "123");
        Assert.assertEquals(StateCode.SUCCESS, messageResult1.getCode());
        Assert.assertEquals("写入成功", messageResult1.getMsg());
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from user where uId=?", messageResult1.getUId());
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("jack", list.get(0).get("username"));
        Assert.assertTrue(Boolean.parseBoolean(String.valueOf(list.get(0).get("gender"))));
        Assert.assertEquals("user", list.get(0).get("role"));
    }

    @Test
    public void deleteUserTest() {
        Result result = usersOperation.deleteUser("1002", "1004");
        Assert.assertEquals(StateCode.DATABASE_FAILURE, result.getCode());
        Assert.assertEquals("无权限", result.getMsg());
        Result result1 = usersOperation.deleteUser("1001", "1005");
        Assert.assertEquals(StateCode.DATABASE_FAILURE, result1.getCode());
        Assert.assertEquals("删除对象不存在或删除对象级别不允许被删除", result1.getMsg());
        Result result2 = usersOperation.deleteUser("1001", "1004");
        Assert.assertEquals(StateCode.SUCCESS, result2.getCode());
        Assert.assertEquals("成功删除1004", result2.getMsg());
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from user where uId=?", "1004");
        Assert.assertEquals(0, list.size());
    }
}
