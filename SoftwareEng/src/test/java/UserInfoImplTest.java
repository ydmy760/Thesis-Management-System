
import com.software.userApp.impl.UserInfoImpl;
import com.software.userApp.vo.MessageResult;
import com.software.userApp.vo.Result;
import com.software.userApp.vo.StateCode;
import com.software.userApp.vo.UserInfoResult;
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
public class UserInfoImplTest{
    static UserInfoImpl userInfo;
    static JdbcTemplate jdbcTemplate;

    @BeforeClass
    public static void init() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .continueOnError(true)
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        userInfo = new UserInfoImpl();
        ReflectionTestUtils.setField(userInfo, "jdbcTemplate", jdbcTemplate);
    }

    @Test
    public void testGetAllUser() {
        String user_uId = "1002";
        UserInfoResult userInfoResult = userInfo.getAllUser(user_uId);
        //无权限获取所有用户信息
        Assert.assertEquals(StateCode.DATABASE_FAILURE, userInfoResult.getCode());
        Assert.assertEquals("无权限", userInfoResult.getMsg());
        //成功获取用户所有信息
        String admin_uId = "1001";
        UserInfoResult userInfoResult1 = userInfo.getAllUser(admin_uId);
        List<Map<String, Object>> allUserInfo = userInfoResult1.getUserInfo();
        Assert.assertEquals(StateCode.SUCCESS, userInfoResult1.getCode());
        Assert.assertEquals("查询成功", userInfoResult1.getMsg());
        Assert.assertEquals(4, allUserInfo.size());
        Map<String, Object> lastUser = allUserInfo.get(0);
        Assert.assertEquals("1001", lastUser.get("uId"));
        Assert.assertEquals("James", lastUser.get("username"));
        Assert.assertFalse(Boolean.parseBoolean(String.valueOf(lastUser.get("gender"))));
        Assert.assertEquals("admin", lastUser.get("role"));

    }

    @Test
    public void testGetUserByUid() {
        String user_uId = "1003";
        String uId = "1002";
        MessageResult messageResult = userInfo.getUserByUid(user_uId, uId);
        //无权限获取所有用户信息
        Assert.assertEquals(StateCode.DATABASE_FAILURE, messageResult.getCode());
        Assert.assertEquals("无权限", messageResult.getMsg());
        //成功获取用户所有信息
        String admin_uId = "1001";
        MessageResult messageResult1 = userInfo.getUserByUid(admin_uId, uId);
        Assert.assertEquals(StateCode.SUCCESS, messageResult1.getCode());
        Assert.assertEquals("查询成功", messageResult1.getMsg());
        Assert.assertEquals("1002", messageResult1.getUId());
        Assert.assertEquals("Donald", messageResult1.getUserName());
        Assert.assertTrue(messageResult1.isGender());
        Assert.assertEquals("user", messageResult1.getRole());
        messageResult1 = userInfo.getUserByUid(uId, uId);
        Assert.assertEquals(StateCode.SUCCESS, messageResult1.getCode());
        Assert.assertEquals("查询成功", messageResult1.getMsg());
        Assert.assertEquals("1002", messageResult1.getUId());
        Assert.assertEquals("Donald", messageResult1.getUserName());
        Assert.assertTrue(messageResult1.isGender());
        Assert.assertEquals("user", messageResult1.getRole());
    }

    @Test
    public void testModifyUserInfo() {
        Result result = userInfo.modifyUserInfo("1002","1006", "lily", false, "123456");
        Assert.assertEquals(StateCode.DATABASE_FAILURE, result.getCode());
        Assert.assertEquals("无权限", result.getMsg());
        Result result1 = userInfo.modifyUserInfo("1001", "1003", "lily", false, "123456");
        Assert.assertEquals(StateCode.SUCCESS, result1.getCode());
        Assert.assertEquals("成功更新1003", result1.getMsg());
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from user where uId=?", "1003");
        Assert.assertEquals("lily", list.get(0).get("username"));
        Assert.assertFalse(Boolean.parseBoolean(String.valueOf(list.get(0).get("gender"))));
        Assert.assertEquals("123456", list.get(0).get("password"));
        result1 = userInfo.modifyUserInfo("1003", "1003", "like", true, "");
        Assert.assertEquals(StateCode.SUCCESS, result1.getCode());
        Assert.assertEquals("成功更新1003", result1.getMsg());
        list = jdbcTemplate.queryForList("select * from user where uId=?", "1003");
        Assert.assertEquals("like", list.get(0).get("username"));
        Assert.assertTrue(Boolean.parseBoolean(String.valueOf(list.get(0).get("gender"))));
        Assert.assertEquals("123456", list.get(0).get("password"));
    }





}
