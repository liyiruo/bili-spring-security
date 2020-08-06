# 介绍
## 如何使用springboot中Spring-security

## 引入依赖包
```xml
       <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

```
> 启动项目，`localhost:8080/login` 进入页面,需要输入用户名和密码。用户名为：user，密码在控制台输出，去控制台查找。

### 在配置文件中使用

```properties
#如果在代码里配置了用户信息 这个就不能使用了呢？
spring.security.user.name=admin
spring.security.user.password=123456
spring.security.user.roles=ADMIN
```

> 启动项目，`localhost:8080/login` 进入页面,需要输入用户名和密码。用户名为：admin,密码：123456

### 在内存中使用    

- （需要将配置文件里配置的注释掉）

```java
@Configuration
@EnableWebSecurity//启用Spring security
@EnableGlobalMethodSecurity(prePostEnabled = true)//拦截@preAuthrize注解的配置
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder encoder;//这个东西很重要
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*
         * 基于内存的方式构建两个账户
         * */
        auth
                .inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin")
                .password(new BCryptPasswordEncoder()
                        .encode("123"))
                .roles("admin");

        //两个构建账户的方式 看着不同 其实是一样的啊

        auth
                .inMemoryAuthentication()
                .passwordEncoder(encoder)
                .withUser("user")
                .password(encoder
                        .encode("123"))
                .roles("normal");
    }
}
```

>  在内存中定义认证用户，需要自己写一个类``WebSecurityConfig``实现`WebSecurityConfigurerAdapter`类,重写其中的方法；
>
> - 需要注意的是：在设置密码的时候，需要是加密后的密码，且要符合加密类型；
>
> 类上面的注解 `@EnableGlobalMethodSecurity`开启后针对不同的方法，会验证其身份角色；



```java
@RestController
public class HelloController {

    @GetMapping(value = "/hello")
    public String hello() {
        return "HelloWorld";
    }

    @GetMapping(value = "/helloAdmin")
    @PreAuthorize("hasAnyRole('admin')")
    public String helloAdmin() {
        return "HelloWorld，helloAdmin";
    }
  
    @PreAuthorize("hasAnyRole('normal','admin')")
    @GetMapping(value = "/helloUser")
    public String helloUser() {
        return "HelloWorld，helloUser";
    }
}
```

> 验证：
>
> 进入`localhost:8080/login`，登录 admin用户，再访问：`localhost:8080/helloAdmin` ，`localhost:8080/helloUser`
>
> 均可访问成功；
>
> 重新登入 user用户，再访问`localhost:8080/helloAdmin` ，`localhost:8080/helloUser`，发现访问`localhost:8080/helloAdmin`时报错，`访问被禁止`

### 使用数据库

#### 1 添加依赖，使具备查询数据库的能

```xml
    <!-- 数据库连接-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<!--数据库-->
<dependency>
    <groupId>org.hsqldb</groupId>
    <artifactId>hsqldb</artifactId>
    <scope>runtime</scope>
</dependency>
```

> hsqldb内存数据库，jpa 连接数据库

#### 2  开发一个根据用户名查询用户信息的接口

```java
public interface UserInfoService {
    public UserInfo findByUsername(String username);
}
```

#### 3 编写一个类`CustomUserDetailsService`实现接口`UserDetailsService` 重写`loadUserByUsername`方法

```java
@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserInfoService service;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        UserInfo userInfo = service.findByUsername(s);
        if (userInfo == null) {
            throw new UsernameNotFoundException("not found : " + s);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + userInfo.getRole().name()));
        User userDetails = new User(userInfo.getUsername(), encoder.encode(userInfo.getPassword()), authorities);

        return userDetails;
    }
}
```

> 1 此方法返回的是一个`UserDetails`  实例，构造方法中有3个参数，分别为 用户名，密码，和权限列表;
>
> 2 次用用到了查询用户信息的接口
>
> 注意：此处的密码需要加密；权限需要前面拼接`ROLE`（权限如果提前预存在数据库已经拼接过，此处写法会不同）

#### 4 在数据库添加用户

```java
@Service
public class DataInit {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @PostConstruct
    public void dataInit() {
        UserInfo user = new UserInfo();
        user.setUsername("user");
        user.setPassword("123");
        user.setRole(UserInfo.Role.normal);
        userInfoRepository.save(user);


        UserInfo admin = new UserInfo();
        admin.setUsername("admin");
        admin.setPassword("123");
        admin.setRole(UserInfo.Role.admin);
        userInfoRepository.save(admin);
    }

}
```

> 验证：
>
> 进入`localhost:8080/login`，登录 admin用户，再访问：`localhost:8080/helloAdmin` ，`localhost:8080/helloUser`
>
> 均可访问成功；
>
> 重新登入 user用户，再访问`localhost:8080/helloAdmin` ，`localhost:8080/helloUser`，发现访问`localhost:8080/helloAdmin`时报错，`访问被禁止`